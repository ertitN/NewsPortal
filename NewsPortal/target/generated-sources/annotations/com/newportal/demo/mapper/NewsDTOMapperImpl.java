package com.newportal.demo.mapper;

import com.newportal.demo.dto.NewsDTO;
import com.newportal.demo.entity.News;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T20:06:29+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
@Component
public class NewsDTOMapperImpl extends NewsDTOMapper {

    @Override
    public NewsDTO newsToNewsDTO(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDTO newsDTO = new NewsDTO();

        newsDTO.setId( news.getId() );
        newsDTO.setTitle( news.getTitle() );
        newsDTO.setContent( news.getContent() );
        newsDTO.setPublishedDate( news.getPublishedDate() );
        newsDTO.setModifiedDate( news.getModifiedDate() );

        newsDTO.setUserId( news.getUser().getId() );

        return newsDTO;
    }
}
