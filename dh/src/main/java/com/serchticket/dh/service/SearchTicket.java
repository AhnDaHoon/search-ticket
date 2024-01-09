package com.serchticket.dh.service;

import com.serchticket.dh.model.SearchResponse;

import java.util.List;

public interface SearchTicket {

    public List<SearchResponse> search(String searchText);
}
