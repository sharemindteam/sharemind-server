package com.example.sharemind.postLike.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postLike.domain.PostLike;
import com.example.sharemind.postLike.exception.PostLikeErrorCode;
import com.example.sharemind.postLike.exception.PostLikeException;
import com.example.sharemind.postLike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeServiceImpl implements PostLikeService {

    private final CustomerService customerService;
    private final PostService postService;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    @Override
    public void createPostLike(Long postId, Long customerId) {
        Post post = postService.getPostByPostId(postId);
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        if (postLikeRepository.existsByPostAndCustomer(post, customer)) {
            PostLike postLike = postLikeRepository.findByPostAndCustomer(post, customer)
                    .orElseThrow(
                            () -> new PostLikeException(PostLikeErrorCode.POST_LIKE_NOT_FOUND));

            postLike.updateIsActivatedTrue();
        } else {
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .customer(customer)
                    .build();

            postLikeRepository.save(postLike);
        }
    }

    @Transactional
    @Override
    public void deletePostLike(Long postId, Long customerId) {
        Post post = postService.getPostByPostId(postId);
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        PostLike postLike = postLikeRepository.findByPostAndCustomerAndIsActivatedIsTrue(post,
                        customer)
                .orElseThrow(() -> new PostLikeException(PostLikeErrorCode.POST_LIKE_NOT_FOUND));

        postLike.updateIsActivatedFalse();
    }
}
