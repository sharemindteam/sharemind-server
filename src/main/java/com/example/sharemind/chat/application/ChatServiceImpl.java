package com.example.sharemind.chat.application;

import com.example.sharemind.consult.repository.ConsultRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConsultRepository consultRepository;

    @Override
    public List<Long> getChat(Long userId, Boolean isCustomer) {
        if (isCustomer) {
            return consultRepository.findChatIdsByCustomerId(userId);
        } else {
            return consultRepository.findChatIdsByCounselorId(userId);
        }
    }
}
