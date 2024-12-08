package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.dto.response.CounselorGetListResponse;
import com.example.sharemind.post.dto.response.PostGetPublicListResponse;
import com.example.sharemind.searchWord.domain.SearchWord;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordCounselorFindRequest;

import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import java.util.List;

public interface SearchWordService {

    List<CounselorGetListResponse> storeSearchWordAndGetCounselorsByCustomer(Long customerId, String sortType,
                                                                             SearchWordCounselorFindRequest searchWordCounselorFindRequest);

    List<CounselorGetListResponse> storeAllSearchWordAndGetCounselors(String sortType,
                                                                      SearchWordCounselorFindRequest searchWordCounselorFindRequest);

    List<String> getRecentSearchWordsByCustomer(Long customerId);

    void removeSearchWordByCustomer(Long customerId, SearchWordDeleteRequest searchWordDeleteRequest);

    void storeSearchWordInDB(String word);

    List<PostGetPublicListResponse> storeAllSearchWordAndGetPosts(String sortType,
                                                            SearchWordPostFindRequest searchWordPostFindRequest);

    List<PostGetPublicListResponse> storeSearchWordAndGetPosts(Long customerId, String sortType,
                                                         SearchWordPostFindRequest searchWordPostFindRequest);

    List<SearchWord> getSearchWordsOrderByCount();
}
