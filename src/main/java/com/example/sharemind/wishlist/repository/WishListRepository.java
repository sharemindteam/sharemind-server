package com.example.sharemind.wishlist.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishlist.domain.WishList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByCustomerAndIsActivatedIsTrue(Customer customer);
}
