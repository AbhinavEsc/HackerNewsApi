package com.spring.hackernewsapi.service;

import java.util.List;

import com.spring.hackernewsapi.entity.HackerNewsApi;
import com.spring.hackernewsapi.entity.HackerNewsApiArchival;

public interface HackerNewsApiService {

	int saveTopStories(HackerNewsApi hackerNewsApi);

	List<HackerNewsApi> getAllStories();

	List<HackerNewsApiArchival> getPastStories();



}
