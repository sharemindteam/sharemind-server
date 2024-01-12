package com.example.sharemind.realtimeConsult.application;

import java.util.List;

public interface RealtimeConsultService {
    List<Long> getRealtimeConsult(Long customerId, Boolean isCustomer);
}
