package com.example.sharemind.postLike.application;

public interface PostLikeService {

    void createPostLike(Long postId, Long customerId);

    void deletePostLike(Long postId, Long customerId);
}
