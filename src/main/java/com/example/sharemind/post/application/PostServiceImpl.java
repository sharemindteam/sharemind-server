package com.example.sharemind.post.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
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

    private final PostRepository postRepository;
    private final CustomerService customerService;

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
}
