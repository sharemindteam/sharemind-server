package com.example.sharemind.wishList.repository;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByCustomerAndIsActivatedIsTrue(Customer customer);

    WishList findByCustomerAndCounselor(Customer customer, Counselor counselor);

    Page<WishList> findByCustomerAndWishlistIdLessThanAndIsActivatedIsTrueOrderByUpdatedAtDesc(Customer customer,
                                                                                               Long wishlistId,
                                                                                               Pageable pageable);

    Page<WishList> findByCustomerAndIsActivatedIsTrueOrderByUpdatedAtDesc(Customer customer, Pageable pageable);
}
