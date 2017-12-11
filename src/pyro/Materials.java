/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package pyro;

import java.awt.Color;

/**
 * @author Julian
 */
public class Materials {
    private static final int NUM_MATERIALS = 50; // Add 1 every time we add a material
    
    public static final int EXPLOSION_NONE = 0;
    public static final int EXPLOSION_REMOVE = 1;
    
    public static final int EXPLOSION_NEW_NON_INCENDIARY = 7;
    public static final int EXPLOSION_ANTI_MATTER = 80;
    public static final int EXPLOSION_NEW_INCENDIARY = 20;
    
    public static final int NOTHING = 0;
    public static final int CONCRETE = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int WOOD = 4;
    public static final int FIRE = 5;
    public static final int FIRE_ORANGE = 6;
    public static final int FIRE_ORANGE_2 = 7;
    public static final int PLANT = 8;
    public static final int OIL = 9;
    public static final int LAVA = 10;
    public static final int RAIN_CLOUD = 11;
    public static final int EXPLOSION_FLASH = 12;
    public static final int EXPLOSION_FLASH_2 = 13;
    public static final int EXPLOSION_FLASH_3 = 14;
    public static final int EXPLOSION_FLASH_4 = 15;
    public static final int GUNPOWDER = 16;
    public static final int TNT = 17;
    public static final int C4 = 18;
    public static final int NITROGLYCERIN = 19;
    public static final int FUSE = 20;
    public static final int ELECTRICITY = 21;
    public static final int ELECTRICITY_LIGHTNING = 22;
    public static final int ELECTRICITY_SPARK = 23;
    public static final int LIFE_SEED = 33;
    public static final int LIFE_SEED_EDGE = 34;
    public static final int GASOLINE = 35;
    public static final int LITHIUM = 36;
    public static final int SODIUM = 37;
    public static final int POTASSIUM = 38;
    public static final int RUBIDIUM = 39;
    public static final int CESIUM = 39;
    public static final int ANTI_MATTER = 40;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_1 = 41;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_2 = 42;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_3 = 43;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_4 = 44;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_5 = 45;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_6 = 46;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_7 = 47;
    public static final int ANTI_MATTER_EXPLOSION_FLASH_8 = 48;
    
    
    private static boolean[] falls;
    private static boolean[] burns;
    private static boolean[] isBurnable;
    private static boolean[] isSparkable;
    private static int[] burnIndex;
    private static boolean[] negatesFire;
    private static int[] flowIndex;
    private static boolean[] canFallThrough;
    private static boolean[] spawnsWater;
    private static int[] explosionIndex;
    private static int[] explosiveRadius;
    private static int[] waterReactionIndex;
    private static boolean[] incendiaryExplosion;
    private static boolean[] isElectric;
    private static boolean[] isAntiGravity;
    private static Color[] color;
    
    public static void init() {
        falls = new boolean[NUM_MATERIALS];
        color = new Color[NUM_MATERIALS];
        burns = new boolean[NUM_MATERIALS];
        isBurnable = new boolean[NUM_MATERIALS];
        isSparkable = new boolean[NUM_MATERIALS];
        negatesFire = new boolean[NUM_MATERIALS];
        flowIndex = new int[NUM_MATERIALS];
        burnIndex = new int[NUM_MATERIALS];
        canFallThrough = new boolean[NUM_MATERIALS];
        spawnsWater = new boolean[NUM_MATERIALS];
        incendiaryExplosion = new boolean[NUM_MATERIALS];
        isElectric = new boolean[NUM_MATERIALS];
        isAntiGravity = new boolean[NUM_MATERIALS];
        explosionIndex = new int[NUM_MATERIALS];
        explosiveRadius = new int[NUM_MATERIALS];
        waterReactionIndex = new int[NUM_MATERIALS];
        
        falls[NOTHING] = false; // Nothing doesn't fall, dumbass
        color[NOTHING] = Color.BLACK; // Nothing should be "empty"
        isBurnable[NOTHING] = false;
        negatesFire[NOTHING] = false;
        
        falls[CONCRETE] = false; // Concrete should be stable
        color[CONCRETE] = Color.LIGHT_GRAY;
        isBurnable[CONCRETE] = false;
        negatesFire[CONCRETE] = false;
        
        falls[SAND] = true; // Sand should definitely fall
        color[SAND] = Color.YELLOW;
        isBurnable[SAND] = false;
        negatesFire[SAND] = false;
        flowIndex[SAND] = 1;
        
        falls[WATER] = true; // Water should fall - need to implement "flatness" of water
        color[WATER] = Color.BLUE;
        isBurnable[WATER] = false;
        negatesFire[WATER] = true;
        flowIndex[WATER] = 4;
        
        falls[WOOD] = false; // Wood is stationary
        color[WOOD] = Color.decode("#6D4300");
        isBurnable[WOOD] = true;
        burnIndex[WOOD] = 40;
        negatesFire[WOOD] = false;
        canFallThrough[WOOD] = false;
        
        // FIRE VARS
        falls[FIRE] = false;
        falls[FIRE_ORANGE] = false;
        falls[FIRE_ORANGE_2] = false;
        burns[FIRE] = true;
        burns[FIRE_ORANGE] = true;
        burns[FIRE_ORANGE_2] = true;
        color[FIRE] = Color.RED;
        color[FIRE_ORANGE] = Color.decode("#FF9A00");
        color[FIRE_ORANGE_2] = Color.decode("#FFB400");
        canFallThrough[FIRE] = true;
        canFallThrough[FIRE_ORANGE] = true;
        canFallThrough[FIRE_ORANGE_2] = true;
        isBurnable[FIRE] = false; // False, right?
        isBurnable[FIRE_ORANGE] = false; // False, right?
        isBurnable[FIRE_ORANGE_2] = false; // False, right?
        negatesFire[FIRE] = false; // Fight fire with fire lulz
        negatesFire[FIRE_ORANGE] = false; // Fight fire with fire lulz
        negatesFire[FIRE_ORANGE_2] = false; // Fight fire with fire lulz
        
        // PLANT
        falls[PLANT] = false;
        color[PLANT] = Color.GREEN;
        isBurnable[PLANT] = true;
        burnIndex[PLANT] = 45;
        negatesFire[PLANT] = false;
        
        // OIL
        falls[OIL] = true;
        color[OIL] = Color.decode("#701C00");
        isBurnable[OIL] = true;
        burnIndex[OIL] = 25;
        negatesFire[OIL] = false;
        flowIndex[OIL] = 2;
        
        // LAVA
        falls[LAVA] = true;
        color[LAVA] = Color.decode("#E06800");
        burns[LAVA] = true;
        flowIndex[LAVA] = 2;
        
        // RAIN_CLOUD
        falls[RAIN_CLOUD] = false;
        color[RAIN_CLOUD] = Color.DARK_GRAY;
        spawnsWater[RAIN_CLOUD] = true;
        canFallThrough[RAIN_CLOUD] = true;
        
        // GUNPOWDER
        falls[GUNPOWDER] = true;
        flowIndex[GUNPOWDER] = 1;
        color[GUNPOWDER] = Color.decode("#535556");
        explosionIndex[GUNPOWDER] = 20;
        explosiveRadius[GUNPOWDER] = 5;
        incendiaryExplosion[GUNPOWDER] = false; // DOES NOT explode into fire
        
        // TNT
        falls[TNT] = false;
        color[TNT] = Color.decode("#810000");
        explosionIndex[TNT] = 60;
        explosiveRadius[TNT] = 10;
        incendiaryExplosion[TNT] = false; // DOES NOT explode into fire
        
        // C4
        falls[C4] = false;
        color[C4] = Color.decode("#D8D8D8");
        explosionIndex[C4] = 30;
        explosiveRadius[C4] = 40;
        incendiaryExplosion[C4] = false;
        
        // NITROGLYCERIN
        falls[NITROGLYCERIN] = true; // is a liquid
        flowIndex[NITROGLYCERIN] = 1; // is a liquid
        color[NITROGLYCERIN] = Color.decode("#FFF9AA");
        explosionIndex[NITROGLYCERIN] = 2; // Highly volatile
        explosiveRadius[NITROGLYCERIN] = 75; // Highly volatile
        incendiaryExplosion[NITROGLYCERIN] = true; // To be implemented
        
        // FUSE
        falls[FUSE] = false;
        isSparkable[FUSE] = true;
        color[FUSE] = Color.decode("#A3A3A3");
        burnIndex[FUSE] = 15;
        
        
        // EXPLOSION_FLASH - EXPLOSION_FLASH_4
        falls[EXPLOSION_FLASH] = false;
        color[EXPLOSION_FLASH] = Color.WHITE;
        
        falls[EXPLOSION_FLASH_2] = false;
        color[EXPLOSION_FLASH_2] = Color.YELLOW;
                
        falls[EXPLOSION_FLASH_3] = false;
        color[EXPLOSION_FLASH_3] = Color.ORANGE;
                
        falls[EXPLOSION_FLASH_4] = false;
        color[EXPLOSION_FLASH_4] = Color.RED;
        
        // ELECTRICITY
        falls[ELECTRICITY] = false;
        color[ELECTRICITY] = Color.YELLOW;
        burns[ELECTRICITY] = true;
        isElectric[ELECTRICITY] = true;
        
        // ELECTRICITY_LIGHTNING
        falls[ELECTRICITY_LIGHTNING] = false;
        color[ELECTRICITY_LIGHTNING] = Color.YELLOW;
        burns[ELECTRICITY_LIGHTNING] = true;
        isElectric[ELECTRICITY_LIGHTNING] = true;
        
        
        // ELECTRICITY_LIGHTNING
        falls[ELECTRICITY_SPARK] = false;
        color[ELECTRICITY_SPARK] = Color.YELLOW;
        burns[ELECTRICITY_SPARK] = true;
        isElectric[ELECTRICITY_SPARK] = true;
        
        // LIFE_SEED
        falls[LIFE_SEED] = false;
        color[LIFE_SEED] = Color.decode("#646D20");
        isBurnable[LIFE_SEED] = true;
        burnIndex[LIFE_SEED] = 25;
        
        // LIFE_SEED_EDGE
        falls[LIFE_SEED_EDGE] = false;
        color[LIFE_SEED_EDGE] = Color.decode("#484F16");
        isBurnable[LIFE_SEED_EDGE] = true;
        burnIndex[LIFE_SEED_EDGE] = 35;
        
        // GASOLINE
        falls[GASOLINE] = true; // Water should fall - need to implement "flatness" of water
        color[GASOLINE] = Color.decode("#F9CC61");
        burnIndex[GASOLINE] = 1;
        explosiveRadius[GASOLINE] = 2;
        explosionIndex[GASOLINE] = 5;
        isBurnable[GASOLINE] = true;
        flowIndex[GASOLINE] = 3;
        
        
        // SODIUM
        falls[SODIUM] = true;
        color[SODIUM] = Color.decode("#E5E7E9");
        burnIndex[SODIUM] = 0;
        explosiveRadius[SODIUM] = 0;
        explosionIndex[SODIUM] = 0;
        isBurnable[SODIUM] = false;
        flowIndex[SODIUM] = 2;
        waterReactionIndex[SODIUM] = 1; // Reacts with water - level 1
        
        
        // DARK MATTER
        falls[ANTI_MATTER] = false;
        isAntiGravity[ANTI_MATTER] = true;
        
        color[ANTI_MATTER] = Color.decode("#9400D8");
        burnIndex[ANTI_MATTER] = 0;
        explosiveRadius[ANTI_MATTER] = 200;
        
        // DARK MATTER EXPLOSIONS
        falls[ANTI_MATTER_EXPLOSION_FLASH_1] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_1] = color[ANTI_MATTER];
        
        falls[ANTI_MATTER_EXPLOSION_FLASH_2] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_2] = Color.decode("#8102D6");
                
        falls[ANTI_MATTER_EXPLOSION_FLASH_3] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_3] = Color.decode("#824E87");
                
        falls[ANTI_MATTER_EXPLOSION_FLASH_4] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_4] = Color.decode("#E6CEEF");

        falls[ANTI_MATTER_EXPLOSION_FLASH_5] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_5] = Color.decode("#7FD19E");
        
        falls[ANTI_MATTER_EXPLOSION_FLASH_6] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_6] = Color.decode("#00FFFF");
                
        falls[ANTI_MATTER_EXPLOSION_FLASH_7] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_7] = Color.decode("#00B6FF");
                
        falls[ANTI_MATTER_EXPLOSION_FLASH_8] = false;
        color[ANTI_MATTER_EXPLOSION_FLASH_8] = Color.decode("#0950A5");
    }
    
    public static boolean falls(int material) {
        return falls[material];
    }
    
    public static Color getColor(int material) {
        return color[material];
    }
    
    public static boolean isBurnable(int material) {
        return isBurnable[material];
    }
    
    public static boolean isSparkable(int material) {
        return isSparkable[material];
    }
    
    public static boolean negatesFire(int material) {
        return negatesFire[material];
    }
    
    public static int getFlowIndex(int material) {
        return flowIndex[material];
    }

    public static int getBurnIndex(int material) {
        return burnIndex[material];
    }
    public static boolean canFallThrough(int material) {
        return canFallThrough[material];
    }
    
    public static boolean burns(int material) {
        return burns[material];
    }
    
    public static boolean spawnsWater(int material) {
        return spawnsWater[material];
    }
    
    public static boolean isExplosive(int material) {
        return explosionIndex[material] > 0;
    }
    
    public static int getExplosionIndex(int material) {
        return explosionIndex[material];
    }
    
    public static int getExplosiveRadius(int material) {
        return explosiveRadius[material];
    }
    
    public static boolean isIncendiaryExplosion(int material) {
        return incendiaryExplosion[material];
    }
    
    public static boolean isElectric(int material) {
        return isElectric[material];
    }
    
    public static boolean isAntiGravity(int material) {
        return isAntiGravity[material];
    }
    
    public static boolean flows(int material) {
        return flowIndex[material] > 0;
    }
    
}
