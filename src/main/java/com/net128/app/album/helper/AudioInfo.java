package com.net128.app.album.helper;

public class AudioInfo {
	public Integer durationMs;
	public Integer sampleRate;
	public String name;
	public String contentType;

	public AudioInfo() {}
	public AudioInfo(String durationMs, String sampleRate, String name, String contentType) {
		this(parse(durationMs, 0).intValue(), parse(sampleRate, 0).intValue(), 
			name, contentType);
	}
	public AudioInfo(Integer durationMs, Integer sampleRate, String name, String contentType) {
		super();
		this.durationMs = durationMs;
		this.sampleRate = sampleRate;
		this.name = name;
		this.contentType = contentType;
	}
	
	public static Number parse(String value, Number defValue) {
		try {
			return Double.parseDouble(value);
		} catch(Exception e) {
			return defValue;
		}
	}
	@Override
	public String toString() {
		return String.format("AudioInfo [durationMs=%s, sampleRate=%s, name=%s, contentType=%s]",
				durationMs, sampleRate, name, contentType);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((durationMs == null) ? 0 : durationMs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sampleRate == null) ? 0 : sampleRate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AudioInfo other = (AudioInfo) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (durationMs == null) {
			if (other.durationMs != null)
				return false;
		} else if (!durationMs.equals(other.durationMs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sampleRate == null) {
			if (other.sampleRate != null)
				return false;
		} else if (!sampleRate.equals(other.sampleRate))
			return false;
		return true;
	}
}
