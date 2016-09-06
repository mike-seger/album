package com.net128.app.album.audio;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.LogManager;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net128.app.album.helper.AudioInfo;
import com.net128.app.album.helper.AudioInfoHelper;

public class AudioTest {
	private final static Logger logger = LoggerFactory.getLogger(AudioTest.class);

	// ls -rt | sort | while read f ; do n=$(echo "$f"|sed -e "s/\..*//"|tr -- "- " "__"); \
	//	e=$(echo "$f"|sed -e "s/.*\.//"); echo "private final String ${n}_${e}=\"$f\";" ; done

	//private final String AAC_8khz_Mono_5_aac="AAC_8khz_Mono_5.m4a";
	private final String iTunes_test1_m4a="iTunes_test1.m4a";
	private final String tone_mp3="tone.mp3";
	private final String tone16bit_flac="tone16bit.flac";
	private final String tone16bit_m4a="tone16bit.m4a";
	private final String tone24bit_flac="tone24bit.flac";
	private final String tone24bit_m4a="tone24bit.m4a";

	private final String Drone_Dark_Suspense_3_mp3 = "Drone Dark Suspense 3.mp3";
	private final String ogg1="ogg1.ogg";
	private final String dvi_5s_wav="dvi-5s.wav";
	private final String g711_ulaw_5s_wav="g711-ulaw-5s.wav";
	private final String gsm_5s_wav="gsm-5s.wav";
	private final String lpc10_5s_wav="lpc10-5s.wav";
	private final String woman2_48_wav="woman2_48.wav";
	private final String woman2_64_wav="woman2_64.wav";
	private final String WindowsSoundRecorder_wav="WindowsSoundRecorder.wav";
	private final String HugeWAV_wav="HugeWAV.wav";
	private final String BlueEyes_mp3="BlueEyes.mp3";
	private final String Enchantment_mp3="Enchantment.mp3";
	private final String Dogies_mp3="Dogies.mp3";
	
	private class UrlInfo {
		public String url;
		public String mimetype;
		public UrlInfo(String url, String mimetype) {
			super();
			this.url = url;
			this.mimetype = mimetype;
		}
	}
	
	private UrlInfo [] urlInfos = {
//		"http://download.wavetlan.com/SVV/Media/HTTP/AAC/iTunes/iTunes_test1_AAC-LC_v4_Stereo_VBR_128kbps_44100Hz.m4a",
//		"http://download.wavetlan.com/SVV/Media/HTTP/AAC_8khz_Mono_5.aac",
		new UrlInfo("http://download.linnrecords.com/test/m4a/tone16bit.aspx", "audio/m4a"),
		new UrlInfo("http://download.linnrecords.com/test/flac/tone16bit.aspx", "audio/x-flac"),
		new UrlInfo("http://download.linnrecords.com/test/flac/tone16bit.aspx", "audio/x-flac"),
		new UrlInfo("http://download.linnrecords.com/test/m4a/tone24bit.aspx", "audio/m4a"),
		new UrlInfo("http://download.linnrecords.com/test/flac/tone24bit.aspx", "audio/x-flac"),
		new UrlInfo("http://download.linnrecords.com/test/mp3/tone.aspx", "audio/mpeg")
	};

	@Test
	public void testFiles()
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, UnsupportedAudioFileException {
		LogManager.getLogManager().reset();
		//assertNetJaudiotagger(AAC_8khz_Mono_5_aac, new AudioInfo(17660, 44100, null, "audio/x-wav"));
		assertNetJaudiotagger(iTunes_test1_m4a, new AudioInfo(307000, 44100, null, "audio/m4a"));
		assertNetJaudiotagger(tone_mp3, new AudioInfo(5042, 44100, null, "audio/mpeg"));
		assertNetJaudiotagger(tone16bit_flac, new AudioInfo(5000, 44100, null, "audio/x-flac"));
		assertNetJaudiotagger(tone16bit_m4a, new AudioInfo(5000, 44100, null, "audio/m4a"));
		assertNetJaudiotagger(tone24bit_flac, new AudioInfo(5000, 88200, null, "audio/x-flac"));
		assertNetJaudiotagger(tone24bit_m4a, new AudioInfo(5000, 88200, null, "audio/m4a"));
		assertNetJaudiotagger(Drone_Dark_Suspense_3_mp3, new AudioInfo(30041, 44100, "Drone Dark Suspense 03 / Drone Dark Suspense 03", "audio/mpeg"));
		assertNetJaudiotagger(HugeWAV_wav, new AudioInfo(17660, 44100, null, "audio/x-wav"));
		assertNetJaudiotagger(ogg1, new AudioInfo(307566, 44100, null, "audio/vorbis"));
		assertNetJaudiotagger(g711_ulaw_5s_wav, new AudioInfo(5504, 8000, null, "audio/x-wav"));
		assertNetJaudiotagger(gsm_5s_wav, new AudioInfo(5500, 8000, null, "audio/x-wav"));
		assertNetJaudiotagger(dvi_5s_wav, new AudioInfo(5504, 8000, null, "audio/x-wav"));
		assertNetJaudiotagger(lpc10_5s_wav, new AudioInfo(5504, 8000, null, "audio/x-wav"));
		assertNetJaudiotagger(woman2_48_wav, new AudioInfo(12280, 16000, null, "audio/x-wav"));
		assertNetJaudiotagger(woman2_64_wav, new AudioInfo(12280, 16000, null, "audio/x-wav"));
		assertNetJaudiotagger(WindowsSoundRecorder_wav, new AudioInfo(10374, 44100, null, "audio/x-wav"));
		assertNetJaudiotagger(BlueEyes_mp3, new AudioInfo(13401, 44100, "My Music At Home / The Tragically Hip / My Music At Home", "audio/mpeg"));
		assertNetJaudiotagger(Enchantment_mp3, new AudioInfo(33384, 22050, "Enchantment", "audio/mpeg"));
		assertNetJaudiotagger(Dogies_mp3, new AudioInfo(12095, 44100, "Big Ones / Aerosmith / Love In An Elevator", "audio/mpeg"));
	}
	
	@Test
	public void testUrls() throws UnsupportedAudioFileException, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		for(UrlInfo urlInfo : urlInfos) {
			AudioInfo audioInfo=AudioInfoHelper.audioInfo(urlInfo.url, urlInfo.mimetype);
			logger.info(audioInfo.toString());
		}
	}
	
	
	private void assertNetJaudiotagger(String location, AudioInfo audioInfo) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, UnsupportedAudioFileException {
		//getNetJaudiotagger(location);
		assertEquals("Location: "+location, audioInfo, AudioInfoHelper.audioInfo(getClass(), location));
	}
/*
	@Test
	public void testMpatric() throws UnsupportedTagException, InvalidDataException, IOException {
		Mp3File mp3file = new Mp3File(ResourceLoader.getClasspathFile(getClass(), Drone_Dark_Suspense_3_mp3));
		assertEquals(30067, mp3file.getLengthInMilliseconds());
		assertEquals(320, mp3file.getBitrate());
		assertEquals(44100, mp3file.getSampleRate());
	}

	@Test
	public void testAudioFormat() throws UnsupportedAudioFileException, IOException {
		//audioFormatAssert(iTunes_test1_m4a, new AudioInfo(307000, 44100, null, "audio/m4a"));
		//audioFormatAssert(tone_mp3, new AudioInfo(5042, 44100, null, "audio/mpeg"));
		//audioFormatAssert(tone16bit_flac, new AudioInfo(5000, 44100, null, "audio/x-flac"));
		//audioFormatAssert(tone16bit_m4a, new AudioInfo(5000, 44100, null, "audio/m4a"));
		//audioFormatAssert(tone24bit_flac, new AudioInfo(5000, 88200, null, "audio/x-flac"));
		//audioFormatAssert(tone24bit_m4a, new AudioInfo(5000, 88200, null, "audio/m4a"));
		//audioFormatAssert(HugeWAV_wav, new AudioInfo(17660, 44100, null, "audio/x-wav"));
		//audioFormatAssert(Drone_Dark_Suspense_3_mp3, new AudioInfo(30067, 44100, "Drone Dark Suspense 03", "audio/mpeg"));
		//audioFormatAssert(ogg1, new AudioInfo(307566, 44100, null, "audio/vorbis"));
		//audioFormatAssert(g711_ulaw_5s_wav, new AudioInfo(5505, 8000, null, "audio/x-wav"));
		//audioFormatAssert(gsm_5s_wav, new AudioInfo(5500, 8000, null, "audio/x-wav"));
		//audioFormatAssert(dvi_5s_wav, new AudioInfo(5505, 8000, null, "audio/x-wav"));
		//audioFormatAssert(woman2_48_wav, new AudioInfo(12280, 16000, null, "audio/x-wav"));
		//audioFormatAssert(woman2_64_wav, new AudioInfo(12280, 16000, null, "audio/x-wav"));
		audioFormatAssert(WindowsSoundRecorder_wav, new AudioInfo(10374, 44100, null, "audio/x-wav"));
		//audioFormatAssert(BlueEyes_mp3, new AudioInfo(13401, 44100, "My Music At Home / My Music At Home", "audio/mpeg"));
		//audioFormatAssert(Enchantment_mp3, new AudioInfo(33463, 22050, "Enchantment", "audio/mpeg"));
		//audioFormatAssert(Dogies_mp3, new AudioInfo(12095, 44100, "Big Ones / Love In An Elevator", "audio/mpeg"));
	}
	
	private void audioFormatAssert(String location, AudioInfo audioInfo) throws UnsupportedAudioFileException, IOException {
		assertEquals("Location: "+location, audioInfo, AudioInfoHelper.audioInfo(getClass(), location));
	}

	@Test
	public void testTika()
		throws UnsupportedTagException, InvalidDataException, IOException, SAXException, TikaException {
		tikaAssert(AAC_8khz_Mono_5_aac, new AudioInfo(2513, 8000, "Cariba / Obo / Luna Azul", "audio/mpeg"));
		tikaAssert(iTunes_test1_m4a, new AudioInfo(308000, 44100, null, "audio/mp4"));
		tikaAssert(tone_mp3, new AudioInfo(5042, 44100, null, "audio/mpeg"));
		tikaAssert(tone16bit_flac, new AudioInfo(0, 44100, null, "audio/x-flac"));
		tikaAssert(tone16bit_m4a, new AudioInfo(5000, 44100, "Test Files / 16 bit Tone ALAC Test File", "audio/mp4"));
		tikaAssert(tone24bit_flac, new AudioInfo(0, 88200, null, "audio/x-flac"));
		tikaAssert(tone24bit_m4a, new AudioInfo(5000, 88200, "24 bit Tone ALAC Test File", "audio/mp4"));
		tikaAssert(Drone_Dark_Suspense_3_mp3, new AudioInfo(30067, 44100, "Drone Dark Suspense 03", "audio/mpeg"));
		tikaAssert(ogg1, new AudioInfo(308000, 44100, null, "audio/vorbis"));
		//tikaAssert(dvi_5s_wav, new AudioInfo(0, 8000, null, "audio/x-wav"));
		//tikaAssert(g711_ulaw_5s_wav, new AudioInfo(0, 8000, null, "audio/x-wav"));
		//tikaAssert(gsm_5s_wav, new AudioInfo(0, 8000, null, "audio/x-wav"));
		//tikaAssert(lpc10_5s_wav, new AudioInfo(0, 8000, null, "audio/x-wav"));
		//tikaAssert(woman2_48_wav, new AudioInfo(0, 16000, null, "audio/x-wav"));
		//tikaAssert(woman2_64_wav, new AudioInfo(0, 16000, null, "audio/x-wav"));
		tikaAssert(WindowsSoundRecorder_wav, new AudioInfo(0, 44100, null, "audio/x-wav"));
		//tikaAssert(HugeWAV_wav, new AudioInfo(0, 44100, null, "audio/x-wav"));
		tikaAssert(BlueEyes_mp3, new AudioInfo(13375, 44100, "My Music At Home / The Tragically Hip / My Music At Home", "audio/mpeg"));
		tikaAssert(Enchantment_mp3, new AudioInfo(32339, 22050, "Enchantment", "audio/mpeg"));
		tikaAssert(Dogies_mp3, new AudioInfo(12069, 44100, "Big Ones / Aerosmith / Love In An Elevator", "audio/mpeg"));
	}
	
	private void tikaAssert(String location, AudioInfo audioInfo) throws IOException, SAXException, TikaException {
		assertEquals("Location: "+location, audioInfo, tikaInfo(location));
	}
	
	private AudioInfo tikaInfo(String location) throws IOException, SAXException, TikaException {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(ResourceLoader.getClasspathFile(getClass(), location));
		ParseContext context = new ParseContext();
		Parser parser = new AutoDetectParser();
		parser.parse(inputstream, handler, metadata, context);
		String[] metadataNames = metadata.names();

		for(String name : metadataNames) {		        
	         logger.info(name + ": " + metadata.get(name));
	     }
		
		AudioInfo audioInfo=new AudioInfo(
			Math.round(AudioInfo.parse(firstNonNull(metadata, "xmpDM:duration"), 0).doubleValue())+"",
				firstNonNull(metadata, "samplerate", "xmpDM:audioSampleRate"), 
				metadata.get("title"), metadata.get("Content-Type"));
		if(audioInfo.name!=null && audioInfo.name.trim().length()<=0) {
			audioInfo.name=null;
		}
		Set<String> wrongDurationTypes=new HashSet<>(Arrays.asList(new String[] {"audio/mp4", "audio/vorbis"}));
		if(wrongDurationTypes.contains(audioInfo.contentType)) {
			if(audioInfo.durationMs!=null && audioInfo.durationMs<10000) {
				audioInfo.durationMs*=1000;
			}
		}
		audioInfo.name=AudioInfoHelper.getName(
			firstNonNull(metadata, "xmpDM:album"), 
			firstNonNull(metadata, "xmpDM:artist", "xmpDM:albumArtist", "Author", "meta:author"), 
			firstNonNull(metadata, "title", "dc:title"));
		return audioInfo;
	}

	private String firstNonNull(Metadata metadata, String ... keys) {
		for(String key : keys) {
			String value=metadata.get(key);
			if(value!=null) {
				value=value.trim();
			}
			if(value==null || value.length()==0 || "0".equals(value) || "None".equals(value) || "Long0".equals(value)) {
				continue;
			}
			if(value!=null) return value;
		}
		return null;
	}*/
}
