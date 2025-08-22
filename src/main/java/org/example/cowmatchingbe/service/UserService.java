
package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.User;
import org.example.cowmatchingbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    /** 가입: 없으면 생성, 있으면 아무 것도 안 함 */
    @Transactional
    public User signUp(Long id, String name, String gender) {
        return userRepository.findById(id)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .id(id)               // 주의: users.id는 수동 PK
                                .name(name)
                                .gender(gender)
                                .checkNum(0)
                                .build()
                ));
    }

    /** 로그인: 존재하면 이름 업데이트, 없으면 생성 */
    @Transactional
    public User login(Long id, String name) {
        User u = userRepository.findById(id)
                .orElse(User.builder().id(id).checkNum(0).build());
        u.setName(name);
        return userRepository.save(u);
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
    }}
