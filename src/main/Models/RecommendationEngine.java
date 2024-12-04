package main.Models;

import java.util.*;

public class RecommendationEngine {
   private Map<Integer, Map<Integer, Double>> userRatings;

   public RecommendationEngine(Map<Integer, Map<Integer, Double>> userRatings) {
       this.userRatings = userRatings;
   }

   public List<Integer> recommendItems(int userId, int numRecommendations) {
       Map<Integer, Double> userRating = userRatings.get(userId);
       Map<Integer, Double> scores = new HashMap<>();
       Map<Integer, Double> totalSim = new HashMap<>();

       for (Map.Entry<Integer, Map<Integer, Double>> entry : userRatings.entrySet()) {
           int otherUserId = entry.getKey();
           if (userId == otherUserId) continue;
           double similarity = SimilarityUtil.cosineSimilarity(userRating, entry.getValue());

           for (Map.Entry<Integer, Double> ratingEntry : entry.getValue().entrySet()) {
               int itemId = ratingEntry.getKey();
               if (!userRating.containsKey(itemId)) {
                   scores.merge(itemId, similarity * ratingEntry.getValue(), Double::sum);
                   totalSim.merge(itemId, similarity, Double::sum);
               }
           }
        }
       
       // Normalize scores
       List<Map.Entry<Integer, Double>> recommendations = new ArrayList<>();
       for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
           int itemId = entry.getKey();
           double predictedRating = entry.getValue() / totalSim.get(itemId);
           recommendations.add(new AbstractMap.SimpleEntry<>(itemId, predictedRating));
       }

       recommendations.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
       List<Integer> sortedRecommendations = new ArrayList<>();
       for (int i = 0; i < numRecommendations && i < recommendations.size(); i++) {
           sortedRecommendations.add(recommendations.get(i).getKey());
       }
       return sortedRecommendations;
   }

   public List<Integer> recommendItemsFromItem(int userId, int numRecommendations) {
    Map<Integer, Double> userRating = userRatings.get(userId);
    Map<Integer, Double> scores = new HashMap<>();
 
    for (Map.Entry<Integer, Double> entry : userRating.entrySet()) {
        int ratedItem = entry.getKey();
        for (Map.Entry<Integer, Double> otherEntry : userRating.entrySet()) {
            int otherItem = otherEntry.getKey();
            if (ratedItem == otherItem) continue;
            double similarity = SimilarityUtil.itemSimilarity(userRatings, ratedItem, otherItem);
            scores.merge(otherItem, similarity * userRating.get(ratedItem), Double::sum);
        }
    }
 
    List<Map.Entry<Integer, Double>> recommendations = new ArrayList<>(scores.entrySet());
    recommendations.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
 
    List<Integer> sortedRecommendations = new ArrayList<>();
    for (int i = 0; i < numRecommendations && i < recommendations.size(); i++) {
        sortedRecommendations.add(recommendations.get(i).getKey());
    }
    return sortedRecommendations;
 }
}