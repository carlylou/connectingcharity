package unit.util;

import com.charityconnector.util.MailUtil;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MailUtilUnitTest {

    MailUtil mailUtil;

    @Test
    public void sendingEmailSucess() {
        String email = "wjb4249@gmail.com";
        String code = "123";
        Long charityID = 1L;
        int expectStatus = 1;
        mailUtil = new MailUtil(email,code,charityID);
        mailUtil.run();
        int returnStatus = mailUtil.getStatus();
        assertThat(returnStatus,equalTo(expectStatus));
    }

    @Test
    public void sendingEmailFail() {
        String email = "398712463";
        String code = "123";
        Long charityID = 1L;
        int expectStatus = -1;
        mailUtil = new MailUtil(email,code,charityID);
        mailUtil.run();
        int returnStatus = mailUtil.getStatus();
        assertThat(returnStatus,equalTo(expectStatus));
    }
}
