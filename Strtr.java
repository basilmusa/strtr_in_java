import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Two public methods can be used, mimicing strtr in php:
 * 
 * {@link #replaceStringUsingChars(String, String, String)}
 * 
 * {@link #replaceStringUsingMap(String, Map)}
 * 
 */
public class Strtr {
	
	/**
	 * Similar to strtr in php, characters in 'from' will be replaced by characters in 'to'
	 * in the same order character by character.
	 * 
	 * @param haystack
	 * @param from
	 * @param to
	 * @return String having the replacements made
	 */
	public static String replaceStringUsingChars(String haystack, String from, String to) {
		if (from.length() != to.length()) {
			throw new IllegalArgumentException("from and to should have same number of characters.");
		}
		Map<String, String> replacements = new HashMap<String, String>();
		int length = from.length();
		for (int i = 0; i < length; i++) {
			replacements.put(String.valueOf(from.charAt(i)), String.valueOf(to.charAt(i)));
		}
		return replaceStringUsingMap(haystack, replacements);
	}
	
	/**
	 * This method will return a string by replacing all occurrences of keys in needle2replacement occurring in
	 * haystack with the corresponding values. Once a replacement is made, it wont be replaced again.
	 * 
	 * The method will start with the longest keys first.
	 * Keys of "" (empty strings) will be ignored.
	 * 
	 * If the map is empty, the method returns the same haystack String directly.
	 * 
	 * @param haystack The string to be replaced
	 * @param needle2replacement The map of keys to search and values to be replaced
	 * @return String containing haystack with all the replacements in needle2replacement made
	 */
	public static String replaceStringUsingMap(String haystack, Map<String, String> needle2replacement)
	{
		// To optimize, just return the haystack if the map has no items
		if (needle2replacement.isEmpty()) {
			return haystack;
		}
		
		// Sort map by length of keys please by longest string first
		Map<String, String> sortedNeedle2replacement = sortKeysByLengthDescending(needle2replacement);
		
		Set<String> arrNeedles = sortedNeedle2replacement.keySet();
		List<Range> ranges = new ArrayList<>();
		
		int lengthDifference = 0;
		for (String needle : arrNeedles) {
			
			int indexOf = -1;
			
			// Ignore needles whom are empty strings
			if (needle.equals("")) {
				continue;
			}
			
			while(-1 != (indexOf = haystack.indexOf(needle, indexOf + 1))) {
				String replacement = sortedNeedle2replacement.get(needle);
				if (replacement == null) {
					throw new IllegalArgumentException("Map value of null found in key ['" + needle + "']");
				}
				
				Range range = new Range(indexOf, indexOf + needle.length() - 1, needle, replacement);
				
				// Check for any overlaps
				boolean addRange = true;
				for (Range target : ranges) {
					if (range.isOverlappingWith(target)) {
						addRange = false;
						break;
					}
				}
				if (addRange) {
					ranges.add(range);
					lengthDifference += range.replacement.length() - range.needle.length();
				}
			}
		}
		
		// Sort ranges please
		Collections.sort(ranges, new Comparator<Range>() {
			@Override
			public int compare(Range o1, Range o2) {
				return o1.begin - o2.begin;
			}
		});
		
		// type them
		StringBuilder stringBuilder = new StringBuilder(haystack.length() + lengthDifference);
		
		int startAt = 0;
		for (Range range : ranges) {
			stringBuilder.append(haystack.substring(startAt, range.begin));
			stringBuilder.append(range.replacement);
			startAt = range.end + 1;
		}
		stringBuilder.append(haystack.substring(startAt));
		
		return stringBuilder.toString();
	}
	
	private static class Range
	{
		public Range(int begin, int end, String needle, String replacement) {
			super();
			if (begin > end) {
				throw new RuntimeException("begin [" + begin + "] should be smaller or equal to end [" + end + "]");
			}
			this.begin = begin;
			this.end = end;
			this.needle = needle;
			this.replacement = replacement;
		}
		
		public final int begin;
		public final int end;
		public final String needle;
		public final String replacement;
		
		public String toString() {
			return String.format("{%d,%d,%s,%s}", begin, end, needle, replacement);
		}
		
		/**
		 * @param target
		 * @return
		 */
		public boolean isOverlappingWith(Range target) {
			final boolean liesBefore = this.end < target.begin;
			final boolean liesAfter = this.begin > target.end;
			if (liesBefore || liesAfter) {
				return false;
			}
			return true;
		}
		
		/**
		 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 *             XXXXXX
		 *          YYYYYYYYYYYYYYYY
		 *          
		 * Y is surround X as shown above
		 * 
		 * @param target
		 * @return
		 */
		public boolean isSurrounding(Range target) {
			if (this.begin < target.begin && this.end > target.end) {
				return true;
			}
			return false;
		}
	}
	

	private static Map<String, String> sortKeysByLengthDescending(
			Map<String, String> needle2replacement) {
		Map<String, String> sortedNeedle2replacement = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				if (s1.length() > s2.length()) {
	                return -1;
	            } else if (s1.length() < s2.length()) {
	                return 1;
	            } else {
	                return s1.compareTo(s2);
	            }
			}
		});
		sortedNeedle2replacement.putAll(needle2replacement);
			
		return sortedNeedle2replacement;
	}
}
