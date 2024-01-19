package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import java.util.List;

public interface SearchWordService {
    List<CounselorGetResponse> getSearchWordAndReturnResults(Long customerId, String word, int index);
}
