package com.example.sharemind.post.application;

import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.*;

import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, Long customerId);

    List<Post> getAllPosts();

    List<Post> getUnpaidPrivatePosts();

    Post getPostByPostId(Long postId);

    void updatePost(PostUpdateRequest postUpdateRequest, Long customerId);

    PostGetResponse getPost(String postId, Long customerId);

    List<PostGetCustomerListResponse> getPostsByCustomer(Boolean filter, String encryptedId,
            Long customerId);

    List<PostGetCounselorListResponse> getPostsByCounselor(Boolean filter, String encryptedId,
            Long customerId);

    List<PostGetPublicListResponse> getPublicPostsByCustomer(String encryptedId, LocalDateTime finishedAt,
            Long customerId);

    List<PostGetPublicListResponse> getPopularityPosts(String encryptedId, LocalDateTime finishedAt,
            Long customerId);

    List<Long> getRandomPosts();

    PostGetResponse getCounselorPostContent(String postId, Long customerId);

    Post checkAndGetCounselorPost(Long postId, Long customerId);

    List<Post> getPostByWordWithPagination(SearchWordPostFindRequest searchWordPostFindRequest,
            String sortType);

    Boolean getIsPostOwner(String postId, Long customerId);
}
