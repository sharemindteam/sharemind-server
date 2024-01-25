package com.example.sharemind.wishList.application;

public interface WishListService {

    void addWishListByCustomer(Long customerId, Long counselorId);

    void removeWishListByCustomer(Long customerId, Long counselorId);
}
