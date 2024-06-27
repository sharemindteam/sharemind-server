package com.example.sharemind.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.sharemind.counselor.CounselorDataFactory;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.Counselor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private CounselorService counselorService;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    @DisplayName("특정 상담사의 프로필 상태를 대기 중으로 수정할 수 있다.")
    void testUpdateCounselorPending() {
        // given
        Long counselorId = 1L;
        Counselor counselor = CounselorDataFactory.createCounselor();
        ReflectionTestUtils.setField(counselor, "counselorId", counselorId);

        when(counselorService.getCounselorByCounselorId(counselor.getCounselorId())).thenReturn(counselor);

        // when
        adminService.updateCounselorPending(counselor.getCounselorId());

        // then
        assertThat(counselor.getProfileStatus()).isEqualTo(ProfileStatus.EVALUATION_PENDING);
    }
}
