package com.example.sharemind.postScrap.application;

public interface PostScrapService {

    void createPostScrap(Long postId, Long customerId);

    void deletePostScrap(Long postId, Long customerId);
}
