package iqaa.xxzh.wzw.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;

/**
 * 使用该ansj进行分词
 * 
 * @author wzw
 *
 */
public class ansjFenci {
	//阈值
	public static double YUZHI = 0.2 ;

	//使用余弦向量的方式计算出两个文本的相似度
	public static double getSimilarity(Vector<String> T1, Vector<String> T2) throws Exception {
		int size = 0, size2 = 0;
		if (T1 != null && (size = T1.size()) > 0 && T2 != null && (size2 = T2.size()) > 0) {

			Map<String, double[]> T = new HashMap<String, double[]>();

			// T1和T2的并集T
			String index = null;
			for (int i = 0; i < size; i++) {
				index = T1.get(i);
				if (index != null) {
					double[] c = T.get(index);
					c = new double[2];
					c[0] = 1; // T1的语义分数Ci
					c[1] = YUZHI;// T2的语义分数Ci
					T.put(index, c);
				}
			}

			for (int i = 0; i < size2; i++) {
				index = T2.get(i);
				if (index != null) {
					double[] c = T.get(index);
					if (c != null && c.length == 2) {
						c[1] = 1; // T2中也存在，T2的语义分数=1
					} else {
						c = new double[2];
						c[0] = YUZHI; // T1的语义分数Ci
						c[1] = 1; // T2的语义分数Ci
						T.put(index, c);
					}
				}
			}

			// 开始计算，百分比
			Iterator<String> it = T.keySet().iterator();
			double s1 = 0, s2 = 0, Ssum = 0; // S1、S2
			while (it.hasNext()) {
				double[] c = T.get(it.next());
				Ssum += c[0] * c[1];
				s1 += c[0] * c[0];
				s2 += c[1] * c[1];
			}
			// 百分比
			return Ssum / Math.sqrt(s1 * s2);
		} else {
			throw new Exception("传入参数有问题！");
		}
	}

	public static Vector<String> fenci(String str) {
		Vector<String> v = new Vector<String>();

		Result result = DicAnalysis.parse(str);
		List<Term> list = result.getTerms();

		for (Term term : list) {
			v.add(term.getName());
			System.out.println(term.getName());
		}

		return v;
	}

	public static void main(String[] args) {
		Vector<String> v1 = fenci("这个中文分词可不可以，用着方不方便");
		Vector<String> v2 = fenci("这个中文分词比较方便，用着方便还可以。");
		
		try {
			double d = getSimilarity(v1, v2);
			System.out.println(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
