package com.serchticket.dh.test;

import com.serchticket.dh.model.SearchResponseTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SearchMelonTicketTest {
    @Value("${melon.url}")
    private String melonUrl;
    private String searchText = "박효신";
    private String connectMelonUrl = null;

    @Autowired
    private com.serchticket.dh.service.SearchMelonTicketTest searchMelonTicket;

    @BeforeEach
    void melonSetup() {
        connectMelonUrl = melonUrl + searchText;
    }

    @Test
    void 연결_실패(){
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect("https://www.naver.com/").get();
            assertFalse(document.text().contains("멜론 티켓"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 연결_성공(){
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(connectMelonUrl).get();
            assertTrue(document.text().contains("멜론 티켓"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 데이터_영역_추출_실패(){
        try {
            Document document = Jsoup.connect(connectMelonUrl).get();

            Elements elements = document.select(".tbl_style02 tbody zxczxc");
            assertFalse(elements.text().length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 데이터_영역_추출_성공(){
        try {
            Document document = Jsoup.connect(connectMelonUrl).get();

            Elements elements = document.select(".tbl_style02 tbody tr");
            assertTrue(elements.text().length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 필요_정보_추출_성공(){
        List<SearchResponseTest> searchResponseTestList = new ArrayList<>();
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(connectMelonUrl).get();

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

                searchResponseTestList.add(new SearchResponseTest(img, icon, url, title, loc, startDate + endDate));
            }

            for (SearchResponseTest searchResponseTest : searchResponseTestList) {
                System.out.println("searchResponse.stateDate() = " + searchResponseTest.img());
                System.out.println("searchResponse.stateDate() = " + searchResponseTest.url());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("searchResponseList.size() = " + searchResponseTestList.size());
        assertTrue(searchResponseTestList.size() > 0);
    }
}
