package com.myapp.assignment.util;

import java.util.ArrayList;
import java.util.List;
/**
 * @author rpinninti
 * Created on 03/07/2023
 */
public class StringUtil {
	
	  public static List<String> quoteWords(String words) {
	        List<String> result = new ArrayList<>();
	        String[] wordArray = words.split(",");
	        for (String word : wordArray) {
	            result.add("'" + word.trim() + "'");
	        }
	        return result;
	    }

}
