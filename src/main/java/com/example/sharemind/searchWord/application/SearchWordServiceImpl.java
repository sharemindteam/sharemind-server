package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import java.util.List;
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
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<CounselorGetResponse> getSearchWordAndReturnResults(Long customerId, String word, int index) {
        //todo: 우선은 최신순으로 구현, 추후 인기순, 별점순 개발
        storeSearchWordInRedis(customerId, word);

        List<Counselor> counselors = counselorService.findCounselorByWordWithPagination(word, index);


    }

    private void storeSearchWordInRedis(Long customerId, String word) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        String redisKey = SEARCH_WORD_PREFIX + customerId;

        listOps.remove(redisKey, 0, word);

        listOps.leftPush(redisKey, word);

        listOps.trim(redisKey, 0, 4);
    }

    @Override
    public List<String> getRecentSearchWords(Long customerId) { //최근 검색어 리턴해주는 거
        return redisTemplate.opsForList().range(SEARCH_WORD_PREFIX + customerId, 0, -1);
    }

    @Override
    public void removeSearchWord(Long customerId, String word) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        listOps.remove(SEARCH_WORD_PREFIX + customerId, 0, word);
    }
}