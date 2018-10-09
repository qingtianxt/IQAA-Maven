package iqaa.xxzh.msl.adapter;

import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class AnsjsegAdapter implements TokenizerAdapter {

	@Override
	public Set<String> getKeyWords(String content) {
		Result parse = NlpAnalysis.parse(content);
		String str = parse.toString();
		System.out.println(str);
		return null;
	}

	@Override
	public String getTokenize(String doc) {
		Result parse = NlpAnalysis.parse(doc);
		String str = parse.toString();
		return str;
	}

}
