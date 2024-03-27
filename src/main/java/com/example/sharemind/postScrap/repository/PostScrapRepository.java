package com.example.sharemind.postScrap.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postScrap.domain.PostScrap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

    Boolean existsByPostAndCustomer(Post post, Customer customer);

    Boolean existsByPostAndCustomerAndIsActivatedIsTrue(Post post, Customer customer);

    Optional<PostScrap> findByPostAndCustomer(Post post, Customer customer);

    Optional<PostScrap> findByPostAndCustomerAndIsActivatedIsTrue(Post post, Customer customer);
}
