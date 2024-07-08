package com.example.sharemind.postScrap.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.utils.EncryptionUtil;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postLike.repository.PostLikeRepository;
import com.example.sharemind.postScrap.domain.PostScrap;
import com.example.sharemind.postScrap.dto.response.PostScrapGetResponse;
import com.example.sharemind.postScrap.exception.PostScrapErrorCode;
import com.example.sharemind.postScrap.exception.PostScrapException;
import com.example.sharemind.postScrap.repository.PostScrapRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostScrapServiceImpl implements PostScrapService {

    private static final int POST_PAGE_SIZE = 6;

    private final CustomerService customerService;
    private final PostService postService;
    private final PostScrapRepository postScrapRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    @Override
    public void createPostScrap(String postId, Long customerId) {
        Post post = postService.getPostByPostId(EncryptionUtil.decrypt(postId));
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        if (postScrapRepository.existsByPostAndCustomer(post, customer)) {
            PostScrap postScrap = postScrapRepository.findByPostAndCustomer(post, customer)
                    .orElseThrow(
                            () -> new PostScrapException(PostScrapErrorCode.POST_SCRAP_NOT_FOUND));

            postScrap.updateIsActivatedTrue();
        } else {
            PostScrap postScrap = PostScrap.builder()
                    .post(post)
                    .customer(customer)
                    .build();

            postScrapRepository.save(postScrap);
        }
    }

    @Transactional
    @Override
    public void deletePostScrap(String postId, Long customerId) {
        Post post = postService.getPostByPostId(EncryptionUtil.decrypt(postId));
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        PostScrap postScrap = postScrapRepository.findByPostAndCustomerAndIsActivatedIsTrue(post,
                        customer)
                .orElseThrow(() -> new PostScrapException(PostScrapErrorCode.POST_SCRAP_NOT_FOUND));

        postScrap.updateIsActivatedFalse();
    }

    @Override
    public List<PostScrapGetResponse> getScrappedPosts(Long postScrapId, LocalDateTime scrappedAt,
            Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        return postScrapRepository.findAllByCustomerAndIsActivatedIsTrue(customer, postScrapId,
                        scrappedAt, POST_PAGE_SIZE).stream()
                .map(postScrap -> PostScrapGetResponse.of(postScrap,
                        postLikeRepository.existsByPostAndCustomerAndIsActivatedIsTrue(
                                postScrap.getPost(), customer)))
                .toList();
    }
}
