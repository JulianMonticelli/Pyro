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
public class ExplosionHandler {
    public static void handleExplosive(int x, int y, int[][] pixel, int[][] pixelAge, int[][] explosionTable) {
        if (explosionTable[x][y] > Materials.EXPLOSION_NONE) { // This pixel is set to explode
            return; // Do nothing
        }
        //explosionTable[x][y] = getExplosionType(pixel[x][y]);
        if(x > 0) {
            if(pixelAge[x-1][y] > Materials.getExplosionIndex(pixel[x][y]))
                markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable);
        }
        if(x < pixel.length-1) {
            if(pixelAge[x+1][y] > Materials.getExplosionIndex(pixel[x][y]))
                markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable);
        }
        if(y > 0) {
            if(pixelAge[x][y-1] > Materials.getExplosionIndex(pixel[x][y]))
                markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable);
        }
        if(y < pixel[x].length-1) {
            if(pixelAge[x][y+1] > Materials.getExplosionIndex(pixel[x][y]))
                markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable);
        }
        
    }
    
    public static void markExplosion(int x, int y, int explosionType, int[][] pixel, int[][] pixelAge, int[][] explosionTable) {
        int radius = Materials.getExplosiveRadius(pixel[x][y]);
        for (int yPos = (-radius); yPos <= (radius); yPos++) {
            for (int xPos = (-radius); xPos <= (radius); xPos++) {
                if (xPos+x >= 0 && xPos+x < pixel.length) {
                    if (yPos+y >= 0 && yPos+y < pixel[0].length) {
                        if (xPos*xPos + yPos*yPos <= radius*radius) {
                            markExplosion(xPos+x, yPos+y, explosionType, explosionTable);
                            if(pixelAge[xPos+x][yPos+y] > 0) {
                                pixelAge[xPos+x][yPos+y] = 0;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void markExplosion(int x, int y, int explosionType, int[][] explosionTable) {
        if (explosionType == 0)
            explosionTable[x][y] = Materials.EXPLOSION_NEW_NON_INCENDIARY;
        else if (explosionType == 1)
            explosionTable[x][y] = Materials.EXPLOSION_NEW_INCENDIARY;
        else if (explosionType == 2)
            explosionTable[x][y] = Materials.EXPLOSION_NEW_NON_INCENDIARY;
        else if (explosionType == 3) {
            explosionTable[x][y] = Materials.EXPLOSION_ANTI_MATTER;
        }
    }
    
    
    // Handles explosion table point
    public static void handleExplosionTable(int x, int y, int[][] pixel, int[][] pixelAge, int[][] explosionTable, Random rand) {
        if (explosionTable[x][y] > Materials.EXPLOSION_NONE) { // if pixel is a marked explosion
            if (explosionTable[x][y] == Materials.EXPLOSION_REMOVE) { // Explosion is about to be removed
                explosionTable[x][y]--; // decrease
                pixel[x][y] = Materials.NOTHING; // We've destroyed the material with an explosion
                pixelAge[x][y] = 0;
            }

            else if (explosionTable[x][y] > 1 && explosionTable[x][y] < 8) { // explosionTable is 2 through 7
                if (explosionTable[x][y] == 7) {
                    if (Materials.getExplosiveRadius(pixel[x][y]) > 0) {
                        markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable); // ?
                    }
                }
                if (explosionTable[x][y] == 7) pixel[x][y] = Materials.EXPLOSION_FLASH;
                else if (explosionTable[x][y] == 6) pixel[x][y] = Materials.EXPLOSION_FLASH_2;
                else if (explosionTable[x][y] == 4) pixel[x][y] = Materials.EXPLOSION_FLASH_3;
                else if (explosionTable[x][y] == 2) pixel[x][y] = Materials.EXPLOSION_FLASH_4;
                explosionTable[x][y]--;
            }

            else if (explosionTable[x][y] > 9 && explosionTable[x][y] < 21) { // Incendiary

                if (explosionTable[x][y] == 10) { // Explosion is about to be removed
                    explosionTable[x][y] = 0;
                    if (rand.nextInt(100) < 99) {
                        pixel[x][y] = Materials.NOTHING;
                    } else {
                        pixel[x][y] = Materials.FIRE;
                    }
                    //pixelAge[x][y] = 2;
                }

                else if (explosionTable[x][y] > 9 && explosionTable[x][y] < 21) { //
                    if (explosionTable[x][y] == 20) {
                        if (Materials.getExplosiveRadius(pixel[x][y]) > 0) {
                            markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable);
                        }
                    }
                    if (explosionTable[x][y] == 20) pixel[x][y] = Materials.EXPLOSION_FLASH;
                    else if (explosionTable[x][y] == 18) pixel[x][y] = Materials.EXPLOSION_FLASH_2;
                    else if (explosionTable[x][y] == 16) pixel[x][y] = Materials.EXPLOSION_FLASH_3;
                    else if (explosionTable[x][y] == 14) pixel[x][y] = Materials.EXPLOSION_FLASH_3;
                    else if (explosionTable[x][y] == 12) pixel[x][y] = Materials.EXPLOSION_FLASH_4;
                    explosionTable[x][y]--;
                }
            }
            
            else if (explosionTable[x][y] > 55 && explosionTable[x][y] <= Materials.EXPLOSION_ANTI_MATTER) { // Anti Matter
                //System.out.println("Markinggggg");
                if (explosionTable[x][y] == 56) { // Explosion is about to be removed
                    explosionTable[x][y] = 0;
                    pixel[x][y] = Materials.NOTHING;
                }

                else if (explosionTable[x][y] > 56 && explosionTable[x][y] < 81) {
                    if (explosionTable[x][y] == 80) {
                        if (Materials.getExplosiveRadius(pixel[x][y]) > 0) {
                            
                            markExplosion(x, y, getExplosionType(pixel[x][y]), pixel, pixelAge, explosionTable);
                        }
                    }
                    if (explosionTable[x][y] == 80) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_1;
                    else if (explosionTable[x][y] == 77) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_2;
                    else if (explosionTable[x][y] == 74) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_3;
                    else if (explosionTable[x][y] == 71) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_4;
                    else if (explosionTable[x][y] == 68) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_5;
                    else if (explosionTable[x][y] == 65) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_6;
                    else if (explosionTable[x][y] == 62) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_7;
                    else if (explosionTable[x][y] == 59) pixel[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_8;
                    explosionTable[x][y]--;
                }
            }

        }
    }

    private static int getExplosionType(int i) {
        if(Materials.isIncendiaryExplosion(i)) {
            return 1;
        }
        else if (i == Materials.ANTI_MATTER) {
            return 3;
        }
        else return 0;
    }
}
