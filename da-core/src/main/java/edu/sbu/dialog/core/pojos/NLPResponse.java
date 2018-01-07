package edu.sbu.dialog.core.pojos;

import java.util.List;
import java.util.Map;

public class NLPResponse {
	Map<String,Map<String,List<String>>> map;
	String text;
	
	public Map<String, Map<String, List<String>>> getMap() {
		return map;
	}
	public void setMap(Map<String, Map<String, List<String>>> map) {
		this.map = map;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
