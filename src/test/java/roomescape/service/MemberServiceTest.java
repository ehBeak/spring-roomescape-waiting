package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static roomescape.model.Role.MEMBER;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.controller.request.UserLoginRequest;
import roomescape.exception.AuthenticationException;
import roomescape.exception.NotFoundException;
import roomescape.model.Member;
import roomescape.service.fake.FakeUserDao;

class MemberServiceTest {

    private final FakeUserDao userDao = new FakeUserDao();
    private final UserService userService = new UserService(userDao);

    @BeforeEach
    void setUp() {
        userDao.clear();
    }

    @DisplayName("아이디와 비밀번호가 같은 유저가 존재하면 해당 유저를 반환한다.")
    @Test
    void should_find_user_when_user_exist() {
        Member member = new Member(1L, "배키", MEMBER, "hello@email.com", "1234");
        userDao.addUser(member);
        UserLoginRequest request = new UserLoginRequest("1234", "hello@email.com");

        Member finduser = userService.findUserByEmailAndPassword(request);

        assertThat(finduser).isEqualTo(member);
    }

    @DisplayName("아이디와 비밀번호 같은 유저가 없으면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_user_not_exist() {
        Member member = new Member(1L, "배키", MEMBER, "hello@email.com", "1234");
        userDao.addUser(member);
        UserLoginRequest request = new UserLoginRequest("1111", "sun@email.com");

        assertThatThrownBy(() -> userService.findUserByEmailAndPassword(request))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("아이디를 통해 사용자 이름을 조회한다.")
    @Test
    void should_find_username_when_give_id() {
        Member member = new Member(1L, "배키", MEMBER, "hello@email.com", "1234");
        userDao.addUser(member);

        String userNameById = userService.findUserNameById(1L);

        assertThat(userNameById).isEqualTo("배키");
    }

    @DisplayName("주어진 아이디에 해당하는 사용자가 없으면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_user_id_not_exist() {
        assertThatThrownBy(() -> userService.findUserNameById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("아이디를 통해 사용자 이름을 조회한다.")
    @Test
    void should_find_user_when_give_id() {
        Member member = new Member(1L, "배키", MEMBER, "hello@email.com", "1234");
        userDao.addUser(member);

        Member memberById = userService.findUserById(1L);

        assertThat(memberById).isEqualTo(member);
    }

    @DisplayName("주어진 아이디에 해당하는 사용자가 없으면 예외가 발생한다.")
    @Test
    void should_not_find_user_and_throw_exception_when_user_id_not_exist() {
        assertThatThrownBy(() -> userService.findUserById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("모든 사용자를 조회한다.")
    @Test
    void should_find_all_users() {
        userDao.addUser(new Member(1L, "썬", MEMBER, "sun@email.com", "1111"));
        userDao.addUser(new Member(2L, "배키", MEMBER, "dmsgml@email.com", "2222"));

        List<Member> members = userService.findAllUsers();

        assertThat(members).hasSize(2);
    }
}