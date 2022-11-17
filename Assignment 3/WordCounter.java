package org.example.A3;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.A3.ArticleStructure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class WordCounter {
    public static void main(String[] args) throws IOException {
        mapWords();
    }
    public static void mapWords() throws FileNotFoundException {
        String[] topics = {"Canada", "Halifax", "hockey", "hurricane", "electricity", "house", "inflation"};
        Map<String,Integer> wordCounts=new HashMap<String, Integer>() ;
        // initialize
        for (String topic : topics)
        {
            wordCounts.put(topic,0);
        }
        for (String topic : topics) {
            try (FileInputStream fis = new FileInputStream("D:\\CSCI 5408\\A3\\Assignment3\\src\\main\\java\\org\\example\\articles\\" + topic + ".txt")) {
                JsonFactory jf = new JsonFactory();
                JsonParser jp = jf.createParser(fis);
                jp.setCodec(new ObjectMapper());
                jp.nextToken();
                while (jp.hasCurrentToken()) {
                    ArticleStructure article = jp.readValueAs(ArticleStructure.class);
                    jp.nextToken();
                    // count words in the title
                    wordCounts.put(topic, wordCounts.get(topic) + countWords(article.getTitle(),topic));
                    // count words in the contents
                    wordCounts.put(topic, wordCounts.get(topic) + countWords(article.getContent(),topic));
                }
            } catch (JsonParseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(wordCounts);
    }
    public static String cleanString(String data) {
        //remove EOLs
        String pass1 =  data.replaceAll("[\r\n]", " ");
        //remove HTML tags
        String pass2 = pass1.replaceAll("[.*,?]", "");
        // remove all unicode characters
        String pass3 = pass2.replaceAll("\\\\u(\\p{XDigit}{4})", "");
        //remove html code
        pass3 = pass3.replaceAll("<.*?>", "");
        return pass3;
    }
    // Compare string and increment the corresponding string in a HashMap
    public static int countWords(String source,String target)
    {
        String cleanSource=cleanString(source);
        StringTokenizer tokenizer = new StringTokenizer(cleanSource," ");
        int count =0;
        while (tokenizer.hasMoreElements()) {
            String b = (String)tokenizer.nextElement();
            if(b.equals(target))
                count++;
        }
        return count;
    }
}

