package com.newportal.demo.service;

import com.newportal.demo.dao.NewsDAO;
import com.newportal.demo.entity.News;
import com.newportal.demo.entity.User;
import com.newportal.demo.exception.NewsAlreadyExistsExc;
import com.newportal.demo.exception.NewsNotFoundExc;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private UserService userService;


    public List<News> getALlNews(){

        return newsDAO.findAll();
    }

    @Transactional
    public News createNews(News news, String name){

      long userId = news.getUser().getId();

      User user = userService.findUserById(userId);

      Optional<News> tempNews = newsDAO.findById(news.getId());

        if (!user.getUserName().equals(name)){
            throw new AccessDeniedException("");
        }

        if (tempNews.isPresent()){
            throw new NewsAlreadyExistsExc("News with given id is exists!");
        }
        return newsDAO.save(news);
    }

    @Transactional
    public News updateNews(News news, Long id, String name) throws Exception {

        Optional<News> tempNews = findById(id);

        boolean isAdmin = tempNews.get().getUser().getRoles().contains("ROLE_ADMIN");;

        if (!tempNews.get().getUser().getUserName().equals(name) || isAdmin){
            throw new AccessDeniedException("You can only update your own news");
        }

        if (tempNews.isEmpty()) {
            throw new NewsNotFoundExc("News Not Found !");
        }


        tempNews.get().setContent(news.getContent());
        tempNews.get().setTitle(news.getTitle());
        tempNews.get().setModifiedDate(LocalDateTime.now());
        return newsDAO.save(tempNews.get());


    }


    @Transactional
    public boolean deleteNews(Long id){
        Optional<News> news = findById(id);

           newsDAO.delete(news.get());
           return true;


    }

    public Optional<News> findById(long id){
            Optional<News> news = newsDAO.findById(id);
        if(news.isEmpty()){
            throw new NewsNotFoundExc("News with "+id +" is doesn't exist !");

        }
        return news;
    }













}
