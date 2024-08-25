package com.example.sharemind.post.application;

import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.*;

import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PostService {

    Long createPost(PostCreateRequest postCreateRequest, Long customerId);

    List<Post> getAllPosts();

    List<Post> getUnpaidPrivatePosts();

    Post getPostByPostId(Long postId);

    Post getPostByPayAppId(String payAppId);

    void updatePost(PostUpdateRequest postUpdateRequest, Long customerId);

    PostGetResponse getPost(Long postId, Long customerId);

    List<PostGetCustomerListResponse> getPostsByCustomer(Boolean filter, Long postId,
            Long customerId);

    List<PostGetCounselorListResponse> getPostsByCounselor(Boolean filter, Long postId,
            Long customerId);

    List<PostGetPublicListResponse> getPublicPostsByCustomer(Long postId, LocalDateTime finishedAt,
            Long customerId);

    List<PostGetPublicListResponse> getPopularityPosts(Long postId, LocalDateTime finishedAt,
            Long customerId);

    List<Long> getRandomPosts();

    PostGetResponse getCounselorPostContent(Long postId, Long customerId);

    Post checkAndGetCounselorPost(Long postId, Long customerId);

    List<Post> getPostByWordWithPagination(SearchWordPostFindRequest searchWordPostFindRequest,
            String sortType);

    Boolean getIsPostOwner(Long postId, Long customerId);
}
