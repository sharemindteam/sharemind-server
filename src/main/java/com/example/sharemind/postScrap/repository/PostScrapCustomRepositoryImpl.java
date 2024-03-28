package com.example.sharemind.postScrap.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.postScrap.domain.PostScrap;
import com.example.sharemind.postScrap.domain.QPostScrap;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostScrapCustomRepositoryImpl implements PostScrapCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPostScrap postScrap = QPostScrap.postScrap;

    @Override
    public List<PostScrap> findAllByCustomerAndIsActivatedIsTrue(Customer customer,
            Long postScrapId, LocalDateTime updatedAt, int size) {
        return jpaQueryFactory
                .selectFrom(postScrap)
                .where(
                        postScrap.customer.eq(customer),
                        postScrap.isActivated.isTrue(),
                        lessThanUpdatedAtAndPostScrapId(postScrapId, updatedAt)
                ).orderBy(postScrap.updatedAt.desc(), postScrap.postScrapId.desc()).limit(size).fetch();
    }

    private BooleanExpression lessThanUpdatedAtAndPostScrapId(Long postScrapId,
            LocalDateTime updatedAt) {
        return postScrapId != 0 ? postScrap.updatedAt.lt(updatedAt)
                .or(postScrap.updatedAt.eq(updatedAt).and(postScrap.postScrapId.lt(postScrapId)))
                : null;
    }
}
