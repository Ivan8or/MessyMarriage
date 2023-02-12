package online.umbcraft.messymariage.util;

import org.bukkit.entity.Player;

public class SafePlayerDistance {

    public static double distance(Player p1, Player p2) {
        if(p1.getWorld() != p2.getWorld())
            return Double.MAX_VALUE;

        double calcdDistance = p1.getLocation().distance(p2.getLocation());
        if(Double.isNaN(calcdDistance))
            return Double.MAX_VALUE;

        return calcdDistance;
    }
}
