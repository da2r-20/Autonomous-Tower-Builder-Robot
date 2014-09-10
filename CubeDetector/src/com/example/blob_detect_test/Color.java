package com.example.blob_detect_test;


import org.opencv.core.Scalar;

public enum Color {
	//BLUE (new Scalar(103,80,24), new Scalar(134,164,148)),
	//GREEN (new Scalar(49,47,0), new Scalar(100,217,109));
	//BLUE (new Scalar(90,153,25), new Scalar(126,255,255)),
	
	YELLOW (new Scalar(16,77,34), new Scalar(64,210,255)),
	GREEN (new Scalar(47,61,23), new Scalar(96,158,154)),
	BLUE (new Scalar(98,51,0), new Scalar(198,158,255));
	//GREEN (new Scalar(69,120,22), new Scalar(88,255,255)),
	//BLUE (new Scalar(102,114,0), new Scalar(136,255,255));
	
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
