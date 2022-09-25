package com.idk.coin;

public class GEF {
		  
	    // Declaring ANSI_RESET so that we can reset the color
	    public static final String ANSI_RESET = "\u001B[0m";
	  
	    // Declaring the color
	    // Custom declaration
	    public static final String ANSI_YELLOW = "\u001B[33m";
	  
	    // Main driver method
	    public static void main(String[] args)
	    {
	        // Printing the text on console prior adding
	        // the desired color
	        System.out.println(ANSI_YELLOW
	                           + "This text is yellow"
	                           + ANSI_RESET);
	    }
}
