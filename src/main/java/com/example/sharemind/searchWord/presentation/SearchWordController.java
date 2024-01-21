package com.example.sharemind.searchWord.presentation;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.searchWord.application.SearchWordService;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SearchWord Controller", description = "검색어 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/searchWords")
@RequiredArgsConstructor
public class SearchWordController {

    private final SearchWordService searchWordService;

    @Operation(summary = "유저의 이전 검색 기록 반환", description = """
            유저의 이전 검색어 목록을 반환
            왼쪽이 가장 최근 검색어이고 시간순으로 정렬되어있습니다.
            검색어가 없을 시 빈 배열 ([ ]) 전달합니다.""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping()
    public ResponseEntity<List<String>> getCustomerSearchWords(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(
                searchWordService.getRecentSearchWordsByCustomer(customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "검색 결과 반환", description = """
            sortType(LATEST: 최근순, POPULARITY: 인기순, STAR_RATING: 별점 평균 순
            주소 형식 예시 : /api/v1/searchWords/results?sortType=STAR_RATING검색어 결과를 상담사 닉네임, 소개 제목, 소개 내용에서 찾아서 반환. 정확하게 일치하는 정보만 반환
            만약 이전에 검색했던 검색어의 경우(ex) 연애, 갈등)에서 갈등이 다시 검색된다면 (갈등, 연애)순서로 다시 서버에 저장합니다.
            (중복 저장x)검색 결관느 4개씩 반환하며, index는 페이지 번호 입니다(ex index 0 : id 0~3에 해당하는 값 반환 index 1: 4~7에 해당하는 값 반환해당하는 검색 결과가 없을 때(범위를 벗어난 인덱스 혹은 음수 인덱스는 빈배열을 리턴합니다.""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "1. 검색어가 2~20자 사이가 아님", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomExceptionResponse.class)))
    })
    @PatchMapping("/results")
    public ResponseEntity<List<CounselorGetResponse>> getSearchWordResults(
            @Valid @RequestBody SearchWordFindRequest searchWordFindRequest, @RequestParam String sortType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (searchWordFindRequest.getIndex() < 0) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(
                searchWordService.storeSearchWordAndGetCounselors(customUserDetails.getCustomer().getCustomerId(),
                        sortType, searchWordFindRequest));
    }

    @Operation(summary = "검색어 삭제", description = "검색어를 삭제하는 api\n"
            + "없는 검색어 삭제해도 그냥 아무 동작 안하게 만들었습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "1. 검색어가 2~20자 사이가 아님", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomExceptionResponse.class)))
    })
    @DeleteMapping()
    public ResponseEntity<Void> deleteCustomerSearchWord(
            @Valid @RequestBody SearchWordDeleteRequest searchWordDeleteRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        searchWordService.removeSearchWordByCustomer(customUserDetails.getCustomer().getCustomerId(),
                searchWordDeleteRequest);

        return ResponseEntity.ok().build();
    }
}
