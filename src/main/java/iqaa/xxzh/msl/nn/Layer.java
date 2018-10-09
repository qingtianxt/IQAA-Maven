package iqaa.xxzh.msl.nn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Layer {

	protected abstract Matrix<Double> activate_function(Matrix<Double> x);
	
	public Matrix<Double> output(Matrix<Double> x,Matrix<Double> w,Matrix<Double> bias){
		
		List<List<Double>> m =new LinkedList<>();
		for(int i = 0; i < x.getWidth(); i++){
			List<Double> v= new LinkedList<>();
//			long t2 = System.currentTimeMillis();
			for (int k = 0; k < w.getLength(); k++){
				double tmp = 0;
//				long t1 = System.currentTimeMillis();
				for(int j = 0; j < x.getLength(); j++){
					Double x1 = x.get(i, j);
					Double x2 = w.get(j, k);
					tmp += x1 * x2;
				}
				tmp+=bias.get(0, k);
				v.add(tmp);
//				System.out.printf("k=%d:%dms\n",k,System.currentTimeMillis()-t1);
			}
			m.add(v);
//			System.out.printf("%dms\n",System.currentTimeMillis()-t2);
		}
		Matrix<Double> matrix = new Matrix<Double>();
		matrix.setVector(m);
		return matrix;//activate_function(x.dot(w).add(bias));
	}
	
	public static Layer getLayer(String name) throws UndefinedLayerException{
		if("relu".equalsIgnoreCase(name))
			return new ReLU();
		else if("sigmoid".equalsIgnoreCase(name))
			return new Sigmoid();
		else if("CrossEntropy".equalsIgnoreCase(name)){
			return new CrossEntropy();
		}
		else
			throw new UndefinedLayerException(name);
	}
}


class ReLU extends Layer{

	@Override
	protected Matrix<Double> activate_function(Matrix<Double> x) {
		Matrix<Double> maxmum = x.maxmum(0d);
		return maxmum;
	}
	
}

class Sigmoid extends Layer{

	@Override
	protected Matrix<Double> activate_function(Matrix<Double> x) {
		List<List<Double>> m = new ArrayList<>();
		List<List<Double>> vector = x.getVector();
		vector.forEach(row->{
			List<Double> newrow = new ArrayList<>();
			row.forEach(item->{
				double pow = Math.pow(Math.E, item);
				newrow.add(pow);
			});
			m.add(newrow);
		});
		Matrix<Double> matrix = new Matrix<Double>();
		matrix.setVector(m);
		return matrix;
	}
	
}

class CrossEntropy extends Layer{

	@Override
	protected Matrix<Double> activate_function(Matrix<Double> x) {
		int width = x.getWidth();
		int length = x.getLength();
		for(int i = 0; i < width; i++){
			// 求出每一行中最大的值
			Double linemax = x.get(i, 0);
			for(int j = 0; j < length; j++){
				if(linemax < x.get(i, j))
					linemax = x.get(i, j);
			}
			double sum = 0;
			for(int j = 0; j < length; j++){
				Double y = x.get(i, j);
				double exp = Math.exp(y-linemax);
				sum += exp;
				x.set(i, j, exp);
			}
			for(int j = 0; j < length; j++){
				Double y = x.get(i, j);
				y /= sum;
				x.set(i, j, y);
			}
		}
		return x;
	}
	
}

class UndefinedLayerException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UndefinedLayerException(String layerName){
		super(String.format("没有名为\"%s\"的网络层结构", layerName));
	}
}
