package org.example.cowmatchingbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 로컬 개발
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                // 배포(베르셀 프리뷰/프로덕션) - 와일드카드는 patterns로
                .allowedOriginPatterns("https://*.vercel.app", "https://your-domain.com")
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Location")
                .allowCredentials(true)
                .maxAge(3600);
    }
}