# strtr_in_java
php strtr similar functionality in java


#Example Usage

	Map<String, String> replacements = new TreeMap<String, String>();
	replacements.put("this", "that");
	replacements.put("that", "this");
	replacements.put("Nothing much", "Everything");
			
	String example = Strtr.replaceStringUsingMap("Whether this or that, whats the difference? Nothing much.", replacements);

	System.out.println(example);

#Output

Input String:

    "Whether this or that, whats the difference? Nothing much."

Output String:

    "Whether that or this, whats the difference? Everything."

#Include it in your project
Just copy and paste the code above in your project, it's only one class. No need for a maven repository or separate jar file here.
