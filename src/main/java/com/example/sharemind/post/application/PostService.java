package com.example.sharemind.post.application;

import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.*;

import com.example.sharemind.searchWord.dto.request.SearchWordPostFindRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, Long customerId);

    List<Post> getAllPosts();

    List<Post> getUnpaidPrivatePosts();

    Post getPostByPostId(Long postId);

    Post getPostByPostUuid(UUID postUuid);

    void updatePost(PostUpdateRequest postUpdateRequest, Long customerId);

    PostGetResponse getPost(UUID postId, Long customerId);

    List<PostGetCustomerListResponse> getPostsByCustomer(Boolean filter, UUID postId,
            Long customerId);

    List<PostGetCounselorListResponse> getPostsByCounselor(Boolean filter, UUID postUuid,
            Long customerId);

    List<PostGetPublicListResponse> getPublicPostsByCustomer(UUID postUuid, LocalDateTime finishedAt,
            Long customerId);

    List<PostGetPublicListResponse> getPopularityPosts(UUID postUuid, LocalDateTime finishedAt,
            Long customerId);

    List<UUID> getRandomPosts();

    PostGetResponse getCounselorPostContent(UUID postId, Long customerId);

    Post checkAndGetCounselorPost(Long postId, Long customerId);

    List<Post> getPostByWordWithPagination(SearchWordPostFindRequest searchWordPostFindRequest,
            String sortType);

    Boolean getIsPostOwner(UUID postUuid, Long customerId);
}
