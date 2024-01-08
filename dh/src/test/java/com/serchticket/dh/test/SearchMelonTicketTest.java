package com.serchticket.dh.test;

import com.serchticket.dh.model.SearchResponse;
import com.serchticket.dh.service.SearchMelonTicket;
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
import java.util.List;

@SpringBootTest
public class SearchMelonTicketTest {
    @Value("${melon.url}")
    private String melonUrl;
    private String searchText = "박효신";
    private String connectMelonUrl = null;

    @Autowired
    private SearchMelonTicket searchMelonTicket;

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
    void tbody_추출_실패(){
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(connectMelonUrl).get();

            // 필요한 정보를 포함하는 HTML 요소 선택
            Elements trs = document.select(".tbl_style02 tbody zxczxc");
            assertFalse(trs.text().length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void tbody_추출_성공(){
        try {
            // Jsoup을 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(connectMelonUrl).get();

            // 필요한 정보를 포함하는 HTML 요소 선택
            Elements trs = document.select(".tbl_style02 tbody tr");
            assertTrue(trs.text().length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 필요_정보_추출_성공(){
        List<SearchResponse> searchResponseList = searchMelonTicket.search(searchText);
        System.out.println("searchResponseList.size() = " + searchResponseList.size());
        assertTrue(searchResponseList.size() > 0);
    }
}
