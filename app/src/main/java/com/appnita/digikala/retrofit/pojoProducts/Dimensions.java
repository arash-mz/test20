package com.appnita.digikala.retrofit.pojoProducts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Dimensions implements Serializable {

	@SerializedName("length")
	private String length;

	@SerializedName("width")
	private String width;

	@SerializedName("height")
	private String height;

	public void setLength(String length){
		this.length = length;
	}

	public String getLength(){
		return length;
	}

	public void setWidth(String width){
		this.width = width;
	}

	public String getWidth(){
		return width;
	}

	public void setHeight(String height){
		this.height = height;
	}

	public String getHeight(){
		return height;
	}

	@Override
 	public String toString(){
		return 
			"Dimensions{" + 
			"length = '" + length + '\'' + 
			",width = '" + width + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}
}