package com.cout970.magneticraft;

import com.cout970.magneticraft.world.OreGenConfig;
import com.cout970.magneticraft.world.GaussOreGenConfig;
import com.cout970.magneticraft.world.WorldGenManagerMg;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManagerConfig {

    public static Configuration config;
    public static boolean MINER_CHUNKLOADING;
	public static int INFINITE_WATER_GENERATION;
	public static boolean CRUSHING_TABLE_DROPS;
    public static List<String> blockBreakerBlacklist = new ArrayList<>();

    public static void init(File file) {
        if (config == null) {
            config = new Configuration(file);
            LoadConfigs();
        }
    }

    @SubscribeEvent
    public void onconfigurationChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Magneticraft.ID)) {
            LoadConfigs();
        }
    }

    public static void LoadConfigs() {
        config.load();
        //@formatter:off
        WorldGenManagerMg.GenCopper = getOreConfig(config,   "copper",    10, 8, 80, 30);
        WorldGenManagerMg.GenTungsten = getOreConfig(config, "tungsten",  2,  6, 10, 0);
        WorldGenManagerMg.GenSulfur = getOreConfig(config,   "sulfur",    3,  8, 12, 0);
        WorldGenManagerMg.GenUranium = getOreConfig(config,  "uranium",   3,  3, 80, 0);
        WorldGenManagerMg.GenThorium = getOreConfig(config,  "thorium",   5,  6, 20, 0);
        WorldGenManagerMg.GenSalt = getOreConfig(config,     "salt",      6,  8, 80, 0);
        WorldGenManagerMg.GenZinc = getOreConfig(config,     "zinc",      4,  6, 80, 0);

        WorldGenManagerMg.GenLime = getGaussOreConfig(config, "limestone", 3, 0.9F, 0, 5, 50, 64, 16);
        
        //@formatter:on
        WorldGenManagerMg.GenOil = config.getBoolean("Oil Generation", Configuration.CATEGORY_GENERAL, true, "Should spawn oil in the world?");
        WorldGenManagerMg.GenOilProbability = config.getInt("Oil Generation Amount", Configuration.CATEGORY_GENERAL, 2000, 200, 50000, "How rare should oil be? Higher value means less oil.");
        WorldGenManagerMg.GenOilMaxHeight = config.getInt("Oil Generation Max Height", Configuration.CATEGORY_GENERAL, 30, 0, 256, "Max Height for a oil deposit");
        WorldGenManagerMg.GenOilMinHeight = config.getInt("Oil Generation Min Height", Configuration.CATEGORY_GENERAL, 10, 0, 256, "Min Height for a oil deposit");
        WorldGenManagerMg.GenOilMaxAmount = config.getInt("Oil Generation Max oil deposits", Configuration.CATEGORY_GENERAL, 9, 1, 16, "Max number of oil deposits nearby");
        
        MINER_CHUNKLOADING = config.getBoolean("Chunk Loading", Configuration.CATEGORY_GENERAL, true, "Should the miner load chunks?");
        INFINITE_WATER_GENERATION = config.getInt("Infinite water", Configuration.CATEGORY_GENERAL, 50, 0, 1000, "Amount of water generated by a Infinite Water Block every tick");
        CRUSHING_TABLE_DROPS = config.getBoolean("Crushing table drops", Configuration.CATEGORY_GENERAL, false, "When smashing ores drop output as item entities");

        String[] list = config.get("Block Breaker Blacklist", Configuration.CATEGORY_GENERAL, new String[0], "A list of blocks that the block breaker can't break, example: minecraft:stone").getStringList();
        blockBreakerBlacklist.clear();
        for(String s : list){
            blockBreakerBlacklist.add(s);
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static OreGenConfig getOreConfig(Configuration conf, String name, int chunk, int vein, int max, int min) {
        boolean active = conf.getBoolean(name + "_gen_active", Configuration.CATEGORY_GENERAL, true, "Generation of " + name);
        int amount_per_chunk = conf.getInt(name + "_amount_chunk", Configuration.CATEGORY_GENERAL, chunk, 0, 20, "Number of veins of " + name + " per chunk");
        int amount_per_vein = conf.getInt(name + "_amount_vein", Configuration.CATEGORY_GENERAL, vein, 0, 20, "Max amount of blocks of " + name + " in a vein");
        int max_height = conf.getInt(name + "_max_height", Configuration.CATEGORY_GENERAL, max, 0, 256, "Max height for generation of " + name);
        int min_height = conf.getInt(name + "_min_height", Configuration.CATEGORY_GENERAL, min, 0, 256, "Min height for generation of " + name);
        return new OreGenConfig(active, amount_per_chunk, amount_per_vein, max_height, min_height);
    }

    private static GaussOreGenConfig getGaussOreConfig(Configuration conf, String name, int chunk, float deviation, int min_am, int max_am, int vein, int max, int min) {
        boolean active = conf.getBoolean(name + "_gen_active", Configuration.CATEGORY_GENERAL, true, "Generation of " + name);
        int amount_per_chunk = conf.getInt(name + "_amount_chunk", Configuration.CATEGORY_GENERAL, chunk, 0, 30, "Average number of veins of " + name + " per chunk");
        float amount_deviation = conf.getFloat(name + "_std_dev", Configuration.CATEGORY_GENERAL, deviation, 0F, 10F, "Standard deviation of number of veins per chunk.\nHigher value means more chunks with low/high amounts of veins, lower - more with medium amount");
        int min_chunk = conf.getInt(name + "_min_per_chunk", Configuration.CATEGORY_GENERAL, min_am, 0, amount_per_chunk, "Minimal amount of veins per chunk");
        int max_chunk = conf.getInt(name + "_max_per_chunk", Configuration.CATEGORY_GENERAL, max_am, 0, 100, "Maximal amount of veins per chunk");
        int amount_per_vein = conf.getInt(name + "_amount_vein", Configuration.CATEGORY_GENERAL, vein, 0, 50, "Max amount of blocks of " + name + " in a vein");
        int max_height = conf.getInt(name + "_max_height", Configuration.CATEGORY_GENERAL, max, 0, 256, "Max height for generation of " + name);
        int min_height = conf.getInt(name + "_min_height", Configuration.CATEGORY_GENERAL, min, 0, 256, "Min height for generation of " + name);
        return new GaussOreGenConfig(active, amount_per_chunk, amount_deviation, min_chunk, max_chunk, amount_per_vein, max_height, min_height);
    }
}
