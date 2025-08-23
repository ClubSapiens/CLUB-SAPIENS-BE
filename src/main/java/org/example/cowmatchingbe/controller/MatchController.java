package org.example.cowmatchingbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.Match;
import org.example.cowmatchingbe.domain.Profile;
import org.example.cowmatchingbe.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    public record CreateMatchReq(Long userId, Long targetId) {}

    /** 매칭 생성: 중복이면 204, 성공이면 200 */
    @PostMapping
    public ResponseEntity<Match> create(@RequestBody CreateMatchReq req) {
        var created = matchService.create(req.userId(), req.targetId());
        if (created == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(created);
    }

    /** 내가 매칭한 목록 */
    @GetMapping("/mine")
    public ResponseEntity<List<Match>> my(@RequestParam Long userId) {
        return ResponseEntity.ok(matchService.my(userId));
    }
    @GetMapping("/my/profiles")
    public ResponseEntity<List<Profile>> myProfiles(@RequestParam Long userId) {
        return ResponseEntity.ok(matchService.myMatchedProfiles(userId));
    }

}