package com.spring.hackernewsapi.entity;

import java.util.List;

public class Comment {
	private List<Integer> kids;
	private String text;
	private String by;
	private int chidCount;

	public int getChidCount() {
		return chidCount;
	}

	public void setChidCount(int chidCount) {
		this.chidCount = chidCount;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	

	public List<Integer> getKids() {
		return kids;
	}

	public void setKids(List<Integer> kids) {
		this.kids = kids;
	}

}
