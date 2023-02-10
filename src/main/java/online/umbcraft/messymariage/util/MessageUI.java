package online.umbcraft.messymariage.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessageUI {

    public static void sendActionbarMessage(Player p, String message) {
        sendMessage(p, message, ChatMessageType.ACTION_BAR);
    }

    public static void sendChatMessage(Player p, String message) {
        sendMessage(p, message, ChatMessageType.SYSTEM);
    }

    private static void sendMessage(Player p, String message, ChatMessageType type) {
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        p.spigot().sendMessage(type, null,  new TextComponent(coloredMessage));
    }
}
