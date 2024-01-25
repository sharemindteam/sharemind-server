package com.example.sharemind.wishList.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
import com.example.sharemind.wishList.exception.WishListErrorCode;
import com.example.sharemind.wishList.exception.WishListException;
import com.example.sharemind.wishList.repository.WishListRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final CustomerService customerService;
    private final CounselorService counselorService;
    private final WishListRepository wishListRepository;

    @Override
    public List<WishList> getWishList(Customer customer) {
        return wishListRepository.findByCustomerAndIsActivatedIsTrue(customer);
    }

    @Override
    public Set<Long> getWishListCounselorIds(Customer customer) {
        List<WishList> wishLists = getWishList(customer);

        return wishLists.stream()
                .map(wishList -> wishList.getCounselor().getCounselorId())
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void addWishListByCustomer(Long customerId, Long counselorId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);

        WishList wishList = wishListRepository.findByCustomerAndCounselor(customer, counselor);
        if (wishList == null) {
            wishListRepository.save(WishList.builder().customer(customer).counselor(counselor).build());
        } else if (!wishList.isActivated()) {
            wishList.updateIsActivatedTrue();
        } else {
            throw new WishListException(WishListErrorCode.WISH_LIST_ALREADY_EXIST, counselorId.toString());
        }
    }

    @Transactional
    @Override
    public void removeWishListByCustomer(Long customerId, Long counselorId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);

        WishList wishList = wishListRepository.findByCustomerAndCounselor(customer, counselor);
        if (wishList == null || !wishList.isActivated()) {
            throw new WishListException(WishListErrorCode.WISH_LIST_NOT_EXIST, counselorId.toString());
        }
        wishList.updateIsActivatedFalse();
    }
}
