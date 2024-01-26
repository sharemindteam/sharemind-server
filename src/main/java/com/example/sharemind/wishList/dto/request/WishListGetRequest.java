package com.example.sharemind.wishList.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class WishListGetRequest {

    @Schema(description = "찜하기 id", example = "1")
    Long wishlistId;

    @Schema(description = "찜하기가 업데이트된 시간", example = "2024-01-25 19:25:06.954532")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
