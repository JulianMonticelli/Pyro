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
    public static void handlePlant(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        pixelWrite[x][y] = pixelRead[x][y];
        pixelAgeWrite[x][y] = pixelAgeRead[x][y];
        if (pixelAgeRead[x][y] > 1) {
            if (y > 0) {
                if (pixelRead[x][y-1] == Materials.WATER) {
                    
                    pixelWrite[x][y-1] = Materials.PLANT;
                }
                if (x > 0) {
                    if (pixelRead[x-1][y-1] == Materials.WATER) {
                        pixelRead[x-1][y-1] = Materials.NOTHING;
                        if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                            pixelWrite[x-1][y-1] = Materials.PLANT;
                        }
                    }
                }
                if (x < pixelRead.length - 1) {
                    if (pixelRead[x+1][y-1] == Materials.WATER) {
                        pixelRead[x+1][y-1] = Materials.NOTHING;
                        if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                            pixelWrite[x+1][y-1] = Materials.PLANT;
                        }
                    }
                }
            }
            if (y < pixelRead[0].length - 1) {
                if (pixelRead[x][y+1] == Materials.WATER) {
                    pixelRead[x][y+1] = Materials.NOTHING;
                    if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                        pixelWrite[x][y+1] = Materials.PLANT;
                    }
                }
                if (x > 0) {
                    if (pixelRead[x-1][y+1] == Materials.WATER) {
                        pixelRead[x-1][y+1] = Materials.NOTHING;
                        if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                            pixelWrite[x-1][y+1] = Materials.PLANT;
                        }
                    }
                }
                if (x < pixelRead.length - 1) {
                    if (pixelRead[x+1][y+1] == Materials.WATER) {
                        pixelRead[x+1][y+1] = Materials.NOTHING;
                        if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                            pixelWrite[x+1][y+1] = Materials.PLANT;
                        }
                    }
                }
            }
            if (x > 0) {
                if (pixelRead[x-1][y] == Materials.WATER) {
                    pixelRead[x-1][y] = Materials.NOTHING;
                    if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                        pixelWrite[x-1][y] = Materials.PLANT;
                    }
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y] == Materials.WATER) {
                    pixelRead[x+1][y] = Materials.NOTHING;
                    if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                        pixelWrite[x+1][y] = Materials.PLANT;
                    }
                }
            }
        }
        if (pixelAgeRead[x][y] < 2)
            pixelAgeWrite[x][y]++;
    }
    
    
    
    

    private static final int LIFE_ROLL_MAX = 49;
    private static final int EDGE_ROLL_MAX = 49;
    private static final int LIFE_ROLL_THRESHOLD = 36;
    private static final int EDGE_ROLL_THRESHOLD = 27;
    public static void handleLifeSeed(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        pixelWrite[x][y] = pixelRead[x][y];
        pixelAgeWrite[x][y] = pixelAgeRead[x][y];
        if(pixelAgeRead[x][y] % (rand.nextInt(10000)+1) == 0) {
           if (y > 0) {
                if (pixelRead[x][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixelWrite[x][y-1] = Materials.LIFE_SEED;
                        pixelAgeWrite[x][y-1] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixelWrite[x][y-1] = Materials.LIFE_SEED_EDGE;
                        pixelAgeWrite[x][y-1] = 0;
                    }
                }
                if (x > 0) {
                    if (pixelRead[x-1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixelWrite[x-1][y-1] = Materials.LIFE_SEED;
                            pixelAgeWrite[x-1][y-1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixelWrite[x-1][y-1] = Materials.LIFE_SEED_EDGE;
                            pixelAgeWrite[x-1][y-1] = 0;
                        }
                    }
                }
                if (x < pixelRead.length - 1) {
                    if (pixelRead[x+1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixelWrite[x+1][y-1] = Materials.LIFE_SEED;
                            pixelAgeWrite[x+1][y-1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixelWrite[x+1][y-1] = Materials.LIFE_SEED_EDGE;
                            pixelAgeWrite[x+1][y-1] = 0;
                        }
                    }
                }
            }
           if (y < pixelRead[x].length - 1) {
                if (pixelRead[x][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixelWrite[x][y+1] = Materials.LIFE_SEED;
                        pixelAgeWrite[x][y+1] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixelWrite[x][y+1] = Materials.LIFE_SEED_EDGE;
                        pixelAgeWrite[x][y+1] = 0;
                    }
                }
                if (x > 0) {
                    if (pixelRead[x-1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixelWrite[x-1][y+1] = Materials.LIFE_SEED;
                            pixelAgeWrite[x-1][y+1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixelWrite[x-1][y+1] = Materials.LIFE_SEED_EDGE;
                            pixelAgeWrite[x-1][y+1] = 0;
                        }
                    }
                }
                if (x < pixelRead.length - 1) {
                    if (pixelRead[x+1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                            pixelWrite[x+1][y+1] = Materials.LIFE_SEED;
                            pixelAgeWrite[x+1][y+1] = 0;
                        }
                        else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                            pixelWrite[x+1][y+1] = Materials.LIFE_SEED_EDGE;
                            pixelAgeWrite[x+1][y+1] = 0;
                        }
                    }
                }
            }
           if (x > 0) {
               if (pixelRead[x-1][y] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixelWrite[x-1][y] = Materials.LIFE_SEED;
                        pixelAgeWrite[x-1][y] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixelWrite[x-1][y] = Materials.LIFE_SEED_EDGE;
                        pixelAgeWrite[x-1][y] = 0;
                    }
                }
           }
           if (x < 0) {
               if (pixelRead[x+1][y] == Materials.NOTHING) {
                    if (rand.nextInt(LIFE_ROLL_MAX) > LIFE_ROLL_THRESHOLD) {
                        pixelWrite[x+1][y] = Materials.LIFE_SEED;
                        pixelAgeWrite[x+1][y] = 0;
                    }
                    else if (rand.nextInt(EDGE_ROLL_MAX) > EDGE_ROLL_THRESHOLD) {
                        pixelWrite[x+1][y] = Materials.LIFE_SEED_EDGE;
                        pixelAgeWrite[x+1][y] = 0;
                    }
                }
           }
        }
        pixelAgeWrite[x][y]++; // regardless of this result, add 1 to age each tick
    }
}
