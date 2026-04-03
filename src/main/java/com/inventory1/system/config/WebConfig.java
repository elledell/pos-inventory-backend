package com.inventory1.system.config;

import com.inventory1.system.repository.StoreSettingsRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StoreSettingsRepository settingsRepository;

    public WebConfig(StoreSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Protect Inventory edits AND PIN changes
        registry.addInterceptor(new AdminInterceptor(settingsRepository))
                .addPathPatterns("/api/inventory/**", "/api/settings/update-pin");
    }
}