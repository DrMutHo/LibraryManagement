package main.Models;

import java.util.Map;
import java.util.Set;

public class SimilarityUtil {
    
    public static double cosineSimilarity(Map<Integer, Double> user1, Map<Integer, Double> user2) {
        Set<Integer> commonItems = user1.keySet();
        commonItems.retainAll(user2.keySet());
        if (commonItems.isEmpty()) return 0.0;
        double dotProduct = 0.0;
        double normUser1 = 0.0;
        double normUser2 = 0.0;
        for (Integer itemId : commonItems) {
            dotProduct += user1.get(itemId) * user2.get(itemId);
        }
        
        // Calculate norm
        for (Integer item : user1.keySet()) {
            normUser1 += Math.pow(user1.get(item), 2);
        }
        for (Integer item : user2.keySet()) {
            normUser2 += Math.pow(user2.get(item), 2);
        }
        return dotProduct / (Math.sqrt(normUser1) * Math.sqrt(normUser2));
    }

    public static double itemSimilarity(Map<Integer, Map<Integer, Double>> userRatings, int item1, int item2) {
        double dotProduct = 0.0;
        double normItem1 = 0.0;
        double normItem2 = 0.0;
        for (Map<Integer, Double> ratings : userRatings.values()) {
            if (ratings.containsKey(item1) && ratings.containsKey(item2)) {
                dotProduct += ratings.get(item1) * ratings.get(item2);
            }
            if (ratings.containsKey(item1)) {
                normItem1 += Math.pow(ratings.get(item1), 2);
            }
            if (ratings.containsKey(item2)) {
                normItem2 += Math.pow(ratings.get(item2), 2);
            }
        }
        return dotProduct / (Math.sqrt(normItem1) * Math.sqrt(normItem2));
     }
}
