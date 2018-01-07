package edu.sbu.dialog.core.dialogmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sbu.dialog.core.feature.Behaviour;
import edu.sbu.dialog.core.feature.ContinueBehaviour;
import edu.sbu.dialog.core.feature.FilterBehaviour;
import edu.sbu.dialog.core.feature.FoodTokenExtractionBehaviour2;
import edu.sbu.dialog.core.feature.NLPBehaviour2;
import edu.sbu.dialog.core.pojos.NLPResponse;
import edu.sbu.dialog.core.pojos.State;
import edu.sbu.dialog.core.pojos.StateEnum;
import edu.sbu.dialog.core.taskmanager.TaskManager;
import edu.sbu.dialog.core.utilities.Constants;
import edu.sbu.dialog.core.utilities.Messages;

/**
 * this class implements the dialog system using concept of finite state automaton.
 *  The user response transitions the system from one state to another. 
 * @author kedar
 *
 */
public class DialogContext implements Context{
	private State currentState,previousState,nextState;
	private static TaskManager taskManager;
	private String textFromUser;
	private String textToUser;
	private static Map<StateEnum,State> stateMap;
	//private State[][] transitionTable;
	private List<String> choices; 
	
	static{
		taskManager=new TaskManager();
		stateMap=new HashMap<StateEnum,State>();
		stateMap.put(StateEnum.INITIAL, new State(StateEnum.INITIAL));
		stateMap.put(StateEnum.FILTER, new State<List<String>,List<String>>(StateEnum.FILTER,new FilterBehaviour(taskManager)));
		stateMap.put(StateEnum.INFORM_NUTRIENTS, new State(StateEnum.INFORM_NUTRIENTS));
		/* Old NLP state */
		//stateMap.put(StateEnum.NLP, new State<String,Map<String,List<String>>>(StateEnum.NLP,new NLPBehaviour()));
		/* New NLP state */
		stateMap.put(StateEnum.NLP, new State<String,NLPResponse>(StateEnum.NLP,new NLPBehaviour2()));
		
		/* Old Food Extraction */
		//stateMap.put(StateEnum.FOODITEM_EXTRACTION, new State<Map<String,List<String>>, List<String>>(StateEnum.FOODITEM_EXTRACTION,new FoodTokenExtractionBehaviour(taskManager)));
		stateMap.put(StateEnum.FOODITEM_EXTRACTION, new State<NLPResponse, List<String>>(StateEnum.FOODITEM_EXTRACTION,new FoodTokenExtractionBehaviour2(taskManager)));
		stateMap.put(StateEnum.CONTINUE, new State<NLPResponse,String>(StateEnum.CONTINUE,new ContinueBehaviour(taskManager)));
		//stateMap.put(StateEnum.GREET, new State(StateEnum.GREET));
	}
	
	/**
	 * This method receives user input as text . it sets the textFromUser variable and calls applytransitionlogic
	 * @param text
	 * @return
	 */
	public String processRequest(String text) {
		this.textFromUser=text;
		applyTransitionLogic();
		return textToUser;
	}
	
	/**
	 * This method holds all the logic of transitioning between states based on user input and previous state
	 * 
	 * The logic starts with initial state being null or INITIAL. When user responds , the input transitions 
	 * the context to nlp state where it breaks down the user response into nouns, verbs, etc
	 * 
	 *  then the system moves to food extraction  state where it identifies the food items in the response
	 *  then based on previous state and the user's response decides the next possible state. Each state is 
	 *  associated with a state behaviour. When system moves to a state , its behaviour is invoked.
	 */
	private void applyTransitionLogic() {
		//BaseVO data=null;
		
		State state=stateMap.get(StateEnum.NLP);
		
		//old NLP Behaviour
		//Behaviour<String,Map<String,List<String>>> behaviour=state.getBehaviour();
		//Map<String,List<String>> posMap=behaviour.doAction(this,textFromUser);
		
		//new NLP Behaviour
		Behaviour<String,NLPResponse> behaviour=state.getBehaviour();
		NLPResponse nlpResponse=behaviour.doAction(this,textFromUser);
		
		//System.out.println(list.getList());
		List<String> tokens=null;
		
		// conditions for getting into food extraction state
		if((previousState==null||previousState.getState()==StateEnum.INITIAL||previousState.getState()==StateEnum.FILTER)) {
			
			state=stateMap.get(StateEnum.FOODITEM_EXTRACTION);
			//Old Food extraction behaviour
			//Behaviour<Map<String,List<String>>,List<String>> foodExtractionBehaviour=state.getBehaviour();
			//tokens=foodExtractionBehaviour.doAction(this,posMap);
			
			//New Food Extraction Behaviour
			Behaviour<NLPResponse,List<String>> foodExtractionBehaviour=state.getBehaviour();
			tokens=foodExtractionBehaviour.doAction(this,nlpResponse);
		}
		
		//conditions to get into continue state
		if(previousState!=null&&previousState.getState()==StateEnum.CONTINUE )/*&& (posMap.get(POSTags.RB)!=null||posMap.get(POSTags.DT)!=null)) */{
			
			choices=null;
			state=stateMap.get(StateEnum.CONTINUE);
			Behaviour<NLPResponse,String> continueBehaviour=state.getBehaviour();
			textToUser=continueBehaviour.doAction(this, nlpResponse);
		}
		
		//conditions for getting into initial state
		if((previousState==null && (tokens==null|| tokens.size()==0))|| (previousState!=null && previousState.getState()==StateEnum.INFORM_NUTRIENTS)) {
			
			currentState=stateMap.get(StateEnum.INITIAL);
			taskManager.initializeFilterList();
			choices=null;
			textToUser=Messages.ASK_CLIENT;	
		}
		
		//conditions that cause system to loop in the same state -- repeat 
		if(previousState!=null&&(previousState.getState()==StateEnum.INITIAL||previousState.getState()==StateEnum.FILTER )&& tokens!=null&& tokens.size()==0){
			
			currentState=previousState;
			textToUser=Messages.REPEAT;
		}		
		
		// conditions for getting into filter state
		if((previousState==null||previousState.getState()==StateEnum.INITIAL) && tokens!=null&& tokens.size()>0 ||
				(previousState!=null &&previousState.getState()==StateEnum.FILTER && tokens!=null&& tokens.size()>0&& choices!=null&&choices.size()>3) ) {
			{
						
						currentState=stateMap.get(StateEnum.FILTER); 
						textToUser=Messages.SELECT_CHOICE;
						Behaviour<List<String>,List<String>> filterBehaviour=currentState.getBehaviour();
						choices= filterBehaviour.doAction(this, tokens);
						if(choices.size()>0) {
						if(choices.size()<3)
						{
							StringBuilder sb=new StringBuilder("Say ");
							String[] ch= {"ONE for {1}","TWO for {2}"};
							for(int i=0;i<choices.size();i++) {
								sb.append(ch[i].replace("{"+(i+1)+"}", choices.get(i)));
								sb.append(",");
							}
							textToUser=sb.toString().substring(0,sb.toString().length()-1);
						} 
						
						else
							for(int i=0;i<Constants.NUMBER_OF_CHOICES;i++) {
								textToUser=textToUser.replace("{"+(i+1)+"}", choices.get(i));
							}
						}
						else{
							textToUser=Messages.REPEAT;
							currentState=stateMap.get(StateEnum.INITIAL);
						}
						
						
						tokens=null;
			 }
		}
		
		// conditions for gettng into inform nutrients state
		if(previousState!=null&&previousState.getState()==StateEnum.FILTER && tokens!=null&& tokens.size()>0&&choices!=null&& choices.size()<=3) {
			
			currentState=stateMap.get(StateEnum.INFORM_NUTRIENTS);
			String choice=choices.get(Constants.choices.get(tokens.get(0))-1);
			String calories=taskManager.getCalories(choice);
			textToUser=Messages.INFORM_NUTRIENTS;
			textToUser=textToUser.replace("{1}", choice).replace("{2}", calories);
			choices=null;
			currentState=stateMap.get(StateEnum.CONTINUE);
			//System.out.println("Current state ="+currentState);
		}		
		previousState=currentState;
	}

	public State getCurrentState() {
		return currentState;
	}

	@Override
	public State getPreviousState() {
		return previousState;
	}
}
