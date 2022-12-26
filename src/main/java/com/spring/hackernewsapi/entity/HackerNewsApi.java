package com.spring.hackernewsapi.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "news_data")
public class HackerNewsApi {
	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "cretated_by")
	private String by;

	@Column(name = "created_date")
	private long time;

	@Column(name = "url")
	private String url;

	@Column(name = "score")
	private int score;

	@Column(name = "title")
	private String title;



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
