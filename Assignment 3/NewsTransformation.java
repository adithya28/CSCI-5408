package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class NewsTransformation {
    static MongoClient mongoClient;
    public static void main(String[] args) throws IOException {
    processData();
    }
    public static void processData() throws IOException {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("BigMongoNews");
        String[] topics = {"Canada", "Halifax", "hockey", "hurricane", "electricity", "house", "inflation"};
        for (String topic : topics) {
            database.createCollection(topic);
            try (FileInputStream fis = new FileInputStream("D:\\CSCI 5408\\A3\\Assignment3\\src\\main\\java\\org\\example\\articles\\" + topic + ".txt")) {
                JsonFactory jf = new JsonFactory();
                JsonParser jp = jf.createParser(fis);
                jp.setCodec(new ObjectMapper());
                jp.nextToken();
                while (jp.hasCurrentToken()) {
                    ArticleStructure article= jp.readValueAs(ArticleStructure.class);
                    jp.nextToken();
                    loadData(mongoClient,article,topic);
               }
            }
       }
    }
    public static void loadData(MongoClient mongoClient,ArticleStructure article , String topic)
    {
        MongoDatabase database = mongoClient.getDatabase("BigMongoNews");
        Document document = new Document("title", cleanString(article.getTitle()))
                .append("contents", cleanString(article.getContent()));
        database.getCollection(topic).insertOne(document);
    }
    public static String cleanString(String data) {
        //remove EOLs
        String pass1 =  data.replaceAll("[\r\n]", " ");
        //remove periods
        String pass2 = pass1.replaceAll("[.*,?]", "");
        // remove all unicode characters
        String pass3 = pass2.replaceAll("\\\\u(\\p{XDigit}{4})", "");
        //remove html code
        pass3 = pass3.replaceAll("<.*?>", "");
        return pass3;
    }
}
