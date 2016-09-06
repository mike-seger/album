package com.net128.app.album.audio;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.net128.app.album.helper.AudioInfo;
import com.swisscom.io.extwc.sdk.helper.ResourceLoader;

public class AudioInfoHelperAlt {
	public static AudioInfo audioInfo(URL url) throws UnsupportedAudioFileException, IOException {
		return audioInfo(AudioSystem.getAudioFileFormat(url));
	}
	
	public static AudioInfo audioInfo(String url) throws UnsupportedAudioFileException, IOException {
		return audioInfo(AudioSystem.getAudioFileFormat(new URL(url)));
	}
	
	public static AudioInfo audioInfo(Class<?> clazz, String location) throws UnsupportedAudioFileException, IOException {
		return audioInfo(AudioSystem.getAudioFileFormat(ResourceLoader.getClasspathFile(clazz, location)));
	}

	public static AudioInfo audioInfo(AudioFileFormat format) throws UnsupportedAudioFileException, IOException {
		AudioFormat audioFormat = format.getFormat();
		String encodingName=audioFormat.getEncoding().toString();
		String contentType;
		AudioInfo mediaInfo;
		Map<String,Object> props=format.properties();		
		if("MPEG1L3".equals(encodingName)||"MPEG2L3".equals(encodingName)) {
			contentType="audio/mpeg";
			mediaInfo=new AudioInfo(
				parseValue(props.get("duration"), 1000),
				parseValue(props.get("mp3.frequency.hz"), 1),
				getName(props, "album", "artist", "title"), contentType);
		} else if("VORBISENC".equals(encodingName)) {
			contentType="audio/vorbis";
			mediaInfo=new AudioInfo(
				parseValue(props.get("duration"), 1000),
				parseValue(props.get("ogg.frequency.hz"), 1),
				getName(props, "album", "artist", "title"), contentType);
		} else{
			contentType="audio/x-wav";
			mediaInfo=new AudioInfo(
				(int)Math.round(1000.0*format.getFrameLength()/audioFormat.getFrameRate()),
				(int)Math.round(audioFormat.getFrameRate()), null, contentType);
		}
		return mediaInfo;
	}
	
	private static Integer parseValue(Object value, int divisor) {
		if(value==null) {
			return null;
		}
		return (int)Math.round(Long.parseLong(value+"")*1.0/divisor);
	}
	
	public static String getName(Map<String,Object> props, String ... keys) {
		List<String> tokenList=new ArrayList<>();
		for(String key : keys) {
			Object o=props.get(key);
			if(o!=null && (o+"").trim().length()>0) {
				tokenList.add(o.toString().trim());
			}
		}
		return getName(tokenList);
	}
	
	public static String getName(String ... tokenList) {
		return getName(new ArrayList<>(Arrays.asList(tokenList)));
	}
	
	public static String getName(List<String> tokenList) {
		tokenList.removeAll(Collections.singleton(null));
		if(tokenList.size()==0) {
			return null;
		}
		return String.join(" / ", tokenList);
	}
}
