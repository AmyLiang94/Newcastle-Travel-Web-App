package com.groupwork.charchar.service.impl;

import com.google.gson.*;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.groupwork.charchar.dao.AttractionsDao;
import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;


@Service("attractionsService")
public class AttractionsServiceImpl extends ServiceImpl<AttractionsDao, AttractionsEntity> implements AttractionsService {
    @Value("${google.maps.api.key}")
    private String key;

    @Override
    public List<AttractionsEntity> getNearByLocation(double latitude, double longitude, double radius) throws IOException {
        List<AttractionsEntity> showList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        String url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&type=tourist_attraction&key=%s", latitude, longitude, radius, key);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        // 解析响应数据
        Gson gson = new Gson();
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
    public List<AttractionsEntity> filterAttractionByOpeningTime(List<AttractionsEntity> attraction) {

        return null;
    }


}