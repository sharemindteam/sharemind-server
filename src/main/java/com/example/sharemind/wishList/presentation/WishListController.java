package com.example.sharemind.wishList.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WishList Controller", description = "찜하기 컨트롤러")
@RestController
@RequestMapping("/api/v1/wishLists")
@RequiredArgsConstructor
public class WishListController {

}
