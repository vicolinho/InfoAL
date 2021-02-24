package de.uni_leipzig.dbs.formRepository.dataModel.graph.data;

import java.util.HashMap;
import java.util.Map;

public class EdgeWeightCostModel {

	
	private Map<String,Float> edgeWeightTypeModel;
	
	public EdgeWeightCostModel(){
		this.setEdgeWeightTypeModel(new HashMap<String,Float>());
	}

	
	public void addEdgeTypeWeight(String edgeType, float weight){
		this.edgeWeightTypeModel.put(edgeType, weight);
	}
	public Map<String,Float> getEdgeWeightTypeModel() {
		return edgeWeightTypeModel;
	}

	public void setEdgeWeightTypeModel(Map<String,Float> edgeWeightTypeModel) {
		this.edgeWeightTypeModel = edgeWeightTypeModel;
	}
}
