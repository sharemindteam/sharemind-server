package com.example.sharemind.wishList.domain;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class WishList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;
}
