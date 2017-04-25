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
public class AliveHandler {
    public static void handlePlant(int x, int y, int[][] pixel, int[][] pixelAge) {
        if (pixelAge[x][y] > 1) {
            if (y > 0) {
                if (pixel[x][y-1] == Materials.WATER) {
                    pixel[x][y-1] = Materials.PLANT;
                }
                if (x > 0) {
                    if (pixel[x-1][y-1] == Materials.WATER) {
                        pixel[x-1][y-1] = Materials.PLANT;
                    }
                }
                if (x < pixel.length - 1) {
                    if (pixel[x+1][y-1] == Materials.WATER) {
                        pixel[x+1][y-1] = Materials.PLANT;
                    }
                }
            }
            if (y < pixel[0].length - 1) {
                if (pixel[x][y+1] == Materials.WATER) {
                    pixel[x][y+1] = Materials.PLANT;
                }
                if (x > 0) {
                    if (pixel[x-1][y+1] == Materials.WATER) {
                        pixel[x-1][y+1] = Materials.PLANT;
                    }
                }
                if (x < pixel.length - 1) {
                    if (pixel[x+1][y+1] == Materials.WATER) {
                        pixel[x+1][y+1] = Materials.PLANT;
                    }
                }
            }
            if (x > 0) {
                if (pixel[x-1][y] == Materials.WATER) {
                    pixel[x-1][y] = Materials.PLANT;
                }
            }
            if (x < pixel.length - 1) {
                if (pixel[x+1][y] == Materials.WATER) {
                    pixel[x+1][y] = Materials.PLANT;
                }
            }
        }
        if (pixelAge[x][y] < 2)
            pixelAge[x][y]++;
    }
    
    
    
    

    private static final int LIFE_ROLL_MAX = 49;
    private static final int EDGE_ROLL_MAX = 49;
    private static final int LIFE_ROLL_THRESHOLD = 36;
    private static final int EDGE_ROLL_THRESHOLD = 27;
    public static void handleLifeSeed(int x, int y, int[][] pixel, int[][] pixelAge, Random rand) {
        if(pixelAge[x][y] % 100 == 0) {
           if (y > 0) {
                if (pixel[x][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixel[x][y-1] = Materials.LIFE_SEED;
                        pixelAge[x][y-1] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixel[x][y-1] = Materials.LIFE_SEED_EDGE;
                        pixelAge[x][y-1] = 0;
                    }
                }
                if (x > 0) {
                    if (pixel[x-1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixel[x-1][y-1] = Materials.LIFE_SEED;
                            pixelAge[x-1][y-1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixel[x-1][y-1] = Materials.LIFE_SEED_EDGE;
                            pixelAge[x-1][y-1] = 0;
                        }
                    }
                }
                if (x < pixel.length - 1) {
                    if (pixel[x+1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixel[x+1][y-1] = Materials.LIFE_SEED;
                            pixelAge[x+1][y-1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixel[x+1][y-1] = Materials.LIFE_SEED_EDGE;
                            pixelAge[x+1][y-1] = 0;
                        }
                    }
                }
            }
           if (y < pixel[x].length - 1) {
                if (pixel[x][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixel[x][y+1] = Materials.LIFE_SEED;
                        pixelAge[x][y+1] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixel[x][y+1] = Materials.LIFE_SEED_EDGE;
                        pixelAge[x][y+1] = 0;
                    }
                }
                if (x > 0) {
                    if (pixel[x-1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixel[x-1][y+1] = Materials.LIFE_SEED;
                            pixelAge[x-1][y+1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixel[x-1][y+1] = Materials.LIFE_SEED_EDGE;
                            pixelAge[x-1][y+1] = 0;
                        }
                    }
                }
                if (x < pixel.length - 1) {
                    if (pixel[x+1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixel[x+1][y+1] = Materials.LIFE_SEED;
                            pixelAge[x+1][y+1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixel[x+1][y+1] = Materials.LIFE_SEED_EDGE;
                            pixelAge[x+1][y+1] = 0;
                        }
                    }
                }
            }
           if (x > 0) {
               if (pixel[x-1][y] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixel[x-1][y] = Materials.LIFE_SEED;
                        pixelAge[x-1][y] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixel[x-1][y] = Materials.LIFE_SEED_EDGE;
                        pixelAge[x-1][y] = 0;
                    }
                }
           }
           if (x < 0) {
               if (pixel[x+1][y] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixel[x+1][y] = Materials.LIFE_SEED;
                        pixelAge[x+1][y] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixel[x+1][y] = Materials.LIFE_SEED_EDGE;
                        pixelAge[x+1][y] = 0;
                    }
                }
           }
        }
        pixelAge[x][y]++; // regardless of this result, add 1 to age each tick
    }
}
