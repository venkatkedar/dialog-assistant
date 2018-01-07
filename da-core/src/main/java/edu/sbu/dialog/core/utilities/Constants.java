package edu.sbu.dialog.core.utilities;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final Integer NUMBER_OF_CHOICES=3;
	
	public static Map<String,Integer>choices=new HashMap<>();
	public static final String DATA_PATH="C:\\Users\\venka\\Desktop\\sp\\prrai-cse523-d5e9e289466b\\data";
	public static final String TAGGER_PATH="C:\\Users\\venka\\Desktop\\";
	
	static {
		choices.put("one", 1);
		choices.put("two", 2);
		choices.put("three", 3);
		choices.put("1", 1);
		choices.put("2", 2);
		choices.put("3", 3);
	}
}
