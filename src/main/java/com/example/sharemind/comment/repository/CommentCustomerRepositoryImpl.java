package com.example.sharemind.comment.repository;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.domain.QComment;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentCustomerRepositoryImpl implements CommentCustomerRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QComment comment = QComment.comment;
    QPost post = QPost.post;

    @Override
    public List<Comment> findAllByCounselorAndIsActivatedIsTrue(Counselor counselor, Boolean filter, Long postId, int size) {

        BooleanExpression predicate = commentByCounselor(counselor)
                .and(filterCondition(filter))
                .and(postIdCondition(postId));

        return jpaQueryFactory
                .selectFrom(comment)
                .where(predicate)
                .orderBy(post.postId.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression commentByCounselor(Counselor counselor) {
        return QComment.comment.isActivated.isTrue()
                .and(QComment.comment.counselor.eq(counselor));
    }

    private BooleanExpression filterCondition(Boolean filter) {
        if (Boolean.FALSE.equals(filter)) {
            return QPost.post.postStatus.eq(PostStatus.PROCEEDING);
        }
        return null;
    }

    private BooleanExpression postIdCondition(Long postId) {
        if (postId != null && postId != 0) {
            return QPost.post.postId.lt(postId);
        }
        return null;
    }
}
