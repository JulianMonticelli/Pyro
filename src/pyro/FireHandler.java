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

// BROKEN
public class FireHandler {
    
    public static void handleFire(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        if (y == 0) { // Make sure our fire is not at the y origin!
            pixelWrite[x][y] = Materials.NOTHING; // for now set fire at top to nothing
            pixelAgeWrite[x][y] = 0; // for now set fire at top to nothing
        }
        
        // Age fire pixel
        pixelWrite[x][y] = pixelRead[x][y];
        pixelAgeWrite[x][y] = pixelAgeRead[x][y] + 1;
        
        // Determine what material the pixel should be
        if (pixelAgeWrite[x][y] == 10) {
            pixelWrite[x][y] = Materials.FIRE_ORANGE;
        }
        else if (pixelAgeWrite[x][y] == 20) {
            pixelWrite[x][y] = Materials.FIRE_ORANGE_2;
        }
        
        // Deal with fire particle effects
        if (y > 0) {
            if (pixelRead[x][y-1] == Materials.NOTHING) {
                if (rand.nextInt(7) > 3) {
                    pixelWrite[x][y-1] = Materials.FIRE_ORANGE;
                    pixelAgeWrite[x][y-1] = pixelAgeWrite[x][y];
                }
            }
            if (x > 0) {
                if (pixelRead[x-1][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 97) {
                        pixelWrite[x-1][y-1] = Materials.FIRE_ORANGE;
                        pixelAgeWrite[x-1][y-1] = pixelAgeWrite[x][y];
                    }
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 97) {
                        pixelWrite[x+1][y-1] = Materials.FIRE_ORANGE;
                        pixelAgeWrite[x+1][y-1] = pixelAgeWrite[x][y];
                    }
                }
            }
        }
        if (pixelAgeWrite[x][y] > 30) {
            pixelWrite[x][y] = Materials.NOTHING;
            pixelAgeWrite[x][y] = 0;
        }
    }
        
    public static void handleFireNegation(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite) {
        if (y > 0) {
            if (Materials.burns(pixelRead[x][y-1])) {
                pixelWrite[x][y-1] = Materials.NOTHING;
                pixelAgeWrite[x][y-1] = 0;
            }
            if (x > 0) {
                if (Materials.burns(pixelRead[x-1][y-1])) {
                    pixelWrite[x-1][y-1] = Materials.NOTHING;
                    pixelAgeWrite[x-1][y-1] = 0;
                }
            }
            if (x < pixelRead.length - 1) {
                if (Materials.burns(pixelRead[x+1][y-1])) {
                    pixelWrite[x+1][y-1] = Materials.NOTHING;
                    pixelAgeWrite[x+1][y-1] = 0;
                }
            }
        }
        if (y < pixelRead[0].length - 1) {
            if (Materials.burns(pixelRead[x][y+1])) {
                pixelWrite[x][y+1] = Materials.NOTHING;
                pixelAgeWrite[x][y+1] = 0;
            }
            if (x > 0) {
                if (Materials.burns(pixelRead[x-1][y+1])) {
                    pixelWrite[x-1][y+1] = Materials.NOTHING;
                    pixelAgeWrite[x-1][y+1] = 0;
                }
            }
            if (x < pixelRead.length - 1) {
                if (Materials.burns(pixelRead[x+1][y+1])) {
                    pixelWrite[x+1][y+1] = Materials.NOTHING;
                    pixelAgeWrite[x+1][y+1] = 0;
                }
            }
        }
        if (x > 0) {
            if (Materials.burns(pixelRead[x-1][y])) {
                pixelWrite[x-1][y] = Materials.NOTHING;
                pixelAgeWrite[x-1][y] = 0;
            }
        }
        if (x < pixelRead.length - 1) {
            if (Materials.burns(pixelRead[x+1][y])) {
                pixelWrite[x+1][y] = Materials.NOTHING;
                pixelAgeWrite[x+1][y] = 0;
            }
        }
        //pixelWrite[x][y] = pixelRead[x][y];
    }
    
    public static boolean handleBurnable(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite) {
        if (getSurroundingFireAge(x, y, pixelRead, pixelAgeRead) > Materials.getBurnIndex(pixelRead[x][y])) {
            pixelWrite[x][y] = Materials.FIRE;
            pixelAgeWrite[x][y] = 0;
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean handleSparkable(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite) {
        if (getSurroundingFireAge(x, y, pixelRead, pixelAgeRead) > Materials.getBurnIndex(pixelRead[x][y])) {
            pixelWrite[x][y] = Materials.ELECTRICITY_SPARK;
            pixelAgeWrite[x][y] = 200;
            return true;
        } else {
            return false;
        }
    }
    
    public static int getSurroundingFireAge(int x, int y, int[][] pixel, int[][] pixelAge) {
        int fireAge = 0; // We're relying on fire age
        if (y > 0) {
            if (Materials.burns(pixel[x][y-1])) {
                fireAge += pixelAge[x][y-1];
                if (pixel[x][y-1] == Materials.LAVA) {
                    fireAge += 35;
                }
            }
            if (x > 0) {
                if (Materials.burns(pixel[x-1][y-1])) {
                    fireAge += pixelAge[x-1][y-1];
                    if (pixel[x-1][y-1] == Materials.LAVA) {
                        fireAge += 35;
                    }
                }
            }
            if (x < pixel.length - 1) {
                if (Materials.burns(pixel[x+1][y-1])) {
                    fireAge += pixelAge[x+1][y-1];
                    if (pixel[x][y-1] == Materials.LAVA) {
                        fireAge += 35;
                    }
                }
            }
        }
        if (y < pixel[0].length - 1) {
            if (Materials.burns(pixel[x][y+1])) {
                fireAge += pixelAge[x][y+1];
                if (pixel[x][y+1] == Materials.LAVA) {
                    fireAge += 35;
                }
            }
            if (x > 0) {
                if (Materials.burns(pixel[x-1][y+1])) {
                    fireAge += pixelAge[x-1][y+1];
                    if (pixel[x-1][y+1] == Materials.LAVA) {
                        fireAge += 35;
                    }
                }
            }
            if (x < pixel.length - 1) {
                if (Materials.burns(pixel[x+1][y+1])) {
                    fireAge += pixelAge[x+1][y+1];
                    if (pixel[x+1][y+1] == Materials.LAVA) {
                        fireAge += 35;
                    }
                }
            }
        }
        if (x > 0) {
            if (Materials.burns(pixel[x-1][y])) {
                fireAge += pixelAge[x-1][y];
                if (pixel[x-1][y] == Materials.LAVA) {
                    fireAge += 35;
                }
            }
        }
        if (x < pixel.length - 1) {
            if (Materials.burns(pixel[x+1][y])) {
                fireAge += pixelAge[x+1][y];
                if (pixel[x+1][y] == Materials.LAVA) {
                    fireAge += 35;
                }
            }
        }
        return fireAge;
    }
}
