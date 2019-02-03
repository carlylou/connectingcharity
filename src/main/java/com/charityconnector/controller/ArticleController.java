package com.charityconnector.controller;

import com.charityconnector.entity.Article;
import com.charityconnector.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @RequestMapping(path = "/articles/{charityId}", method = RequestMethod.GET)
    @ResponseBody
    Article[] getArticlesByCharityId(@PathVariable("charityId") Long charityId) {
        return articleService.findArticlesByCharityId(charityId);
    }

    @RequestMapping(path = "/article/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Article getArticleById(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }

    @RequestMapping(path = "/article/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteArticleById(@PathVariable("id") Long id) {
        articleService.deleteById(id);
    }

    @RequestMapping(path = "/article", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Article> addArticle(@RequestBody Article article) {
        Article res = articleService.addArticle(article);
        return new ResponseEntity<Article>(res, HttpStatus.OK);
    }

    @RequestMapping(path = "/article", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<String> updateArticle(@RequestBody Article article) {
        articleService.updateSelective(article);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
