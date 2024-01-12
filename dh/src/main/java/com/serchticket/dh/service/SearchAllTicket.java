package com.serchticket.dh.service;


import com.serchticket.dh.model.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SearchAllTicket implements SearchTicket{
    @Autowired
    private SearchInterparkTicket searchInterparkTicket;

    @Autowired
    private SearchMelonTicket searchMelonTicket;

    private final static String searchText = "박효신";

    @Override
    public List<SearchResponse> search(String searchText) {
        List<SearchResponse> searchInterpark = searchInterparkTicket.search(searchText);
        List<SearchResponse> searchMelon = searchMelonTicket.search(searchText);

        List<SearchResponse> searchResponseList = mergeAndSortDates(searchInterpark, searchMelon);

        return searchResponseList;
    }

    /**
     * 병합 후 날짜순으로 정렬
     * @param list1
     * @param list2
     * @return
     */
    public static List<SearchResponse> mergeAndSortDates(List<SearchResponse> list1, List<SearchResponse> list2) {
        List<SearchResponse> mergedList = new ArrayList<>();
        mergedList.addAll(list1);
        mergedList.addAll(list2);

        // Record를 날짜로 정렬
        Collections.sort(mergedList, new Comparator<SearchResponse>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

            @Override
            public int compare(SearchResponse record1, SearchResponse record2) {
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
