package com.likeacat.websocketchatbot.bot;

import com.likeacat.websocketchatbot.entities.Message;
import com.likeacat.websocketchatbot.entities.User;
import com.likeacat.websocketchatbot.functions.MyMath;
import com.likeacat.websocketchatbot.services.MessageService;
import com.likeacat.websocketchatbot.services.UserService;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class bot {
    String name = "Bot";

    public Message start() {
        Message botMessage = new Message();
        botMessage.setType(Message.MessageType.CHAT);
        botMessage.setContent("Hello. How can I help you?");
        botMessage.setSender(name);
        return botMessage;
    }

    private String timezone() throws IOException, JSONException {
        String requestUrl = "http://worldtimeapi.org/api/ip";
        JSONObject json = new JSONObject(IOUtils.toString(new URL(requestUrl), StandardCharsets.UTF_8));
        String abbreviation = (String) json.get("abbreviation");
        String timezone = (String) json.get("timezone");
        String datetime = (String) json.get("datetime");
        String utc_offset = (String) json.get("utc_offset");
        int day_of_week = (int) json.get("day_of_week");
        String str_day_of_week;
        int week_number = (int) json.get("week_number");
        int day_of_year = (int) json.get("day_of_year");
        switch (day_of_week) {
            case 1: str_day_of_week = "Monday"; break;
            case 2: str_day_of_week = "Tuesday"; break;
            case 3: str_day_of_week = "Wednesday"; break;
            case 4: str_day_of_week = "Thursday"; break;
            case 5: str_day_of_week = "Friday"; break;
            case 6: str_day_of_week = "Saturday"; break;
            case 7: str_day_of_week = "Sunday"; break;
            default : str_day_of_week = Integer.toString(day_of_week);
        }
        return "Local time: " + datetime.substring(11, 19) + "\r\n" +
                "Timezone: " + timezone + "(UTC" + utc_offset + ") \r\n" +
                "Abbreviation: " + abbreviation + "\r\n" +
                "Date: " + datetime.substring(0, 10) + "\r\n" +
                "Week number: " + week_number + "\r\n" +
                "Day of week: " + str_day_of_week + "\r\n" +
                "Day of year: " + day_of_year;
    }

    private String weather(String location, String spec) throws IOException, JSONException {
        String apikey = "b79362d4c85140dc4293d0f0a534177d";
        String result;
        if (spec.equals("current")) {
            String requestUrl = "https://api.openweathermap.org/data/2.5/find?q=" + location + "&units=metric&appid=" + apikey;
            JSONObject json = new JSONObject(IOUtils.toString(new URL(requestUrl), StandardCharsets.UTF_8));
            JSONArray list = (JSONArray) json.get("list");
            JSONObject member = list.getJSONObject(0);
            String name = (String) member.get("name");
            JSONObject main = (JSONObject) member.get("main");
            String temp, feels_like;
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
            JSONObject wind = (JSONObject) member.get("wind");
            String speed;
            if (wind.get("speed").getClass().equals(Double.class)) {
                speed = Long.toString(Math.round((double) wind.get("speed")));
            } else {
                speed = Integer.toString((int) wind.get("speed"));
            }
            JSONArray weather = (JSONArray) member.get("weather");
            JSONObject weather_member = weather.getJSONObject(0);
            //String weather_main = (String) weather_member.get("main");
            String weather_description = (String) weather_member.get("description");
            String weather_icon = (String) weather_member.get("icon");
            weather_description = weather_description.substring(0,1).toUpperCase() + weather_description.substring(1);
            JSONObject clouds = (JSONObject) member.get("clouds");
            String clouds_all = Integer.toString((int) clouds.get("all"));
            result = name + ": " +
                    "icon: " + weather_icon + "\r\n" +
                    weather_description + " (clouds: " + clouds_all + "%)\r\n" +
                    "Temp: " + temp + "°C (feels like: " + feels_like + "°C) \r\n" +
                    "Wind speed: " + speed + " m/s. \r\n " +
                    "Pressure: " + pressure + " hpa \r\n " +
                    "Humidity: " + humidity + "%";
        }
        else {
            result = name + ": " + "temp" + "°C. \r\n Feels like: " + "feels_like" + "°C";
        }
        return result;
    }

    private String compute(String question) {
        return MyMath.compute(question);
    }

    public String answer(String question, String sender, MessageService messageService, UserService userService) throws IOException, JSONException {

        Message botmessage = new Message();
        question = question.toLowerCase();
        switch (question) {
            case "hello":
                botmessage.setType(Message.MessageType.CHAT);
                botmessage.setContent("Hello. What do you want?");
                botmessage.setSender(name);
                return botmessage.getContent();
            case "weather":
                botmessage.setType(Message.MessageType.CHAT);
                botmessage.setContent("Where?");
                botmessage.setSender(name);
                return botmessage.getContent();
            case "timezone":
                return timezone() + "\r\n Anything else?";
            case "compute":
                botmessage.setType(Message.MessageType.CHAT);
                botmessage.setContent("Write the expression \r\n It can contain only those symbols: +, -, *, /, (, ), ^.");
                botmessage.setSender(name);
                return botmessage.getContent();
            case "bye":
                botmessage.setType(Message.MessageType.CHAT);
                botmessage.setContent("Bye.");
                botmessage.setSender(name);
                return botmessage.getContent();
            default:
                if (question.equals("back"))
                    return "Ok. Anything else?";
                if (messageService.findPreLast(sender).getType() == Message.MessageType.CHAT &&
                        messageService.findPreLast(sender).getContent().toLowerCase().equals("weather"))
                {
                    if (question.equals("edit")) {
                        return "Which one do you want to delete?";
                    }
                    else {
                        String result = weather(question, "current");
                        int i = 0;
                        for (String bookmark : userService.findByName(sender).getBookmarks()) {
                            i++;
                            if (bookmark.toLowerCase().equals(question))
                                return result + "\r\n Anything else?";
                        }
                        if (i < 3)
                            return result + "\r\n Do you want to save this location?";
                        else
                            return result + "\r\n Anything else?";
                    }
                }
                if (messageService.findLast(sender).getType() == Message.MessageType.CHAT &&
                        (messageService.findLast(sender).getContent().endsWith("Do you want to save this location?"))) {
                    if (question.equals("yes")) {
                        User user = userService.findByName(sender);
                        int n = user.getBookmarks().length;
                        String[] newbookmarks = new String[n + 1];
                        for (int i = 0; i < n; i++)
                            newbookmarks[i] = user.getBookmarks()[i];
                        newbookmarks[n] = messageService.findPreLast(sender).getContent();
                        user.setBookmarks(newbookmarks);
                        userService.update(user);
                        return "Saved. \r\n Anything else?";
                    }
                    if (question.equals("no"))
                        return "Ok. \r\n Anything else?";
                }
                if (messageService.findLast(sender).getType() == Message.MessageType.CHAT &&
                        (messageService.findLast(sender).getContent().equals("Which one do you want to delete?"))) {
                    User user = userService.findByName(sender);
                    boolean flag = false;
                    for (String bookmark : user.getBookmarks())
                        if (bookmark.toLowerCase().equals(question)) {
                            flag = true;
                            break;
                        }
                    if (flag) {
                        int n = user.getBookmarks().length;
                        String[] newbookmarks = new String[n - 1];
                        for (int i = 0, k = 0; i < n; i++) {
                            if (user.getBookmarks()[i].toLowerCase().equals(question))
                                continue;
                            newbookmarks[k++] = user.getBookmarks()[i];
                        }
                        user.setBookmarks(newbookmarks);
                        userService.update(user);
                        return "Saved. \r\n Anything else?";
                    }
                    else
                        return "You already don't have that location.";
                }
                if (messageService.findPreLast(sender).getType() == Message.MessageType.CHAT &&
                        messageService.findPreLast(sender).getContent().toLowerCase().equals("compute"))
                    return compute(question);
                if (messageService.findLast(sender).getType() == Message.MessageType.CHAT &&
                        (messageService.findLast(sender).getContent().equals("Invalid number of braces. Please check and try again.") ||
                                messageService.findLast(sender).getContent().equals("Expression is invalid. Please check and try again.")))
                    return compute(question);
                else {
                    botmessage.setType(Message.MessageType.CHAT);
                    botmessage.setContent("I can't do that. Anything else?");
                    botmessage.setSender(name);
                    return botmessage.getContent();
                }
        }
    }
}
