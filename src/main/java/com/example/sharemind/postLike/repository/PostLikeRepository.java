package com.example.sharemind.postLike.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postLike.domain.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Boolean existsByPostAndCustomer(Post post, Customer customer);

    Boolean existsByPostAndCustomerAndIsActivatedIsTrue(Post post, Customer customer);

    Optional<PostLike> findByPostAndCustomer(Post post, Customer customer);

    Optional<PostLike> findByPostAndCustomerAndIsActivatedIsTrue(Post post, Customer customer);
}
