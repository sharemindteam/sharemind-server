package com.example.sharemind.postLike.application;

public interface PostLikeService {

    void createPostLike(String postId, Long customerId);

    void deletePostLike(String postId, Long customerId);
}
