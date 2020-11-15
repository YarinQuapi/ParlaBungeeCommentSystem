package xyz.yarinlevi.parlabungeecomments.helpers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utilities {
    public static void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
