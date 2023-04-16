package com.groupwork.charchar.service.impl;

import com.google.gson.*;
import com.google.maps.model.OpeningHours;
import com.google.maps.model.PlacesSearchResponse;
import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.groupwork.charchar.dao.AttractionsDao;
import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sun.net.www.protocol.http.HttpURLConnection;

import java.net.http.HttpClient;

import javax.xml.transform.Result;


@Service("attractionsService")
public class AttractionsServiceImpl extends ServiceImpl<AttractionsDao, AttractionsEntity> implements AttractionsService {
    @Value("${google.maps.api.key}")
    private String key;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException {
        List<AttractionsEntity> showList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        //RequestBody body = RequestBody.create(mediaType, "");
        String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&type=tourist_attraction&key=%s", latitude, longitude, radius, key);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        // 解析响应数据
        //Gson gson = new Gson();
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        //获取列表
        JsonArray datas = json.getAsJsonArray("results");
        for (JsonElement data : datas) {
            JsonObject curPlace = data.getAsJsonObject();
            String name = curPlace.get("name").getAsString();
//            String icon = curPlace.get("icon").getAsString();
            double lat = curPlace.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
            double lng = curPlace.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();
            String photo = curPlace.getAsJsonArray("photos").get(0).getAsJsonObject().get("photo_reference").getAsString();
            // json to AttractionEntity
            AttractionsEntity attractions = new AttractionsEntity();
            attractions.setAttractionName(name);
            attractions.setLatitude(BigDecimal.valueOf(lat));
            attractions.setLongitude(BigDecimal.valueOf(lng));
            attractions.setImageUrl(photo);
            showList.add(attractions);
        }
        return showList;
    }

    // 返回的数据类似："57 mins"
    @SneakyThrows
    @Override
    public String getWalkTime(double departLat, double departLng, double desLat, double desLng) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        String url = String.format("https://maps.googleapis.com/maps/api/directions/json?origin%f,%f&destination=%f,%f&key=%s", departLat, departLng, desLat, desLng, key);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", body)
                .build();
            Response response = client.newCall(request).execute();
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        JsonObject walk = json.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonArray("leg").get(0).getAsJsonObject();
        String time = walk.getAsJsonObject("duration").get("text").getAsString();
        return time;

    }

    @Override
    public List<AttractionsEntity> filterAttractionByCategory(List<AttractionsEntity> attractions, String category) {

        List<AttractionsEntity> filteredAttractions = attractions.stream().filter(a -> a
                        .getCategory().equals(category))
                .collect(Collectors.toList());



        return filteredAttractions;
    }

    @Override
    public List<AttractionsEntity> filterAttractionByWheelChairAccessibility(List<AttractionsEntity> attractions, Integer wheelchairAllow) {
        List<AttractionsEntity> filteredAttractions = new ArrayList<>();
        for (AttractionsEntity attraction: attractions){
            if (attraction.getWheelchairAllow() == wheelchairAllow){
                filteredAttractions.add(attraction);
            }
        }




        return filteredAttractions;
    }
    @Override
    public String getOpeningHours(String placeId, DayOfWeek dayOfWeek) {
        // Set up the API key and request URL

        String urlString = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId + "&fields=opening_hours&key=" + key;

        try {
            // Create a URL object from the request URL
            URL url = new URL(urlString);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the response was successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response body
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the response JSON
                JSONObject json = new JSONObject(response.toString());
                JSONObject result = json.getJSONObject("result");
                JSONObject openingHours = result.getJSONObject("opening_hours");

                // Extract the opening hours information for the specified day
                JSONArray periods = openingHours.getJSONArray("periods");
                LocalTime openingTime = null;
                LocalTime closingTime = null;
                for (int i = 0; i < periods.length(); i++) {
                    JSONObject period = periods.getJSONObject(i);
                    int dayOfWeekValue = period.getJSONObject("open").getInt("day");
                    if (dayOfWeekValue == dayOfWeek.getValue()) {
                        String openTimeStr = period.getJSONObject("open").getString("time");
                        String closeTimeStr = period.getJSONObject("close").getString("time");
                        openingTime = LocalTime.parse(openTimeStr, DateTimeFormatter.ofPattern("HHmm"));
                        closingTime = LocalTime.parse(closeTimeStr, DateTimeFormatter.ofPattern("HHmm"));
                        break;
                    }
                }

                // Format the opening and closing times as strings
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                String openingTimeStr = openingTime.format(formatter);
                String closingTimeStr = closingTime.format(formatter);

                // Return the formatted opening and closing times
                return openingTimeStr + " - " + closingTimeStr;
            } else {
                // The response was not successful
                System.out.println("Failed to get opening hours: " + responseCode);
            }
        } catch (IOException e) {
            // An error occurred while sending the request
            e.printStackTrace();
        }

        // Return null if an error occurred or if the response was not successful
        return null;
    }


}