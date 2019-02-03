package unit.service;

import com.charityconnector.entity.Paypal;
import org.mockito.ArgumentMatcher;

public class SelectiveUpdatePaypalMatcher extends ArgumentMatcher<Paypal>  {

    Paypal thisObject;

    SelectiveUpdatePaypalMatcher(Paypal thisObject) {
        this.thisObject = thisObject;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Paypal))
            return false;
        if (argument == thisObject)
            return true;
        if (argument.equals(thisObject))
            return true;
        if (thisObject.getId() != null && !thisObject.getId().equals(((Paypal) argument).getId()))
            return false;
        if (thisObject.getCharityId() != (((Paypal) argument).getCharityId()))
            return false;
        if (thisObject.getTransactionId() != null && !thisObject.getTransactionId().equals(((Paypal) argument).getTransactionId()))
            return false;
        return thisObject.getAmount() == (((Paypal) argument).getAmount());
    }
}
