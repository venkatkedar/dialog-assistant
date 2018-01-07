package edu.sbu.dialog.core.feature;

import java.util.List;

import edu.sbu.dialog.core.dialogmanager.Context;
import edu.sbu.dialog.core.taskmanager.TaskManager;

/**
 * This behaviour is associated with Filter state and it performs the action 
 * of filtering by delagating to the taskmanger.
 * @author Kedar
 *
 */
public class FilterBehaviour implements Behaviour<List<String>,List<String>>{
	private TaskManager taskManager;
	
	public FilterBehaviour(TaskManager taskManager) {
		this.taskManager=taskManager;
	}
	public List<String> doAction(Context context, List<String> tokens) {
		List<String> items=taskManager.filterFoodItemsBy(tokens);
		System.out.println("items size="+items.size());
		List<String> frequentTokens=null;
		if(items.size()>3)
			frequentTokens=taskManager.getNextFrequentTokens(tokens);
		//String[] stringTokens=new String[] {"cookie","nuts","mint"};
		System.out.println("frequent tokens ="+frequentTokens);
		List<String> choices=frequentTokens!=null&&frequentTokens.size()>0?frequentTokens:items;
		return choices;
	}

}
