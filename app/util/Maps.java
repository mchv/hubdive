package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Maps {
	
	
		public static <K, V extends Comparable<V>> List<Entry<K, V>> sortByValue(Map<K, V> map) {
		        List<Entry<K, V>> entries = new ArrayList<Entry<K, V>>(map.entrySet());
		        Collections.sort(entries, new ByValue<K, V>());
		        return entries;
		}

		public static <K, V extends Comparable<V>> List<Entry<K, V>> sortByDecreasingValue(Map<K, V> map) {
	        List<Entry<K, V>> entries = new ArrayList<Entry<K, V>>(map.entrySet());
	        Collections.sort(entries, new ByValue<K, V>(true));
	        return entries;
	}
		
		public final static class ByValue<K, V extends Comparable<V>> implements Comparator<Entry<K, V>> {

			private boolean inverse = false;
			
			public ByValue() {
			}
			
			public ByValue(boolean inverse) {
				this.inverse = inverse;
			}
			
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				int comparison = o1.getValue().compareTo(o2.getValue()); 
				if (inverse)
					comparison = (-comparison);
				return comparison;
			}
		}

	
	
}
