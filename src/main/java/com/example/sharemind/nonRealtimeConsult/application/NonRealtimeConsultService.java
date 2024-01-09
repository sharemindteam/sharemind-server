package com.example.sharemind.nonRealtimeConsult.application;

import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;

public interface NonRealtimeConsultService {
    NonRealtimeConsult createNonRealtimeConsult();

    NonRealtimeConsult getNonRealtimeConsultByNonReatimeConsultId(Long nonRealtimeConsultId);
}
