package com.nordstrom.automation.testng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * This class is meant for utility methods to assist in creating DataProviders
 * for parameterized TestNG test methods.
 */
public class DataProviders {
    
    private DataProviders() {
        throw new AssertionError("DataProviders is a static utility class that cannot be instantiated");
    }

	/**
	 * Returns an iterator over a collection of object arrays that contains all
	 * possible combinations of parameter sets from the specified data provider
	 * parameter objects. Each specified argument must be either an array of
	 * array of objects (Object[][]) or an iterator over a collection of object
	 * arrays ({@literal Iterator<Object[]>}), which are the two supported types
	 * for TestNG data providers. As a convenience, array of object (Object[])
	 * arguments are also supported through the {@link #unflatten} method.
	 * 
	 * @param providers
	 *            data provider parameter objects to be combined
	 * @return an iterator over a collection of all possible parameter set
	 *         combinations
	 * @see #unflatten
	 */
	public static Iterator<Object[]> createIterator(Object... providers) {
		List<Set<Object[]>> setList = new ArrayList<>();
		// iterate over input arguments
		for (Object thisObj : providers) {
			// if this is an iterator
			if (thisObj instanceof Iterator) {
				// extract iterator contents into a set
				Set<Object[]> thisSet = new HashSet<>();
				while (((Iterator<?>) thisObj).hasNext()) {
					thisSet.add((Object[]) ((Iterator<?>) thisObj).next());
				}
				// add set to list
				setList.add(thisSet);
			// otherwise, if this is an array of array of object
			} else if (thisObj instanceof Object[][]) {
				// add to list as set of array of object
				setList.add(Sets.newHashSet((Object[][]) thisObj));
			// otherwise, if this is an array of object
			} else if (thisObj instanceof Object[]) {
				// add to list as set of array of object
				setList.add(Sets.newHashSet(unflatten((Object[]) thisObj)));
			} else {
				throw new IllegalArgumentException(
						"Types of all arguments must be Object[][], Iterator<Object[]>, or Object[]");
			}
		}
		// return Cartesian data provider
		return new CartesianDataProvider(setList);
	}

	/**
	 * Converts the specified flat array into a two-dimensional by wrapping each
	 * array item
	 * 
	 * @param flatArray
	 *            flat object array to convert
	 * @return two-dimensional array
	 */
	public static Object[][] unflatten(Object[] flatArray) {
		Object[][] arrayOfArray = new Object[flatArray.length][];
		for (int thisIndex = 0; thisIndex < flatArray.length; thisIndex++) {
			arrayOfArray[thisIndex] = new Object[] { flatArray[thisIndex] };
		}
		return arrayOfArray;
	}

	private static class CartesianDataProvider implements Iterator<Object[]> {

		private final Iterator<List<Object[]>> provider;

		CartesianDataProvider(List<Set<Object[]>> setList) {
			// store iterator over Cartesian product
			provider = Sets.cartesianProduct(setList).iterator();
		}

		@Override
		public boolean hasNext() {
			return provider.hasNext();
		}

		@Override
		public Object[] next() {

			// FLATTEN OBJECT ARRAY LIST

			// Each combination from the original data providers is stored as a
			// list of object arrays, with each data provider supplying one
			// object array to the list. The order of object array items within
			// the list corresponds to the order that the arguments were
			// specified in the createIterator() call.
			//
			// The output of this method is a single array comprising all of the
			// objects in each combination. To achieve this result, we need to
			// flatten the list of arrays of objects into a flat object array.

			List<Object> dataList = new ArrayList<>();
			// iterate over this combination's items
			for (Object[] thisItem : provider.next()) {
				// add this item's objects to flat list
				dataList.addAll(Arrays.asList(thisItem));
			}
			// return list as array
			return dataList.toArray();
		}

		@Override
		public void remove() {
			provider.remove();
		}

	}

}
