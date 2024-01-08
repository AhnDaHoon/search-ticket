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
public class SearchMelonTicket implements SearchTicket{

    @Value("${melon.url}")
    private String url;

    @Override
    public List<SearchResponse> search(String searchText) {
        String connectionUrl = url + searchText;
        List<SearchResponse> searchResponseList = new ArrayList<>();
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(connectionUrl).get();

            // 필요한 정보를 포함하는 HTML 요소 선택
            Elements trs = document.select(".tbl_style02 tbody tr");

            // 선택한 요소에서 필요한 데이터 추출
            Elements showInfo = trs.select(".show_infor");
            Elements showLoc = trs.select("td.show_loc");
            Elements showDate = trs.select("td.show_date");

            for (int i = 0; i < showLoc.size(); i++) {
                String img = showInfo.get(i).select("div > a > img").attr("src");
                String icon = showInfo.get(i).select(".ico_list ").text();
                String url = showInfo.get(i).select(".infor_text > a").attr("href").substring(2); // ..잘라주고 웹 페이지에서 https://ticket.melon.com를 붙여줌
                String title = showInfo.get(i).select(".show_title").text();
                String loc = showLoc.get(i).text();
                String date = showDate.get(i).text();

                searchResponseList.add(new SearchResponse(img, icon, url, title, loc, date));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponseList;
    }
}
