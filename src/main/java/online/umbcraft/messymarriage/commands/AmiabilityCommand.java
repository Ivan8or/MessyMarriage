package online.umbcraft.messymarriage.commands;

import online.umbcraft.messymarriage.amiability.LevelSanitizer;
import online.umbcraft.messymarriage.data.PairData;
import online.umbcraft.messymarriage.util.ExpLevelConverter;
import online.umbcraft.messymarriage.util.MessageUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AmiabilityCommand implements CommandExecutor, TabCompleter {

    final private Plugin plugin;
    final private PairData pairs;
    final private LevelSanitizer exp;

    public AmiabilityCommand(Plugin plugin, LevelSanitizer exp, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.exp = exp;
    }

    public void start() {
        plugin.getServer().getPluginCommand("amiability").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch(args.length) {
            case 1:
                if(!(sender instanceof Player)) {
                    sender.sendMessage("Console does not have an amiability... forever alone :(");
                    return false;
                }
                return checkAmiabilityWith((Player) sender, args);

            case 2:
                return checkPairAmiability(sender, args);

            default:
                return amiabilityHelp(sender);
        }
    }

    public boolean amiabilityHelp(CommandSender sender) {
        String[] message = {
                "&eAmiability is a gauge of how close two players are.",
                "&eRaise your amiability by spending time together, as",
                "&ewell as by doing romantic activities together.",
                " ",
                "&cCommands:",
                "&6/amiability &e- the help menu.",
                "&6/amiability <playername> &e- reports the amiability between you and the target player.",
                "&6/amiability <p1> <p2> &e- reports the amiability between two players."
        };
        for(String m : message)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));

        return true;
    }

    public boolean checkAmiabilityWith(Player sender, String[] args) {
        Player p2 = plugin.getServer().getPlayerExact(args[0]);
        if(p2 == null) {
            sender.sendMessage(isNotOnline(args[0]));
            return false;
        }
        if(sender == p2) {
            String toSend = "&cYou do not have an amiability with yourself.";
            MessageUI.sendChatMessage(sender, toSend);
            return false;
        }

        UUID pairID = pairs.pairID(sender.getUniqueId(), p2.getUniqueId());
        int level = exp.getLevel(pairID);
        int levelProgress = ExpLevelConverter.percentProgress(exp.getExp(pairID));
        String toSend = "&eYour amiability with "+p2.getName()+" is &6"+level+" &e(&6"+levelProgress+"%&e).";
        MessageUI.sendChatMessage(sender, toSend);
        return true;
    }

    public boolean checkPairAmiability(CommandSender sender, String[] args) {
        Player p1 = plugin.getServer().getPlayerExact(args[0]);
        Player p2 = plugin.getServer().getPlayerExact(args[1]);
        if(p1 == null) {
            sender.sendMessage(isNotOnline(args[0]));
            return false;
        }
        if(p2 == null) {
            sender.sendMessage(isNotOnline(args[1]));
            return false;
        }
        if(p1 == p2) {
            String toSendRaw = "&cPlayers do not have an amiability with themselves.";
            String toSend = ChatColor.translateAlternateColorCodes('&', toSendRaw);
            sender.sendMessage(toSend);
            return false;
        }

        UUID pairID = pairs.pairID(p1.getUniqueId(), p2.getUniqueId());
        String toSendRaw = "&e"+p1.getName()+"'s amiability with "+p2.getName()+" is "+exp.getLevel(pairID);
        String toSend = ChatColor.translateAlternateColorCodes('&', toSendRaw);
        sender.sendMessage(toSend);
        return true;
    }

    private String isNotOnline(String name) {
        String toReturn = "&cPlayer by the name of "+name+" is not online.";
        return ChatColor.translateAlternateColorCodes('&', toReturn);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 1)
            return plugin.getServer().getOnlinePlayers()
                .stream()
                .map(pl -> pl.getName())
                .filter(name -> StringUtil.startsWithIgnoreCase(name, args[0]))
                .collect(Collectors.toList());

        if(args.length == 2)
        return plugin.getServer().getOnlinePlayers()
                .stream()
                .map(pl -> pl.getName())
                .filter(pl -> !pl.equals(args[0]))
                .filter(name -> StringUtil.startsWithIgnoreCase(name, args[1]))
                .collect(Collectors.toList());

        return List.of();
    }
}
