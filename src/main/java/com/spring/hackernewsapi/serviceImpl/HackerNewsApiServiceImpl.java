package com.spring.hackernewsapi.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.hackernewsapi.entity.HackerNewsApi;
import com.spring.hackernewsapi.entity.HackerNewsApiArchival;
import com.spring.hackernewsapi.repository.HackerNewsApiArchivalRepository;
import com.spring.hackernewsapi.repository.HackerNewsApiRepository;
import com.spring.hackernewsapi.service.HackerNewsApiService;

@Service("hackerNewsApiService")
public class HackerNewsApiServiceImpl implements HackerNewsApiService {

	@Autowired
	private HackerNewsApiRepository hackerNewsApiRepository;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private HackerNewsApiArchivalRepository hackerNewsApiArchivalRepository;

	@Scheduled(cron = "0 0/15 * * * *")
	public void copyData() {
		try {
			List<HackerNewsApi> archivalList = new ArrayList<>();
			archivalList = hackerNewsApiRepository.findAll();
			if (archivalList.size() == 10) {
				for (HackerNewsApi hackerNewsApi : archivalList) {
					HackerNewsApiArchival hackerNewsApiArchival = new HackerNewsApiArchival();
					BeanUtils.copyProperties(hackerNewsApi, hackerNewsApiArchival);
					hackerNewsApiArchivalRepository.save(hackerNewsApiArchival);
				}
				hackerNewsApiRepository.deleteAll();
			} else if (archivalList.size() == 0) {
				// do nothing
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	@CachePut(value = "hackerStories", key = "#hackerNewsApi.id")
	public int saveTopStories(HackerNewsApi hackerNewsApi) {
		int result = 0;
		try {
			hackerNewsApiRepository.save(hackerNewsApi);
			result += 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	@Cacheable(value = "hackerStories")
	public List<HackerNewsApi> getAllStories() {
		return hackerNewsApiRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));

	}

	@Override
	public List<HackerNewsApiArchival> getPastStories() {
		List<HackerNewsApiArchival> pastStories = new ArrayList<>();
		try {
			pastStories = hackerNewsApiArchivalRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pastStories;
	}

}
