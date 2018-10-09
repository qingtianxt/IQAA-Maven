package iqaa.xxzh.msl.adapter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirAdapter implements TokenizerAdapter{

	private Clibrary instance;
	public NlpirAdapter(){
		this.instance = Clibrary.instance;
	}
	@Override
	public Set<String> getKeyWords(String content) {
		instance.NLPIR_Init("".getBytes(), 1, "0".getBytes());
		String nlpir_GetKeyWords = instance.NLPIR_GetKeyWords(content, 100, false);
		String[] split = nlpir_GetKeyWords.split("#");
		Set<String> set = new HashSet<>();
		for (String string : split) {
			if(string.isEmpty())
				continue;
			set.add(string);
		}
		instance.NLPIR_Exit();
		return set;
	}
	
	public String paragraphProcess(String s){
		instance.NLPIR_Init("".getBytes(), 1, "0".getBytes());
		String nlpir_GetKeyWords = instance.NLPIR_ParagraphProcess(s, 3);
		instance.NLPIR_Exit();
		return nlpir_GetKeyWords;
	}
	@Override
	public String getTokenize(String s) {
		instance.NLPIR_Init("".getBytes(), 1, "0".getBytes());
		String nlpir_GetKeyWords = instance.NLPIR_ParagraphProcess(s, 3);
		instance.NLPIR_Exit();
		return nlpir_GetKeyWords;
	}
//	private static Logger log = Logger.getLogger(NlpirAdapter.class);

	
}
interface Clibrary extends Library,Serializable{
	Clibrary instance =(Clibrary) Native.loadLibrary("NLPIR", Clibrary.class);
	// 初始化函数声明
	public int NLPIR_Init(byte[] sDataPath, int encoding, byte[] sLicenceCode);
	
	// 执行分词函数声明
	public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
	
	// 提取关键词函数声明
	public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
	
	// 退出函数声明
	public void NLPIR_Exit();
	 
}

