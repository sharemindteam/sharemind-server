package com.example.sharemind.wishList.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
import com.example.sharemind.wishList.exception.WishListErrorCode;
import com.example.sharemind.wishList.exception.WishListException;
import com.example.sharemind.wishList.repository.WishListRepository;
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

    @Transactional
    @Override
    public void addWishListByCustomer(Long customerId, Long counselorId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);
        if (counselor.getProfileStatus() != ProfileStatus.EVALUATION_COMPLETE) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_COMPLETE_EVALUATION, counselorId.toString());
        }
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
