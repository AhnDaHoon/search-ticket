package com.serchticket.dh.service;

import com.serchticket.dh.model.SearchResponseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SearchAllTicketTest implements SearchTicketTest{
    @Autowired
    private SearchInterparkTicketTest searchInterparkTicketTest;

    @Autowired
    private SearchMelonTicketTest searchMelonTicketTest;

    private final static String searchText = "박효신";

    @Override
    public List<SearchResponseTest> search(String searchText) {
        List<SearchResponseTest> searchInterpark = searchInterparkTicketTest.search(searchText);
        List<SearchResponseTest> searchMelon = searchMelonTicketTest.search(searchText);

        List<SearchResponseTest> searchResponseList = mergeAndSortDates(searchInterpark, searchMelon);

        return searchResponseList;
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
