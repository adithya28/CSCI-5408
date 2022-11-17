package org.example.A3;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.*;
import java.util.Arrays;

public class SparkCounter {

    private static void wordCount() throws IOException {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("WordCounter");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        String[] topics = {"Canada", "Halifax", "hockey", "hurricane", "electricity", "house", "inflation"};
        for (String topic : topics) {
            topic = "D:\\CSCI 5408\\A3\\Assignment3\\src\\main\\java\\org\\example\\A3\\articles\\"+ topic+".txt";
            JsonFactory jf = new JsonFactory();
            FileInputStream fis = new FileInputStream(topic);
            JsonParser jp = jf.createParser(fis);
            jp.setCodec(new ObjectMapper());
            jp.nextToken();
            while (jp.hasCurrentToken()) {
                ArticleStructure article = jp.readValueAs(ArticleStructure.class);
                jp.nextToken();
                String title =WordCounter.cleanString(article.getTitle());
                String contents = WordCounter.cleanString(article.getContent());
                BufferedWriter writer = new BufferedWriter(new FileWriter("tempData.txt", true));
                // Use a temporary file to parse the contents and title.
                String titles= writer.append(title).toString();
                String content= writer.append(contents).toString();
                writer.close();
                // count words in the title
            }
            JavaRDD<String> inputData = sparkContext.textFile("tempData.txt");
            JavaRDD<String> words = inputData.flatMap(content -> Arrays.asList(content.split(" ")).iterator());
            // MAP- REDUCE steps
            JavaPairRDD countData = words.mapToPair(tuple -> new Tuple2(
                    tuple, 1)).reduceByKey((x, y) -> (int) x + (int) y);
            countData.saveAsTextFile("WordCounts.txt");
        }
    }
    public static void main(String [] args) throws IOException {
        wordCount();
    }
}

