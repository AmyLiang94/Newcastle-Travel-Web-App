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
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;


@Service("attractionsService")
public class AttractionsServiceImpl extends ServiceImpl<AttractionsDao, AttractionsEntity> implements AttractionsService {
    private Logger logger = LoggerFactory.getLogger(AttractionsServiceImpl.class);
    @Value("AIzaSyAyrCOQjmxR0Z2xeYxDh4-L_ipXlFHk85M")
    private String key;
    @Resource
    private AttractionsDao attractionsDao;
    @Resource
    private ReviewsService reviewsService;
    @Override
    public List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException, JSONException {
        List<AttractionsEntity> showList = new ArrayList<>();
        int uniqueId = 0;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&type=tourist_attraction&key=%s", latitude, longitude, radius, key);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();

        JsonArray datas = json.getAsJsonArray("results");
        for (JsonElement data : datas) {
            JsonObject curPlace = data.getAsJsonObject();
            String name = curPlace.get("name").getAsString();
            String address = curPlace.get("vicinity").getAsString();
            double rating = curPlace.has("rating") ? curPlace.get("rating").getAsDouble() : 0.0;
            String placeId = curPlace.get("place_id").getAsString();
            String overview = getOverViewByGoogleID(placeId);
            Random rand = new Random();
//            double ticketPrice = Math.round(rand.nextDouble() * 170) / 10.0;
            /**int randomNumber = rand.nextInt(2) + 1;
            String Category = null;
            if (randomNumber == 1){
                Category ="Historic";
            }else{
                Category ="Natural";
            }**/
            double lat = curPlace.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
            double lng = curPlace.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();
            String photo = curPlace.getAsJsonArray("photos").get(0).getAsJsonObject().get("photo_reference").getAsString();
            int WC_Accessibilty = getWheelChair_AccessblityByGoogleID(placeId);

            AttractionsEntity attractions = new AttractionsEntity();

            attractions.setAttractionName(name);
            attractions.setDescription(overview);
            //attractions.setCategory(Category);//
            attractions.setLatitude(BigDecimal.valueOf(lat));
            attractions.setLongitude(BigDecimal.valueOf(lng));
//            attractions.setTicketPrice(BigDecimal.valueOf(ticketPrice));
            attractions.setAttrRating(rating);
            attractions.setWheelchairAllow(WC_Accessibilty);
            attractions.setPlaceId(placeId);
            attractions.setAddress(address);
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
        String url = String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%.6f,%.6f&destination=%.6f,%.6f&mode=walking&key=%s", departLat, departLng, desLat, desLng, key);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();

        JsonArray routes = json.getAsJsonArray("routes");

        if (routes != null && routes.size() > 0) {
            JsonObject firstRoute = routes.get(0).getAsJsonObject();
            JsonArray legs = firstRoute.getAsJsonArray("legs");
            if (legs != null && legs.size() > 0) {
                JsonObject walk = legs.get(0).getAsJsonObject();
                String time = walk.getAsJsonObject("duration").get("text").getAsString();
                String[] strTimeList = time.split(" ");
                String finalTime = strTimeList [0];
                return finalTime;
            }
        }
        return "can't access";
    }

    @Override
    public List<AttractionsEntity> filterAttractionByCategory(List<Integer> attractionIDs, String category) {

        List<AttractionsEntity> tempAttractionEntities = new ArrayList<>();
        List<AttractionsEntity> filteredAttractions = new ArrayList<>();
        for (int a : attractionIDs){
            tempAttractionEntities.add(attractionsDao.getAttractionById(a));
        }
        for(AttractionsEntity a : tempAttractionEntities){
            if (a.getCategory().equals(category)){
                filteredAttractions.add(a);
            }
        }

        return filteredAttractions;
    }
    @Override
    public List<String> filterAttractionByWheelChairAccessibility(List<String> attractionGoogleId, Integer wc_allowed) throws JSONException, IOException {
        List<String> filteredAttractions = new ArrayList<>();
        for (String s: attractionGoogleId){
            if (getWheelChair_AccessblityByGoogleID(s) == wc_allowed){
                filteredAttractions.add(s);
            }
        }
        return filteredAttractions;
    }
    @Override
    public List<String> getOpeningHourMK2(String placeID) throws JSONException, IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&fields=opening_hours&key=" + key)
                .build();
        List<String> hoursList = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("opening_hours")) {
                JSONObject openingHours = result.getJSONObject("opening_hours");
                JSONArray periods = openingHours.getJSONArray("periods");

                String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                for (int i = 0; i < daysOfWeek.length; i++) {
                    String dayOfWeek = daysOfWeek[i];
                    boolean isOpen = false;
                    for (int j = 0; j < periods.length(); j++) {
                        JSONObject period = periods.getJSONObject(j);
                        JSONObject open = period.getJSONObject("open");
                        int openDay = open.getInt("day");
                        if (openDay == i) {
                            isOpen = true;
                            String openTime = open.getString("time");
                            JSONObject close = period.getJSONObject("close");
                            String closeTime = close.getString("time");
                            hoursList.add( openTime + "-" + closeTime);
                        }
                    }
                    if (!isOpen) {
                        hoursList.add("Closed");
                    }
                }

            } else {
            }
        }
        return hoursList;
    }
    @Override
    public int getCurrentOpeningStatus(String placeID) throws JSONException, IOException {
        int openStatus = 4;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&fields=opening_hours&key=" + key)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("opening_hours")) {
                JSONObject openingHours = result.getJSONObject("opening_hours");
                boolean openNow = openingHours.getBoolean("open_now");
                System.out.println("Is the shop open now? " + openNow);
                if (openNow == true){
                    openStatus = 1;
                } else if (openNow == false) {
                    openStatus = 0;
                }
            } else {
                System.out.println("Opening hours information is not available for this place.");
                openStatus = -1;
            }
        }
        return openStatus;
    }
    @Override
    public String getGooglePlaceIDByCoordinateAndName(String attractionName, String attractionAddress) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();

        String input = attractionName + " " + attractionAddress;
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + input + "&inputtype=textquery&fields=place_id&key=" + key)
                .build();
        String tempPlaceId = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONArray candidates = json.getJSONArray("candidates");

            if (candidates.length() > 0) {
                JSONObject candidate = candidates.getJSONObject(0);
                tempPlaceId = candidate.getString("place_id");

            } else {
            }
        }
        return tempPlaceId;
    }



    @Override
    public int getWheelChair_AccessblityByGoogleID(String attractionGoogleID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleID + "&fields=wheelchair_accessible_entrance&key=" + key)
                .build();
        boolean wheelchairAccessible = false;
        int accessibility = -1;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("wheelchair_accessible_entrance")) {
                wheelchairAccessible = result.getBoolean("wheelchair_accessible_entrance");
                if (wheelchairAccessible == true){
                    accessibility = 1;
                }else if (wheelchairAccessible == false){
                    accessibility = 0;
                }
                // do something with the wheelchairAccessible value
            } else {
                accessibility=-1;
            }
        }
        return accessibility;
    }

    @Override
    public String getCategoryByGoogleID(String attractionGoogleID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleID + "&fields=types&key=" + key)
                .build();
        String category = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("types")) {
                category = result.getString("types");

            } else {
                category="Category not Specified";
            }
        }
        return category;
    }

    @Override
    public double getRatingByGoogleID(String attractionGoogleID) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleID + "&fields=rating&key=" + key)
                .build();
        double rating = 0;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("rating")) {
                rating = result.getDouble("rating");

            } else {
                rating = -1 ;
            }
        }
        return rating;
    }

    @Override
    public String getPhoneNumberByGoogleID(String attractionGoogleI) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleI + "&fields=formatted_phone_number&key=" + key)
                .build();
        String phone = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("formatted_phone_number")) {
                phone = result.getString("formatted_phone_number");

            } else {
                phone="Phone Number not Specified";
            }
        }
        return phone;


    }
    @Override
    public String getAddressByGoogleID(String attractionGoogleI) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleI + "&fields=formatted_address&key=" + key)
                .build();
        String address = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("formatted_address")) {
                address = result.getString("formatted_address");

            } else {
                address="Address not Specified";
            }
        }
        return address;

    }

    @Override
    public String getOverViewByGoogleID(String attractionGoogleI) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleI + "&fields=editorial_summary&key=" + key)
                .build();
        String overview = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("editorial_summary")) {
                overview = result.getString("editorial_summary");

            } else {
                overview="overview not Specified";
            }
        }
        return overview;

    }

    @Override
    public String getNameByGoogleID(String attractionGoogleI) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleI + "&fields=name&key=" + key)
                .build();
        String name = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("name")) {
                name = result.getString("name");

            } else {
                name="overview not Specified";
            }
        }
        return name;

    }

    @Override
    public double getLatCoordByGoogleID(String attractionGoogleID) throws IOException, JSONException {
        String urlString = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + attractionGoogleID + "&fields=geometry&key=" + key;

        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create a request object
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        double lat = 0;
        try {
            // Execute the request and get the response
            Response response = client.newCall(request).execute();

            // Parse the response as a JSON object
            JSONObject jsonResponse = new JSONObject(response.body().string());

            // Extract the latitude and longitude coordinates from the response
            lat = jsonResponse.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");

            // Print the coordinates

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lat;
    }





    @Override
    public double getLngCoordByGoogleID(String attractionGoogleID) throws IOException, JSONException {
        String urlString = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + attractionGoogleID + "&fields=geometry&key=" + key;

        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create a request object
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        double lng = 0;
        try {
            // Execute the request and get the response
            Response response = client.newCall(request).execute();

            // Parse the response as a JSON object
            JSONObject jsonResponse = new JSONObject(response.body().string());

            // Extract the latitude and longitude coordinates from the response
            lng = jsonResponse.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            // Print the coordinates


        } catch (IOException e) {
            e.printStackTrace();
        }
        return lng;


    }

    @Override
    public String getOfficalWebsiteByGoogleID(String attractionGoogleI) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleI + "&fields=website&key=" + key)
                .build();
        String website = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("website")) {
                website = result.getString("website");

            } else {
                website="Phone not Specified";
            }
        }
        return website;

    }
    @Override
    public int getTotalNumberOfRatingsByGoogleID(String attractionGoogleI) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + attractionGoogleI + "&fields=user_ratings_total&key=" + key)
                .build();
        int NOR = 0;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONObject result = json.getJSONObject("result");
            if (result.has("user_ratings_total")) {
                NOR = result.getInt("user_ratings_total");

            } else {
                NOR=-1;
            }
        }
        return NOR;

    }




    @Override
    public String getGooglePlaceIDByName(String attractionName) throws IOException, JSONException {



        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + attractionName + "&inputtype=textquery&fields=place_id&key=" + key)
                .build();
        String tempPlaceId = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject json = new JSONObject(response.body().string());
            JSONArray candidates = json.getJSONArray("candidates");

            if (candidates.length() > 0) {
                JSONObject candidate = candidates.getJSONObject(0);
                tempPlaceId = candidate.getString("place_id");

                System.out.println("Place ID: " + tempPlaceId);
            } else {
                System.out.println("No place found with that name.");
            }
        }
        return tempPlaceId;
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
        if (reviews == null || reviews.size() == 0) {
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