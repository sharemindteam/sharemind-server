package com.example.sharemind.post.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.repository.CommentRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.PostGetIsSavedResponse;
import com.example.sharemind.post.dto.response.PostGetListResponse;
import com.example.sharemind.post.dto.response.PostGetPopularityResponse;
import com.example.sharemind.post.dto.response.PostGetResponse;
import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import com.example.sharemind.post.repository.PostRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private static final int POST_CUSTOMER_PAGE_SIZE = 4;
    private static final int POST_POPULARITY_SIZE = 3;

    private final CustomerService customerService;
    private final CounselorService counselorService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void createPost(PostCreateRequest postCreateRequest, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        postRepository.save(postCreateRequest.toEntity(customer));
    }

    @Override
    public List<Post> getUnpaidPrivatePosts() {
        return postRepository.findAllByIsPaidIsFalseAndIsActivatedIsTrue();
    }

    @Override
    public Post getPostByPostId(Long postId) {
        return postRepository.findByPostIdAndIsActivatedIsTrue(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_NOT_FOUND, postId.toString()));
    }

    @Transactional
    @Override
    public void updatePost(PostUpdateRequest postUpdateRequest, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Post post = getPostByPostId(postUpdateRequest.getPostId());
        ConsultCategory consultCategory = ConsultCategory.getConsultCategoryByName(
                postUpdateRequest.getConsultCategory());

        post.updatePost(consultCategory, postUpdateRequest.getTitle(),
                postUpdateRequest.getContent(), postUpdateRequest.getIsCompleted(), customer);
    }

    @Override
    public PostGetIsSavedResponse getIsSaved(Long postId) {
        Post post = getPostByPostId(postId);

        if ((post.getIsCompleted() != null) && !post.getIsCompleted()) {
            return PostGetIsSavedResponse.of(post);
        } else {
            return PostGetIsSavedResponse.of();
        }
    }

    @Override
    public PostGetResponse getPost(Long postId, Long customerId) {
        Post post = getPostByPostId(postId);

        if (customerId != 0) {
            customerService.getCustomerByCustomerId(customerId);
        }
        post.checkReadAuthority(customerId);

        return PostGetResponse.of(post);
    }

    @Override
    public List<PostGetListResponse> getPostsByCustomer(Boolean filter, Long postId,
            Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        return postRepository.findAllByCustomerAndIsActivatedIsTrue(customer, filter, postId,
                        POST_CUSTOMER_PAGE_SIZE).stream()
                .map(post -> (post.getIsCompleted() != null && !post.getIsCompleted())
                        ? PostGetListResponse.ofIsNotCompleted(post) : PostGetListResponse.of(post))
                .toList();
    }

    @Override
    public List<PostGetListResponse> getPublicPostsByCustomer(Long postId,
            LocalDateTime finishedAt) {
        return postRepository.findAllByIsPublicAndIsActivatedIsTrue(postId, finishedAt,
                        POST_CUSTOMER_PAGE_SIZE).stream()
                .map(PostGetListResponse::of)
                .toList();
    }

    @Override
    public List<PostGetPopularityResponse> getPopularityPosts() {
        return postRepository.findPopularityPosts(LocalDate.now().minusWeeks(1),
                        POST_POPULARITY_SIZE).stream()
                .map(PostGetPopularityResponse::of)
                .toList();
    }

    @Override
    public List<Long> getRandomPosts() {
        return postRepository.findRandomProceedingPostIds();
    }

    @Override
    public PostGetResponse getCounselorPostContent(Long postId, Long customerId) {
        Post post = checkAndGetCounselorPost(postId, customerId);

        return PostGetResponse.of(post);
    }

    @Override
    public Post checkAndGetCounselorPost(Long postId, Long customerId) {
        if (checkCounselorReadAuthority(postId, customerId)) {
            return getPostByPostId(postId);
        }

        return getProceedingPost(postId);
    }

    private Boolean checkCounselorReadAuthority(Long postId, Long customerId) {
        Post post = getPostByPostId(postId);

        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);

        Comment comment = commentRepository.findByPostAndCounselorAndIsActivatedIsTrue(post,
                counselor);

        return comment != null;
    }

    private Post getProceedingPost(Long postId) {
        Post post = getPostByPostId(postId);

        post.checkPostProceeding();
        return post;
    }
}
