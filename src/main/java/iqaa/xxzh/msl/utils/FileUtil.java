package iqaa.xxzh.msl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.UUID;

import org.mozilla.universalchardet.UniversalDetector;

public class FileUtil {
	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName
	 *            :file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String codeString(String fileName) {
		File file = new File(fileName);
		return codeString(file);
	}

	public static String codeString(InputStream fis) throws IOException {

		byte[] buf = new byte[4096];
		// (1)
		UniversalDetector detector = new UniversalDetector(null);

		// (2)
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		// (3)
		detector.dataEnd();

		// (4)
		String encoding = detector.getDetectedCharset();
		if (encoding != null) {
			System.out.println("Detected encoding = " + encoding);
		} else {
			System.out.println("No encoding detected.");
		}

		// (5)
		detector.reset();
		return encoding;
	}

	public static String codeString(File file) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			return codeString(is);
		} catch (Exception e) {
			e.printStackTrace();
			return Charset.defaultCharset().name();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String getContent(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public static String getUUIDName(String oldName) {
		int of = oldName.lastIndexOf(".");
		String sbu = oldName.substring(of);
		String ss = UUID.randomUUID().toString().replaceAll("-", "");
		return ss + sbu;
	}

	public static boolean copy(File file, File targer) {
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(file);
			os = new FileOutputStream(targer);
			int index = 0;
			byte[] buffer = new byte[1024 * 5];
			while ((index = is.read(buffer)) > 0) {
				os.write(buffer, 0, index);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}
}
