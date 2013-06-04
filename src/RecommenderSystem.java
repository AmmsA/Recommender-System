/*
 *  Coded by: Mustafa S.
 *  Please do not copy or use my code without my permission.
 * 
 *  Pearson Cor. Recommender System
 *  Movie Rating Example.
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RecommenderSystem {

	static Map<String, Map<String, Double>> f = new HashMap<String,Map<String,Double>>();

	public static void main(String[] args) {

		// Filling HashMap with Data. Note: More data in, better result.
		// LIW: Lady In Water
		// SOP: Snakes on Plane
		// etc for other movies.
		
		
		Map<String,Double> ratings = new HashMap<String,Double>();
		ratings.put("LIW", 2.5);
		ratings.put("SOP",3.5);
		ratings.put("JML",3.0);
		ratings.put("SR",3.5);
		ratings.put("YMAD",2.5);
		ratings.put("TNL",3.0);
		
		f.put("Lisa Rose",ratings);
		
		Map<String,Double> ratings2 = new HashMap<String,Double>();
		
		ratings2.put("LIW", 3.0);
		ratings2.put("SOP",3.5);
		ratings2.put("JML",1.5);
		ratings2.put("SR",5.0);
		ratings2.put("YMAD",3.0);
		ratings2.put("TNL",3.5);
		
		f.put("Gene Seymour",ratings2);

		Map<String,Double> ratings3 = new HashMap<String,Double>();
		
		ratings3.put("LIW", 2.5);
		ratings3.put("SOP",3.0);
		ratings3.put("SR",3.5);
		ratings3.put("TNL",4.0);
		
		f.put("Micheal Phillips",ratings3);
		
		Map<String,Double> ratings4 = new HashMap<String,Double>();
		
		ratings4.put("SOP",3.5);
		ratings4.put("JML",3.0);
		ratings4.put("SR",4.0);
		ratings4.put("YMAD",2.5);
		ratings4.put("TNL",4.5);
		
		f.put("Claudia Puig",ratings4);

		Map<String,Double> ratings5 = new HashMap<String,Double>();

		ratings5.put("LIW", 3.0);
		ratings5.put("SOP",4.0);
		ratings5.put("JML",2.0);
		ratings5.put("SR",3.0);
		ratings5.put("YMAD",2.0);
		ratings5.put("TNL",3.0);
		
		f.put("Mike LaSalle",ratings5);
		
		Map<String,Double> ratings6 = new HashMap<String,Double>();

		ratings6.put("LIW", 3.0);
		ratings6.put("SOP",4.0);
		ratings6.put("SR",5.0);
		ratings6.put("YMAD",3.5);
		ratings6.put("TNL",3.0);
		
		f.put("Jack Matthews",ratings6);

		Map<String,Double> ratings7 = new HashMap<String,Double>();

		ratings7.put("SOP",4.5);
		ratings7.put("SR",4.0);
		ratings7.put("YMAD",1.0);
		
		f.put("Toby",ratings7);
		

		// Example: predict rating of the movie Lady in Water for user Toby.
		// r_hat for movie Lady in Water to Toby
		System.out.println (r_hat("Toby","LIW"));
	}
	
	public static Double pearsonSim(String u, String v){
		
		//compute means
		Double sumU =0.0;
		Double sumV =0.0;
		Double count = 0.0;
		Map<String,Double> items = (Map<String, Double>) f.get(u);
		
		Iterator iterator = items.entrySet().iterator();

		while(iterator.hasNext()){
			Map.Entry mapEntry2 = (Map.Entry) iterator.next();
	
			Map<String,Double> itemsV = (Map<String, Double>) f.get(v);
			if(itemsV.containsKey(mapEntry2.getKey())){
				sumU += (Double) mapEntry2.getValue();			
				sumV += (Double) itemsV.get(mapEntry2.getKey());
				count++;
			}

			
		}
		
		Double meanU,meanV = 0.0;
		meanU = sumU/count;
		meanV = sumV/count;
				
		
		Double sumprod = 0.0;
		Double sumsqrU = 0.0;
		Double sumsqrV = 0.0;
		
		items = (Map<String, Double>) f.get(u);
		iterator = items.entrySet().iterator();

		while(iterator.hasNext()){
			Map.Entry mapEntry2 = (Map.Entry) iterator.next();

			Map<String,Double> itemsV = (Map<String, Double>) f.get(v);
			if(itemsV.containsKey(mapEntry2.getKey())){
				sumprod += ((Double)mapEntry2.getValue() - meanU) * ((Double) itemsV.get(mapEntry2.getKey()) - meanV);
				sumsqrU += ((Double)mapEntry2.getValue() - meanU) * ((Double)mapEntry2.getValue() - meanU);
				sumsqrV += ((Double) itemsV.get(mapEntry2.getKey()) - meanV) * ((Double) itemsV.get(mapEntry2.getKey()) - meanV);
			}
		}
		
		return sumprod/(Math.sqrt(sumsqrU) * Math.sqrt(sumsqrV));
	}
	
	
	public static Double r_hat(String u, String i){
		
		
		String [] iterateSim = new String[f.size()-1];
		
		Iterator iterator = f.entrySet().iterator();
		int j=0;
		
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			
			if(!u.contentEquals((String)mapEntry.getKey())){
				iterateSim[j]=(String)mapEntry.getKey() +  pearsonSim(u,(String)mapEntry.getKey());	
				j++;
			}
			
		}
		
		iterateSim = sort(iterateSim);

		Double rate_sim_sum = 0.0;
		Double sim_sum = 0.0;
		
		// k = 5, since we dont have a lot of movie crticis in our dataset
		
		for(int k = 0;k<5;k++){
			Map<String,Double> itemsV = (Map<String, Double>) f.get(iterateSim[k].replaceAll("[^a-zA-Z ]", ""));
			if(itemsV.containsKey(i)){
				rate_sim_sum += (Double) itemsV.get(i) * Double.parseDouble(iterateSim[k].replaceAll("[^0-9.-]", "")); 
				sim_sum += Double.parseDouble(iterateSim[k].replaceAll("[^0-9.-]", ""));
			}
			
		}
		
		return rate_sim_sum/sim_sum;
	}
	
	public static String[] sort(String[] data){
		
		int lenD = data.length;
		  String tmp;
		
		  for(int i = 0;i<lenD;i++){
		    for(int j = (lenD-1);j>=(i+1);j--){
		      if(Double.parseDouble(data[j].replaceAll("[^0-9.-]",""))>Double.parseDouble(data[j-1].replaceAll("[^0-9.-]",""))){
		        tmp = data[j];
		        data[j]=data[j-1];
		        data[j-1]=tmp;
		      }
		    }
		  }
		  return data;
		}
}



	