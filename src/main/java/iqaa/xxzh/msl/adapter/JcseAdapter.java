package iqaa.xxzh.msl.adapter;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lionsoul.jcseg.extractor.impl.TextRankKeyphraseExtractor;
import org.lionsoul.jcseg.extractor.impl.TextRankKeywordsExtractor;
import org.lionsoul.jcseg.extractor.impl.TextRankSummaryExtractor;
import org.lionsoul.jcseg.sentence.SentenceSeg;
import org.lionsoul.jcseg.tokenizer.NLPSeg;
import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.ISegment;
import org.lionsoul.jcseg.tokenizer.core.IWord;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.lionsoul.jcseg.tokenizer.core.SegmentFactory;
import org.lionsoul.jcseg.util.ArrayUtil;

public class JcseAdapter implements TokenizerAdapter {

	
	public static void main(String[] args) throws Exception {
		JcseAdapter jc = new JcseAdapter();
		String doc = "远程实验室为开发者提供了7×24小时的免费的云化实验室环境，提供真实的设备供开发者远程在线开发调试。借助远程实验室自助管理平台，开发者不需要购置产品便可基于远程实验室针对相关产品进行二次开发，并实现远程对接测试认证。";
		Set<String> set = jc.getKeyPhrase(doc);
		System.out.println(set);
		System.out.println(jc.getTokenize(doc));
		System.out.println(jc.getKeyWords(doc));
		System.out.println(jc.getSummary(doc, 100));
	}
	
	
	
	private JcsegTaskConfig tokenizerConfig;
	private ADictionary dic;
	private ISegment tokenizerSeg;
	private ISegment extractorSeg;
	private TextRankKeyphraseExtractor keyphraseExtractor;
	private TextRankKeywordsExtractor keywordsExtractor;
	private TextRankSummaryExtractor summaryExtractor;

	public Set<String> getKeyPhrase(String content) {
		if(keywordsExtractor == null)
			keywordsExtractor = new TextRankKeywordsExtractor(tokenizerSeg);
		try {
			List<String> list = keywordsExtractor.getKeywordsFromString(content);
			Set<String> set = new HashSet<String>(list);
			return set;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public String getTokenize(String doc){
		StringBuffer sb = new StringBuffer();
		IWord word = null;
		
		
		boolean isFirst = true;
		boolean entity = (extractorSeg instanceof NLPSeg);
		 
		try {
			extractorSeg.reset(new StringReader(doc));
			while((word = extractorSeg.next()) != null){
				if(isFirst){
					sb.append(word.getValue());
					isFirst = false;
				}
				else{
					sb.append(" ");
					sb.append(word.getValue());
				}
				
				if(word.getPartSpeech() != null){
					sb.append('/');
					sb.append(word.getPartSpeech()[0]);
				}
				if( entity ){
					sb.append('/');
					sb.append(ArrayUtil.implode("|", word.getEntity()));
				}
				word = null; 
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取关键语句
	 * @param doc
	 * @return
	 */
	public Set<String> getKeySentence(String doc){
		try {
			if(summaryExtractor == null)
				summaryExtractor = new TextRankSummaryExtractor(tokenizerSeg,new SentenceSeg());
			List<String> list = summaryExtractor.getKeySentenceFromString(doc);
			return new HashSet<String>(list);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取关键短语
	 * @param doc
	 * @return
	 */
	public Set<String> getKeyWords(String doc){
		if(keyphraseExtractor == null)
			keyphraseExtractor = new TextRankKeyphraseExtractor(tokenizerSeg);
//		TextRankKeyphraseExtractor trkp = keyphraseExtractor;
		keyphraseExtractor.setAutoMinLength(4);
		keyphraseExtractor.setMaxWordsNum(6);
		try {
			List<String> list = keyphraseExtractor.getKeyphraseFromString(doc);
			return new HashSet<String>(list);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取摘要
	 * @param doc
	 * @param length
	 * @return
	 */
	public String getSummary(String doc,int length){
		try {
			if(summaryExtractor == null)
				summaryExtractor = new TextRankSummaryExtractor(tokenizerSeg,new SentenceSeg());
			String summary = summaryExtractor.getSummaryFromString(doc,length);
			return summary;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

	public JcseAdapter() throws Exception{
		
		tokenizerConfig = new JcsegTaskConfig(true);
		dic = DictionaryFactory.createSingletonDictionary(tokenizerConfig);
		tokenizerSeg = SegmentFactory.createJcseg(
				JcsegTaskConfig.COMPLEX_MODE, 
				new Object[]{tokenizerConfig,dic}
			);
		JcsegTaskConfig extractorConfig = tokenizerConfig.clone();
		extractorConfig.setAppendCJKPinyin(false);
		extractorConfig.setClearStopwords(true);
		extractorConfig.setKeepUnregWords(false);
		extractorSeg = SegmentFactory.createJcseg(
				JcsegTaskConfig.COMPLEX_MODE, 
				new Object[]{extractorConfig,dic}
		);
	}
}
