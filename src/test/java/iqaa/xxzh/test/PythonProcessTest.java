package iqaa.xxzh.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import org.junit.Test;

public class PythonProcessTest {

	
	@Test
	public void testCallPythonPro() throws IOException{
		Socket socket = new Socket("192.168.111.1",12345);
		socket.setSoTimeout(10000);
		OutputStream os = socket.getOutputStream();
		PrintStream out = new PrintStream(os);
		out.print("java 调用测试");
		InputStream is = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		String rec = reader.readLine();
		System.out.println("收到消息："+rec);
		out.close();
		os.close();
		reader.close();
		is.close();
		socket.close();
	}
}
