import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HttpQueryExecution {
    String query;
    String ip_address = "35.165.111.84:8983";// ip address and port of the server running solr
    String url_query = "";

    /** To fire the solr query, get results and score them */
    public ArrayList<String> fireQuery(StringBuilder query, String text, ArrayList<String> objects, ArrayList<String> subjects, String root){
    	
        url_query = "http://" + ip_address + "/solr/VSM/select?fl=*,score&indent=on&defType=dismax&q="+query+"&qf=text_en&rows=100&wt=json&";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url_query);
        ArrayList<String> tweet_text = new ArrayList<String>();
        try {
            CloseableHttpResponse response = httpclient.execute(httpget);
            String json_string = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(json_string);
            JSONObject resp = (JSONObject) obj.get("response");
            JSONArray docs = (JSONArray) resp.get("docs");
            
            String[][] scores = new String[docs.size()][2]; 
            
            for (int j = 0; j < docs.size(); j++ ){
            	JSONObject doc = (JSONObject) docs.get(j);
            	
            	System.out.println("OLDScore: "+ doc.get("score").toString() +" Text: "+ doc.get("text").toString());
            }
            
            for(int i=0; i<docs.size(); i++){
                JSONObject docs_iterator = (JSONObject) docs.get(i);
                
                double score1 = 0.0;
                score1 = Double.parseDouble(docs_iterator.get("score").toString());
                String content = docs_iterator.get("text").toString();
                String tweet = content.substring(2, content.length() - 2);
                
                String jsonString = docs_iterator.toString();
                
                if (text.toLowerCase().contains("who")){
                	if(jsonString.contains("PERSON")){
                		score1 = score1 + 1;
                	}
                }else if(text.toLowerCase().contains("when")){
                	if(jsonString.contains("TIME")){
                		score1 = score1 + 1;
                	}
                }else if(text.toLowerCase().contains("where")){
                	if(jsonString.contains("LOCATION") || jsonString.contains("EVENT")){
                		score1 = score1 + 1;
                	}
                }
                
                if (jsonString.contains("SUBJECT")){
                	Iterator<String> iterator = subjects.iterator();
                	while(iterator.hasNext()){
                		if (docs_iterator.get("SUBJECT").toString().contains(iterator.next())){
                			score1 = score1 + 1;
                		}
                	}
                }
                if (jsonString.contains("OBJECT")){
                	Iterator<String> iterator = objects.iterator();
                	while(iterator.hasNext()){
                		if (docs_iterator.get("OBJECT").toString().contains(iterator.next())){
                			score1 = score1 + 1;
                		}
                	}
                }
                
                scores[i][0] = Double.toString(score1);
                scores[i][1] = tweet;
            }
            Arrays.sort(scores, (a, b) -> Double.compare(Double.parseDouble(b[0]), Double.parseDouble(a[0])));
            
            for (int k = 0; k < 10; k++){
            	System.out.println("Score: "+ scores[k][0] +" Text: "+ scores[k][1]);
            	tweet_text.add(scores[k][1].replace("\\","").replace("?*", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweet_text;
    }
}
