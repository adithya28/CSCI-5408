package org.example;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NewsExtraction {
    public static void main(String[] args) throws IOException {
       loadData();
    }
    public static void loadData() throws IOException {
        final JSONObject jsonLoader = new JSONObject();
        NewsApiClient newsApiClient = new NewsApiClient("4b26d175c1614fdd904ab3f4c44ef5c8");
        String[] topics = {"Canada", "Halifax", "hockey", "hurricane", "electricity", "house","inflation"};
        Writer fstream = null;
        BufferedWriter out = null;
        for (String topic : topics)
        {
            // Get data from News API and store them in .txt files.
            fstream = new OutputStreamWriter(new FileOutputStream("D:\\CSCI 5408\\A3\\Assignment3\\src\\main\\java\\org\\example\\articles\\" + topic+".txt"), StandardCharsets.UTF_8);
            Writer finalFstream = fstream;
            newsApiClient.getEverything(
                    new EverythingRequest.Builder()
                            .q(topic)
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            List<Article> articleList = response.getArticles();
                            for(int i=0;i<10;i++)
                            {
                                // Convert POJO to JSON Object.
                                Article currentArticle=articleList.get(i);
                                jsonLoader.put("title", currentArticle.getTitle());
                                jsonLoader.put("content", currentArticle.getContent());
                                try {
                                    finalFstream.write(jsonLoader.toString());
                                    finalFstream.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    });
        }
    }
}