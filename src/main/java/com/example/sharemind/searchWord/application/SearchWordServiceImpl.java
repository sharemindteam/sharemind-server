package com.example.sharemind.searchWord.application;

import static com.example.sharemind.global.constants.Constants.REALTIME_COUNSELOR;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.response.CounselorGetListResponse;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.response.PostGetPublicListResponse;
import com.example.sharemind.postLike.repository.PostLikeRepository;
import com.example.sharemind.postScrap.repository.PostScrapRepository;
import com.example.sharemind.searchWord.domain.SearchWord;
import com.example.sharemind.searchWord.dto.request.SearchWordDeleteRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordCounselorFindRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
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
    private final PostService postService;
    private final SearchWordRepository searchWordRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, List<Long>> counselorRedisTemplate;

    @Transactional
    @Override
    public List<CounselorGetListResponse> storeSearchWordAndGetCounselorsByCustomer(Long customerId, String sortType,
                                                                                    SearchWordCounselorFindRequest searchWordCounselorFindRequest) {
        storeSearchWordInRedis(customerId, searchWordCounselorFindRequest.getWord());
        storeSearchWordInDB(searchWordCounselorFindRequest.getWord());

        List<Counselor> counselors = counselorService.getCounselorByWordWithPagination(searchWordCounselorFindRequest,
                sortType);
        List<Long> counselorIds = counselorRedisTemplate.opsForValue().get(REALTIME_COUNSELOR);
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Set<Long> wishListCounselorIds = wishListCounselorService.getWishListCounselorIdsByCustomer(customer);
        if (counselorIds == null) {
            return counselors.stream()
                    .map(counselor -> CounselorGetListResponse.of(counselor,
                            wishListCounselorIds.contains(counselor.getCounselorId()), false))
                    .toList();
        }
        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor,
                        wishListCounselorIds.contains(counselor.getCounselorId()), counselorIds.contains(counselor.getCounselorId())))
                .toList();
    }

    @Transactional
    @Override
    public List<CounselorGetListResponse> storeAllSearchWordAndGetCounselors(String sortType,
                                                                             SearchWordCounselorFindRequest searchWordCounselorFindRequest) {
        storeSearchWordInDB(searchWordCounselorFindRequest.getWord());

        List<Counselor> counselors = counselorService.getCounselorByWordWithPagination(searchWordCounselorFindRequest,
                sortType);
        List<Long> counselorIds = counselorRedisTemplate.opsForValue().get(REALTIME_COUNSELOR);
        if (counselorIds == null) {
            return counselors.stream()
                    .map(counselor -> CounselorGetListResponse.of(counselor, false, false))
                    .toList();
        }
        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor, false, counselorIds.contains(counselor.getCounselorId())))
                .toList();
    }

    @Transactional
    @Override
    public List<PostGetPublicListResponse> storeAllSearchWordAndGetPosts(String sortType,
                                                                   SearchWordPostFindRequest searchWordPostFindRequest) {
        storeSearchWordInDB(searchWordPostFindRequest.getWord());
        List<Post> posts = postService.getPostByWordWithPagination(searchWordPostFindRequest, sortType);

        return posts.stream()
                .map(post -> PostGetPublicListResponse.of(post, false, false))
                .toList();
    }

    @Transactional
    @Override
    public List<PostGetPublicListResponse> storeSearchWordAndGetPosts(Long customerId, String sortType,
                                                                SearchWordPostFindRequest searchWordPostFindRequest) {
        storeSearchWordInRedis(customerId, searchWordPostFindRequest.getWord());
        storeSearchWordInDB(searchWordPostFindRequest.getWord());

        List<Post> posts = postService.getPostByWordWithPagination(searchWordPostFindRequest, sortType);

        Customer customer = customerService.getCustomerByCustomerId(customerId);

        return posts.stream()
                .map(post -> PostGetPublicListResponse.of(post,
                        postLikeRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post, customer),
                        postScrapRepository.existsByPostAndCustomerAndIsActivatedIsTrue(post, customer)))
                .toList();
    }

    @Override
    public List<SearchWord> getSearchWordsOrderByCount() {
        return searchWordRepository.findAllByOrderByCountDesc();
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
