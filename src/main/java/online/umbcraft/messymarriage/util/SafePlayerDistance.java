package online.umbcraft.messymarriage.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SafePlayerDistance {

    public static double distance(Player p1, Player p2) {
        return distance(p1.getLocation(), p2.getLocation());
    }

    public static double distance(Location d1, Location d2) {
        if(d1.getWorld() != d2.getWorld())
            return Double.MAX_VALUE;

        double calcdDistance = d1.distance(d2);
        if(Double.isNaN(calcdDistance))
            return Double.MAX_VALUE;

        return calcdDistance;
    }

}
