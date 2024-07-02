package com.newportal.demo.dao;

import com.newportal.demo.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NewsDAO extends JpaRepository<News,Long> {


}
