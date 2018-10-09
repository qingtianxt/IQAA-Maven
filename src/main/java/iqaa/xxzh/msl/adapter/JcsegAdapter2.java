package iqaa.xxzh.msl.adapter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lionsoul.jcseg.extractor.impl.TextRankKeyphraseExtractor;
import org.lionsoul.jcseg.extractor.impl.TextRankKeywordsExtractor;
import org.lionsoul.jcseg.extractor.impl.TextRankSummaryExtractor;
import org.lionsoul.jcseg.sentence.SentenceSeg;
import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.ISegment;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.lionsoul.jcseg.tokenizer.core.SegmentFactory;

public class JcsegAdapter2 implements TokenizerAdapter {

//	private ISegment extractorSeg;
	private TextRankKeywordsExtractor keywordsExtractor;
	private TextRankSummaryExtractor summaryExtractor;
	private TextRankKeyphraseExtractor keyphraseExtractor;
	public JcsegAdapter2() throws Exception {
		JcsegTaskConfig tokenizerConfig = new JcsegTaskConfig(true);
		JcsegTaskConfig extractorConfig = tokenizerConfig.clone();
		ADictionary dic = DictionaryFactory.createSingletonDictionary(tokenizerConfig);
		ISegment tokenizerSeg = SegmentFactory.createJcseg(
	            JcsegTaskConfig.COMPLEX_MODE, 
	            new Object[]{tokenizerConfig, dic}
	        );
		extractorConfig.setAppendCJKPinyin(false);
        extractorConfig.setClearStopwords(true);
        extractorConfig.setKeepUnregWords(false);
        /*extractorSeg = SegmentFactory.createJcseg(
            JcsegTaskConfig.COMPLEX_MODE, 
            new Object[]{extractorConfig, dic}
        );*/
        keywordsExtractor = new TextRankKeywordsExtractor(tokenizerSeg);
        keyphraseExtractor = new TextRankKeyphraseExtractor(tokenizerSeg);
        summaryExtractor = new TextRankSummaryExtractor(tokenizerSeg, new SentenceSeg());
        TextRankKeyphraseExtractor trkp = (TextRankKeyphraseExtractor)keyphraseExtractor;
        trkp.setAutoMinLength(4);
        trkp.setMaxWordsNum(4);
        
	}
	@Override
	public Set<String> getKeyWords(String content) {
		try {
			List<String> keywordsFromString = keywordsExtractor.getKeywordsFromString(content);
			HashSet<String> set = new HashSet<String>(keywordsFromString);
			return set;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getSummary(String str){
		try {
			String summary = summaryExtractor.getSummaryFromString(str, 100);
			return summary;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public Set<String> getKeySentence(String str){
		try {
			List<String> keyphraseFromString = keyphraseExtractor.getKeyphraseFromString(str);
			HashSet<String> set = new HashSet<String>(keyphraseFromString);
			return set;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Set<String> getKeyphrase(String str){
		try {
			List<String> keyphraseFromString = keyphraseExtractor.getKeyphraseFromString(str);
			HashSet<String> set = new HashSet<String>(keyphraseFromString);
			return set;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public String getTokenize(String doc) {
		// TODO Auto-generated method stub
		return null;
	}
}
