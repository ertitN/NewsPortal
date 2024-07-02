package com.newportal.demo.mapper;

import com.newportal.demo.dto.NewsDTO;
import com.newportal.demo.entity.News;
import com.newportal.demo.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class NewsDTOMapper {

    @Autowired
    UserService userService;

    @Mapping(expression = "java(news.getUser().getId())",target = "userId")
      public abstract NewsDTO newsToNewsDTO(News news);

     public News newsDTOToNews(NewsDTO newsDTO){
         if ( newsDTO == null ) {
             return null;
         }

         News news = new News();
         news.setPublishedDate( newsDTO.getPublishedDate() );
         news.setModifiedDate( newsDTO.getModifiedDate() );
         news.setId( newsDTO.getId() );
         news.setTitle( newsDTO.getTitle());
         news.setUser(userService.findUserById(newsDTO.getId()));
         news.setContent( newsDTO.getContent() );
         news.getUser().setId(newsDTO.getId());

         return news;
     };
}
