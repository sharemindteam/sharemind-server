package com.example.sharemind.counselor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sharemind.counselor.CounselorDataFactory;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.CustomerDataFactory;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.repository.CustomerRepository;
import com.example.sharemind.global.config.QueryDslConfig;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
public class CounselorRepositoryTest {

    @Autowired
    private CounselorRepository counselorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        counselorRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        counselorRepository.deleteAll();
    }

    @Test
    @DisplayName("keyword를 포함하는 이메일 또는 닉네임을 가진 상담사 목록을 조회할 수 있다.")
    void testFindAllByNicknameOrEmail() {
        // given
        String emailPrefix = "sharemind";
        int size = 5;
        List<Customer> customers = CustomerDataFactory.createCustomers(emailPrefix, size);
        customerRepository.saveAll(customers);

        List<String> nicknames = Arrays.asList("apple", "april", "alice", "bacon", "banana");
        List<Counselor> counselors = CounselorDataFactory.createCounselors(nicknames);
        counselorRepository.saveAll(counselors);

        for (int i = 0; i < size; i++) {
            customers.get(i).setCounselor(counselors.get(i));
        }

        // when
        List<Counselor> testByEmailPrefix = counselorRepository.findAllByNicknameOrEmail(emailPrefix);
        List<Counselor> testByNicknameStartedWithA = counselorRepository.findAllByNicknameOrEmail("a");
        List<Counselor> testByNicknameStartedWithB = counselorRepository.findAllByNicknameOrEmail("b");
        List<Counselor> testByNicknameStartedWithAP = counselorRepository.findAllByNicknameOrEmail("ap");
        List<Counselor> testByNicknameStartedWithBAN = counselorRepository.findAllByNicknameOrEmail("ban");

        // then
        assertThat(testByEmailPrefix.size()).isEqualTo(size);
        assertThat(testByNicknameStartedWithA.size()).isEqualTo(5);
        assertThat(testByNicknameStartedWithB.size()).isEqualTo(2);
        assertThat(testByNicknameStartedWithAP.size()).isEqualTo(2);
        assertThat(testByNicknameStartedWithBAN.size()).isEqualTo(1);
    }
}
