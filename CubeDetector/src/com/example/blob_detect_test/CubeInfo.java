package com.example.blob_detect_test;

import java.util.ArrayList;

public class CubeInfo {
	
	private static CubeInfo instance = null;
	private double horizontalLocation;
	private double distance;
	private int colorIndex;
	
	
	protected CubeInfo(){
		this.horizontalLocation = -100;
		this.distance = -1;
		colorIndex = -1;
	}
	
	public void setHorizontalLocation(double location){
		this.horizontalLocation = location;
	}
	
	public void setDistance(double dist){
		this.distance = dist;
	}
	
	public void setColorIndex(int colorIndex){
		this.colorIndex = colorIndex;
	}
	
	public double getHorizontalLocation(){
		return this.horizontalLocation;
	}
	
	public double getDistance(){
		return this.distance;
	}
	
	public int getColorIndex(){
		return this.colorIndex;
	}
	
	public static CubeInfo getInstance(){
		if (instance == null){
			instance = new CubeInfo();
		} 
		return instance;
	}
	
	
}
