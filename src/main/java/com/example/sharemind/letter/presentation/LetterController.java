package com.example.sharemind.letter.presentation;

import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @GetMapping("/categories/{letterId}")
    public ResponseEntity<LetterGetCounselorCategoriesResponse> getCounselorCategories(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCounselorCategories(letterId));
    }

    @GetMapping("/customer-info/{letterId}")
    public ResponseEntity<LetterGetNicknameCategoryResponse> getCustomerNicknameAndCategory(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCustomerNicknameAndCategory(letterId));
    }
}
