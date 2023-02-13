package online.umbcraft.messymariage;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByBed;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByDamage;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByFood;
import online.umbcraft.messymariage.amiability.adjusters.AmiabilityByProximity;
import online.umbcraft.messymariage.amiability.affecters.DivorceGesture;
import online.umbcraft.messymariage.amiability.affecters.GlobalAmiability;
import online.umbcraft.messymariage.amiability.affecters.MarriageDamageTransfer;
import online.umbcraft.messymariage.amiability.affecters.MarryGesture;
import online.umbcraft.messymariage.commands.AmiabilityCommand;
import online.umbcraft.messymariage.crafting.listeners.CancelCustomItemsUse;
import online.umbcraft.messymariage.crafting.DivorcePapers;
import online.umbcraft.messymariage.crafting.WeddingBand;
import online.umbcraft.messymariage.crafting.WeddingRing;
import online.umbcraft.messymariage.crafting.listeners.RecipeList;
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

        new GlobalAmiability(this, levelSanitizer, pairData).start();
        new MarriageDamageTransfer(this, levelSanitizer, pairData).start();
        new MarryGesture(this, levelSanitizer, pairData).start();
        new DivorceGesture(this, levelSanitizer, pairData).start();

        new AmiabilityCommand(this, levelSanitizer, pairData).start();


        RecipeList recipes = new RecipeList(this);

        new WeddingRing(this, recipes).start();
        new WeddingBand(this, recipes).start();
        new DivorcePapers(this, recipes).start();

        recipes.start();

        new CancelCustomItemsUse(this).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }




}
