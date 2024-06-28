package com.example.sharemind.counselor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sharemind.counselor.CounselorDataFactory;
import com.example.sharemind.counselor.content.ProfileStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CounselorTest {

    @Test
    @DisplayName("상담사의 프로필 상태와 프로필 최근 수정일자를 수정할 수 있다.")
    void testUpdateProfileStatusAndProfileUpdatedAt() {
        // given
        Counselor counselor = CounselorDataFactory.createCounselor();

        // when
        ProfileStatus profileStatus = ProfileStatus.EVALUATION_PENDING;
        counselor.updateProfileStatusAndProfileUpdatedAt(profileStatus);

        // then
        assertThat(counselor.getProfileStatus()).isEqualTo(profileStatus);
        assertThat(counselor.getProfileUpdatedAt()).isAfter(LocalDateTime.now().minusSeconds(5));
    }
}
