package com.charityconnector.serviceImpl;

import com.charityconnector.dao.ArticleRepository;
import com.charityconnector.entity.Article;
import com.charityconnector.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findOne(id);
    }

    @Override
    public Article[] findArticlesByCharityId(Long id) {
        return articleRepository.findArticlesByCharityId(id);
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.delete(id);
    }

    @Override
    public Article addArticle(Article article) {
        Date now = new Date();
        article.setInsertTime(now);
        article.setUpdateTime(now);
        return articleRepository.save(article);
    }

    @Override
    public void updateSelective(Article article) {
        Article readyToUpdate = new Article();
        if (article.getId() == null) {
            return;
        } else {
            Article origin = articleRepository.findOne(article.getId());

            readyToUpdate.setId(article.getId());

            String title = article.getTitle() == null ? origin.getTitle() : article.getTitle();
            readyToUpdate.setTitle(title);

            String body = article.getBody() == null ? origin.getBody() : article.getBody();
            readyToUpdate.setBody(body);

            Long cId = article.getCharityId() == null ? origin.getCharityId() : article.getCharityId();
            readyToUpdate.setCharityId(cId);
        }
        Date now = new Date();
        readyToUpdate.setUpdateTime(now);
        articleRepository.save(readyToUpdate);
    }

    @Override
    public void updateDirect(Article article) {
        Date now = new Date();
        article.setUpdateTime(now);
        articleRepository.save(article);
    }
}
