package com.serchticket.dh.test;

import com.serchticket.dh.model.SearchResponseTest;
import com.serchticket.dh.service.SearchAllTicket;
import com.serchticket.dh.service.SearchAllTicketTest;
import com.serchticket.dh.service.SearchInterparkTicketTest;
import com.serchticket.dh.service.SearchMelonTicketTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class SearchAllTicketCode {

    @Autowired
    private SearchInterparkTicketTest searchInterparkTicketTest;

    @Autowired
    private SearchMelonTicketTest searchMelonTicketTest;

    @Autowired
    private SearchAllTicketTest searchAllTicketTest;

    private final static String searchText = "박효신";

    @Test
    void 인터파크_멜론_데이터_날짜순으로_병합(){
        List<SearchResponseTest> searchInterpark = searchInterparkTicketTest.search(searchText);
        List<SearchResponseTest> searchMelon = searchMelonTicketTest.search(searchText);

        List<SearchResponseTest> searchResponseTests = mergeAndSortDates(searchInterpark, searchMelon);
        for (SearchResponseTest searchResponseTest : searchResponseTests) {
            System.out.println("searchResponseTest.type() = " + searchResponseTest.type());
            System.out.println("searchResponseTest.date() = " + searchResponseTest.date());
        }
    }

    @Test
    void searchTest(){
        List<SearchResponseTest> searchResponseList = searchAllTicketTest.search(searchText);
        for (SearchResponseTest searchResponseTest : searchResponseList) {
            System.out.println("searchResponseTest.date() = " + searchResponseTest.date());
        }

    }

    /**
     * 병합 후 날짜순으로 정렬
     * @param list1
     * @param list2
     * @return
     */
    public static List<SearchResponseTest> mergeAndSortDates(List<SearchResponseTest> list1, List<SearchResponseTest> list2) {
        List<SearchResponseTest> mergedList = new ArrayList<>();
        mergedList.addAll(list1);
        mergedList.addAll(list2);

        // Record를 날짜로 정렬
        Collections.sort(mergedList, new Comparator<SearchResponseTest>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

            @Override
            public int compare(SearchResponseTest record1, SearchResponseTest record2) {
                try {
                    Date dateObj1 = sdf.parse(record1.startDate());
                    Date dateObj2 = sdf.parse(record2.startDate());
                    return dateObj2.compareTo(dateObj1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return mergedList;
    }

}
