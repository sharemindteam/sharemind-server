package com.example.sharemind.global.exception;

import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chatMessage.exception.ChatMessageException;
import com.example.sharemind.comment.exception.CommentException;
import com.example.sharemind.commentLike.exception.CommentLikeException;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.email.exception.EmailException;
import com.example.sharemind.letter.exception.LetterException;
import com.example.sharemind.letterMessage.exception.LetterMessageException;
import com.example.sharemind.payApp.exception.PayAppException;
import com.example.sharemind.payment.exception.PaymentException;
import com.example.sharemind.post.exception.PostException;
import com.example.sharemind.postLike.exception.PostLikeException;
import com.example.sharemind.postScrap.exception.PostScrapException;
import com.example.sharemind.review.exception.ReviewException;
import com.example.sharemind.wishList.exception.WishListException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<CustomExceptionResponse> catchAuthException(AuthException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<CustomExceptionResponse> catchCustomerException(CustomerException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(CounselorException.class)
    public ResponseEntity<CustomExceptionResponse> catchCounselorException(CounselorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(ConsultException.class)
    public ResponseEntity<CustomExceptionResponse> catchConsultException(ConsultException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(WishListException.class)
    public ResponseEntity<CustomExceptionResponse> catchWishListException(WishListException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<CustomExceptionResponse> catchEmailException(EmailException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(LetterException.class)
    public ResponseEntity<CustomExceptionResponse> catchLetterException(LetterException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(LetterMessageException.class)
    public ResponseEntity<CustomExceptionResponse> catchLetterMessageException(LetterMessageException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<CustomExceptionResponse> catchChatException(ChatException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(ChatMessageException.class)
    public ResponseEntity<CustomExceptionResponse> catchChatMessageException(ChatMessageException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<CustomExceptionResponse> catchCommentException(CommentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(CommentLikeException.class)
    public ResponseEntity<CustomExceptionResponse> catchCommentLikeException(CommentLikeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<CustomExceptionResponse> catchReviewException(ReviewException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<CustomExceptionResponse> catchPaymentException(PaymentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<CustomExceptionResponse> catchPostException(PostException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(PostLikeException.class)
    public ResponseEntity<CustomExceptionResponse> catchPostLikeException(PostLikeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(PostScrapException.class)
    public ResponseEntity<CustomExceptionResponse> catchPostScrapException(PostScrapException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(PayAppException.class)
    public ResponseEntity<CustomExceptionResponse> catchPayAppException(PayAppException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<CustomExceptionResponse> catchGlobalException(GlobalException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> catchMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomExceptionResponse.of(HttpStatus.BAD_REQUEST.name(),
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomExceptionResponse> catchConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomExceptionResponse.of(HttpStatus.BAD_REQUEST.name(),
                        e.getConstraintViolations().stream().findFirst().get().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> catchException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }
}
