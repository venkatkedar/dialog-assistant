package edu.sbu.dialog.core.data.filters;

import java.util.List;
import java.util.Set;

public interface Filter {
	List<String> filter(List<String> tokens);
	void intializeFilterList();
	List<String> getNextFrequentTokens(List<String> currentTokens);
	List<String> getCurrentFilteredList();
	List<String> getPreviousTokens();
	List<String> getCurrentFrequentTokens();
	Set<String> getAllFoodTokens();
}
