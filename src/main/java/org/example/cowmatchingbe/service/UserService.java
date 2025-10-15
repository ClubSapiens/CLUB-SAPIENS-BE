
package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.User;
import org.example.cowmatchingbe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    /** 가입: 없으면 생성, 있으면 아무 것도 안 함 */
//    @Transactional
//    public User signUp(Long id, String name, String gender) {
//        return userRepository.findById(id)
//                .orElseGet(() -> userRepository.save(
//                        User.builder()
//                                .id(id)               // 주의: users.id는 수동 PK
//                                .name(name)
//                                .gender(gender)
//                                .checkNum(0)
//                                .build()
//                ));
//    }

    @Transactional
    public User signUp(Long id, String name, String gender) {
        return userRepository.findById(id).map(u -> {
            if (!sameName(u.getName(), name)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "ID_ALREADY_REGISTERED_WITH_DIFFERENT_NAME");
            }
            // 필요 시 gender 보정만
            // if (gender != null && !gender.equals(u.getGender())) { u.setGender(gender); }
            return u;
        }).orElseGet(() -> userRepository.save(
                User.builder()
                        .id(id)               // users.id = 수동 PK
                        .name(normalize(name))
                        .gender(gender)
                        .checkNum(0)
                        .build()
        ));
    }

    /** 로그인: 존재하면 이름 업데이트, 없으면 생성 */
//    @Transactional
//    public User login(Long id, String name) {
//        User u = userRepository.findById(id)
//                .orElse(User.builder().id(id).checkNum(0).build());
//        u.setName(name);
//        return userRepository.save(u);
//    }
    /** 로그인: 존재 + 이름 일치만 통과, 이름 갱신 금지 */
    @Transactional
    public User login(Long id, String name) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        if (!sameName(user.getName(), name)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NAME_MISMATCH");
        }
        return user; // 이름 업데이트 절대 금지
    }


    public int getCheckNum(Long id) {
        return userRepository.findById(id)
                .map(User::getCheckNum)
                .orElse(0);
    }


    /** 본인 check_num +1 */
    @Transactional
    public void increaseCheckNum(Long id) {
        userRepository.findById(id).ifPresent(u -> {
            Integer c = u.getCheckNum();
            u.setCheckNum((c == null ? 0 : c) + 1);
        });
    }

    // --- helpers ---
    private static boolean sameName(String a, String b) {
        return normalize(a).equals(normalize(b));
    }

    private static String normalize(String s) {
        return (s == null) ? "" : s.trim().toLowerCase(Locale.ROOT);
    }
}

