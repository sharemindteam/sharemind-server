package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.response.CounselorGetListResponse;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.searchWord.domain.SearchWord;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import com.example.sharemind.searchWord.repository.SearchWordRepository;
import com.example.sharemind.wishList.application.WishListCounselorService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchWordServiceImpl implements SearchWordService {

    private static final String SEARCH_WORD_PREFIX = "searchWord: ";

    private final CounselorService counselorService;
    private final WishListCounselorService wishListCounselorService;
    private final CustomerService customerService;
    private final SearchWordRepository searchWordRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Override
    public List<CounselorGetListResponse> storeSearchWordAndGetCounselorsByCustomer(Long customerId, String sortType,
                                                                                    SearchWordFindRequest searchWordFindRequest) {
        storeSearchWordInRedis(customerId, searchWordFindRequest.getWord());
        storeSearchWordInDB(searchWordFindRequest.getWord());

        List<Counselor> counselors = counselorService.getCounselorByWordWithPagination(searchWordFindRequest, sortType);

        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Set<Long> wishListCounselorIds = wishListCounselorService.getWishListCounselorIdsByCustomer(customer);

        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor,
                        wishListCounselorIds.contains(counselor.getCounselorId())))
                .toList();
    }

    @Transactional
    @Override
    public List<CounselorGetListResponse> storeAllSearchWordAndGetCounselors(String sortType,
                                                                          SearchWordFindRequest searchWordFindRequest) {
        storeSearchWordInDB(searchWordFindRequest.getWord());

        List<Counselor> counselors = counselorService.getCounselorByWordWithPagination(searchWordFindRequest, sortType);

        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor, false))
                .toList();
    }

    @Transactional
    @Override
    public void storeSearchWordInDB(String word) {
        SearchWord searchWord = searchWordRepository.findByWordAndIsActivatedTrue(word)
                .orElseGet(() -> SearchWord.builder().word(word).build());
        searchWord.increaseCount();

        searchWordRepository.save(searchWord);
    }

    private void storeSearchWordInRedis(Long customerId, String word) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        String redisKey = SEARCH_WORD_PREFIX + customerId;

        listOps.remove(redisKey, 0, word);

        listOps.leftPush(redisKey, word);

        listOps.trim(redisKey, 0, 4);
    }

    @Override
    public List<String> getRecentSearchWordsByCustomer(Long customerId) {
        return redisTemplate.opsForList().range(SEARCH_WORD_PREFIX + customerId, 0, -1);
    }

    @Override
    public void removeSearchWordByCustomer(Long customerId, SearchWordDeleteRequest searchWordDeleteRequest) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        listOps.remove(SEARCH_WORD_PREFIX + customerId, 0, searchWordDeleteRequest.getWord());
    }
}
