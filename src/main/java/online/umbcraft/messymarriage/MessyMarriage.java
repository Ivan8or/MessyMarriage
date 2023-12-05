package online.umbcraft.messymarriage;

import online.umbcraft.messymarriage.amiability.LevelSanitizer;
import online.umbcraft.messymarriage.amiability.adjusters.AmiabilityByBed;
import online.umbcraft.messymarriage.amiability.adjusters.AmiabilityByDamage;
import online.umbcraft.messymarriage.amiability.adjusters.AmiabilityByFood;
import online.umbcraft.messymarriage.amiability.adjusters.AmiabilityByProximity;
import online.umbcraft.messymarriage.amiability.gestures.DivorceGesture;
import online.umbcraft.messymarriage.amiability.affecters.GlobalAmiability;
import online.umbcraft.messymarriage.amiability.affecters.MarriageDamageTransfer;
import online.umbcraft.messymarriage.amiability.gestures.MarryGesture;
import online.umbcraft.messymarriage.commands.AmiabilityCommand;
import online.umbcraft.messymarriage.crafting.listeners.CancelCustomItemsUse;
import online.umbcraft.messymarriage.crafting.DivorcePapers;
import online.umbcraft.messymarriage.crafting.WeddingBand;
import online.umbcraft.messymarriage.crafting.WeddingRing;
import online.umbcraft.messymarriage.crafting.listeners.DiscoverRecipes;
import online.umbcraft.messymarriage.data.AmiabilityData;
import online.umbcraft.messymarriage.data.PairData;
import online.umbcraft.messymarriage.data.cache.CachedAmiabilityData;
import online.umbcraft.messymarriage.data.cache.CachedPairData;
import online.umbcraft.messymarriage.data.json.JsonAmiabilityData;
import online.umbcraft.messymarriage.data.json.JsonPairData;
import online.umbcraft.messymarriage.data.memory.MemoryAmiabilityData;
import online.umbcraft.messymarriage.data.memory.MemoryPairData;
import online.umbcraft.messymarriage.tips.MarriageTipsVendor;
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

        DiscoverRecipes recipes = new DiscoverRecipes(this);

        new WeddingRing(this, recipes).start();
        new WeddingBand(this, recipes).start();
        new DivorcePapers(this, recipes).start();

        recipes.start();
        new CancelCustomItemsUse(this).start();

        new MarriageTipsVendor().vendAtRate(this, 20 * 300);
    }

    @Override
    public void onDisable() {
        levelSanitizer.flush();
    }




}
