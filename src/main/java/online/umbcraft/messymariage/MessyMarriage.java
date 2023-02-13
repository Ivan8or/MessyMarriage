package online.umbcraft.messymariage;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByBed;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByDamage;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByFood;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByProximity;
import online.umbcraft.messymariage.amiability.affecters.GlobalAmiabilityEffects;
import online.umbcraft.messymariage.commands.AmiabilityCommand;
import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.data.PairData;
import online.umbcraft.messymariage.data.cache.CachedAmiabilityData;
import online.umbcraft.messymariage.data.cache.CachedPairData;
import online.umbcraft.messymariage.data.json.JsonAmiabilityData;
import online.umbcraft.messymariage.data.json.JsonPairData;
import online.umbcraft.messymariage.data.memory.MemoryAmiabilityData;
import online.umbcraft.messymariage.data.memory.MemoryPairData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MessyMarriage extends JavaPlugin {

    private AmiabilityData amiabilityData;
    private PairData pairData;

    private LevelSanitizer levelSanitizer;

    @Override
    public void onEnable() {

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists())
            saveDefaultConfig();

        pairData = new CachedPairData(
                new MemoryPairData(),
                new JsonPairData(getDataFolder().getPath() + File.separator + "pairs.json")
        );
        amiabilityData = new CachedAmiabilityData(
                new MemoryAmiabilityData(),
                new JsonAmiabilityData(getDataFolder().getPath() + File.separator + "amiability.json")
        );
        levelSanitizer = new LevelSanitizer(amiabilityData, pairData);


        new AmiabilityByProximity(this, levelSanitizer, pairData).start();
        new AmiabilityByBed(this, levelSanitizer, pairData).start();
        new AmiabilityByFood(this, levelSanitizer, pairData).start();
        new AmiabilityByDamage(this, levelSanitizer, pairData).start();

        new GlobalAmiabilityEffects(this, levelSanitizer, pairData).start();

        new AmiabilityCommand(this, levelSanitizer, pairData).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }




}
