package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.CounselorGetPendingResponse;
import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ConsultService consultService;
    private final LetterService letterService;
    private final ChatService chatService;
    private final CounselorService counselorService;

    @Override
    public List<ConsultsGetUnpaidResponse> getUnpaidConsults() {
        return consultService.getUnpaidConsults().stream()
                .map(ConsultsGetUnpaidResponse::of)
                .toList();
    }

    @Transactional
    public void updateIsPaid(Long consultId) {

        Consult consult = consultService.getConsultByConsultId(consultId);
        if (consult.getIsPaid()) {
            throw new ConsultException(ConsultErrorCode.CONSULT_ALREADY_PAID, consultId.toString());
        }

        switch (consult.getConsultType()) {
            case LETTER -> {
                Letter letter = letterService.createLetter();

                consult.updateIsPaidAndLetter(letter);
            }
            case CHAT -> {
                Chat chat = chatService.createChat(consult);

                consult.updateIsPaidAndChat(chat);
            }
        }
    }

    @Override
    public List<CounselorGetPendingResponse> getPendingCounselors() {
        return counselorService.getEvaluationPendingConsults().stream()
                .map(CounselorGetPendingResponse::of)
                .toList();
    }
}
