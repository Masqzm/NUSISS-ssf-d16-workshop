package vttp.batch5.ssf.day16.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ImageSearchService {
    public static final String GET_URL = "https://api.giphy.com/v1/gifs/search";
    public final String QUERY_API_KEY;
    public static final int QUERY_OFFSET = 0; 
    public static final String QUERY_LANG = "en"; 
    public static final String QUERY_BUNDLE = "messaging_non_clips"; 

    public ImageSearchService(@Value("${api.key}") String QUERY_API_KEY) {
        this.QUERY_API_KEY = QUERY_API_KEY;
    }

    // @Value injection from app.properties
    public List<String> getWithQueryParams(String query, int limit, String rating) {
        String url = UriComponentsBuilder.fromUriString(GET_URL)
                    .queryParam("api_key", QUERY_API_KEY)
                    .queryParam("q", query)
                    .queryParam("limit", limit)
                    .queryParam("offset", QUERY_OFFSET)
                    .queryParam("rating", rating)
                    .queryParam("lang", QUERY_LANG)
                    .queryParam("bundle", QUERY_BUNDLE)
                    .toUriString();

        //System.out.printf("URL with query params: \n%s\n", url);

        RequestEntity<Void> req = RequestEntity
                    .get(url)
                    .accept(MediaType.APPLICATION_JSON)     
                    .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;
        List<String> imgUrls = new ArrayList<>();
        
        try {
            // Make call
            resp = template.exchange(req, String.class);
            // Extract payload
            String payload = resp.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();

            JsonArray data = result.getJsonArray("data");

            // Iterate over data arr
            for(int i = 0; i < data.size(); i++) {
                JsonObject images = data.getJsonObject(i).getJsonObject("images");
                JsonObject imgFixedHeight = images.getJsonObject("fixed_height");

                imgUrls.add(imgFixedHeight.getString("url"));
            }
        } catch(Exception ex) {
            // Handle error
            ex.printStackTrace();
        }

        return imgUrls;
    }
}
