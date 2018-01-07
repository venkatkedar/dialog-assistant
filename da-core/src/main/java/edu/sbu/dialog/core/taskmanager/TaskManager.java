package edu.sbu.dialog.core.taskmanager;

import java.io.File;
import java.util.List;
import java.util.Set;

import edu.sbu.dialog.core.data.filters.Filter;
import edu.sbu.dialog.core.data.filters.FrequencyFilter;
import edu.sbu.dialog.core.ds.FileDS;
import edu.sbu.dialog.core.ds.FileDSImpl;
import edu.sbu.dialog.core.utilities.Constants;

/**
 * This class decouples the finite state automaton from backend logic. 
 * This makes easier for the backend logic evolve independent of the 
 * automaton logic. 
 * @author kedar
 *
 */
public class TaskManager {
	private Filter filter;
	private FileDS ds;
	
	public TaskManager() {
		filter=new FrequencyFilter();
		ds=new FileDSImpl();
	}

	public TaskManager(Filter filter) {
		super();
		this.filter = filter;
	}
	
	public List<String> getCurrentFilteredList(){
		return filter.getCurrentFilteredList();
	}
	
	public List<String> getPreviousTokens(){
		return filter.getPreviousTokens();
	}
	
	public List<String> getNextFrequentTokens(List<String> currentTokens){
		return filter.getNextFrequentTokens(currentTokens);
	}
	
	public List<String> filterFoodItemsBy(List<String> tokens){
		return filter.filter(tokens);
	}
	
	public String getCalories(String fooditem) {
		File loc=new File(Constants.DATA_PATH);
		String contents, lines[];
		contents=ds.readFile(loc,fooditem);
		System.out.println(contents);
		if(contents!=null) {
			int initial_pos=contents.indexOf("\"Energy\"");
			int final_pos=contents.indexOf("\"Protein\"");
			lines=contents.substring(initial_pos, final_pos).split(",");
			contents=lines[2];
		}
		return contents;
	}
	
	public List<String> getCurrentFrequentTokens(){
		return filter.getCurrentFrequentTokens();
	}
	
	public Set<String> getAllFoodTokens(){
		return filter.getAllFoodTokens();
	}
	
	public void initializeFilterList(){
		filter.intializeFilterList();
	}
}
