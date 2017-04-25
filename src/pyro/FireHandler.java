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
public class FireHandler {
    
    public static void handleFire(int x, int y, int[][] pixel, int[][] pixelAge, Random rand) {
        if (y == 0) { // Make sure our fire is not at the y origin!
            pixel[x][y] = Materials.NOTHING; // for now set fire at top to nothing
        }
        if (pixelAge[x][y] == 10) pixel[x][y] = Materials.FIRE_ORANGE;
        if (pixelAge[x][y] == 20) pixel[x][y] = Materials.FIRE_ORANGE_2;
        pixelAge[x][y] += 1;
        if (y > 0) {
            if (pixel[x][y-1] == Materials.NOTHING) {
                if (rand.nextInt(7) > 3) {
                    pixel[x][y-1] = Materials.FIRE_ORANGE;
                    pixelAge[x][y-1] = pixelAge[x][y];
                }
            }
            if (x > 0) {
                if (pixel[x-1][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 97) {
                        pixel[x-1][y-1] = Materials.FIRE_ORANGE;
                        pixelAge[x-1][y-1] = pixelAge[x][y];
                    }
                }
            }
            if (x < pixel.length - 1) {
                if (pixel[x+1][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 97) {
                        pixel[x+1][y-1] = Materials.FIRE_ORANGE;
                        pixelAge[x+1][y-1] = pixelAge[x][y];
                    }
                }
            }
        }
        if (pixelAge[x][y] > 30) {
            pixel[x][y] = Materials.NOTHING;
            pixelAge[x][y] = 0;
        }
    }
        
    public static void handleFireNegation(int x, int y, int[][] pixel, int[][] pixelAge) {
        if (y > 0) {
            if (Materials.burns(pixel[x][y-1])) {
                pixel[x][y-1] = Materials.NOTHING;
                pixelAge[x][y-1] = 0;
            }
            if (x > 0) {
                if (Materials.burns(pixel[x-1][y-1])) {
                    pixel[x-1][y-1] = Materials.NOTHING;
                    pixelAge[x-1][y-1] = 0;
                }
            }
            if (x < pixel.length - 1) {
                if (Materials.burns(pixel[x+1][y-1])) {
                    pixel[x+1][y-1] = Materials.NOTHING;
                    pixelAge[x+1][y-1] = 0;
                }
            }
        }
        if (y < pixel[0].length - 1) {
            if (Materials.burns(pixel[x][y+1])) {
                pixel[x][y+1] = Materials.NOTHING;
                pixelAge[x][y+1] = 0;
            }
            if (x > 0) {
                if (Materials.burns(pixel[x-1][y+1])) {
                    pixel[x-1][y+1] = Materials.NOTHING;
                    pixelAge[x-1][y+1] = 0;
                }
            }
            if (x < pixel.length - 1) {
                if (Materials.burns(pixel[x+1][y+1])) {
                    pixel[x+1][y+1] = Materials.NOTHING;
                    pixelAge[x+1][y+1] = 0;
                }
            }
        }
        if (x > 0) {
            if (Materials.burns(pixel[x-1][y])) {
                pixel[x-1][y] = Materials.NOTHING;
                pixelAge[x-1][y] = 0;
            }
        }
        if (x < pixel.length - 1) {
            if (Materials.burns(pixel[x+1][y])) {
                pixel[x+1][y] = Materials.NOTHING;
                pixelAge[x+1][y] = 0;
            }
        }
    }
    
    public static void handleBurnable(int x, int y, int[][] pixel, int[][] pixelAge) {
        if (getSurroundingFireAge(x, y, pixel, pixelAge) > Materials.getBurnIndex(pixel[x][y])) {
            pixel[x][y] = Materials.FIRE;
            pixelAge[x][y] = 0;
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
