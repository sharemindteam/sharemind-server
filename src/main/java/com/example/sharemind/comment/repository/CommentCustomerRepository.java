package com.example.sharemind.comment.repository;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.counselor.domain.Counselor;

import java.util.List;

public interface CommentCustomerRepository {

    List<Comment> findAllByCounselorAndIsActivatedIsTrue(Counselor counselor, Boolean filter, Long postId, int size);
}
