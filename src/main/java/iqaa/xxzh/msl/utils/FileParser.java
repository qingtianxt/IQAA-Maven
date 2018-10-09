package iqaa.xxzh.msl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface FileParser {

	@Deprecated
	String getText(InputStream is) throws Exception;
	
	String getText()throws Exception;
	
	public static FileParser newInstance(String fileName){
		File file = new File(fileName);
		if(fileName.endsWith(".html")||fileName.endsWith(".htm"))
			return new HtmlParser(file);
		if(fileName.endsWith(".txt"))
			return new FileParser(){

				@Override
				public String getText(InputStream is) throws Exception {
					
					//is.reset();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String buffer = null;
					StringBuffer sb = new StringBuffer();
					while((buffer=br.readLine())!=null){
						sb.append(buffer).append("\n");
					}
					return sb.toString();
				}

				@Override
				public String getText() throws Exception {
					String codeString = FileUtil.codeString(file);
					InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),codeString);
					BufferedReader reader = new BufferedReader(inputStreamReader);
					StringBuffer sb = new StringBuffer();
					while(reader.ready()){
						sb.append(reader.readLine()).append("\n");
					}
					reader.close();
					return sb.toString();
				}};
		return null;
	}
}
