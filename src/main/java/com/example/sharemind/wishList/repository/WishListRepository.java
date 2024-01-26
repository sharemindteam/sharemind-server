package com.example.sharemind.wishList.repository;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByCustomerAndIsActivatedIsTrue(Customer customer);

    WishList findByCustomerAndCounselor(Customer customer, Counselor counselor);

    @Query("SELECT w FROM WishList w WHERE "
            + "w.customer = :customer AND "
            + "(w.updatedAt < :updatedAt OR (w.updatedAt = :updatedAt AND w.wishlistId < :wishlistId)) "
            + "AND w.isActivated = true "
            + "ORDER BY w.updatedAt DESC, w.wishlistId DESC")
    Page<WishList> findByCustomerOrderByUpdatedAtDesc(Customer customer, LocalDateTime updatedAt, Long wishlistId,
                                                      Pageable pageable);

    Page<WishList> findByCustomerAndIsActivatedIsTrueOrderByUpdatedAtDesc(Customer customer, Pageable pageable);

    Boolean existsByCustomerAndCounselorAndIsActivatedIsTrue(Customer customer, Counselor counselor);
}
