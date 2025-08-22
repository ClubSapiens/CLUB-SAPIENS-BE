package org.example.cowmatchingbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.User;
import org.example.cowmatchingbe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public record LoginReq(Long id, String name) {}
    public record SignUpReq(Long id, String name, String gender) {}

    /** 로그인: 있으면 이름 업데이트, 없으면 생성 */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginReq req) {
        return ResponseEntity.ok(userService.login(req.id(), req.name()));
    }

    /** 회원가입(없으면 생성, 있으면 그대로) */
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpReq req) {
        return ResponseEntity.ok(userService.signUp(req.id(), req.name(), req.gender()));
    }

    /** 존재 여부 */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(userService.exists(id));
    }

    /** check_num +1 */
    @PostMapping("/{id}/checknum/increase")
    public ResponseEntity<Void> increase(@PathVariable Long id) {
        userService.increaseCheckNum(id);
        return ResponseEntity.noContent().build();
    }
}