package com.example.sharemind.counselor.repository;

import com.example.sharemind.counselor.domain.ProfileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRecordRepository extends JpaRepository<ProfileRecord, Long> {

}
