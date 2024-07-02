package com.newportal.demo.service;

import com.newportal.demo.dao.NewsDAO;
import com.newportal.demo.entity.News;
import com.newportal.demo.entity.Role;
import com.newportal.demo.entity.User;
import com.newportal.demo.exception.NewsAlreadyExistsExc;
import com.newportal.demo.exception.NewsNotFoundExc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource("/application-test.properties")
public class NewsServiceTest {
    @Mock
    NewsDAO newsDAO;

    @Mock
    UserService userService;


    @InjectMocks
    NewsService newsServiceMock;

    User user;
    News news;

    @BeforeEach
    public void setup() {
        user = new User(1L, "test", "test123", "Nedim", "Ertit", Arrays.asList(
                new Role("AUTHOR")
        ));

        news = new News(1L, "DenemeTitle", "DenemeContent", user);
    }

    @Test
    @DisplayName("Test For Finding All News")
    public void testFindAllNews() {
        User user = new User(1L, "test", "test123", "Nedim", "Ertit", Arrays.asList(
                new Role("AUTHOR")
        ));

        News news = new News(1L, "DenemeTitle", "DenemeContent", user);

        News news1 = new News(2L, "DenemeTitle1", "DenemeContent1", user);
        News news2 = new News(3L, "DenemeTitle2", "DenemeContent2", user);


        when(newsDAO.findAll()).thenReturn(Arrays.asList(news, news1, news2));

        List<News> actual = newsServiceMock.getALlNews();

        assertEquals(3, actual.size());


    }

    @Test
    @DisplayName("Successful Test For Finding News With Given Id")
    public void testForFindNews() {

        when(newsDAO.findById(anyLong())).thenReturn(Optional.of(news));

        assertEquals(1, newsServiceMock.findById(1).get().getId());

    }


    @Test
    @DisplayName("Create news when given id does not exist")
    public void test_When_Create_News_News_With_Given_Id_Does_Not_Exist() {

        String userName = user.getUserName();

        when(userService.findUserById(1L)).thenReturn(user);
        when(newsDAO.save(news)).thenReturn(news);


        assertNotNull(newsServiceMock.createNews(news, userName));

    }

    @Test
    @DisplayName("Failing creation test when given id is not exist")
    public void test_When_Given_Id_Is_Exists() {

        when(newsDAO.findById(news.getId())).thenReturn(Optional.of(news));
        when(userService.findUserById(1L)).thenReturn(user);

        assertThrows(NewsAlreadyExistsExc.class,
                () -> {
                    newsServiceMock.createNews(news, user.getUserName());
                }
        );


    }


    @Test
    @DisplayName("Failing creation  test when user is not authorized")
    public void test_When_User_Is_Not_Authorized_To_Create_News() {

        when(userService.findUserById(1L)).thenReturn(user);

        assertThrows(AccessDeniedException.class, () -> {
            newsServiceMock.createNews(news, "user");
        });


    }

    @Test
    @DisplayName("Successfull test for updating news")
    public void test_For_Update_Existing_News() throws Exception {


        News updatedNews = new News(1L, "UpdatedTitle", "updatedContent", user);

        when(newsDAO.findById(news.getId())).thenReturn(Optional.of(news));
        when(newsDAO.save(any())).thenReturn(updatedNews);

        assertEquals(updatedNews.getTitle(), newsServiceMock.updateNews(news, 1L, "test").getTitle());
        assertEquals(updatedNews.getId(), newsServiceMock.updateNews(news, 1L, "test").getId());
        assertEquals(updatedNews.getContent(), newsServiceMock.updateNews(news, 1L, "test").getContent());


    }


    @Test
    @DisplayName("Failed test for updating news when news not found")
    public void test_For_Updating_news_When_News_Not_Found() {

        when(newsDAO.findById(1L)).thenThrow(NewsNotFoundExc.class);

        assertThrows(NewsNotFoundExc.class, () -> {
            newsServiceMock.updateNews(news, 1L, "test");
        });


    }


    @Test
    @DisplayName("Failed test for updating news when user is not authorized")
    public void test_For_Updating_News_When_User_Is_Not_Authorized() throws Exception {

        when(newsDAO.findById(1L)).thenReturn(
                Optional.of(news)
        );

        assertThrows(AccessDeniedException.class,
                () -> {
                    newsServiceMock.updateNews(news, 1L, "wrongusername");
                });
    }

    @Test
    @DisplayName("Successfull test for deleting user when given user Not Found found")
    public void test_For_Deleting_User_When_Given_User_Not_Found() {

        when(newsDAO.findById(1L)).thenThrow(NewsNotFoundExc.class);

        assertThrows(NewsNotFoundExc.class,
                () -> {
                    newsServiceMock.deleteNews(1L);
                });


    }


}
