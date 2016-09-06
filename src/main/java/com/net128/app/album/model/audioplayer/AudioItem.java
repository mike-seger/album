package com.net128.app.album.model.audioplayer;

import java.time.LocalTime;

import com.swisscom.io.extwc.sdk.types.JsonObject;

public class AudioItem extends JsonObject {
	public String url;
	public String name;
	public Long length;
	public String lengthString;
	public AudioItem(String url, String name, Long length) {
		super();
		this.url = url;
		this.name = name;
		this.length = length;
		if(length!=null) {
			lengthString=durationAsString(length);
		}
	}
	
	private final String durationAsString(Long durationMs) {
		int seconds = (int)Math.round(durationMs/1000.0); 
		LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
		String s=timeOfDay.toString();
		if(s.startsWith("00:")) {
			s=s.substring("00:".length());
		} 
		return s;
	}
}
