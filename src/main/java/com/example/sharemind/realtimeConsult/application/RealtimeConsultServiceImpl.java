package com.example.sharemind.realtimeConsult.application;

import com.example.sharemind.consult.repository.ConsultRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RealtimeConsultServiceImpl implements RealtimeConsultService {

    private final ConsultRepository consultRepository;

    @Override
    public List<Long> getRealtimeConsult(Long customerId) {
        return consultRepository.findRealtimeConsultIdsByCustomerId(customerId);
    }
}
