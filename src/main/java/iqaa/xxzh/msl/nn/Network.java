package iqaa.xxzh.msl.nn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
@Component("network")
public class Network {
	
	private List<Layer> layers;
	private List<Matrix<Double>> weights;
	private List<Matrix<Double>> bias;
	public Network(){
		layers = new ArrayList<>();
		weights = new ArrayList<>();
		bias = new ArrayList<>();
		String path = getClass().getClassLoader().getResource("model.json").getPath();
		try {
			loadModel(path);
		} catch (UndefinedLayerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Network(String model){
		this();
		try {
			loadModel(model);
		} catch (UndefinedLayerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	private void loadModel(String file) throws UndefinedLayerException{
		File f = new File(file);
		JsonParser jsonParser = new JsonParser();
		try {
			JsonElement rootElement = jsonParser.parse(new InputStreamReader(new FileInputStream(f)));
			JsonObject root = rootElement.getAsJsonObject();
			JsonArray jsonArray = root.get("layers").getAsJsonArray();
			for (JsonElement jsonElement : jsonArray) {
				JsonObject object = jsonElement.getAsJsonObject();
				String layer = object.get("layer").getAsString();
				Layer l = Layer.getLayer(layer);
				layers.add(l);
				JsonElement weightElement = object.get("weight");
				Matrix<Double> loadWeight = loadWeight(weightElement);
				weights.add(loadWeight);
				Matrix<Double> bias = loadWeight(object.get("bias"));
				this.bias.add(bias);
			}
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	private Matrix<Double> loadWeight(JsonElement weightElement) {
		Matrix<Double> matrix = new Matrix<Double>();
		List<List<Double>> m = new LinkedList<>();
		JsonArray asJsonArray = weightElement.getAsJsonArray();
		for (JsonElement jsonElement : asJsonArray) {
			List<Double> row = new LinkedList<>();
			JsonArray element = jsonElement.getAsJsonArray();
			for (JsonElement jsonElement2 : element) {
				double asDouble = jsonElement2.getAsDouble();
				row.add(asDouble);
			}
			m.add(row);
		}
		matrix.setVector(m);
		return matrix;
	}
	
	public List<Integer> predict(Matrix<Double> x){
		int depth = layers.size();
		for (int i = 0; i < depth; i++){
			Layer layer = layers.get(i);
			x = layer.output(x, weights.get(i), bias.get(i));
		}
		List<List<Double>> vector = x.getVector();
		List<Integer> indexs = new ArrayList<>(x.getWidth());
		for (List<Double> list : vector) {
			int i = 0;
			int size = list.size();
			for( int j = 1; j< size; j++){
				 if(list.get(i) < list.get(j))
					 i = j;
			}
			indexs.add(i);
		}
		System.out.println(vector);
		return indexs;
	}
}
