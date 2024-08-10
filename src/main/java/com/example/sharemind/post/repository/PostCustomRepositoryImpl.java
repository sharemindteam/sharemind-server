package com.example.sharemind.post.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.domain.QPost;
import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import com.querydsl.core.types.OrderSpecifier;
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
                        post.postStatus.in(PostStatus.TIME_OUT, PostStatus.COMPLETED),
                        post.isActivated.isTrue(),
                        lessThanFinishedAtAndPostId(postId, finishedAt)
                ).orderBy(post.finishedAt.desc(), post.postId.desc()).limit(size).fetch();
    }

    @Override
    public List<Post> findPopularityPosts(Long postId, LocalDateTime finishedAt, int size) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.isPublic.isTrue(),
                        post.postStatus.in(PostStatus.TIME_OUT, PostStatus.COMPLETED),
                        post.totalLike.goe(10),
                        post.isActivated.isTrue(),
                        lessThanFinishedAtAndPostId(postId, finishedAt)
                ).orderBy(post.finishedAt.desc(), post.postId.desc()).limit(size).fetch();
    }

    @Override
    public List<Post> getFirstPostByWordWithSortType(SearchWordPostFindRequest searchWordPostFindRequest,
                                                     String sortColumn, int size) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.isPublic.isTrue(),
                        post.isActivated.isTrue(),
                        post.postStatus.in(PostStatus.TIME_OUT, PostStatus.COMPLETED),
                        (post.title.contains(searchWordPostFindRequest.getWord())
                                .or(post.content.contains(searchWordPostFindRequest.getWord())))
                )
                .orderBy(getOrderSpecifier(sortColumn, post), post.postId.asc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<Post> getPostByWordWithSortType(SearchWordPostFindRequest searchWordPostFindRequest,
                                                String sortColumn, Post lastPost, int size) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.isPublic.isTrue(),
                        post.isActivated.isTrue(),
                        post.postStatus.in(PostStatus.TIME_OUT, PostStatus.COMPLETED),
                        (post.title.contains(searchWordPostFindRequest.getWord())
                                .or(post.content.contains(searchWordPostFindRequest.getWord()))),
                        getSortColumnCondition(post, sortColumn, lastPost)
                )
                .orderBy(getOrderSpecifier(sortColumn, post), post.postId.asc())
                .limit(size)
                .fetch();
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

    private OrderSpecifier<?> getOrderSpecifier(String sortColumn, QPost post) {
        return switch (sortColumn) {
            case "publishedAt" -> post.publishedAt.desc();
            case "totalComment" -> post.totalComment.desc();
            case "totalLike" -> post.totalLike.desc();
            default -> throw new PostException(PostErrorCode.POST_SORT_TYPE_NOT_FOUND, sortColumn);
        };
    }

    private BooleanExpression getSortColumnCondition(QPost post, String sortColumn, Post lastPost) {
        return switch (sortColumn) {
            case "publishedAt" -> post.publishedAt.before(lastPost.getPublishedAt());
            case "totalComment" -> post.totalComment.lt(lastPost.getTotalComment())
                    .or(post.totalComment.eq(lastPost.getTotalComment())
                            .and(post.postId.gt(lastPost.getPostId())));
            case "totalLike" -> post.totalLike.lt(lastPost.getTotalLike())
                    .or(post.totalLike.eq(lastPost.getTotalLike())
                            .and(post.postId.gt(lastPost.getPostId())));
            default -> throw new PostException(PostErrorCode.POST_SORT_TYPE_NOT_FOUND, sortColumn);
        };
    }
}
