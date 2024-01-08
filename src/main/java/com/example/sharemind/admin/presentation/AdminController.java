package com.example.sharemind.admin.presentation;

import com.example.sharemind.admin.application.AdminService;
import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<ConsultsGetUnpaidResponse>> getUnpaidConsults() {
        return ResponseEntity.ok(adminService.getUnpaidConsults());
    }

    @PatchMapping("/{consultId}")
    public ResponseEntity<Void> updateIsPaid(@PathVariable Long consultId) {
        adminService.updateIsPaid(consultId);
        return ResponseEntity.ok().build();
    }
}
