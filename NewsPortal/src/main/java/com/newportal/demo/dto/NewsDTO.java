package com.newportal.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private LocalDateTime modifiedDate;
    @JsonIgnore
    private long userId;
}
