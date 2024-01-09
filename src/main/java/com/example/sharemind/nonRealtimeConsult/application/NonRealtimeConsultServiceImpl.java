package com.example.sharemind.nonRealtimeConsult.application;

import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
import com.example.sharemind.nonRealtimeConsult.exception.NonRealtimeConsultErrorCode;
import com.example.sharemind.nonRealtimeConsult.exception.NonRealtimeConsultException;
import com.example.sharemind.nonRealtimeConsult.repository.NonRealtimeConsultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NonRealtimeConsultServiceImpl implements NonRealtimeConsultService {

    private final NonRealtimeConsultRepository nonRealtimeConsultRepository;

    @Transactional
    @Override
    public NonRealtimeConsult createNonRealtimeConsult() {
        NonRealtimeConsult nonRealtimeConsult = NonRealtimeConsult.builder().build();
        nonRealtimeConsultRepository.save(nonRealtimeConsult);

        return nonRealtimeConsult;
    }

    @Override
    public NonRealtimeConsult getNonRealtimeConsultByNonReatimeConsultId(Long nonRealtimeConsultId) {
        return nonRealtimeConsultRepository.findByNonRealtimeIdAndIsActivatedIsTrue(nonRealtimeConsultId)
                .orElseThrow(() -> new NonRealtimeConsultException(NonRealtimeConsultErrorCode.NON_REALTIME_CONSULT_NOT_FOUND,
                        nonRealtimeConsultId.toString()));
    }
}
