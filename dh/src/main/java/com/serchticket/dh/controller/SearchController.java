package com.serchticket.dh.controller;

import com.serchticket.dh.service.SearchAllTicket;
import com.serchticket.dh.service.SearchInterparkTicket;
import com.serchticket.dh.service.SearchMelonTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchInterparkTicket searchInterparkTicket;

    private final SearchMelonTicket searchMelonTicket;

    private final SearchAllTicket searchAllTicket;

    @GetMapping("/search")
    public String search(@RequestParam(value = "searchText", required = false) String searchText, @RequestParam(value = "searchType", required = false) String searchType, Model model){
        System.out.println("searchText = " + searchText);
        if(searchText != null){
            if(searchType.equals("interpark")){
                model.addAttribute("dataList", searchInterparkTicket.search(searchText));
            }else if(searchType.equals("melon")){
                model.addAttribute("dataList", searchMelonTicket.search(searchText));
            }else{
                model.addAttribute("dataList", searchAllTicket.search(searchText));
            }
            model.addAttribute("searchText", searchText);
            model.addAttribute("searchType", searchType);
        }
        return "search";
    }
}
