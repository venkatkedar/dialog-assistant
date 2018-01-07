package edu.sbu.dialog.core.data.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.sbu.dialog.core.ds.FileDS;
import edu.sbu.dialog.core.ds.FileDSImpl;
import edu.sbu.dialog.core.utilities.Constants;

/**
 * This class filters the food items based on user's response 
 * and presents most frequent types in a selected food item 
 * The idea is Frequent food types are most probable food types the user
 * might select next
 * @author Kedar
 *
 */
public class FrequencyFilter implements Filter{
	private static final FrequencyFilter filter=new FrequencyFilter();
	Map<String,Integer> frequencyMap=new TreeMap<String,Integer>();
	final String DATA_PATH=Constants.DATA_PATH;
	final Integer NUMBER_FOOD_ITEMS=8800;
	final String EXTENSION="csv";
	private FileDS ds;
	
	String[] allFoodItems;
	private List<String> allFiles;
	private List<String> filteredList;
	private List<String> previousTokens;
	private List<String> frequentTokens;
	private Set<String> foodTokens;
	
	
	List<String> removeWords=new ArrayList<>(Arrays.asList(new String[] {"grain","hand","pan","dish","14","12","hut","s","50","topping","made","thick","lunch","whole","school","food","chain","with","added","other","than","no","serve","covered","and","or","type","frozen","double","triple","foods","fast"}));
	
	public FrequencyFilter() {
		super();
		ds=new FileDSImpl();
		loadData();
		intializeFilterList();
		System.out.println("frequency filter loaded");
	}

	public static FrequencyFilter getInstance() {
		return filter;
	}
	
	private void loadData() {
		File path=new File(DATA_PATH);
		List<String> files=ds.getAllFileNames(path, EXTENSION);
		
		allFiles=new ArrayList<>(files.size());
		foodTokens=new HashSet<>();
		files.forEach((c)->{
								allFiles.add(c.substring(0, c.lastIndexOf(".")));
								foodTokens.addAll(Arrays.asList(c.split("-")));
								
					});
		foodTokens.removeAll(removeWords);
		System.out.println("allFiles="+allFiles);
	}

	/**
	 * this method gets the next frequent types in the selected food item so that
	 * user can choose among them
	 */
	@Override
	public List<String> getNextFrequentTokens(List<String> currentTokens){
		List<String> frequentTokens=new ArrayList<>();
		if(filteredList==null||filteredList.size()==0)
			return frequentTokens;
		Map<String,Integer> map=new HashMap<String, Integer>();
		for(String item :filteredList) {
			String[] tokens=item.split("-");
			for(String token:tokens) {
				Integer freq=map.get(token);
				if(freq==null)
					freq=0;
				map.put(token, freq+1);
			}
				
		}
		List<Map.Entry<String, Integer>> listMap=new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
		
		Collections.sort(listMap,new Comparator<Map.Entry<String,Integer>>() {

			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				int val=0;
				if(o1.getValue()>o2.getValue())
					val=-1;
				else if(o1.getValue()<o2.getValue())
					val=1;
				return val;
			}
		
		});
		listMap.removeIf((s)-> removeWords.contains(s.getKey()));
		listMap.removeIf((s)-> previousTokens.contains(s.getKey()));
		listMap.removeIf((s)-> currentTokens.contains(s.getKey()));
		listMap.forEach((c)->frequentTokens.add(c.getKey()));
		this.frequentTokens=frequentTokens;
		return frequentTokens;
	}

	/**
	 * This method filters the food items whose names contain the 
	 * food tokens in user's response 
	 */
	public List<String> filter(List<String> tokens) {
		
		if(tokens==null||tokens.size()==0)
			return filteredList;
		
		List<String> finalList=new ArrayList<String>();
		for(String file:filteredList) {
			//file=file.substring(0, file.lastIndexOf("."));
			List<String> fileTokens=Arrays.asList(file.split("-"));
			
			if(fileTokens.containsAll(tokens))
				finalList.add(file);
		}
		filteredList.retainAll(finalList);
		System.out.println("filterd list="+filteredList);
		System.out.println("filterd list size="+filteredList.size());
		previousTokens.addAll(tokens);
		return filteredList;
	}
	
	/**
	 * This method intializes the filtered list to all food items once the user 
	 * receives the calorific information
	 */
	public void intializeFilterList() {
		
		filteredList=new ArrayList<>(allFiles);
		System.out.println("filtered list initialized : size ="+filteredList.size());
		if(previousTokens==null)
			previousTokens=new ArrayList<>();
		else
			previousTokens.clear();
		frequentTokens=null;
		
	}
	
	
	// for testing
	public static void main(String[] args) {
		long time=System.currentTimeMillis();
		FrequencyFilter filter=new FrequencyFilter();//.getInstance();
		filter.intializeFilterList();
		
		long finaltime=System.currentTimeMillis();
		System.out.println("time taken="+(finaltime-time));
		List<String> tokens=Arrays.asList(new String [] {"ice","cream"});
		List<String> items=filter.filter(tokens);
		List<String> ftypes=filter.getNextFrequentTokens(tokens);
		tokens=Arrays.asList(new String [] {ftypes.get(1)});
		items=filter.filter(tokens);
		ftypes=filter.getNextFrequentTokens(tokens);
		
		tokens=Arrays.asList(new String [] {ftypes.get(0)});
		items=filter.filter(tokens);
		ftypes=filter.getNextFrequentTokens(tokens);

		
	}

	
	
	@Override
	public List<String> getCurrentFilteredList() {
		return filteredList;
	}

	@Override
	public List<String> getPreviousTokens() {
		return previousTokens;
	}

	@Override
	public List<String> getCurrentFrequentTokens() {
		return frequentTokens;
	}

	@Override
	public Set<String> getAllFoodTokens() {
		return foodTokens;
	}
	
	
	
}
