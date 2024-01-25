package com.example.sharemind.wishList.presentation;

import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.wishList.application.WishListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WishList Controller", description = "찜하기 컨트롤러")
@RestController
@RequestMapping("/api/v1/wishLists")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @PatchMapping()
    public ResponseEntity<Void> addWishList(@RequestParam Long counselorId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        wishListService.addWishListByCustomer(customUserDetails.getCustomer().getCustomerId(), counselorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeWishList(@RequestParam Long counselorId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        wishListService.removeWishListByCustomer(customUserDetails.getCustomer().getCustomerId(), counselorId);
        return ResponseEntity.ok().build();
    }
}
