package com.example.sharemind.post.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.repository.PostRepository;
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
        ConsultCategory consultCategory = ConsultCategory.getConsultCategoryByName(
                postCreateRequest.getConsultCategory());

        postRepository.save(postCreateRequest.toEntity(customer, consultCategory));
    }
}
