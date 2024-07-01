package com.example.sharemind.post.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.repository.CommentRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.content.PostListSortType;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.*;
import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import com.example.sharemind.post.repository.PostRepository;
import com.example.sharemind.postLike.repository.PostLikeRepository;
import com.example.sharemind.postScrap.repository.PostScrapRepository;
import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private static final int POST_PAGE_SIZE = 4;
    private static final int POST_PAGE_SEARCH_SIZE = 5;
    private static final int TOTAL_POSTS = 50;
    private static final int POSTS_AFTER_24H_COUNT = TOTAL_POSTS / 3;
    private static final Boolean POST_IS_NOT_LIKED = false;
    private static final Boolean POST_IS_NOT_SCRAPPED = false;
    private static final Boolean IS_NOT_POST_OWNER = false;

    private final CustomerService customerService;
    private final CounselorService counselorService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;

    @Transactional
    @Override
    public void createPost(PostCreateRequest postCreateRequest, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        postRepository.save(postCreateRequest.toEntity(customer));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByIsActivatedIsTrue();
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
    public PostGetResponse getPost(Long postId, Long customerId) {
        Post post = getPostByPostId(postId);
        post.checkReadAuthority(customerId);

        if (customerId != 0) {
            Customer customer = customerService.getCustomerByCustomerId(customerId);

            return PostGetResponse.of(post,
                    postLikeRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post, customer),
                    postScrapRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                            customer));
        }

        return PostGetResponse.of(post, POST_IS_NOT_LIKED, POST_IS_NOT_SCRAPPED);
    }

    @Override
    public List<PostGetCustomerListResponse> getPostsByCustomer(Boolean filter, Long postId,
            Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        return postRepository.findAllByCustomerAndIsActivatedIsTrue(customer, filter, postId,
                        POST_PAGE_SIZE).stream()
                .map(post -> (post.getIsCompleted() != null && post.getIsCompleted())
                        ? PostGetCustomerListResponse.of(post,
                        postLikeRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                                customer),
                        postScrapRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                                customer)) : PostGetCustomerListResponse.ofIsNotCompleted(post))
                .toList();
    }

    @Override
    public List<PostGetCounselorListResponse> getPostsByCounselor(Boolean filter, Long postId,
            Long customerId) {
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);
        List<Comment> comments = commentRepository.findAllByCounselorAndIsActivatedIsTrue(counselor,
                filter, postId, POST_PAGE_SIZE);

        return comments.stream()
                .filter(comment -> comment.getPost().isActivated())
                .map(comment -> PostGetCounselorListResponse.of(comment.getPost(), comment))
                .toList();
    }

    @Override
    public List<PostGetPublicListResponse> getPublicPostsByCustomer(Long postId,
            LocalDateTime finishedAt, Long customerId) {
        if (customerId != 0) {
            Customer customer = customerService.getCustomerByCustomerId(customerId);

            return postRepository.findAllByIsPublicAndIsActivatedIsTrue(postId, finishedAt,
                            POST_PAGE_SIZE).stream()
                    .map(post -> PostGetPublicListResponse.of(post,
                            postLikeRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                                    customer),
                            postScrapRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                                    customer)))
                    .toList();
        }

        return postRepository.findAllByIsPublicAndIsActivatedIsTrue(postId, finishedAt,
                        POST_PAGE_SIZE).stream()
                .map(post -> PostGetPublicListResponse.of(post, POST_IS_NOT_LIKED,
                        POST_IS_NOT_SCRAPPED))
                .toList();
    }

    @Override
    public List<PostGetPublicListResponse> getPopularityPosts(Long postId, LocalDateTime finishedAt,
            Long customerId) {
        if (customerId != 0) {
            Customer customer = customerService.getCustomerByCustomerId(customerId);

            return postRepository.findPopularityPosts(postId, finishedAt, POST_PAGE_SIZE).stream()
                    .map(post -> PostGetPublicListResponse.of(post,
                            postLikeRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                                    customer),
                            postScrapRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post,
                                    customer)))
                    .toList();
        }

        return postRepository.findPopularityPosts(postId, finishedAt, POST_PAGE_SIZE).stream()
                .map(post -> PostGetPublicListResponse.of(post, POST_IS_NOT_LIKED,
                        POST_IS_NOT_SCRAPPED))
                .toList();
    }

    @Override
    public List<Long> getRandomPosts() {
        List<Long> postsAfter24h = postRepository.findRandomProceedingPostIdsAfter24Hours();
        List<Long> postsWithin24h = postRepository.findRandomProceedingPostIdsWithin24Hours();

        List<Long> randomPosts = new ArrayList<>(TOTAL_POSTS);

        for (int i = 0; i < Math.min(POSTS_AFTER_24H_COUNT, postsAfter24h.size()); i++) {
            randomPosts.add(postsAfter24h.get(i));
        }

        List<Long> remainingPosts = new ArrayList<>(postsWithin24h);
        remainingPosts.addAll(postsAfter24h.subList(randomPosts.size(), postsAfter24h.size()));
        Collections.shuffle(remainingPosts);

        int remainingSize = TOTAL_POSTS - randomPosts.size();
        for (int i = 0; i < Math.min(remainingSize, remainingPosts.size()); i++) {
            randomPosts.add(remainingPosts.get(i));
        }
        Collections.shuffle(randomPosts);
        return randomPosts;
    }

    @Override
    public PostGetResponse getCounselorPostContent(Long postId, Long customerId) {
        Post post = checkAndGetCounselorPost(postId, customerId);

        return PostGetResponse.of(post, POST_IS_NOT_LIKED, POST_IS_NOT_SCRAPPED);
    }

    @Override
    public Post checkAndGetCounselorPost(Long postId, Long customerId) {
        if (checkCounselorReadAuthority(postId, customerId)) {
            return getPostByPostId(postId);
        }

        return getProceedingPost(postId);
    }

    @Override
    public List<Post> getPostByWordWithPagination(
            SearchWordPostFindRequest searchWordPostFindRequest, String sortType) {
        String sortColumn = getPostSortColumn(sortType);
        if (searchWordPostFindRequest.getPostId() == 0) {
            return postRepository.getFirstPostByWordWithSortType(searchWordPostFindRequest,
                    sortColumn, POST_PAGE_SEARCH_SIZE);
        }
        Post post = getPostByPostId(searchWordPostFindRequest.getPostId());
        return postRepository.getPostByWordWithSortType(searchWordPostFindRequest, sortColumn, post,
                POST_PAGE_SIZE);
    }

    @Override
    public Boolean getIsPostOwner(Long postId, Long customerId) {
        if (customerId != 0) {
            Post post = getPostByPostId(postId);
            Customer customer = customerService.getCustomerByCustomerId(customerId);

            return post.checkOwner(customer.getCustomerId());
        }

        return IS_NOT_POST_OWNER;
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkPostStatus() {
        postRepository.findAllProceedingPublicPostsAfter72Hours()
                .forEach(post -> {
                    if (post.getTotalComment() > 0) {
                        post.updatePostStatus(PostStatus.TIME_OUT);
                    }
                });
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

    private String getPostSortColumn(String sortType) {
        PostListSortType postListSortType = PostListSortType.getSortTypeByName(sortType);
        return postListSortType.getSortColumn();
    }
}
