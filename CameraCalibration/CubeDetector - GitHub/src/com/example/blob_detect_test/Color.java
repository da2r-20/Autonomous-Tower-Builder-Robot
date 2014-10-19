package com.example.blob_detect_test;


import org.opencv.core.Scalar;

public enum Color {
	BLUE (new Scalar(103,80,24), new Scalar(134,164,148)),
	GREEN (new Scalar(49,47,0), new Scalar(100,217,109));
	
	public final Scalar hsvMin;
	public final Scalar hsvMax;
	
	private Color(Scalar hsvMin, Scalar hsvMax){
		this.hsvMin = hsvMin;
		this.hsvMax = hsvMax;
	}
	
	public Scalar getHsvMin(){
		return this.hsvMin;
	}
	public Scalar getHsvMax(){
		return this.hsvMax;
	}
}
