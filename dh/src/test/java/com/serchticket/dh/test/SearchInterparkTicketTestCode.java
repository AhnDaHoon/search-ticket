package com.serchticket.dh.test;

import com.serchticket.dh.model.SearchResponseTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class SearchInterparkTicketTestCode {
    @Value("${interpark.first-url}")
    private String firstUrl;

    @Value("${interpark.last-url}")
    private String lastUrl;

    private String searchText = "박효신";

    private String connectInterparkUrl = null;

    @BeforeEach
    void melonSetup() {
        connectInterparkUrl = firstUrl + searchText + lastUrl;
    }
    
    @Test
    void 연결_성공() {
        try {
            Document document = Jsoup.connect(connectInterparkUrl).get();
            String title = document.title();
            assertTrue(title.contains("인터파크"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 데이터_영역_추출_실패(){
        try {
            Document document = Jsoup.connect(firstUrl + "ㅋㅌㅊㅋㅌㅊ" + lastUrl).get();

            Elements elements = document.select(".result-ticket_wrapper__H41_U");
            System.out.println("trs = " + elements);
            assertFalse(elements.text().length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 데이터_영역_추출_성공(){
        try {
            Document document = Jsoup.connect(connectInterparkUrl).get();

            Elements elements = document.select(".result-ticket_wrapper__H41_U");
            System.out.println("trs = " + elements);
            assertTrue(elements.text().length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 필요_정보_추출_성공(){
        List<SearchResponseTest> searchResponseTestList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(connectInterparkUrl).get();

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
                searchResponseTestList.add(new SearchResponseTest("Interpark", img, icon, url, title, loc, startDate, startDate + endDate));
            }

            for (SearchResponseTest searchResponseTest : searchResponseTestList) {
                System.out.println("searchResponseTest.date() = " + searchResponseTest.date());
            }

            assertTrue(elements.size() == searchResponseTestList.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

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

