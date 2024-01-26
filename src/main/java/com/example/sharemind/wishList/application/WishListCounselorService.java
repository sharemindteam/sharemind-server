package com.example.sharemind.wishList.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
import com.example.sharemind.wishList.dto.request.WishListGetRequest;
import com.example.sharemind.wishList.repository.WishListRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishListCounselorService {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int WISHLIST_PAGE = 4;

    private final CustomerService customerService;
    private final WishListRepository wishListRepository;

    public Set<Long> getWishListCounselorIds(Customer customer) {
        List<WishList> wishLists = getWishListByCustomer(customer);

        return wishLists.stream()
                .map(wishList -> wishList.getCounselor().getCounselorId())
                .collect(Collectors.toSet());
    }

    private List<WishList> getWishListByCustomer(Customer customer) {
        return wishListRepository.findByCustomerAndIsActivatedIsTrue(customer);
    }

    public List<WishList> getWishList(WishListGetRequest wishListGetRequest, Long customerId) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, WISHLIST_PAGE);
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        Page<WishList> page =
                (wishListGetRequest.getWishlistId() == 0)
                        ? wishListRepository.findByCustomerAndIsActivatedIsTrueOrderByUpdatedAtDesc(customer,
                        pageable) :
                        wishListRepository.findByCustomerOrderByUpdatedAtDesc(
                                customer,
                                wishListGetRequest.getUpdatedAt(),
                                wishListGetRequest.getWishlistId(),
                                pageable);
        return page.getContent();
    }
}
