package com.example.sharemind.post.application;

import com.example.sharemind.post.dto.request.PostCreateRequest;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, Long customerId);
}
