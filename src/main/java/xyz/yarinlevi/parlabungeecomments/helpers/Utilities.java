package xyz.yarinlevi.parlabungeecomments.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;
import xyz.yarinlevi.parlabungeecomments.exceptions.UUIDNotFoundException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Utilities {
    public static void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public static void sendMessage(ProxiedPlayer player, TextComponent message) {
        player.sendMessage(message);
    }

    public static String getUUIDOfUsername(String username) throws IOException, UUIDNotFoundException {
        Gson gson = new Gson();

        InputStream inputStream = new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openStream();

        if (inputStream == null) throw new UUIDNotFoundException("&cThe UUID of the player you requested was not found.");

        Reader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        PlayerProfile playerProfile = gson.fromJson(readAll(in), PlayerProfile.class);

        if (playerProfile != null) {
            return playerProfile.getId();
        } else {
            throw new UUIDNotFoundException("&cThe UUID of the player you requested was not found.");
        }
    }

    /*private static String streamToString(InputStream inputStream) {
        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }*/

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /*private static JsonElement readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JsonElement json = new JsonParser().parse(jsonText);
            return json;
        } finally {
            is.close();
        }
    }*/

    private static class PlayerProfile {
        @Getter @Setter String name;
        @Getter @Setter String id;
    }
}
