package com.charityconnector.dao;

import com.charityconnector.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article[] findArticlesByCharityId(Long id);

}
