package com.example.sharemind.postScrap.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.postScrap.domain.PostScrap;
import com.example.sharemind.postScrap.domain.QPostScrap;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostScrapCustomRepositoryImpl implements PostScrapCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QPostScrap postScrap = QPostScrap.postScrap;

    @Override
    public List<PostScrap> findAllByCustomerAndIsActivatedIsTrue(Customer customer,
            Long postScrapId, int size) {
        return jpaQueryFactory
                .selectFrom(postScrap)
                .where(
                        postScrap.customer.eq(customer),
                        postScrap.isActivated.isTrue(),
                        lessThanPostScrapId(postScrapId)
                ).orderBy(postScrap.postScrapId.desc()).limit(size).fetch();
    }

    private BooleanExpression lessThanPostScrapId(Long postScrapId) {
        return postScrapId != 0 ? postScrap.postScrapId.lt(postScrapId) : null;
    }
}
