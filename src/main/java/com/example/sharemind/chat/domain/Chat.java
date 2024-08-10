package com.example.sharemind.chat.domain;

import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.chat.content.ChatStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "chat_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatStatus chatStatus = ChatStatus.WAITING;

    @Column(name = "customer_read_id", nullable = false)
    private Long customerReadId = 0L;

    @Column(name = "counseolor_read_id", nullable = false)
    private Long counselorReadId = 0L;

    @OneToOne(mappedBy = "chat")
    private Consult consult;

    @Column(name = "auto_refund", nullable = false)
    private Boolean autoRefund = true;

    public void updateChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;

        if (this.chatStatus.equals(ChatStatus.FINISH)) {
            this.consult.updateConsultStatusFinish();
        }
        if (this.chatStatus.equals(ChatStatus.ONGOING)) {
            this.updateStartedAt();

            this.consult.updateConsultStatusOnGoing();
        }
    }

    public ChatStatus changeChatStatusForChatList() {
        if (chatStatus == ChatStatus.SEND_REQUEST)
            return ChatStatus.WAITING;
        else if (chatStatus == ChatStatus.FIVE_MINUTE_LEFT || chatStatus == ChatStatus.TIME_OVER)
            return ChatStatus.ONGOING;
        else if (chatStatus == ChatStatus.COUNSELOR_CANCEL || chatStatus == ChatStatus.CUSTOMER_CANCEL)
            return ChatStatus.FINISH;
        return chatStatus;
    }

    public void checkChatAuthority(Chat chat, Boolean isCustomer, Customer customer) {
        if (isCustomer) {
            if (chat.getConsult().getCustomer() != customer) {
                throw new ChatException(ChatErrorCode.USER_NOT_IN_CHAT, chat.getChatId().toString());
            }
        } else if ((chat.getConsult().getCounselor() != customer.getCounselor())) {
            throw new ChatException(ChatErrorCode.USER_NOT_IN_CHAT, chat.getChatId().toString());
        }
    }

    public void updateStartedAt() {
        this.startedAt = LocalDateTime.now();
    }

    public void updateAutoRefundTrue() {
        this.autoRefund = true;
    }

    public void updateCustomerReadId(Long id) {
        this.customerReadId = id;
    }

    public void updateCounselorReadId(Long id) {
        this.counselorReadId = id;
    }

    public void setConsult(Consult consult) {
        this.consult = consult;
    }

    public static Chat newInstance() {
        return new Chat();
    }
}
