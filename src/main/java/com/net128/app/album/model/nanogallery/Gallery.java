package com.net128.app.album.model.nanogallery;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.swisscom.io.extwc.sdk.types.JsonObject;

public class Gallery extends JsonObject {
	public List<ImageItem> items;
	@JsonUnwrapped
	public ThumbnailParameters thumbnailParameters;
	public String itemsBaseURL;
	public Boolean locationHash;
}
