package unit.service;

import com.charityconnector.dao.PaypalRepository;
import com.charityconnector.entity.Paypal;
import com.charityconnector.service.PaypalService;
import com.charityconnector.serviceImpl.PaypalServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PaypalServiceUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    PaypalService paypalService;

    @Mock
    PaypalRepository paypalRepository;


    @Before
    public void initService() {
        paypalService = new PaypalServiceImpl(paypalRepository);
    }

    @Test
    public void testGetPaypal() {
        Paypal paypal = new Paypal();
        paypal.setId(1L);

        when(paypalRepository.findOne(1L)).thenReturn(paypal);
        Paypal returned = paypalService.findById(1L);

        assertThat(returned.getId(), is(1L));
    }

    @Test
    public void testAddPaypal() {
        Paypal paypal = new Paypal();
        paypal.setId(1L);

        paypalService.addPaypal(paypal);

        verify(paypalRepository, times(1)).save(paypal);
        verifyNoMoreInteractions(paypalRepository);
    }


    @Test
    public void testDeletePaypal() {
        paypalService.deleteById(1L);

        verify(paypalRepository, times(1)).delete(1L);
        verifyNoMoreInteractions(paypalRepository);
    }

    @Test
    public void testUpdatePaypal() {

        Paypal oldPaypal = new Paypal();
        Paypal newPaypal = new Paypal();

        oldPaypal.setId(1L);
        oldPaypal.setCharityId(2L);
        oldPaypal.setAmount(1000D);
        oldPaypal.setDate(new Date());
        oldPaypal.setTransactionId("xixi");

        newPaypal.setId(1L);
        newPaypal.setCharityId(3L);
        newPaypal.setAmount(10D);
        newPaypal.setDate(new Date());
        newPaypal.setTransactionId("hehe");
        when(paypalRepository.findOne(1L)).thenReturn(oldPaypal);

        paypalService.updateSelective(newPaypal);
        verify(paypalRepository).save(argThat(new SelectiveUpdatePaypalMatcher(newPaypal)));
    }
}
