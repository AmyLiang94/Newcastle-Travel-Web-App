package com.groupwork.charchar.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.groupwork.charchar.common.Constants;
import com.groupwork.charchar.dao.AttractionsDao;
import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.exception.AttractionNotFoundException;
import com.groupwork.charchar.service.AttractionsService;
import com.groupwork.charchar.service.ReviewsService;
import com.groupwork.charchar.vo.UpdateAttractionRatingVO;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service("attractionsService")
public class AttractionsServiceImpl extends ServiceImpl<AttractionsDao, AttractionsEntity> implements AttractionsService {
    private Logger logger = LoggerFactory.getLogger(AttractionsServiceImpl.class);
    @Value("${google.maps.api.key}")
    private String key;
    @Resource
    private AttractionsDao attractionsDao;
    @Resource
    private ReviewsService reviewsService;

    @Override
    public List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException {
        List<AttractionsEntity> showList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&type=tourist_attraction&key=%s", latitude, longitude, radius, key);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        //获取列表
        JsonArray datas = json.getAsJsonArray("results");
        for (JsonElement data : datas) {
            JsonObject curPlace = data.getAsJsonObject();
            String name = curPlace.get("name").getAsString();
            double lat = curPlace.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
            double lng = curPlace.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();
            String photo = curPlace.getAsJsonArray("photos").get(0).getAsJsonObject().get("photo_reference").getAsString();
            AttractionsEntity attractions = new AttractionsEntity();
            attractions.setAttractionName(name);
            attractions.setLatitude(BigDecimal.valueOf(lat));
            attractions.setLongitude(BigDecimal.valueOf(lng));
            attractions.setImageUrl(photo);
            showList.add(attractions);
        }
        return showList;
    }

    // 返回的数据类似："57" 单位：分钟
    @SneakyThrows
    @Override
    public String getWalkTime(double departLat, double departLng, double desLat, double desLng) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%.15f,%.15f&destination=%.15f,%.15f&mode=walking&key=%s", departLat, departLng, desLat, desLng, key);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        System.out.println(json.toString());
        JsonArray routes = json.getAsJsonArray("routes");
        System.out.println(routes.toString());
        if (routes != null && routes.size() > 0) {
            JsonObject firstRoute = routes.get(0).getAsJsonObject();
            JsonArray legs = firstRoute.getAsJsonArray("legs");
            if (legs != null && legs.size() > 0) {
                JsonObject walk = legs.get(0).getAsJsonObject();
                String time = walk.getAsJsonObject("duration").get("text").getAsString();

                Pattern pattern = Pattern.compile("(\\d+)\\s+(\\w+)");
                Matcher matcher = pattern.matcher(time);

                int totalMinutes = 0;
                while (matcher.find()) {
                    int value = Integer.parseInt(matcher.group(1));
                    String unit = matcher.group(2);

                    if ("days".equalsIgnoreCase(unit)) {
                        totalMinutes += value * 24 * 60;
                    } else if ("hours".equalsIgnoreCase(unit)) {
                        totalMinutes += value * 60;
                    } else if ("mins".equalsIgnoreCase(unit) || "min".equalsIgnoreCase(unit)) {
                        totalMinutes += value;
                    }
                }
                return String.valueOf(totalMinutes);
            }
        }
        return "can't access";
    }

    @Override
    public List<AttractionsEntity> filterAttractionByCategory(List<AttractionsEntity> attractions, String category) {

        List<AttractionsEntity> filteredAttractions = attractions.stream().filter(a -> a
                        .getCategory().equals(category))
                .collect(Collectors.toList());

        return filteredAttractions;
    }

    @Override
    public List<AttractionsEntity> filterAttractionByWheelChairAccessibility(List<AttractionsEntity> attractions, Integer wc_allowed) {
        List<AttractionsEntity> filteredAttractions = new ArrayList<>();
        for (AttractionsEntity attraction: attractions){
            if (attraction.getWheelchairAllow() == wc_allowed){
                filteredAttractions.add(attraction);
            }
        }
        return filteredAttractions;
    }
    @SneakyThrows
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
                return openingTimeStr + "-" + closingTimeStr;
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


    @Override
    public UpdateAttractionRatingVO updateAttractionRating(Integer attractionId) {
        AttractionsEntity attraction = attractionsDao.getAttractionById(attractionId);
        if (attraction == null) {
            throw new AttractionNotFoundException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo() + ": " + attractionId);
        }
        Double attrSumRating = 0d;
        Integer reviewCount = 0;
        List<ReviewsEntity> reviews = reviewsService.listReviewsByAttractionId(attractionId);
        // if there is no one review the attraction, if will return a empty attraction entity.
        if (null == reviews || reviews.size() == 0) {
            return new UpdateAttractionRatingVO();
        }
        for (ReviewsEntity review : reviews) {
            attrSumRating += review.getRating();
            reviewCount++;
        }
        Double attrRating = attrSumRating / reviewCount;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        attrRating = Double.parseDouble(decimalFormat.format(attrRating));
        try {
            int updateStatus = attractionsDao.updateAttractionRating(attractionId, attrRating);
            if (updateStatus == 0) {
                throw new RuntimeException("update fail , attraction : " + attractionId);
            }
        } catch (Exception e) {
            logger.error("Failed to update attraction rating, attraction ID: " + attractionId, e);
            throw new RuntimeException("can't update, attraction : " + attractionId);
        }
        UpdateAttractionRatingVO updateAttraction = new UpdateAttractionRatingVO();
        updateAttraction.setAttractionId(attractionId);
        updateAttraction.setAttrRating(attrRating);
        return updateAttraction;
    }


}