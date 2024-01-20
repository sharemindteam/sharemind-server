package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import java.util.List;

public interface SearchWordService {

    List<CounselorGetResponse> storeSearchWordAndGetCounselors(Long customerId,
                                                               SearchWordFindRequest searchWordFindRequest);

    List<String> getRecentSearchWordsByCustomer(Long customerId);

    void removeSearchWordByCustomer(Long customerId, SearchWordDeleteRequest searchWordDeleteRequest);

    void storeSearchWordInDB(String word);
}
