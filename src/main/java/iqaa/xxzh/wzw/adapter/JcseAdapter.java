package iqaa.xxzh.wzw.adapter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lionsoul.jcseg.extractor.impl.TextRankKeyphraseExtractor;
import org.lionsoul.jcseg.tokenizer.core.ISegment;

public class JcseAdapter {
	/**
	 * 获取关键短语
	 * @param doc
	 * @return
	 */
	private static TextRankKeyphraseExtractor keyphraseExtractor;
	private static ISegment tokenizerSeg;
	
	
	public static Set<String> getKeyWords(String doc){
		if(keyphraseExtractor == null)
			keyphraseExtractor = new TextRankKeyphraseExtractor(tokenizerSeg);
//		TextRankKeyphraseExtractor trkp = keyphraseExtractor;
		//keyphraseExtractor.setAutoMinLength(4);
		//keyphraseExtractor.setMaxWordsNum(6);
		keyphraseExtractor.setMaxWordsNum(20);
		try {
			List<String> list = keyphraseExtractor.getKeyphraseFromString(doc);
			return new HashSet<String>(list);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) {
		String doc="用户使用UQuery有四类场景： 明细数据查询 典型应用：设备运维仪表盘，故障分析，能耗统计，轨迹回放，医疗健康报告，用户行为事件分析等。 场景特点：海量数据，查询的有效数据不大，查询往往携带过滤条件如时间等，查询维度不固定。 企业商业智能分析 典型应用：流水审计，经营状况分析，行情分析，业务报表，订单跟踪等。 场景特点：往往对接BI工具协同分析，分析维度不固定，常涉及多个表联合查询，响应时延低。 政府公共事务统计 典型应用：人口/住房统计，犯罪追踪，关联案件查询，交通拥堵分析，景点热度统计。 场景特点：统计计算，对比分析，响应时间要求通常不苛刻。 海量日志分析 典型应用：新媒体热点事件追踪，消息来源分类筛选，广告收益统计等。 场景特点：半结构化，海量数据，部分涉及模糊查询，计算量大。";
		Set<String> set = getKeyWords(doc);
		if(null!=set){
			System.out.println(set);
		}
	}
}
