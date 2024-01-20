package com.example.sharemind.searchWord.presentation;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.searchWord.application.SearchWordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SearchWord Controller", description = "검색어 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/searchWords")
@RequiredArgsConstructor
public class SearchWordController {

    private final SearchWordService searchWordService;

    @GetMapping()
    public ResponseEntity<List<String>> getCustomerSearchWords(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(searchWordService.getRecentSearchWordsByCustomer(customUserDetails.getCustomer()));
    }

    @GetMapping("results")
    public ResponseEntity<List<CounselorGetResponse>> getCounselorCategories(@RequestParam String word,
                                                                             @RequestParam int index,
                                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(
                searchWordService.getSearchWordAndReturnResults(customUserDetails.getCustomer(), word, index));
    }
}
