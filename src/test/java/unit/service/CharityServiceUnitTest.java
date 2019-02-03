package unit.service;

import com.charityconnector.dao.CharityRepository;
import com.charityconnector.entity.Charity;
import com.charityconnector.service.CharityService;
import com.charityconnector.serviceImpl.CharityServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class CharityServiceUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    CharityService charityService;

    @Mock
    CharityRepository mockRepo;

    @Before
    public void initService() {
        charityService = new CharityServiceImpl(mockRepo);
    }

    @Test
    public void gettingACharity() {
        Charity charity = new Charity();

        when(mockRepo.findOne(1L)).thenReturn(charity);

        Charity returnedCharity = charityService.getCharity();

        verify(mockRepo, times(1)).findOne(1L);
        assertThat(returnedCharity, equalTo(charity));
    }

    @Test
    public void addingCharity() {
        Charity charity = new Charity();

        when(mockRepo.save(charity)).thenReturn(charity);

        Charity returnedCharity = charityService.addCharity(charity);

        assertThat(returnedCharity, equalTo(charity));

        verify(mockRepo, times(1)).save(charity);
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void gettingCharitiesByExisthingName() {
        Charity charity = new Charity();
        Charity[] charities = {charity};
        Page<Charity> charityPage = new PageImpl(Arrays.asList(charities));

        when(mockRepo.findByNameLike("name", new PageRequest(0, 10))).thenReturn(charityPage);

        Page<Charity> returnedCharityPage = charityService.findByNameLike("name", new PageRequest(0, 10));

        verify(mockRepo, times(1)).findByNameLike("name", new PageRequest(0, 10));
        verifyNoMoreInteractions(mockRepo);
        assertThat(returnedCharityPage, equalTo(charityPage));
    }

    @Test
    public void gettingARandomCharity() {
        Charity charity = new Charity();

        when(mockRepo.findRandom()).thenReturn(charity);

        Charity returnedCharity = charityService.findRandom();

        verify(mockRepo, times(1)).findRandom();
        assertThat(returnedCharity, equalTo(charity));
    }

    @Test
    public void selectiveUpdatingCharity() {
        Charity oldCharity = new Charity(1L, "name", "description", "123", "email", "paypal", null, null, null,"verifyCode", 1, 1L,null);

        Charity newCharity = new Charity(1L, null, "adifferentDescription", null, "email2", "paypal", null, null, null, null, 0, 2L,null);

        when(mockRepo.findOne(1L)).thenReturn(oldCharity);

        charityService.updateSelective(newCharity);

        verify(mockRepo).save(argThat(new SelectiveUpdateCharityMatcher(newCharity)));
    }

    @Test
    public void directlyUpdatingCharity() {
        Charity charity = new Charity();

        charityService.updateDirect(charity);

        verify(mockRepo, times(1)).save(charity);
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void deletingCharity() {
        charityService.deleteById(1L);

        verify(mockRepo, times(1)).delete(1L);
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void gettingCharityByExisthingId() {
        Charity charity = new Charity();
        charity.setId(1L);

        when(mockRepo.findOne(1L)).thenReturn(charity);

        Charity returnedCharity = charityService.findById(1L);

        assertThat(returnedCharity.getId(), is(1L));
    }

    @Test
    public void gettingCharityByPage() {
        Charity charity = new Charity();
        charity.setId(1L);
        List<Charity> charities = new ArrayList<>();
        charities.add(charity);

        Pageable pageable = new PageRequest(0, 1);

        Page page = new PageImpl(charities);

        when(mockRepo.findAll(pageable)).thenReturn(page);

        Charity[] returnedCharities = charityService.findPaged(pageable);

        verify(mockRepo, times(1)).findAll(pageable);
        verifyNoMoreInteractions(mockRepo);

        assertThat(returnedCharities[0], equalTo(charity));
    }

    @Test
    public void gettingCharityByNameAndPage() {
        Charity charity = new Charity();
        charity.setId(1L);

        List<Charity> charities = new ArrayList<>();
        charities.add(charity);

        Pageable pageable = new PageRequest(0, 1);

        Page page = new PageImpl(charities);

        String name = "name";

        when(mockRepo.findByNameLike(name, pageable)).thenReturn(page);

        Page<Charity> returnedCharities = charityService.findByNameLike(name, pageable);

        verify(mockRepo, times(1)).findByNameLike(name, pageable);
        verifyNoMoreInteractions(mockRepo);

        assertThat(returnedCharities.getContent().get(0), equalTo(charity));
    }
}
