package unit.service;

import com.charityconnector.entity.Charity;
import org.mockito.ArgumentMatcher;

public class SelectiveUpdateCharityMatcher extends ArgumentMatcher<Charity> {

    Charity thisObject;

    SelectiveUpdateCharityMatcher(Charity thisObject) {
        this.thisObject = thisObject;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Charity))
            return false;
        if (argument == thisObject)
            return true;
        if (argument.equals(thisObject))
            return true;
        if (thisObject.getId() != null && !thisObject.getId().equals(((Charity) argument).getId()))
            return false;
        if (thisObject.getDescription() != null && !thisObject.getDescription().equals(((Charity) argument).getDescription()))
            return false;
        if (thisObject.getLogoFile() != null && !thisObject.getLogoFile().equals(((Charity) argument).getLogoFile()))
            return false;
        return thisObject.getName() == null || thisObject.getName().equals(((Charity) argument).getName());
    }
}
