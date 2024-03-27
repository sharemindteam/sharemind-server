package com.example.sharemind.postScrap.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postScrap.domain.PostScrap;
import com.example.sharemind.postScrap.exception.PostScrapErrorCode;
import com.example.sharemind.postScrap.exception.PostScrapException;
import com.example.sharemind.postScrap.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostScrapServiceImpl implements PostScrapService {

    private final CustomerService customerService;
    private final PostService postService;
    private final PostScrapRepository postScrapRepository;

    @Transactional
    @Override
    public void createPostScrap(Long postId, Long customerId) {
        Post post = postService.getPostByPostId(postId);
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
    public void deletePostScrap(Long postId, Long customerId) {
        Post post = postService.getPostByPostId(postId);
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        PostScrap postScrap = postScrapRepository.findByPostAndCustomerAndIsActivatedIsTrue(post,
                        customer)
                .orElseThrow(() -> new PostScrapException(PostScrapErrorCode.POST_SCRAP_NOT_FOUND));

        postScrap.updateIsActivatedFalse();
    }
}
