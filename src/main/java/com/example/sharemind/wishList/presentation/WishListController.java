package com.example.sharemind.wishList.presentation;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.dto.response.CounselorGetWishListResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.wishList.application.WishListService;
import com.example.sharemind.wishList.dto.request.WishListGetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WishList Controller", description = "찜하기 컨트롤러")
@RestController
@RequestMapping("/api/v1/wishLists")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;
    private final CounselorService counselorService;

    @Operation(summary = "찜하기 추가", description = "찜하기에 해당 상담사를 추가하는 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "찜하기 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 이미 찜하기 되어 있는 상담사 추가 중복 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "1. 존재하지 않는 상담사 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameter(name = "counselorId", description = "상담사 아이디")
    @PatchMapping()
    public ResponseEntity<Void> addWishList(@RequestParam Long counselorId,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        wishListService.addWishListByCustomer(customUserDetails.getCustomer().getCustomerId(), counselorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "찜하기 삭제", description = "찜하기에서 해당 상담사를 삭제하는 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "찜하기 삭제 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 이미 찜하기 되어 있지 않은 상담사 추가 중복 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "1. 존재하지 않는 상담사 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameter(name = "counselorId", description = "상담사 아이디")
    @DeleteMapping()
    public ResponseEntity<Void> removeWishList(@RequestParam Long counselorId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        wishListService.removeWishListByCustomer(customUserDetails.getCustomer().getCustomerId(), counselorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "찜하기 목록 받아오기", description = "찜하기 목록을 받아오는 api"
            + "마지막 찜하기의 wishlistId와 updatedAt 바디로 넘겨주시면 됩니다~"
            + "4개씩 리턴합니다."
            + "처음 요청의 경우 wishlistId 0으로 쏴주세요"
            + "해당 wishlsit가 없을 경우 빈배열로 리턴합니다.")
    @PostMapping()
    public ResponseEntity<List<CounselorGetWishListResponse>> getWishList(
            @RequestBody WishListGetRequest wishListGetRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(counselorService.getCounselorWishListByCustomer(
                wishListGetRequest, customUserDetails.getCustomer()
                        .getCustomerId()));
    }
}
