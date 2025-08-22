package org.example.cowmatchingbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.Profile;
import org.example.cowmatchingbe.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    public record UpsertProfileReq(Long userId, String nickname, String mbti, String description, String instaProfile) {}

    /** 프로필 저장/수정(UPSERT) */
    @PostMapping
    public ResponseEntity<Profile> upsert(@RequestBody UpsertProfileReq req) {
        var saved = profileService.upsert(
                req.userId(), req.nickname(), req.mbti(), req.description(), req.instaProfile()
        );
        return ResponseEntity.ok(saved);
    }

    /** 페이지 조회: gender(옵션), offset, limit */
    @GetMapping("/page")
    public ResponseEntity<List<Profile>> page(
            @RequestParam(required = false) String gender,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "12") int limit
    ) {
        return ResponseEntity.ok(profileService.getProfilesPage(gender, offset, limit));
    }
}