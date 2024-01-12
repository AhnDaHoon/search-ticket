package com.serchticket.dh.service;

import com.serchticket.dh.model.SearchResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                String img = "https://tickets.interpark.com/" + elements.get(i).select("img").attr("src"); // 웹 페이지에서 를 붙여줌
                String url = "https://tickets.interpark.com/goods/" + elements.get(i).attr("data-prd-no");
                String title = elements.get(i).select(".result-ticket_goodsName__vbwnM").text();
                String loc = elements.get(i).select(".result-ticket_placeName__viaLo").text();

                String date = elements.get(i).select(".result-ticket_playDate__xHIcA").text();
                String startDate = "";
                String endDate = "";

                // 기존 endDate 6.29 -> 수정 endDate ~ 2023.06.29
                if(date.contains("~")) {
                    String[] dateArr = date.split(" ~ ");
                    startDate = parseMonthOrDay(dateArr[0]);
                    endDate = dateArr[1];

                    // endDate에 년도가 없을 때
                    if(endDate.length() < 6){
                        endDate = " ~ " + parseMonthOrDay(startDate.substring(0, 4)+"."+endDate);
                    }else if(endDate.length() > 6){  // endDate에 년도가 있을 때
                        endDate = " ~ " + parseMonthOrDay(endDate);
                    }
                    // 월, 일에 0붙이기 2023.6.1 -> 2023.06.01

                }else{
                    startDate = parseMonthOrDay(date);;
                    endDate = "";
                }

                String icon = "";
                searchResponseList.add(new SearchResponse("Interpark", img, icon, url, title, loc, startDate, startDate + endDate));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResponseList;
    }

    public String parseMonthOrDay(String date){
        String[] split = date.split("\\.");
        StringBuffer result = new StringBuffer();

        for (String s : split) {
            if(s.length() == 1){
                result.append(".0"+s);
            }else if(s.length() == 2){
                result.append("."+s);
            }else if(s.length() == 4){
                result.append(s);
            }
        }

        return result.toString();
    }
}
