package com.serchticket.dh.service;

import com.serchticket.dh.model.SearchResponseTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
public class SearchInterparkTicketTest implements SearchTicketTest {

    @Value("${interpark.first-url}")
    private String firstUrl;

    @Value("${interpark.last-url}")
    private String lastUrl;

    @Override
    public List<SearchResponseTest> search(String searchText) {
        String connectionUrl = firstUrl + searchText + lastUrl;
        List<SearchResponseTest> searchResponseTestList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(connectionUrl).get();

            Elements elements = document.select(".result-ticket_listWrapper__xcEo3 a");
            System.out.println("elements = " + elements.size());

            for (int i = 0; i < elements.size(); i++) {
                String img = "https://tickets.interpark.com/" + elements.get(i).select("img").attr("src"); // 웹 페이지에서 를 붙여줌
                String url = "https://tickets.interpark.com/goods/" + elements.get(i).attr("data-prd-no");
                String title = elements.get(i).select(".result-ticket_goodsName__vbwnM").text();
                String loc = elements.get(i).select(".result-ticket_placeName__viaLo").text();

                String date = elements.get(i).select(".result-ticket_playDate__xHIcA").text();
                String startDate = "";
                String endDate = "";

                // 기존 endDate 6.29 -> 수정 endDate ~ 2023.6.29
                if(date.contains("~")){
                    String[] dateArr = date.split(" ~ ");
                    startDate = dateArr[0];
                    endDate = dateArr[1];
                    if(!endDate.contains("20")){
                        endDate = " ~ " + startDate.substring(0, 4)+"."+endDate;
                    }
                }else{
                    startDate = date;
                    endDate = "";
                }

                String icon = "";
                searchResponseTestList.add(new SearchResponseTest(img, icon, url, title, loc, startDate + endDate));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResponseTestList;
    }
}
