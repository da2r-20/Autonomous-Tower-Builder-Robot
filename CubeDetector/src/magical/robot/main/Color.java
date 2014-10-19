package magical.robot.main;


import org.opencv.core.Scalar;

public enum Color {
	//BLUE (new Scalar(103,80,24), new Scalar(134,164,148)),
	//GREEN (new Scalar(49,47,0), new Scalar(100,217,109));
	//BLUE (new Scalar(90,153,25), new Scalar(126,255,255)),
	
	
	//YELLOW (new Scalar(16,77,34), new Scalar(64,210,255)),
	//YELLOW (new Scalar(21,0,0), new Scalar(49,255,255)),
	
	//afternoon values
	//GREEN (new Scalar(66,105,29), new Scalar(100,255,255)),
	//BLUE (new Scalar(93,115,0), new Scalar(147,255,255));
	
	//evening values
	//GREEN (new Scalar(73,128,0), new Scalar(91,229,225)),
	//BLUE (new Scalar(94,114,0), new Scalar(141,255,255));
	
	//Morning values
	//GREEN (new Scalar(69,120,22), new Scalar(88,255,255)),
	//BLUE (new Scalar(102,114,0), new Scalar(136,255,255));
	
	//Colors Pavel's flat:
//	BLUE (new Scalar(86,79,0), new Scalar(118,255,255)),
//	GREEN (new Scalar(48,111,0), new Scalar(95,255,255)),
//	YELLOW (new Scalar(0,75,0), new Scalar(80,255,255));
	
	//Colors Pavel's flat evening:
//	BLUE (new Scalar(77,18,0), new Scalar(132,255,255)),
//	GREEN (new Scalar(31,18,0), new Scalar(95,255,255)),
//	YELLOW (new Scalar(6,77,0), new Scalar(42,255,255));
	
	
	//Colors 34 Lab noon
	/*
	BLUE (new Scalar(90,63,0), new Scalar(255,255,255)),
	GREEN (new Scalar(37,54,0), new Scalar(87,255,255)),
	YELLOW (new Scalar(18,72,0), new Scalar(44,255,255));*/
	
	GREEN (new Scalar(45,98,0), new Scalar(94,255,255)),
	BLUE (new Scalar(95,0,0), new Scalar(149,255,255)),
	YELLOW (new Scalar(13,46,0), new Scalar(54,255,255)),
	LINE_COLOR (new Scalar(0,0,0), new Scalar(255,255,70));
	
	
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
