package com.example.sharemind.letter.application;

import com.example.sharemind.letter.domain.Letter;

public interface LetterService {
    Letter createLetter();

    Letter getLetterByLetterId(Long letterId);
}
