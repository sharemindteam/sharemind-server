package com.example.sharemind.post.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.domain.Post;
import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllByCustomerAndIsActivatedIsTrue(Customer customer, Boolean filter,
            Long postId, int size);
}