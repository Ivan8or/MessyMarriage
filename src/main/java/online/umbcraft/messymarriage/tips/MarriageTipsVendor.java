package online.umbcraft.messymarriage.tips;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MarriageTipsVendor {

    final static private String TIPS_PREFIX = "&6[&cMessyMarriage&6] &e";
    final static private List<String> TIPS_LIST;

    static {
        String[] tips = {
                "Gain amiability with someone by being within 64 blocks of them.",
                "Eating food at the same time provides a big boost to amiability. (60s cooldown)",
                "Sleeping together provides a huge boost to amiability.",
                "Players with an amiability of 50 can marry each other.",
                "Use /amiability <player> to check your amiability with someone.",
                "Wedding Rings and Wedding Bands can both be used to propose to someone.",
                "Divorce Papers can be used to cancel a marriage.",
                "The max amiability is 50 for non married players, and 100 for married players.",
                "Players with high amiability (>29) deal less damage to each other.",
                "Married players share damage taken, at a rate of +0.5% / amiability level.",
                "Damaging someone lowers your amiability with them."
        };
        TIPS_LIST = new ArrayList<>(List.of(tips));
        Collections.shuffle(TIPS_LIST);
    }

    private int index = 0;
    private Optional<BukkitTask> currentTask = Optional.empty();

    public void vendAtRate(Plugin plugin, long ticks) {
        stopVending();
        currentTask = Optional.of(
                plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                    if(plugin.getServer().getOnlinePlayers().size() > 0) {
                        String fullMessage = TIPS_PREFIX + TIPS_LIST.get(index);
                        plugin.getServer().broadcastMessage(fullMessage);
                        index = (index + 1) % TIPS_LIST.size();
                    }
                }, 0, ticks)
        );
    }

    public void stopVending() {
        currentTask.ifPresent(BukkitTask::cancel);
    }
}
