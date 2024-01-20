package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import java.util.List;

public interface SearchWordService {
    List<CounselorGetResponse> getSearchWordAndReturnResults(Customer customer,
                                                             SearchWordFindRequest searchWordFindRequest);

    List<String> getRecentSearchWordsByCustomer(Customer customer);

    void removeSearchWordByCustomer(Customer customer, SearchWordDeleteRequest searchWordDeleteRequest);

    void storeSearchWordInDB(String word);
}
