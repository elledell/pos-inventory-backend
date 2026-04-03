package com.inventory1.system.config;

import com.inventory1.system.entity.StoreSettings;
import com.inventory1.system.repository.StoreSettingsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    private final StoreSettingsRepository settingsRepository;

    public AdminInterceptor(StoreSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("GET")) {
            return true;
        }

        String pin = request.getHeader("X-Admin-PIN");

        // Fetch the LIVE pin from the database
        StoreSettings settings = settingsRepository.findById(1L).orElse(new StoreSettings(1L, "0000"));

        if (settings.getAdminPin().equals(pin)) {
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}