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
public class ElectricityHandler {
    public static void handleElectricity(int x, int y, int[][] pixel, int[][] pixelAge, Random rand) {
        if (pixel[x][y] == Materials.ELECTRICITY) {   
            if (y > 0) {
                if (pixel[x][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 90) {
                        pixel[x][y-1] = Materials.ELECTRICITY;
                        pixelAge[x][y-1] = pixelAge[x][y];
                    }
                }
                if (x > 0) {
                    if (pixel[x-1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(100) > 90) {
                            pixel[x-1][y-1] = Materials.ELECTRICITY;
                            pixelAge[x-1][y-1] = pixelAge[x][y];
                        }
                    }
                }
                if (x < pixel.length - 1) {
                    if (pixel[x+1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(100) > 90) {
                            pixel[x+1][y-1] = Materials.ELECTRICITY;
                            pixelAge[x+1][y-1] = pixelAge[x][y];
                        }
                    }
                }
            }
            if (y < pixel[0].length - 1) {
                if (pixel[x][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 90) {
                        pixel[x][y+1] = Materials.ELECTRICITY;
                        pixelAge[x][y+1] = pixelAge[x][y];
                    }
                }
                if (x > 0) {
                    if (pixel[x-1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(100) > 90) {
                            pixel[x-1][y+1] = Materials.ELECTRICITY;
                            pixelAge[x-1][y+1] = pixelAge[x][y];
                        }
                    }
                }
                if (x < pixel.length - 1) {
                    if (pixel[x+1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(100) > 90) {
                            pixel[x+1][y+1] = Materials.ELECTRICITY;
                            pixelAge[x+1][y+1] = pixelAge[x][y];
                        }
                    }
                }
            }
            if (x > 0) {
                if (pixel[x-1][y] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 90) {
                        pixel[x-1][y] = Materials.ELECTRICITY;
                        pixelAge[x-1][y] = pixelAge[x][y];
                    }
                }
            }
            if (x < pixel.length - 1) {
                if (pixel[x+1][y] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 90) {
                        pixel[x+1][y] = Materials.ELECTRICITY;
                        pixelAge[x+1][y] = pixelAge[x][y];
                    }
                }
            }
            if (pixelAge[x][y] > 100) {
                pixel[x][y] = Materials.NOTHING;
                pixelAge[x][y] = 0;
            }
            pixelAge[x][y] += 10;
        }
        else if (pixel[x][y] == Materials.ELECTRICITY_LIGHTNING) {  // Code needs improvement - want BOLTS not "spurts"
            if (y < pixel[0].length - 1) {
                if (pixel[x][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(100) > 80) {
                        pixel[x][y+1] = Materials.ELECTRICITY_LIGHTNING;
                        pixelAge[x][y+1] = pixelAge[x][y];
                    }
                    else if (x > 0) {
                        if (pixel[x-1][y+1] == Materials.NOTHING) {
                            if (rand.nextInt(100) > 80) {
                                pixel[x-1][y+1] = Materials.ELECTRICITY_LIGHTNING;
                                pixelAge[x-1][y+1] = pixelAge[x][y];
                            }
                        }
                    }
                    else if (x < pixel.length - 1) {
                        if (pixel[x+1][y+1] == Materials.NOTHING) {
                            if (rand.nextInt(100) > 80) {
                                pixel[x+1][y+1] = Materials.ELECTRICITY_LIGHTNING;
                                pixelAge[x+1][y+1] = pixelAge[x][y];
                            }
                        }
                    }
                }
                
            }
            if (pixelAge[x][y] > 200) {
                pixel[x][y] = Materials.NOTHING;
                pixelAge[x][y] = 0;
            }
            pixelAge[x][y] += 5;
        }
    }
}
