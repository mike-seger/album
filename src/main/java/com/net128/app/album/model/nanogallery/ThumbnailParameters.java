package com.net128.app.album.model.nanogallery;

import java.util.ArrayList;
import java.util.List;

import com.swisscom.io.extwc.sdk.types.JsonObject;

public class ThumbnailParameters extends JsonObject {
	public String thumbnailAlignment;
	public Label thumbnailLabel;
	public String thumbnailHeight, thumbnailWidth;
	public Integer touchAutoOpenDelay, thumbnailDisplayInterval, 
		thumbnailLazyLoadTreshold, thumbnailGutterWidth, thumbnailGutterHeight;
	public Boolean thumbnailOpenImage, touchAnimation, thumbnailDisplayTransition, thumbnailLazyLoad, 
		thumbnailAdjustLastRowHeight, thumbnailAlbumDisplayImage, galleryEnableKeyboard;
	public List<ThumbnailHoverEffect> thumbnailHoverEffect;
	
	public ThumbnailParameters setThumbnailHoverEffect(String ... names) {
		List<ThumbnailHoverEffect> thumbnailHoverEffectList=new ArrayList<ThumbnailHoverEffect>();
		for(String name : names) {
			ThumbnailHoverEffect thumbnailHoverEffect=new ThumbnailHoverEffect();
			thumbnailHoverEffect.name=name;
			thumbnailHoverEffectList.add(thumbnailHoverEffect);
		}
		thumbnailHoverEffect=thumbnailHoverEffectList;
		return this;
	}
	
	public static class Label {
		public String position;
		public Boolean display, displayDescription, hideIcons;
		public Integer titleMaxLength, descriptionMaxLength;
		public String align, title;
		public String itemsCount;
	}
}
