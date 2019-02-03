package unit.service;

import com.charityconnector.dao.ArticleRepository;
import com.charityconnector.entity.Article;
import com.charityconnector.service.ArticleService;
import com.charityconnector.serviceImpl.ArticleServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ArticleServiceUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    ArticleService articleService;
    @Mock
    ArticleRepository mockRepo;

    //can use to capture changes in an object
    //@Captor
    //private ArgumentCaptor<Article> articleCaptor;

    @Before
    public void initService() {
        articleService = new ArticleServiceImpl(mockRepo);
    }

    @Test
    public void gettingArticleByExisthingId() {
        //creating expected return value
        Article article = new Article();
        article.setId(1L);

        //instructing mock repository
        when(mockRepo.findOne(1L)).thenReturn(article);

        //calling method under test
        Article returnedArticle = articleService.findById(1L);

        //checking return value
        assertThat(returnedArticle.getId(), is(1L));
    }

    @Test
    public void gettingArticlesByExisthingCharityId() {
        //creating expected return value
        Article article = new Article();
        article.setCharityId(1L);
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article);

        //instructing mock repository
        when(mockRepo.findArticlesByCharityId(1L)).thenReturn(articles.toArray(new Article[0]));

        //calling method under test
        Article[] returnedArticles = articleService.findArticlesByCharityId(1L);

        //checking return value
        assertThat(returnedArticles[0].getCharityId(), is(1L));
    }

    @Test
    public void deletingArticle() {
        articleService.deleteById(1L);

        //checking that mock repository's method delete has been called exactly one time with parameter 1L
        verify(mockRepo, times(1)).delete(1L);
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void addingArticle() {
        Article article = new Article();
        article.setCharityId(1L);

        articleService.addArticle(article);

        //checking that mock repository's method delete has been called exactly one time with parameter 1L
        verify(mockRepo, times(1)).save(article);
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void selectiveUpdatingArticle() {
        Article oldArticle = new Article(1L, "myarticle", "abody", 1L, new Date(), new Date());

        Article newArticle = new Article(1L, null, "adifferentbody", null, new Date(), null);

        when(mockRepo.findOne(1L)).thenReturn(oldArticle);

        articleService.updateSelective(newArticle);

        verify(mockRepo).save(argThat(new SelectiveUpdateArticleMatcher(newArticle)));
    }

    @Test
    public void directlyUpdatingArticle() {
        Article article = new Article();
        article.setCharityId(1L);

        articleService.updateDirect(article);

        //checking that mock repository's method delete has been called exactly one time with parameter 1L
        verify(mockRepo, times(1)).save(article);
        verifyNoMoreInteractions(mockRepo);
    }
}
