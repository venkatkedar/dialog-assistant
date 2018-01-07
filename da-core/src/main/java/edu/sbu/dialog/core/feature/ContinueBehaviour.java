package edu.sbu.dialog.core.feature;

import java.util.List;

import edu.sbu.dialog.core.dialogmanager.Context;
import edu.sbu.dialog.core.pojos.NLPResponse;
import edu.sbu.dialog.core.pojos.StateEnum;
import edu.sbu.dialog.core.taskmanager.TaskManager;
import edu.sbu.dialog.core.utilities.Messages;
import edu.sbu.dialog.core.utilities.POSTags;

public class ContinueBehaviour implements Behaviour<NLPResponse, String>{
	private TaskManager taskManager;
	
	public ContinueBehaviour(TaskManager taskManager) {
		this.taskManager=taskManager;
	}
	@Override
	public String doAction(Context context, NLPResponse input) {
		String textToUser=Messages.GREET;
		taskManager.initializeFilterList();
		// write logic for yes or no, output is a text message to be sent to user
		/*
		List<String> responses=posMap.get(POSTags.RB);
		if(responses==null)
			responses=posMap.get(POSTags.DT);
		
		String response="yes";
		if(responses!=null)
			response=responses.get(0);
		if("no".equalsIgnoreCase(response)) {
			
			textToUser=Messages.GREET;
			currentState=null;
		}else if("yes".equalsIgnoreCase(response)) {
		currentState=stateMap.get(StateEnum.INITIAL);
		
		textToUser=Messages.ASK_CLIENT;
		}
		*/
		return textToUser;
	}

}
