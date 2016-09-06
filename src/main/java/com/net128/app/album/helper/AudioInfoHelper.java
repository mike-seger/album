package com.net128.app.album.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

import com.swisscom.io.extwc.sdk.helper.ResourceLoader;

public class AudioInfoHelper {
	static {
		LogManager.getLogManager().reset();
	}
	public static AudioInfo audioInfo(String url, String mimeType) 
			throws UnsupportedAudioFileException, IOException, CannotReadException, 
			TagException, ReadOnlyFileException, InvalidAudioFrameException {
		File downloadFile=null;
		try {
			downloadFile=FileDownloader.downloadFromUrl(url, ext(mimeType));
			return audioInfo(downloadFile);
		} catch(Exception e) {
			throw new RuntimeException("Failed to get audio info from: "+url, e);
		} finally {
			if(downloadFile!=null) {
				downloadFile.delete();
			}
		}
	}
	
	private static String ext(String mimeType) {
		String ext=".tmp";
		if("audio/m4a".equals(mimeType) || "audio/alac".equals(mimeType)) {
			ext=".m4a";
		} else if("audio/mpeg".equals(mimeType) || "audio/mp3".equals(mimeType)) {
			ext=".mp3";
		} else if("audio/x-flac".equals(mimeType) || "audio/flac".equals(mimeType)) {
			ext=".flac";
		} else if("audio/vorbis".equals(mimeType)) {
			ext=".ogg";
		} else if("audio/x-wav".equals(mimeType) || "audio/wav".equals(mimeType)) {
			ext=".wav";
		}
		return ext;
	}

	public static AudioInfo audioInfo(Class<?> clazz, String location) throws UnsupportedAudioFileException, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		return audioInfo(ResourceLoader.getClasspathFile(clazz, location));
	}

	public static AudioInfo audioInfo(File file)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		AudioFile f = AudioFileIO.read(file);
		AudioHeader audioHeader = f.getAudioHeader();
		AudioInfo audioInfo=new AudioInfo();
		String format=audioHeader.getFormat()==null?"unknown":audioHeader.getFormat();
		if(format.startsWith("MPEG")) {
			audioInfo.contentType="audio/mpeg";
		} else if(format.startsWith("WAV ")) {
			audioInfo.contentType="audio/x-wav";
		} else if(format.startsWith("AAC")) {
			audioInfo.contentType="audio/m4a";
		} else if(format.startsWith("Apple Lossless")) {
			audioInfo.contentType="audio/m4a";
		} else if(format.startsWith("FLAC")) {
			audioInfo.contentType="audio/x-flac";
		} else if(format.startsWith("Ogg ")) {
			audioInfo.contentType="audio/vorbis";
		} else {
			audioInfo.contentType=format;
		}
		
		audioInfo.sampleRate=audioHeader.getSampleRateAsNumber();
		audioInfo.durationMs=(int)Math.round(audioHeader.getPreciseTrackLength()*1000.0);
		Iterator<TagField> tagFieldIt=f.getTag().getFields();
		String artist=null;
		String title=null;
		String album=null;
		while(tagFieldIt.hasNext()) {
			TagField tf=tagFieldIt.next();
			if("TIT2".equals(tf.getId())) {
				title=artist=getTagValue(tf.getRawContent());
			} else if("TT2".equals(tf.getId())) {
				title=artist=getTagValue(tf.getRawContent());
			} else if("TPE1".equals(tf.getId())) {
				artist=getTagValue(tf.getRawContent());
			} else if("TALB".equals(tf.getId())) {
				album=getTagValue(tf.getRawContent());
			}
		}
		audioInfo.name=AudioInfoHelper.getName(album, artist, title);
		return audioInfo;
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
	
	private static String getTagValue(byte [] data) {
		String result=new String(data, java.nio.charset.StandardCharsets.UTF_8).replaceAll("^.*\0", "").trim();
		if(result.length()==0) {
			return null;
		}
		return result;
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
