package com.newportal.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newportal.demo.entity.News;
import com.newportal.demo.entity.Role;
import com.newportal.demo.entity.User;
import com.newportal.demo.exception.NewsAlreadyExistsExc;
import com.newportal.demo.exception.NewsNotFoundExc;
import com.newportal.demo.service.NewsService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {


    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    NewsService newsServiceMock;

    @Autowired
    MockMvc mockMvc;

    List<News> newsList;

    User author;
    Role role;
    News news1;
    News news2;
    News news3;
    News news4;


    @BeforeEach
    public void setup() {


        newsList = new ArrayList<>();
        role = new Role("ROLE_AUTHOR");

        author = new User(1L, "ertitn", "test123", "Nedim", "Ertit",
                Arrays.asList(role));

        news1 = new News(1L, "news1Title", "news1Content", author);
        news2 = new News(2L, "news2Title", "news2Content", author);
        news3 = new News(3L, "news3Title", "news3Content", author);
        news4 = new News(4L, "news4Title", "news4Content", author);


        newsList.add(news1);
        newsList.add(news2);
        newsList.add(news3);
        newsList.add(news4);


    }


    @Test
    public void httpTestForGettingAllNews() throws Exception {

        when(newsServiceMock.getALlNews()).thenReturn(newsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

    }

    @Test
    @DisplayName("Http Test For Get News By Id When User Not Exists")
    public void httpTestForGetNewsByIdWhenUserNotExists() throws Exception {

        when(newsServiceMock.findById(5)).thenThrow(NewsNotFoundExc.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/news/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName(" Successfull Http Test For Get News By Id")
    public void httpTestForGetNewsById() throws Exception {


        when(newsServiceMock.findById(4)).thenReturn(Optional.of(news4));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/news/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("news4Title")));

    }

    @Test
    @DisplayName("Successfull Http Test For Creating News")
    @WithMockUser(username = "ertitn", password = "test123", roles = "AUTHOR")
    public void httpTestForCreatingNews() throws Exception {

        News tempNews = new News(5L, "tempNewsTitle", "tempNewsContent", author);

        when(newsServiceMock.createNews(ArgumentMatchers.any(News.class), anyString())).thenReturn(tempNews);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempNews))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("tempNewsTitle")))
                .andExpect(jsonPath("$.content", is("tempNewsContent")));


    }

    @Test
    @DisplayName("Failed Http Test For Creating News When User Is Not Authenticated")
    @WithMockUser(username = "ertitn", password = "test123", roles = "AUTHOR")
    public void httpTestForCreatingNewsWhenUserIsNotAuthorized() throws Exception {

        News tempNews = new News(5L, "tempNewsTitle", "tempNewsContent", author);

        when(newsServiceMock.createNews(ArgumentMatchers.any(News.class),
                anyString())).thenThrow(AccessDeniedException.class);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempNews))
                )
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("Failed Http Test For Creating News When News ID Already Exists")
    @WithMockUser(username = "ertitn", password = "test123", roles = "AUTHOR")
    public void httpTestForCreatingNewsWhenNewsIdIsAlreadyExists() throws Exception {

        News tempNews = new News(5L, "tempNewsTitle", "tempNewsContent", author);

        when(newsServiceMock.createNews(ArgumentMatchers.any(News.class),
                anyString())).thenThrow(NewsAlreadyExistsExc.class);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempNews))
                )
                .andExpect(status().is4xxClientError());


    }


    @Test
    @DisplayName("Successfull Http Test For Updating News")
    @WithMockUser(username = "ertitn", password = "test123", roles = "AUTHOR")
    public void httpTestForUpdatingNews() throws Exception {
        news1.setContent("UpdatedContent");
        news1.setTitle("UpdatedTitle");

        when(newsServiceMock.updateNews(ArgumentMatchers.any(News.class), anyLong(), anyString())).thenReturn(news1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/news/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(news1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("UpdatedContent")))
                .andExpect(jsonPath("$.title", is("UpdatedTitle")));


    }


    @Test
    @DisplayName("Failed Http Test For Updating News When User Is Not Authorized")
    public void httpTestForUpdatingNewsWhenUserIsNotAuthorized() throws Exception {
        news1.setContent("UpdatedContent");
        news1.setTitle("UpdatedTitle");
        when(newsServiceMock.updateNews(ArgumentMatchers.any(News.class), anyLong(), anyString()))
                .thenThrow(AccessDeniedException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/news/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(news1)))
                .andExpect(status().is4xxClientError());


    }

    @Test
    @DisplayName("Failed Http Test For Updating News When News Id Is Not Exists")
    @WithMockUser(username = "ertitn", password = "test123", roles = "AUTHOR")
    public void httpTestForUpdatingNewsWhenNewsIsNotExist() throws Exception {
        news1.setContent("UpdatedContent");
        news1.setTitle("UpdatedTitle");
        when(newsServiceMock.updateNews(ArgumentMatchers.any(News.class), anyLong(), anyString()))
                .thenThrow(NewsNotFoundExc.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/news/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(news1)))
                .andExpect(status().is4xxClientError());


    }



    @Test
    @DisplayName("Successfull Http Test For Deleting News")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void httpTestForDeletingNews() throws Exception {
        when(newsServiceMock.deleteNews(anyLong())).thenReturn(true);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/news/{id}",3))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletion successfull"));

    }

    @Test
    @DisplayName("Failed Http Test For Deleting News When News Not Found")
    public void httpTestForDeletingNewsWhenNewsNotFound() throws Exception {
        when(newsServiceMock.deleteNews(anyLong())).thenThrow(NewsNotFoundExc.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/news/{id}",3))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Failed Http Test For Deleting News When User Is Not Authorized")
    public void httpTestForDeletingNewsWhenUserIsNotAuthorized() throws Exception {
        news1.setContent("UpdatedContent");
        news1.setTitle("UpdatedTitle");

        when(newsServiceMock.updateNews(ArgumentMatchers.any(News.class), anyLong(), anyString())).thenReturn(news1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/news/{id]",1)
                ).andExpect(status().is4xxClientError());
    }




}
