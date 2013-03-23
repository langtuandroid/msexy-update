package com.hdc.data;

import android.graphics.Bitmap;

public class Item {
	public String id;
	public String title;
	public String info;
	public String summary;
	public int width;
	public int height;
	public Bitmap img;
	public String download;
	public String file_3gp;
	public String duration;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFile_3gp() {
		return file_3gp;
	}

	public void setFile_3gp(String file_3gp) {
		this.file_3gp = file_3gp;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getIntrotext() {
		return introtext;
	}

	public void setIntrotext(String introtext) {
		this.introtext = introtext;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String introtext;
	public String file;

	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;

		if (this.title.length() > 27)
			this.title = title.substring(0, 27) + " ...";
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String src;
}
