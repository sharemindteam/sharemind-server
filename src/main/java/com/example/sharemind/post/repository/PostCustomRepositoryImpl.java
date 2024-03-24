package com.example.sharemind.post.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.domain.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;

    @Override
    public List<Post> findAllByCustomerAndIsActivatedIsTrue(Customer customer, Boolean filter,
            Long postId, int size) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.customer.eq(customer),
                        postStatusIn(filter),
                        post.isPaid.isTrue(),
                        post.isActivated.isTrue(),
                        lessThanPostId(postId)
                ).orderBy(post.postId.desc()).limit(size).fetch();
    }

    @Override
    public List<Post> findAllByIsPublicAndIsActivatedIsTrue(Long postId, LocalDateTime finishedAt,
            int size) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.isPublic.isTrue(),
                        post.postStatus.eq(PostStatus.COMPLETED),
                        post.isActivated.isTrue(),
                        lessThanFinishedAtAndPostId(postId, finishedAt)
                ).orderBy(post.finishedAt.desc(), post.postId.desc()).limit(size).fetch();
    }

    private BooleanExpression postStatusIn(Boolean filter) {
        return filter ? post.postStatus.in(PostStatus.WAITING, PostStatus.PROCEEDING,
                PostStatus.REPORTED) : null;
    }

    private BooleanExpression lessThanPostId(Long postId) {
        return postId != 0 ? post.postId.lt(postId) : null;
    }

    private BooleanExpression lessThanFinishedAtAndPostId(Long postId, LocalDateTime finishedAt) {
        return postId != 0 ? post.finishedAt.lt(finishedAt)
                .or(post.finishedAt.eq(finishedAt).and(post.postId.lt(postId))) : null;
    }
}
