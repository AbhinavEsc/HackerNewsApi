package com.spring.hackernewsapi.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.spring.hackernewsapi.entity.Comment;
import com.spring.hackernewsapi.entity.HackerNewsApi;
import com.spring.hackernewsapi.entity.HackerNewsApiArchival;
import com.spring.hackernewsapi.service.HackerNewsApiService;

@RestController
public class HackerNewsApiController {

	@Autowired
	@Qualifier("hackerNewsApiService")
	HackerNewsApiService hackerNewsApiService;

	@GetMapping("/get-stories")
	public List<Integer> getTopStories() {
		String uri = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
		RestTemplate restTempalte = new RestTemplate();
		List<Integer> listOfAllNews = restTempalte.getForObject(uri, List.class);

		return listOfAllNews;
	}

	@GetMapping("/get-stories-data")
	public List<HackerNewsApi> getTopStoriesData() {
		RestTemplate restTempalte = new RestTemplate();
		List<HackerNewsApi> listofData = new ArrayList<>();
		List<Integer> idList = getTopStories();
		for (Integer id : idList) {
			String uri = "https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty";
			HackerNewsApi hackernewsApi = new HackerNewsApi();
			hackernewsApi = restTempalte.getForObject(uri, HackerNewsApi.class);
			listofData.add(hackernewsApi);
		}
		List<HackerNewsApi> sortedData = listofData.stream().sorted(Comparator.comparing(HackerNewsApi::getScore))
				.collect(Collectors.toList());

		List<HackerNewsApi> topTenStories = sortedData.subList(Math.max(sortedData.size() - 10, 0), sortedData.size());
		System.out.println(listofData);

		return topTenStories;
	}

	@GetMapping("/insert-stories")
	public List<HackerNewsApi> saveTopStories() {
		List<HackerNewsApi> topTenStories = getTopStoriesData();
		for (HackerNewsApi hackerNewsApi : topTenStories) {
			hackerNewsApiService.saveTopStories(hackerNewsApi);
		}
		return topTenStories;
	}

	@GetMapping("/top-stories")
	public List<HackerNewsApi> getAllStories() {
		List<HackerNewsApi> allStoriesSaved = new ArrayList<>();
		try {
			allStoriesSaved = hackerNewsApiService.getAllStories();
			if (allStoriesSaved.size() == 0) {
				allStoriesSaved = saveTopStories();
				List<HackerNewsApi> sortedData = allStoriesSaved.stream()
						.sorted(Comparator.comparing(HackerNewsApi::getScore).reversed()).collect(Collectors.toList());
				allStoriesSaved = sortedData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return allStoriesSaved;
	}

	@GetMapping("/past-stories")
	public List<HackerNewsApiArchival> getPastStories() {
		List<HackerNewsApiArchival> pastStories = new ArrayList<>();
		pastStories = hackerNewsApiService.getPastStories();
		return pastStories;

	}

	@GetMapping("/comments/{id}")
	public List<Comment> getComments(@PathVariable int id) {
		List<Comment> finalCommentsList = new ArrayList<>();
		try {
			RestTemplate restTempalte = new RestTemplate();
			List<Comment> comments = new ArrayList<>();
			String uri = "https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty";
			Comment comment = new Comment();
			comment = restTempalte.getForObject(uri, Comment.class);
			List<Integer> kids = new ArrayList<>();
			if (comment.getKids() != null) {
				comments.add(comment);
				kids = comments.get(0).getKids();
			} else {
				comments.add(comment);
				return comments;
			}
			comments.add(comment);

			for (Integer kid : kids) {
				uri = "https://hacker-news.firebaseio.com/v0/item/" + kid + ".json?print=pretty";
				Comment kidComments = new Comment();
				kidComments = restTempalte.getForObject(uri, Comment.class);
				if (kidComments.getKids() != null) {
					kidComments.setChidCount(kidComments.getKids().size());
				} else {
					kidComments.setChidCount(0);
				}
				comments.add(kidComments);
			}
			finalCommentsList = comments.stream().sorted(Comparator.comparing(Comment::getChidCount).reversed())
					.limit(10).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return finalCommentsList;
	}

}
