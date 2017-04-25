/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package pyro;

import java.util.Random;

/**
 * @author Julian
 */
public class SpawnHandler {
    public static void handleSpawnsWater(int x, int y, int[][] pixel, Random rand) {
        int cloudThreshold = 145 + rand.nextInt(4);
        if (y < pixel[0].length - 1) {
            if (pixel[x][y+1] == Materials.NOTHING) {

                if (rand.nextInt(150) > cloudThreshold) {
                    pixel[x][y+1] = Materials.WATER;
                }
                else if (pixel[x][y] == Materials.RAIN_CLOUD) {
                    if (rand.nextInt(100000) > 99998) {
                        pixel[x][y+1] = Materials.ELECTRICITY;
                    }
                }
            }
        } else {
            return; // Don't spawn anything
        }
    }
}
