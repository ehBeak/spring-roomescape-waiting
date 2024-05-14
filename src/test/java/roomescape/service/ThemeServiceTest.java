package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;
import roomescape.service.fake.FakeThemeDao;

class ThemeServiceTest {

    private final FakeThemeDao fakeThemeDao = new FakeThemeDao();
    private final ThemeService themeService = new ThemeService(fakeThemeDao);

    @BeforeEach
    void setUp() {
        fakeThemeDao.clear();
    }

    @DisplayName("테마를 조회한다.")
    @Test
    void should_find_all_themes() {
        fakeThemeDao.addTheme(new Theme("리사", "공포", "image.jpg"));
        fakeThemeDao.addTheme(new Theme("네오", "스릴러", "image1.jpg"));

        assertThat(themeService.findAllThemes()).hasSize(2);
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void should_add_theme() {
        ThemeRequest themeRequest = new ThemeRequest("에버", "공포", "공포.jpg");

        themeService.addTheme(themeRequest);

        assertThat(themeService.findAllThemes()).hasSize(1);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void should_delete_theme() {
        fakeThemeDao.addTheme(new Theme(1L, "리사", "공포", "image.jpg"));
        fakeThemeDao.addTheme(new Theme(2L, "네오", "스릴러", "image1.jpg"));

        themeService.deleteTheme(1L);

        assertThat(themeService.findAllThemes()).hasSize(1);
    }

    @DisplayName("최근 일주일 간 가장 인기 있는 테마 10개를 조회한다.")
    @Test
    void should_find_popular_theme_of_week() {
        List<Theme> popularThemes = themeService.findPopularThemes();

        assertThat(popularThemes).containsExactly(
                new Theme(10L, "name10", "description10", "thumbnail10"),
                new Theme(9L, "name9", "description9", "thumbnail9"),
                new Theme(1L, "name1", "description1", "thumbnail1"),
                new Theme(2L, "name2", "description2", "thumbnail2"),
                new Theme(3L, "name3", "description3", "thumbnail3"),
                new Theme(4L, "name4", "description4", "thumbnail4"),
                new Theme(5L, "name5", "description5", "thumbnail5"),
                new Theme(6L, "name6", "description6", "thumbnail6"),
                new Theme(7L, "name7", "description7", "thumbnail7"),
                new Theme(8L, "name8", "description8", "thumbnail8")
        );
    }
}