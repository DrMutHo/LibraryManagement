package main.Models;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
public class BookRecommendation {
    public List<Book> Recommendation(){
        Map<Integer, Map<Integer, Double>> userRatings = Model.getInstance().getDatabaseDriver().loadDataFromDatabase();
        RecommendationEngine recommendationEngine = new RecommendationEngine(userRatings);
        Integer client_id = Model.getInstance().getClient().getClientId();
        List<Integer> userBasedRecommendations = recommendationEngine.recommendItems(client_id, 20);
        List<Integer> itemBasedRecommendations = recommendationEngine.recommendItemsFromItem(client_id, 20);
        List<Integer> newList = Stream.concat(userBasedRecommendations.stream(), itemBasedRecommendations.stream()).toList();
        Set<Integer> combinedSet = new HashSet<>(newList);

        List<Integer> finalRecommendations = new ArrayList<>(combinedSet).subList(0, Math.min(10, combinedSet.size()));
        List<Book> Recommended = new ArrayList<>();


        for(Integer i : finalRecommendations){
            System.out.println(i + " " + client_id + " sieudakae222 "); 
            Book dakmim = Model.getInstance().getDatabaseDriver().getBookByBookId(i);
            System.out.println( dakmim.getTitle() + " sieudakae333 "); 
            Recommended.add(Model.getInstance().getDatabaseDriver().getBookByBookId(i));
        }
        return Recommended;
    }
}
