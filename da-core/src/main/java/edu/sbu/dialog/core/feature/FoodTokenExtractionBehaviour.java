package edu.sbu.dialog.core.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.sbu.dialog.core.dialogmanager.Context;
import edu.sbu.dialog.core.taskmanager.TaskManager;
import edu.sbu.dialog.core.utilities.Constants;
import edu.sbu.dialog.core.utilities.POSTags;

/**
 * This class performs the action of extracting the food tokens from user speech
 */
public class FoodTokenExtractionBehaviour implements Behaviour<Map<String, List<String>>, List<String>> {
	private TaskManager taskManager;
	Map<String, Integer> choices = new HashMap<String, Integer>();
	Set<String> foodTokenSet;

	public FoodTokenExtractionBehaviour(TaskManager taskManager) {
		choices.put("one", 1);
		choices.put("two", 2);
		choices.put("three", 3);
		choices.put("1", 1);
		choices.put("2", 2);
		choices.put("3", 3);
		this.taskManager = taskManager;
		foodTokenSet = taskManager.getAllFoodTokens();
	}

	public List<String> doAction(Context context, Map<String, List<String>> posMap) {
		List<String> foodTokens = new ArrayList<>();
		List<String> tokensFromUser = posMap.get(POSTags.NN);
		List<String> adjectives = posMap.get(POSTags.ADJECTIVES);
		if (tokensFromUser != null && adjectives != null)
			tokensFromUser.addAll(adjectives);
		// List<String> foods=new ArrayList<String>(Arrays.asList(new String[]
		// {"chocalate","ice","cream","nuts","milk","mint"}));

		if (tokensFromUser != null && tokensFromUser.size() > 0) {
			for (String noun : tokensFromUser) {
				if (foodTokenSet.contains(noun))
					foodTokens.add(noun);
			}
		} else {

			// number choices like one, two, three

			List<String> numberChoices = posMap.get(POSTags.CD);
			if (numberChoices != null && numberChoices.size() > 0) {
				List<String> frequentTokens = taskManager.getCurrentFrequentTokens();
				List<String> items = taskManager.getCurrentFilteredList();
				if (items.size() > Constants.NUMBER_OF_CHOICES) {
					int choice = choices.get(numberChoices.get(0));
					if(frequentTokens!=null) {
						if (choice < 1 || choice > frequentTokens.size())
							choice = 1;
						foodTokens.add(frequentTokens.get(choice - 1));
					}
				}
				else
					foodTokens.add(numberChoices.get(0));
				// foodTokens.add(new FoodToken(foods.get(choice)));
			}

		}

		return foodTokens;

	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

}
