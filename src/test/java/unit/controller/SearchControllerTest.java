//package unit.controller;
//
//import com.charityconnector.controller.SearchController;
//import com.charityconnector.entity.Charity;
//import com.charityconnector.service.CharityService;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//public class SearchControllerTest {
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    @Mock
//    CharityService mockCharityService;
//    SearchController searchController;
//    //can use to capture changes in an object
//    @Captor
//    private ArgumentCaptor<PageRequest> pageReqCaptor;
//    @Captor
//    private ArgumentCaptor<String> nameCaptor;
//
//    @Before
//    public void initController() {
//        searchController = new SearchController(mockCharityService);
//    }
//
//    @Test
//    public void gettingResultsPageNullName() {
//        Map<String, Object> model = new HashMap<>();
//
////        searchController.getResultsPage(model, null, null, 0, 0, null);
//
//        assertTrue(model.isEmpty());
//    }
//
//    @Test
//    public void gettingResultsPageEmptyName() {
//        Map<String, Object> model = new HashMap<>();
//
////        searchController.getResultsPage(model, null, "", 0, 0, null);
//
//        assertTrue(model.isEmpty());
//    }
//
//    @Test
//    public void gettingResultNoCharities() {
//        Map<String, Object> model = new HashMap<>();
//
//        ArrayList<Charity> charities = new ArrayList<>();
//
//        Page<Charity> page = new PageImpl<Charity>(charities);
//
//        when(mockCharityService.findByNameLike(any(String.class), any(Pageable.class))).thenReturn(page);
//
//        searchController.getResultsPage(model, "", "", 0, 10, null);
//
//        assertTrue(model.isEmpty());
//    }
//
//    @Test
//    public void correctlyCallingFindByNameLike() {
//        Map<String, Object> model = new HashMap<>();
//
//        ArrayList<Charity> charities = new ArrayList<>();
//        charities.add(new Charity());
//
//        String name = "name";
//        String searchValue = "name";
//        int pageNumber = 5;
//        int pageSize = 10;
//
//        Page<Charity> page = new PageImpl<Charity>(charities);
//
//        when(mockCharityService.findByNameLike(any(String.class), any(Pageable.class))).thenReturn(page);
//
////        searchController.getResultsPage(model, name, searchValue, pageNumber, pageSize, null);
//
//        verify(mockCharityService).findByNameLike(nameCaptor.capture(), pageReqCaptor.capture());
//
//        assertThat(nameCaptor.getValue().indexOf('%'), equalTo(0));
//        assertThat(nameCaptor.getValue().lastIndexOf('%'), equalTo(nameCaptor.getValue().length() - 1));
//
//        assertThat(pageReqCaptor.getValue().getPageNumber(), equalTo(pageNumber));
//        assertThat(pageReqCaptor.getValue().getPageSize(), equalTo(pageSize));
//
//        assertThat(model.get("charities"), equalTo(charities));
//    }
//
//    @Test
//    public void correctlyFillingModel() {
//        Map<String, Object> model = new HashMap<>();
//
//        int pageNumber = 2;
//        int pageSize = 10;
//        long total = 50L;
//        int thisPageSize = 3;
//        String name = "name";
//        String searchKey="name";
//
//        ArrayList<Charity> charities = new ArrayList<>();
//        for (int i = 0; i < thisPageSize; i++)
//            charities.add(new Charity());
//
//        Page<Charity> page = new PageImpl<Charity>(charities, new PageRequest(pageNumber, pageSize), total);
//
//        when(mockCharityService.findByNameLike(any(String.class), any(Pageable.class))).thenReturn(page);
//
//        searchController.getResultsPage(model, name, searchKey, pageNumber, pageSize, null);
//
//        assertThat(model.get("charities"), equalTo(charities));
//        assertThat(model.get("numberOfResults"), equalTo(total));
//        assertThat(model.get("pageNumber"), equalTo(pageNumber));
//        assertThat(model.get("searchedName"), equalTo(name));
//        assertThat(model.get("pageSize"), equalTo(pageSize));
//        assertThat(model.get("thisPageSize"), equalTo(thisPageSize));
//    }
//
//
//}
