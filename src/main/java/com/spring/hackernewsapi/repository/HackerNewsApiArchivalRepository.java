package com.spring.hackernewsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.hackernewsapi.entity.HackerNewsApiArchival;

public interface HackerNewsApiArchivalRepository extends JpaRepository <HackerNewsApiArchival,Long>{

}
