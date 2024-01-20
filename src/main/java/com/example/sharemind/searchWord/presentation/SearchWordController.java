package com.example.sharemind.searchWord.presentation;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.searchWord.application.SearchWordService;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PatchMapping("results")
    public ResponseEntity<List<CounselorGetResponse>> getSearchWordResults(
            @Valid @RequestBody SearchWordFindRequest searchWordFindRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(
                searchWordService.getSearchWordAndReturnResults(customUserDetails.getCustomer(),
                        searchWordFindRequest));
    }

    @PatchMapping()
    public ResponseEntity<Void> deleteCustomerSearchWord(
            @Valid @RequestBody SearchWordDeleteRequest searchWordDeleteRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        searchWordService.removeSearchWordByCustomer(customUserDetails.getCustomer(), searchWordDeleteRequest);

        return ResponseEntity.ok().build();
    }
}
