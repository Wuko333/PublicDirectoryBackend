package com.sligobaptistchurch.directory.backend.static_methods;

import java.util.Collection;

public class HelperMethods {
	// Collection intersection method for search
		public static <T, C extends Collection<T>> C findIntersection(C newCollection, Collection<T>... collections) {
			boolean first = true;
			for (Collection<T> collection : collections) {
				if (first) {
					newCollection.addAll(collection);
					first = false;
				} else {
					newCollection.retainAll(collection);
				}
			}
			return newCollection;
		}
		
		public static <T, C extends Collection<T>> C findUnion(C newCollection, Collection<T>... collections) {
			for (Collection<T> collection : collections) {
					newCollection.addAll(collection);
				}
			return newCollection;
		}
}
