package com.example.sharemind.searchWord.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.response.CounselorGetResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchWordServiceImpl implements SearchWordService {

    private final CounselorService counselorService;

    @Override
    public List<CounselorGetResponse> getSearchWordAndReturnResults(Long customerId, String word, int index) {
        //todo: 우선은 최신순으로 구현, 추후 인기순, 별점순 개발
        List<Counselor> counselors = counselorService.findCounselorByWordWithPagination(word, index);
    }

}
