package iqaa.xxzh.msl.utils;

import java.util.Base64;
import java.util.Base64.Encoder;

public class Base64EncoderUtils {

	public static String encoding(String value){
		Encoder encoder = Base64.getEncoder();
		String encodeToString = encoder.encodeToString(value.getBytes());
		return encodeToString;
	}
	
	public static String decoding(String value){
		byte[] decode = Base64.getDecoder().decode(value.getBytes());
		return new String(decode);
	}
}
