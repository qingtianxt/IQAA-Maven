package iqaa.xxzh.msl.utils;

import java.text.SimpleDateFormat;

public class Date extends java.util.Date {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8891876481827919487L;
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:dd:ss");
		return sdf.format(this);
	}

}
