package com.example.sharemind.searchWord.domain;

import com.example.sharemind.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SearchWord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private Long count;

    @Builder
    public SearchWord(String word) {
        this.word = word;
        this.count = 0L;
    }

    public void increaseCount() {
        this.count += 1;
    }
}
