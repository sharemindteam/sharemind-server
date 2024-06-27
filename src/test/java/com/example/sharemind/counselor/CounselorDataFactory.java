package com.example.sharemind.counselor;

import com.example.sharemind.counselor.domain.Counselor;
import java.util.ArrayList;
import java.util.List;

public class CounselorDataFactory {

    public static List<Counselor> createCounselors(List<String> nicknames) {
        List<Counselor> counselors = new ArrayList<>();
        for (String nickname : nicknames) {
            Counselor counselor = Counselor.builder()
                    .nickname(nickname)
                    .build();

            counselors.add(counselor);
        }

        return counselors;
    }
}
