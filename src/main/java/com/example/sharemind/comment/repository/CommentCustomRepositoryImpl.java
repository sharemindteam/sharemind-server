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
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QComment comment = QComment.comment;
    private final QPost post = comment.post;

    @Override
    public List<Comment> findAllByCounselorAndIsActivatedIsTrue(Counselor counselor, Boolean filter, Long postId, int size) {
        return jpaQueryFactory
                .selectFrom(comment)
                .where(
                        commentByCounselor(counselor),
                        filterCondition(filter),
                        postIdCondition(postId)
                )
                .orderBy(post.postId.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression commentByCounselor(Counselor counselor) {
        return comment.isActivated.isTrue()
                .and(comment.counselor.eq(counselor));
    }

    private BooleanExpression filterCondition(Boolean filter) {
        if (Boolean.FALSE.equals(filter)) {
            return post.postStatus.eq(PostStatus.PROCEEDING);
        }
        return null;
    }

    private BooleanExpression postIdCondition(Long postId) {
        if (postId != null && postId != 0) {
            return post.postId.lt(postId);
        }
        return null;
    }
}
