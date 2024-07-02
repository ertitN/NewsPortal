package com.newportal.demo.controller;

import com.newportal.demo.config.CustomUserDetails;
import com.newportal.demo.dto.NewsDTO;
import com.newportal.demo.entity.News;
import com.newportal.demo.entity.User;
import com.newportal.demo.mapper.NewsDTOMapper;
import com.newportal.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private NewsService newsService;
    private NewsDTOMapper newsDTOMapper;


    @Autowired
    public NewsController(NewsService newsService, NewsDTOMapper newsDTOMapper) {
        this.newsService = newsService;
        this.newsDTOMapper = newsDTOMapper;
    }



    @GetMapping
    public List<NewsDTO> getAllNews(){
        List<News> newsList = newsService.getALlNews();
        List<NewsDTO> dtoList = new ArrayList<>();
        for(News news:newsList){
            dtoList.add(newsDTOMapper.newsToNewsDTO(news));
        }
    return dtoList ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNews(@PathVariable Long id){
        Optional<News> news = newsService.findById(id);
        NewsDTO dto = newsDTOMapper.newsToNewsDTO(news.get());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody News news,Principal principal){


        News tempNews = newsService.createNews(news,principal.getName());
        NewsDTO dto = newsDTOMapper.newsToNewsDTO(tempNews);
       return new ResponseEntity<>(dto,HttpStatus.OK);


    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @RequestBody News news, Principal principal) throws Exception {
        News updatedNews = newsService.updateNews(news,id,principal.getName());
        NewsDTO dto = newsDTOMapper.newsToNewsDTO(updatedNews);
        return  new ResponseEntity<>(dto,HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable Long id){
        newsService.deleteNews(id);
        return new ResponseEntity<>("Deletion successfull",HttpStatus.OK);

    }



}
