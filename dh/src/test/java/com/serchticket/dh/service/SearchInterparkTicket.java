package com.serchticket.dh.service;

import com.serchticket.dh.model.SearchResponse;
import com.serchticket.dh.service.SearchTicket;
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
public class SearchInterparkTicket implements SearchTicket {

    @Value("${interpark.first-url}")
    private String firstUrl;

    @Value("${interpark.last-url}")
    private String lastUrl;

    @Override
    public List<SearchResponse> search(String searchText) {
        String connectionUrl = firstUrl + searchText + lastUrl;
        List<SearchResponse> searchResponseList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(connectionUrl).get();

            Elements elements = document.select(".result-ticket_listWrapper__xcEo3 a");
            System.out.println("elements = " + elements.size());

            for (int i = 0; i < elements.size(); i++) {
                String url = "https://tickets.interpark.com/goods/"+elements.get(i).attr("data-prd-no");
                String img = elements.get(i).select("img").attr("src"); // 웹 페이지에서 https://tickets.interpark.com/를 붙여줌
                String title = elements.get(i).select(".result-ticket_goodsName__vbwnM").text();
                String loc = elements.get(i).select(".result-ticket_placeName__viaLo").text();
                String date = elements.get(i).select(".result-ticket_playDate__xHIcA").text();
                String icon = "";
                searchResponseList.add(new SearchResponse(img, icon, url, title, loc, date));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResponseList;
    }
}
