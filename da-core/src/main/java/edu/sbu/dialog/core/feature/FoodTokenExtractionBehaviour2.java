package edu.sbu.dialog.core.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.sbu.dialog.core.dialogmanager.Context;
import edu.sbu.dialog.core.pojos.NLPResponse;
import edu.sbu.dialog.core.taskmanager.TaskManager;

public class FoodTokenExtractionBehaviour2 implements Behaviour<NLPResponse,List<String>>{
	private TaskManager taskManager;
	Map<String, Integer> choices = new HashMap<String, Integer>();
	Set<String> foodTokenSet;

	public FoodTokenExtractionBehaviour2(TaskManager taskManager) {
		choices.put("one", 1);
		choices.put("two", 2);
		choices.put("three", 3);
		choices.put("1", 1);
		choices.put("2", 2);
		choices.put("3", 3);
		this.taskManager = taskManager;
		foodTokenSet = taskManager.getAllFoodTokens();
	}
	
	@Override
	public List<String> doAction(Context context, NLPResponse input) {
		List<String> tokens=null;
		//write code here
		return tokens;
	}

}
