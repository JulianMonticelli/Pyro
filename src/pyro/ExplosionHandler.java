/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package pyro;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                            explosionTableWrite[xPos+x][yPos+y] = explosionType;
                            pixelAgeWrite[xPos+x][yPos+y] = 0;
                        }
                    }
                }
            }
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
                switch (explosionTableRead[x][y]) {
                    case 7:
                        pixelWrite[x][y] = Materials.EXPLOSION_FLASH;
                        break;
                    case 6:
                        pixelWrite[x][y] = Materials.EXPLOSION_FLASH_2;
                        break;
                    case 4:
                        pixelWrite[x][y] = Materials.EXPLOSION_FLASH_3;
                        break;
                    case 2:
                        pixelWrite[x][y] = Materials.EXPLOSION_FLASH_4;
                        break;
                    default:
                        break;
                }
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
                }

                else if (explosionTableRead[x][y] > 9 && explosionTableRead[x][y] < 21) { //
                    if (explosionTableRead[x][y] == 20) {
                        if (Materials.getExplosiveRadius(pixelRead[x][y]) > 0) {
                            markExplosion(x, y, getExplosionType(pixelRead[x][y]), pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                        }
                    }
                    switch (explosionTableRead[x][y]) {
                        case 20:
                            pixelWrite[x][y] = Materials.EXPLOSION_FLASH;
                            break;
                        case 18:
                            pixelWrite[x][y] = Materials.EXPLOSION_FLASH_2;
                            break;
                        case 16:
                            pixelWrite[x][y] = Materials.EXPLOSION_FLASH_3;
                            break;
                        case 14:
                            pixelWrite[x][y] = Materials.EXPLOSION_FLASH_3;
                            break;
                        case 12:
                            pixelWrite[x][y] = Materials.EXPLOSION_FLASH_4;
                            break;
                        default:
                            break;
                    }
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
                    switch (explosionTableRead[x][y]) {
                        case 80:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_1;
                            break;
                        case 77:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_2;
                            break;
                        case 74:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_3;
                            break;
                        case 71:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_4;
                            break;
                        case 68:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_5;
                            break;
                        case 65:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_6;
                            break;
                        case 62:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_7;
                            break;
                        case 59:
                            pixelWrite[x][y] = Materials.ANTI_MATTER_EXPLOSION_FLASH_8;
                            break;
                        default:
                            break;
                    }
                    explosionTableWrite[x][y] = explosionTableRead[x][y] - 1;
                }
            }
            return true;
        } else return false;
    }

    private static int getExplosionType(int i) {
        if(Materials.isIncendiaryExplosion(i)) {
            return Materials.EXPLOSION_NEW_INCENDIARY;
        }
        else if (Materials.isAntiGravity(i)) {
            return Materials.EXPLOSION_ANTI_MATTER;
        }
        else return Materials.EXPLOSION_NEW_NON_INCENDIARY;
    }
}
