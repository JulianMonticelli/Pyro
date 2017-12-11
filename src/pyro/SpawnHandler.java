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
    public static void handleSpawnsWater(int x, int y, int[][] pixelRead, int[][] pixelWrite, Random rand) {
        pixelWrite[x][y] = pixelRead[x][y]; // Set itself to be in write buffer
        int cloudThreshold = 145 + rand.nextInt(4);
        if (y < pixelRead[0].length - 1) {
            if (pixelRead[x][y+1] == Materials.NOTHING) {

                if (rand.nextInt(150) > cloudThreshold) {
                    pixelWrite[x][y+1] = Materials.WATER;
                }
                else if (pixelRead[x][y] == Materials.RAIN_CLOUD) {
                    if (rand.nextInt(100000) > 99998) {
                        pixelWrite[x][y+1] = Materials.ELECTRICITY_LIGHTNING;
                    }
                }
            }
        } else {
            return; // Don't spawn anything
        }
    }
}
