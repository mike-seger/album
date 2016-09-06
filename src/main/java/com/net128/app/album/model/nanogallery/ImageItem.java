package com.net128.app.album.model.nanogallery;

import com.swisscom.io.extwc.sdk.types.JsonObject;

public class ImageItem extends JsonObject {
	public String title;
	public String description;
	public Integer ID;
	public Integer albumID;
	public String kind, src, srct, srcXS, srcSM, srcME, srcLA, srcXL, destURL;
	public Integer imgtHeigt;
	public Integer imgtWidth;
	public Object customData;
	
	public ImageItem(){}
	public ImageItem(String title, String src, String srct) {
		super();
		this.title = title;
		this.src = src;
		this.srct = srct;
	}
}
