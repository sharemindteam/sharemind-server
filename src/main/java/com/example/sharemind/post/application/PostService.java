package com.example.sharemind.post.application;

import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.PostGetIsSavedResponse;
import com.example.sharemind.post.dto.response.PostGetResponse;
import java.util.List;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, Long customerId);

    List<Post> getUnpaidPrivatePosts();

    Post getPostByPostId(Long postId);

    void updatePost(PostUpdateRequest postUpdateRequest, Long customerId);

    PostGetIsSavedResponse getIsSaved(Long postId);

    PostGetResponse getPost(Long postId, Long customerId);

    List<PostGetResponse> getPostsByCustomer(Boolean filter, Long postId, Long customerId);

    List<Long> getRandomPosts();

    PostGetResponse getCounselorPostContent(Long postId);

    Post getProceedingPost(Long postId);

    Boolean checkCounselorReadAuthority(Long postId, Long customerId);
}
