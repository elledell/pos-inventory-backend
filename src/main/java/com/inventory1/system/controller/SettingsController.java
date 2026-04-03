package com.inventory1.system.controller;

import com.inventory1.system.entity.StoreSettings;
import com.inventory1.system.repository.StoreSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SettingsController {

    private final StoreSettingsRepository settingsRepository;

    // Helper to get settings or create default if it doesn't exist yet
    private StoreSettings getSettings() {
        return settingsRepository.findById(1L).orElse(new StoreSettings(1L, "0000"));
    }

    // React calls this to check if the typed PIN is correct
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPin(@RequestBody Map<String, String> request) {
        StoreSettings settings = getSettings();
        if (settings.getAdminPin().equals(request.get("pin"))) {
            return ResponseEntity.ok().build(); // 200 OK (PIN is correct)
        }
        return ResponseEntity.status(401).build(); // 401 Unauthorized (Wrong PIN)
    }

    // React calls this to change the PIN
    @PutMapping("/update-pin")
    public ResponseEntity<?> updatePin(@RequestBody Map<String, String> request) {
        StoreSettings settings = getSettings();
        settings.setAdminPin(request.get("newPin"));
        settingsRepository.save(settings);
        return ResponseEntity.ok("PIN Updated successfully!");
    }
}