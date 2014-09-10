package com.example.blob_detect_test;


import org.opencv.core.Scalar;

public enum Color {
	//BLUE (new Scalar(103,80,24), new Scalar(134,164,148)),
	//GREEN (new Scalar(49,47,0), new Scalar(100,217,109));
	//BLUE (new Scalar(90,153,25), new Scalar(126,255,255)),
	
	YELLOW (new Scalar(16,77,34), new Scalar(64,210,255)),
	GREEN (new Scalar(11,115,4), new Scalar(99,211,255)),
	BLUE (new Scalar(90,153,25), new Scalar(126,255,255));
	
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
