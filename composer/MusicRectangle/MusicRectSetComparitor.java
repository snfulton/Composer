package composer.MusicRectangle;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

public final class MusicRectSetComparitor {

    /**
     * Gets the maximum integer value of a given property among all Music Rectangles in the collection.
     * Assumes that the lambda expression returns an integer.
     * @param lambda A lambda expression which which calls a getter for a field of a music note.
     * @return An integer representing the maximum value of the given property in the hashset
     */
    public static int getMaxIntegerProperty(Collection<MusicRectangle> collection, Function<MusicRectangle, Integer> lambda) {
        MusicRectangle maxRect = collection.stream().max(Comparator.comparing(lambda)).get();
        return lambda.apply(maxRect);
    }

    /**
     * Gets the minimum integer value of a given property among all Music Rectangles in the collection.
     * Assumes that the lambda expression returns an integer.
     * @param lambda A lambda expression which which calls a getter method for a field of a music note.
     * @return An integer representing the maximum value of the given property in the hashset
     */
    public static int getMinIntegerProperty(Collection<MusicRectangle> collection,Function<MusicRectangle,Integer> lambda){
        MusicRectangle minRect = collection.stream().min(Comparator.comparing(lambda)).get();
        return lambda.apply(minRect);
    }

}