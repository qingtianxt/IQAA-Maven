package iqaa.xxzh.msl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpUtil {

	
	public static String getContent(String urlString){
		String content = null;
		HttpURLConnection conn=null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			conn.setConnectTimeout(30*1000);
			int responseCode = conn.getResponseCode();
			if(responseCode / 100 == 2){
				InputStream is = conn.getInputStream();
				content = FileUtil.getContent(is);
			}else{
				content ="连接异常"+responseCode;
			}
			Map<String, List<String>> headerFields = conn.getHeaderFields();
			Set<Entry<String,List<String>>> entrySet = headerFields.entrySet();
			for (Entry<String, List<String>> entry : entrySet) {
				String responseHander = entry.getKey();
				System.out.print(responseHander+"\t");
				List<String> value = entry.getValue();
				for (String string : value) {
					System.out.print(string+"\t");
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			
			if(conn!=null){
				conn.disconnect();
			}
		}
		return content;
	}
}
