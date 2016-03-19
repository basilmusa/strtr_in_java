# strtr in Java
This class provides two public static methods that provide strtr similar functionality in java. This is not a port from the strtr code base, the implementation uses a different algorithm. The name Strtr has been used to make it easy for developers to find this page on google coming from a PHP background.

You can use it directly by calling:

    String result = Strtr.replaceStringUsingMap(String haystack, Map<String, String> needle2replacement);
    String result = Strtr.replaceStringUsingChars(String haystack, String from, String to);
    
php strtr similar functionality in java

##Example Usage

	Map<String, String> replacements = new TreeMap<String, String>();
	replacements.put("this", "that");
	replacements.put("that", "this");
	replacements.put("Nothing much", "Everything");
			
	String example = Strtr.replaceStringUsingMap("Whether this or that, whats the difference? Nothing much.", replacements);

	System.out.println(example);

##Output

Input String:

    "Whether this or that, whats the difference? Nothing much."

Output String:

    "Whether that or this, whats the difference? Everything."

##Include it in your project
Just copy and paste the code above in your project, it's only one class. No need for a maven repository or separate jar file here.
