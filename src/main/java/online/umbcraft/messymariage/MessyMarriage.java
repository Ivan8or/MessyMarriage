package online.umbcraft.messymariage;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByProximity;
import online.umbcraft.messymariage.amiability.affecters.GlobalAmiabilityEffects;
import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.data.PairData;
import online.umbcraft.messymariage.data.cache.CachedPairData;
import online.umbcraft.messymariage.data.json.JsonPairData;
import online.umbcraft.messymariage.data.memory.MemoryPairData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MessyMarriage extends JavaPlugin {

    private AmiabilityData amiabilityData;
    private PairData pairData;

    private LevelSanitizer levelSanitizer;

    @Override
    public void onEnable() {

        pairData = new CachedPairData(
                new MemoryPairData(),
                new JsonPairData(getDataFolder().getPath() + File.separator + "data/pairs.json")
        );

        new AmiabilityByProximity(this, levelSanitizer, pairData).start();
        new GlobalAmiabilityEffects(this, levelSanitizer, pairData).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }




}
