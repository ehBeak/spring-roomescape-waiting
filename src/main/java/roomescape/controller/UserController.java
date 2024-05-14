package roomescape.controller;

import java.util.List;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.annotation.AuthenticationPrincipal;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.response.UserNameResponse;
import roomescape.model.Member;
import roomescape.service.AuthService;
import roomescape.service.UserService;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        Member member = userService.findUserByEmailAndPassword(request);
        Cookie cookie = authService.createCookieByUser(member);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserNameResponse> login(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new UserNameResponse(member.getName()));
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllUsers() {
        List<Member> members = userService.findAllUsers();
        return ResponseEntity.ok(members);
    }
}