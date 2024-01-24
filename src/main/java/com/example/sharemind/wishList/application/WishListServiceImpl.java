package com.example.sharemind.wishList.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.wishList.domain.WishList;
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

    private final WishListRepository wishListRepository;

    @Override
    public List<WishList> getWishListByCustomer(Customer customer) {
        return wishListRepository.findByCustomerAndIsActivatedIsTrue(customer);
    }

    @Override
    public Set<Long> getWishListCounselorIdsByCustomer(Customer customer) {
        List<WishList> wishLists = getWishListByCustomer(customer);

        return wishLists.stream()
                .map(wishList -> wishList.getCounselor().getCounselorId())
                .collect(Collectors.toSet());
    }
}
