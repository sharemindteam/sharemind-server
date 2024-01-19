package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import com.example.sharemind.customer.domain.Customer;
import java.util.List;

public interface SearchWordService {
    List<CounselorGetResponse> getSearchWordAndReturnResults(Customer customer, String word, int index);

    List<String> getRecentSearchWords(Long customerId);

    void removeSearchWord(Long customerId, String word);

    void storeSearchWordInDB(String word);
}
