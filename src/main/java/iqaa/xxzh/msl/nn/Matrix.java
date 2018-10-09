package iqaa.xxzh.msl.nn;

import java.util.ArrayList;
import java.util.List;

public class Matrix<T extends Number> {
	
	protected int length;
	protected int width;
	protected List<List<T>> vector;
	public Matrix(){
		
	}
	public Matrix(int length ,int width){
		this.length = length;
		this.width = length;
		vector = new ArrayList<>(width);
		for(int i = 0; i < length; i++){
			List<T> rows = new ArrayList<>();
			vector.add(rows);
		}
	}
	public <Z extends Number> Matrix<T> dot(Matrix<Z> m) throws MatrixNotSupportMultifyException{
		if(this.length != m.width)
			throw new MatrixNotSupportMultifyException();
		
		return null;
	}
	
	public void set(int i,int j,T value){
		vector.get(i).set(j, value);
	}
	
	public T get(int i,int j){
		return vector.get(i).get(j);
	}
	
	public <Z extends Number> Matrix<T> add(Matrix<Z> x){
		return null;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public List<List<T>> getVector() {
		return vector;
	}
	public void setVector(List<List<T>> vector) {
		this.width = vector.size();
		if (!vector.isEmpty())
			this.length = vector.get(0).size();
		else
			this.length = 0;
		this.vector = vector;
	}
	/**
	 * 比较矩阵中与i的大小，返回大的那个
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <Z extends Number> Matrix<Z> maxmum(Z i) {
		Matrix<Z> matrix = new Matrix<Z>();
		List<List<Z>> m = new ArrayList<>(this.width);
		vector.forEach(list->{
			List<Z> newRows = new ArrayList<>(this.length);
			list.forEach(item->{
				newRows.add(item.doubleValue() > i.doubleValue()? (Z)item:(Z)i);
			});
			m.add(newRows);
			matrix.setLength(list.size());
		});
		matrix.setWidth(vector.size());
		matrix.setVector(m);
		return matrix;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for (List<T> list : vector) {
			sb.append('[');
			boolean i = false;
			for (T t : list) {
				sb.append(t).append(',');
				i = true;
			}
			if(i)
				sb.deleteCharAt(sb.length()-1);
			sb.append(']');
			sb.append(',');
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length()-1);
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}


class MatrixNotSupportMultifyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MatrixNotSupportMultifyException() {
		
	}
	
}
