package com.example.sharemind.post.application;

import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.*;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, Long customerId);

    List<Post> getUnpaidPrivatePosts();

    Post getPostByPostId(Long postId);

    void updatePost(PostUpdateRequest postUpdateRequest, Long customerId);

    PostGetIsSavedResponse getIsSaved(Long postId, Long customerId);

    PostGetResponse getPost(Long postId, Long customerId);

    List<PostGetListResponse> getPostsByCustomer(Boolean filter, Long postId, Long customerId);

    List<PostGetCounselorListResponse> getPostsByCounselor(Boolean filter, Long postId, Long customerId);

    List<PostGetListResponse> getPublicPostsByCustomer(Long postId, LocalDateTime finishedAt,
            Long customerId);

    List<PostGetPopularityResponse> getPopularityPosts();

    List<Long> getRandomPosts();

    PostGetResponse getCounselorPostContent(Long postId, Long customerId);

    Post checkAndGetCounselorPost(Long postId, Long customerId);
}
