package com.example.sharemind.postScrap.application;

import com.example.sharemind.postScrap.dto.response.PostScrapGetResponse;
import java.util.List;

public interface PostScrapService {

    void createPostScrap(Long postId, Long customerId);

    void deletePostScrap(Long postId, Long customerId);

    List<PostScrapGetResponse> getScrappedPosts(Long postScrapId, Long customerId);
}
