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
    public static void handleExplosive(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, int[][] explosionTableRead, int[][] explosionTableWrite) {
        if (explosionTableRead[x][y] > Materials.EXPLOSION_NONE) { // This pixel is set to explode
            return; // Do nothing
        }
        
        //explosionTable[x][y] = getExplosionType(pixel[x][y]);
        if(x > 0) {
            if(Materials.burns(pixelRead[x-1][y]) && pixelAgeRead[x-1][y] > Materials.getExplosionIndex(pixelRead[x][y])) {
                markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                return;
            }
        }
        
        if(x < pixelRead.length-1) {
            if(Materials.burns(pixelRead[x+1][y]) && pixelAgeRead[x+1][y] > Materials.getExplosionIndex(pixelRead[x][y])) {
                markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                return;
            }
        }
        
        else if(y > 0) {
            if(Materials.burns(pixelRead[x][y-1]) && pixelAgeRead[x][y-1] > Materials.getExplosionIndex(pixelRead[x][y])) {
                markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                return;
            }
        }
        
        else if(y < pixelRead[x].length-1) {
            if(Materials.burns(pixelRead[x][y+1]) && pixelAgeRead[x][y+1] > Materials.getExplosionIndex(pixelRead[x][y])) {
                markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                return;
            }
        }
        
        // If it flows, don't rewrite it. Rewriting it will cause problems!
        if (!Materials.flows(pixelRead[x][y])) {
            pixelWrite[x][y] = pixelRead[x][y];
        }
        
    }
    
    public static void wipeAllContents(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite) {
        int radius = Materials.getExplosiveRadius(pixelWrite[x][y]);
        for (int yPos = (-radius); yPos <= (radius); yPos++) {
            for (int xPos = (-radius); xPos <= (radius); xPos++) {
                if (xPos+x >= 0 && xPos+x < pixelWrite.length) {
                    if (yPos+y >= 0 && yPos+y < pixelWrite[0].length) {
                        if (xPos*xPos + yPos*yPos <= radius*radius) {
                            pixelRead[x][y] = 0;
                            pixelAgeRead[x][y] = 0;
                            pixelWrite[x][y] = 0;
                            pixelAgeWrite[x][y] = 0;
                        }
                    }
                }
            }
        }
    }
    
    public static void markExplosion(int x, int y, int explosionType, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, int[][] explosionTableRead, int[][] explosionTableWrite) {
        int radius = Materials.getExplosiveRadius(pixelRead[x][y]);
        for (int yPos = (-radius); yPos <= (radius); yPos++) {
            for (int xPos = (-radius); xPos <= (radius); xPos++) {
                if (xPos+x >= 0 && xPos+x < pixelRead.length) {
                    if (yPos+y >= 0 && yPos+y < pixelRead[0].length) {
                        if (xPos*xPos + yPos*yPos <= radius*radius) {
                            markExplosion(xPos+x, yPos+y, explosionType, explosionTableWrite);
                            pixelAgeWrite[xPos+x][yPos+y] = 0;
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
    public static boolean handleExplosionTable(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, int[][] explosionTableRead, int[][] explosionTableWrite, Random rand) {
        if (explosionTableRead[x][y] > Materials.EXPLOSION_NONE) { // if pixel is a marked explosion
            if (explosionTableRead[x][y] == Materials.EXPLOSION_REMOVE) { // Explosion is about to be removed
                explosionTableWrite[x][y]--; // decrease
                pixelWrite[x][y] = Materials.NOTHING; // We've destroyed the material with an explosion
                pixelAgeWrite[x][y] = 0;
            }

            else if (explosionTableRead[x][y] > 1 && explosionTableRead[x][y] < 8) { // explosionTable is 2 through 7
                if (explosionTableRead[x][y] == 7) {
                    if (Materials.getExplosiveRadius(pixelRead[x][y]) > 0) {
                        markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite); // ?
                    }
                }
                if (explosionTableRead[x][y] == 7) pixelWrite[x][y] = Materials.EXPLOSION_FLASH;
                else if (explosionTableRead[x][y] == 6) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_2;
                else if (explosionTableRead[x][y] == 4) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_3;
                else if (explosionTableRead[x][y] == 2) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_4;
                explosionTableWrite[x][y] = explosionTableRead[x][y] - 1;
            }

            else if (explosionTableRead[x][y] > 9 && explosionTableRead[x][y] < 21) { // Incendiary

                if (explosionTableRead[x][y] == 10) { // Explosion is about to be removed
                    explosionTableWrite[x][y] = 0;
                    if (rand.nextInt(10000) < 9997) {
                        pixelWrite[x][y] = Materials.NOTHING;
                    } else {
                        pixelWrite[x][y] = Materials.FIRE;
                    }
                    //pixelAge[x][y] = 2;
                }

                else if (explosionTableRead[x][y] > 9 && explosionTableRead[x][y] < 21) { //
                    if (explosionTableRead[x][y] == 20) {
                        if (Materials.getExplosiveRadius(pixelRead[x][y]) > 0) {
                            markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                        }
                    }
                    if (explosionTableRead[x][y] == 20) pixelWrite[x][y] = Materials.EXPLOSION_FLASH;
                    else if (explosionTableRead[x][y] == 18) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_2;
                    else if (explosionTableRead[x][y] == 16) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_3;
                    else if (explosionTableRead[x][y] == 14) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_3;
                    else if (explosionTableRead[x][y] == 12) pixelWrite[x][y] = Materials.EXPLOSION_FLASH_4;
                    explosionTableWrite[x][y] = explosionTableRead[x][y] - 1;
                }
            }
            
            else if (explosionTableRead[x][y] > 55 && explosionTableRead[x][y] <= Materials.EXPLOSION_ANTI_MATTER) { // Anti Matter
                if (explosionTableRead[x][y] == 56) { // Explosion is about to be removed
                    explosionTableRead[x][y] = 0;
                    pixelWrite[x][y] = Materials.NOTHING;
                    pixelAgeWrite[x][y] = 0;
                }

                else if (explosionTableRead[x][y] > 56 && explosionTableRead[x][y] < 81) {
                    if (explosionTableRead[x][y] == 80) {
                        if (Materials.getExplosiveRadius(pixelRead[x][y]) > 0) {
                            wipeAllContents(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite);
                            markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                        }
                    }
                    if (explosionTableRead[x][y] == 80) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_1;
                    else if (explosionTableRead[x][y] == 77) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_2;
                    else if (explosionTableRead[x][y] == 74) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_3;
                    else if (explosionTableRead[x][y] == 71) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_4;
                    else if (explosionTableRead[x][y] == 68) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_5;
                    else if (explosionTableRead[x][y] == 65) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_6;
                    else if (explosionTableRead[x][y] == 62) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_7;
                    else if (explosionTableRead[x][y] == 59) pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_8;
                    explosionTableWrite[x][y] = explosionTableRead[x][y] - 1;
                }
            }
            return true;
        } else return false;
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
