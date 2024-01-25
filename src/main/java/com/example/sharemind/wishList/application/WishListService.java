package com.example.sharemind.wishList.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
import java.util.List;
import java.util.Set;

public interface WishListService {
    List<WishList> getWishList(Customer customer);

    Set<Long> getWishListCounselorIds(Customer customer);
}
