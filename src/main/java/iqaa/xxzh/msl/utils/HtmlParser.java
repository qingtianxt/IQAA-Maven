package iqaa.xxzh.msl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlParser extends ParserCallback implements FileParser{

	private StringBuffer text = new StringBuffer();
	private File file;
	public HtmlParser(){}
	public HtmlParser(File file) {
		this.file = file;
	}
	public String getText(InputStream is) throws IOException{
		 ParserDelegator delegator = new ParserDelegator();  
		 delegator.parse(new InputStreamReader(is), this, Boolean.TRUE);
		String result = text.toString();
		return result;
	}
	@Override
	public void handleText(char[] data, int pos) {
		String str = new String(data);
		if(!str.trim().isEmpty())
			text.append(data).append("\n");
	}
	
	public String getText2(InputStream is) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String text = null;
		while((text = reader.readLine())!=null){
			sb.append(StringEscapeUtils.escapeHtml4(text)).append("\n");
		}
		StringEscapeUtils.escapeHtml4(FileUtil.getContent(is));
		return null;
	}
	@Override
	public String getText() throws Exception {
		if(file==null)
			throw new Exception("解析文件为null");
		String codeString = FileUtil.codeString(file);
		ParserDelegator delegator = new ParserDelegator();  
		 delegator.parse(new InputStreamReader(new FileInputStream(file),codeString), this, Boolean.TRUE);
		String result = text.toString();
		return result;
	}
	
}
