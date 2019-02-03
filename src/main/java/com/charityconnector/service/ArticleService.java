package com.charityconnector.service;

import com.charityconnector.entity.Article;

public interface ArticleService {

    Article findById(Long id);

    Article[] findArticlesByCharityId(Long id);

    void deleteById(Long id);

    Article addArticle(Article article);

    void updateSelective(Article article);

    void updateDirect(Article article);
}
