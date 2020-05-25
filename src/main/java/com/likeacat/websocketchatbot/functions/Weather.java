package com.likeacat.websocketchatbot.functions;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Weather {
    public static String weather(String location, String spec) throws JSONException {
        try {
            String apikey = "8fa08987a4bda241abf7f88ad13a50a4";
            String result;
            if (spec.equals("current")) {
                String requestUrl = "https://api.openweathermap.org/data/2.5/find?q=" + location + "&units=metric&appid=" + apikey;
                JSONObject json = new JSONObject(IOUtils.toString(new URL(requestUrl), StandardCharsets.UTF_8));
                JSONArray list = (JSONArray) json.get("list");
                JSONObject member = list.getJSONObject(0);
                String name = (String) member.get("name");
                result = name + ": " + getMainInfo(member);
            } else if (spec.equals("24-hour forecast")) {
                String requestUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" + location + "&units=metric&appid=" + apikey;
                JSONObject json = new JSONObject(IOUtils.toString(new URL(requestUrl), StandardCharsets.UTF_8));
                JSONObject city = (JSONObject) json.get("city");
                String name = (String) city.get("name");
                result = "24-hour forecast for " + name + ": \r\n";
                JSONArray list = (JSONArray) json.get("list");
                JSONObject member;
                String dt_txt;
                for (int i = 0; i < 9; i++) {
                    member = list.getJSONObject(i);
                    dt_txt = ((String) member.get("dt_txt")).substring(11, 16);
                    result += dt_txt + ": \r\n" + getMainInfo(member) + "\r\n ---";
                }
            } else {
                result = "no data";
            }
            return result;
        } catch (IOException e) {
            return "City not found";
        }
    }

    public static String getMainInfo(JSONObject object) {
        String result;
        JSONObject main = (JSONObject) object.get("main");
        String temp, feels_like, speed;
        if (main.get("temp").getClass().equals(Double.class)) {
            temp = Long.toString(Math.round((double) main.get("temp")));
        } else {
            temp = Integer.toString((int) main.get("temp"));
        }
        if (main.get("feels_like").getClass().equals(Double.class)) {
            feels_like = Long.toString(Math.round((double) main.get("feels_like")));
        } else {
            feels_like = Integer.toString((int) main.get("feels_like"));
        }
        String pressure = Integer.toString((int) main.get("pressure"));
        String humidity = Integer.toString((int) main.get("humidity"));
        JSONObject wind = (JSONObject) object.get("wind");
        if (wind.get("speed").getClass().equals(Double.class)) {
            speed = Long.toString(Math.round((double) wind.get("speed")));
        } else {
            speed = Integer.toString((int) wind.get("speed"));
        }
        JSONArray weather = (JSONArray) object.get("weather");
        JSONObject weather_member = weather.getJSONObject(0);
        //String weather_main = (String) weather_member.get("main");
        String weather_description = (String) weather_member.get("description");
        String weather_icon = (String) weather_member.get("icon");
        weather_description = weather_description.substring(0, 1).toUpperCase() + weather_description.substring(1);
        JSONObject clouds = (JSONObject) object.get("clouds");
        String clouds_all = Integer.toString((int) clouds.get("all"));
        result = "icon: " + weather_icon + "\r\n" +
                weather_description + " (clouds: " + clouds_all + "%)\r\n" +
                "Temp: " + temp + "°C (feels like: " + feels_like + "°C) \r\n" +
                "Wind speed: " + speed + " m/s. \r\n" +
                "Pressure: " + pressure + " hpa \r\n" +
                "Humidity: " + humidity + "%";
        return result;
    }
}
