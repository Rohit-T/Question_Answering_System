import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.language.v1beta1.CloudNaturalLanguage;
import com.google.api.services.language.v1beta1.CloudNaturalLanguageScopes;
import com.google.api.services.language.v1beta1.model.AnalyzeEntitiesRequest;
import com.google.api.services.language.v1beta1.model.AnalyzeEntitiesResponse;
import com.google.api.services.language.v1beta1.model.AnalyzeSyntaxRequest;
import com.google.api.services.language.v1beta1.model.AnalyzeSyntaxResponse;
import com.google.api.services.language.v1beta1.model.Document;
import com.google.api.services.language.v1beta1.model.Entity;
import com.google.api.services.language.v1beta1.model.EntityMention;
import com.google.api.services.language.v1beta1.model.Token;

import java.io.IOException;
import java.io.PrintStream;

import java.security.GeneralSecurityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyzeText {

    private final CloudNaturalLanguage languageApi;

    private static final String APPLICATION_NAME = "IRQuestionAnsweringSystem/1.0";

    static ArrayList<String> subjects = new ArrayList<String>();
    static ArrayList<String> objects = new ArrayList<String>();
    
    static String root;

    public AnalyzeText() {
        
        CloudNaturalLanguage lang = null;

        try {
            lang = getLanguageService();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        this.languageApi = lang;
    }

    public ArrayList<String> GoogleNLP(String text) throws IOException, GeneralSecurityException {

        final String POS = "NOUN VERB ADJ NUM";

        ArrayList<String> outputTweets = new ArrayList<String>();

        String[][] partsOfSpeech = findPartsOfSpeech(analyzeSyntax(text));

        if(partsOfSpeech == null) {
            return null;
        }

        String entities = findEntities(analyzeEntities(text));
        
        if(entities == null) {
            return null;
        }
        
        for (int i = 0; i < partsOfSpeech.length; i++){

        	if (entities.contains(partsOfSpeech[i][0])){
        		
                partsOfSpeech[i][2] = "Yes";
        	}else{

        		partsOfSpeech[i][2] = "No";
        	}
        }
        
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < partsOfSpeech.length; i++){
        	if(POS.contains(partsOfSpeech[i][1]) || partsOfSpeech[i][2] == "Yes"){
        		query.append(partsOfSpeech[i][0]);
        		query.append("+");
        	}
        }

        if(query.length() != 0){
            outputTweets = passQueryParams(query,text,objects,subjects,root);
        }

        return outputTweets;
    }

    /** To generate query for solr instance and fetch results */

    public ArrayList<String> passQueryParams(StringBuilder query, String text, ArrayList<String> objects, ArrayList<String> subjects, String root){
    	
        ArrayList<String> tweets = new ArrayList<String>();
        HttpQueryExecution hq = new HttpQueryExecution();
        tweets = hq.fireQuery(query,text,objects,subjects,root);
        
        return tweets;
    }

    /** To get Entities of the input text */

    public static String findEntities(List<Entity> entities) {
    	
    	String entityString = null;

        if (entities == null || entities.size() == 0) {

            return entityString;

        }

        for (Entity entity : entities) {
        	
        	entityString = entity.getName() + " " + entityString;
        }
        
        return entityString;
    }

    public List<Entity> analyzeEntities(String text) throws IOException {

        AnalyzeEntitiesRequest request =

                new AnalyzeEntitiesRequest()

                        .setDocument(new Document().setContent(text).setType("PLAIN_TEXT"))

                        .setEncodingType("UTF16");

        CloudNaturalLanguage.Documents.AnalyzeEntities analyze =

                languageApi.documents().analyzeEntities(request);

        AnalyzeEntitiesResponse response = analyze.execute();

        return response.getEntities();
    }

    /** To get the parts of speech of input text */

    public static String[][] findPartsOfSpeech(List<Token> tokens) {

        String object = "DOBJ IOBJ POBJ";
        String subject = "CSUBJ CSUBJPASS NSUBJ NSUBJPASS NOMCSUBJ NOMCSUBJP";

        if (tokens == null || tokens.size() == 0) {

            return null;
        }
        
        String[][] partsOfSpeech = new String[tokens.size()][3];
        
        for (int i = 0; i < tokens.size(); i++) {
            
            Token token = tokens.get(i);

            partsOfSpeech[i][0] = token.getText().getContent();
            partsOfSpeech[i][1] = token.getPartOfSpeech().getTag();

            if (object.contains(token.getDependencyEdge().getLabel())){

            	objects.add(partsOfSpeech[i][0]);
            }else if (subject.contains(token.getDependencyEdge().getLabel())){

            	subjects.add(partsOfSpeech[i][0]);
            }else if (token.getDependencyEdge().getLabel() == "ROOT"){

            	root = partsOfSpeech[i][0];
            }
        }

        return partsOfSpeech;
    }

    public List<Token> analyzeSyntax(String text) throws IOException {

        AnalyzeSyntaxRequest request =

                new AnalyzeSyntaxRequest()

                        .setDocument(new Document().setContent(text).setType("PLAIN_TEXT"))

                        .setEncodingType("UTF16");

        CloudNaturalLanguage.Documents.AnalyzeSyntax analyze =

                languageApi.documents().analyzeSyntax(request);

        AnalyzeSyntaxResponse response = analyze.execute();

        return response.getTokens();
    }

    /** To connect to Google Cloud Platform */

    public static CloudNaturalLanguage getLanguageService()

            throws IOException, GeneralSecurityException {

        GoogleCredential credential =

                GoogleCredential.getApplicationDefault().createScoped(CloudNaturalLanguageScopes.all());

        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new CloudNaturalLanguage.Builder(

                GoogleNetHttpTransport.newTrustedTransport(),

                jsonFactory, new HttpRequestInitializer() {

            @Override

            public void initialize(HttpRequest request) throws IOException {

                credential.initialize(request);

            }

        })

                .setApplicationName(APPLICATION_NAME)

                .build();
    }
}