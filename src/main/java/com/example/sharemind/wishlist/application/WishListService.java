package com.example.sharemind.wishlist.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishlist.domain.WishList;
import java.util.List;
import java.util.Set;

public interface WishListService {
    List<WishList> getWishListByCustomer(Customer customer);

    Set<Long> getWishListCounselorIdsByCustomer(Customer customer);
}
