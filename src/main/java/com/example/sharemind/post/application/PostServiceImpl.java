package com.example.sharemind.post.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.repository.CommentRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.PostGetIsSavedResponse;
import com.example.sharemind.post.dto.response.PostGetResponse;
import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import com.example.sharemind.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private static final int POST_CUSTOMER_PAGE_SIZE = 4;

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
        post.checkReadAuthority(customerId); // TODO: 비공개 상담에 답변 단 판매자도 볼 수 있게 해야함

        return PostGetResponse.of(post);
    }

    @Override
    public List<PostGetResponse> getPostsByCustomer(Boolean filter, Long postId, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        return postRepository.findAllByCustomerAndIsActivatedIsTrue(customer, filter, postId,
                        POST_CUSTOMER_PAGE_SIZE).stream()
                .map(post -> (post.getIsCompleted() != null && !post.getIsCompleted())
                        ? PostGetResponse.ofIsNotCompleted(post) : PostGetResponse.of(post))
                .toList();
    }

    @Override
    public List<Long> getRandomPosts() {
        return postRepository.findRandomProceedingPostIds();
    }

    @Override
    public PostGetResponse getCounselorPostContent(Long postId) {
        Post post = getProceedingPost(postId);

        return PostGetResponse.of(post);
    }

    @Override
    public Post getProceedingPost(Long postId) {
        Post post = getPostByPostId(postId);

        checkPostProceeding(post);
        return post;
    }

    @Override
    public Boolean checkCounselorReadAuthority(Long postId, Long customerId){
        Post post = getPostByPostId(postId);

        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);

        Comment comment = commentRepository.findByPostAndCounselorAndIsActivatedIsTrue(post, counselor);

        return comment != null;
    }

    private void checkPostProceeding(Post post) {
        if (post.getPostStatus() != PostStatus.PROCEEDING)
            throw new PostException(PostErrorCode.POST_NOT_PROCEEDING, post.getPostId().toString());
    }
}
