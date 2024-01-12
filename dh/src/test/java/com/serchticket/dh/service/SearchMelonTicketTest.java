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

@Service
public class SearchMelonTicketTest implements SearchTicketTest {

    @Value("${melon.url}")
    private String url;

    @Override
    public List<SearchResponseTest> search(String searchText) {
        String connectionUrl = url + searchText;
        List<SearchResponseTest> searchResponseTestList = new ArrayList<>();
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(connectionUrl).get();

            // 필요한 정보를 포함하는 HTML 요소 선택
            Elements elements = document.select(".tbl_style02 tbody tr");

            // 선택한 요소에서 필요한 데이터 추출
            Elements showInfo = elements.select(".show_infor");
            Elements showLoc = elements.select("td.show_loc");
            Elements showDate = elements.select("td.show_date");

            for (int i = 0; i < showLoc.size(); i++) {
                String img = showInfo.get(i).select("div > a > img").attr("src");
                String icon = showInfo.get(i).select(".ico_list ").text();
                String url = "https://ticket.melon.com" + showInfo.get(i).select(".infor_text > a").attr("href").substring(2);
                String title = showInfo.get(i).select(".show_title").text();
                String loc = showLoc.get(i).text();

                String date = showDate.get(i).text();
                String startDate = "";
                String endDate = "";
                if(date.contains("-")){
                    String[] dateArr = date.split(" - ");
                    startDate = dateArr[0];
                    endDate = " ~ " + dateArr[1];
                }else{
                    startDate = date;
                    endDate = "";
                }

                searchResponseTestList.add(new SearchResponseTest("Melon", img, icon, url, title, loc, startDate, startDate + endDate));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponseTestList;
    }
}
