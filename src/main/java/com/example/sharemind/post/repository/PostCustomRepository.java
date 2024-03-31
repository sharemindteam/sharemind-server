package com.example.sharemind.post.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllByCustomerAndIsActivatedIsTrue(Customer customer, Boolean filter,
            Long postId, int size);

    List<Post> findAllByIsPublicAndIsActivatedIsTrue(Long postId, LocalDateTime finishedAt,
            int size);

    List<Post> getPostByWordWithSortType(SearchWordPostFindRequest searchWordPostFindRequest,
                                           String sortColumn, Post lastPost, int size);

    List<Post> getFirstPostByWordWithSortType(SearchWordPostFindRequest searchWordPostFindRequest,
                                              String sortColumn, int size);
}
