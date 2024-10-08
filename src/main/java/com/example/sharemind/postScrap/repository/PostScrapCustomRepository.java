package com.example.sharemind.postScrap.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.postScrap.domain.PostScrap;
import java.time.LocalDateTime;
import java.util.List;

public interface PostScrapCustomRepository {

    List<PostScrap> findAllByCustomerAndIsActivatedIsTrue(Customer customer, Long postScrapId,
           LocalDateTime updatedAt, int size);
}
