package com.example.sharemind.counselor.utils;

import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CounselorUtil {
    public static List<String> convertConsultCategories(Counselor counselor) {
        return counselor.getConsultCategories().stream()
                .map(ConsultCategory::getDisplayName)
                .toList();
    }

    public static List<String> convertConsultTypes(Counselor counselor) {
        return counselor.getConsultTypes().stream()
                .map(ConsultType::getDisplayName)
                .toList();
    }

    public static Map<String, List<String>> convertConsultTimes(Counselor counselor) {
        Map<String, List<String>> consultTimes = new HashMap<>();
        for (ConsultTime consultTime : counselor.getConsultTimes()) {
            consultTimes.put(consultTime.getDay().name(), consultTime.getTimes().stream().toList());
        }
        return consultTimes;
    }

    public static Map<String, Long> convertConsultCosts(Counselor counselor) {
        Map<String, Long> consultCosts = new HashMap<>();
        for (ConsultCost consultCost : counselor.getConsultCosts()) {
            consultCosts.put(consultCost.getConsultType().getDisplayName(), consultCost.getCost());
        }
        return consultCosts;
    }
}
