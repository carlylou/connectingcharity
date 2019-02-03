package unit.service;

import com.charityconnector.entity.Article;
import org.mockito.ArgumentMatcher;

public class SelectiveUpdateArticleMatcher extends ArgumentMatcher<Article> {

    Article thisObject;

    SelectiveUpdateArticleMatcher(Article thisObject) {
        this.thisObject = thisObject;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Article))
            return false;
        if (argument == thisObject)
            return true;
        if (argument.equals(thisObject))
            return true;
        if (thisObject.getId() != null && !thisObject.getId().equals(((Article) argument).getId()))
            return false;
        if (thisObject.getBody() != null && !thisObject.getBody().equals(((Article) argument).getBody()))
            return false;
        if (thisObject.getTitle() != null && !thisObject.getTitle().equals(((Article) argument).getTitle()))
            return false;
        return thisObject.getCharityId() == null || thisObject.getCharityId().equals(((Article) argument).getCharityId());
    }
}
