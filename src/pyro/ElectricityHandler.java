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
    
    // WARNING: If you choose to edit this number, beware that there is most
    // definitely a possiblity that you will screw up and create infinite
    // electricity. Keep in mind that the ELECTRICITY_START_AGE should be set
    // such that it will never REACH zero. This is because zero is used to define
    // a new bout of electricity.
    public static final int ELECTRICITY_AGE_LOSS = 5;
    public static final int ELECTRICITY_LIGHTNING_AGE_LOSS = 5;
    public static final int ELECTRICITY_SPARK_AGE_LOSS = 45;
    
    public static final int ELECTRICITY_START_AGE = 107;
    public static final int ELECTRICITY_LIGHTNING_START_AGE = 201;
    
    public static final int CHANCE_ROLL_MAX = 100;
    public static final int ELECTRICITY_CHANCE_ROLL_MIN = 85;    
    public static final int ELECTRICITY_SPARK_CHANCE_ROLL_MIN = 90;    
    public static final int ELECTRICITY_LIGHTNING_CHANCE_ROLL_MIN_DOWNWARDS = 45;
    public static final int ELECTRICITY_LIGHTNING_CHANCE_ROLL_MIN_SIDEWAYS = 85;
    
    
    
    
    public static void handleElectricity(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        if (pixelRead[x][y] == Materials.ELECTRICITY) {  
            if (pixelAgeRead[x][y] == 0) {
                pixelAgeWrite[x][y] = ELECTRICITY_START_AGE;
            } else {
                pixelAgeWrite[x][y] = pixelAgeRead[x][y] - ELECTRICITY_AGE_LOSS;
            }
            if (y > 0) {
                if (pixelRead[x][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                        pixelWrite[x][y-1] = Materials.ELECTRICITY;
                        pixelAgeWrite[x][y-1] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                    }
                }
                if (x > 0) {
                    if (pixelRead[x-1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                            pixelWrite[x-1][y-1] = Materials.ELECTRICITY;
                            pixelAgeWrite[x-1][y-1] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                        }
                    }
                }
                if (x < pixelRead.length - 1) {
                    if (pixelRead[x+1][y-1] == Materials.NOTHING) {
                        if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                            pixelWrite[x+1][y-1] = Materials.ELECTRICITY;
                            pixelAgeWrite[x+1][y-1] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                        }
                    }
                }
            }
            if (y < pixelRead[0].length - 1) {
                if (pixelRead[x][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                        pixelWrite[x][y+1] = Materials.ELECTRICITY;
                        pixelAgeWrite[x][y+1] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                    }
                }
                if (x > 0) {
                    if (pixelRead[x-1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                            pixelWrite[x-1][y+1] = Materials.ELECTRICITY;
                            pixelAgeWrite[x-1][y+1] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                        }
                    }
                }
                if (x < pixelRead.length - 1) {
                    if (pixelRead[x+1][y+1] == Materials.NOTHING) {
                        if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                            pixelWrite[x+1][y+1] = Materials.ELECTRICITY;
                            pixelAgeWrite[x+1][y+1] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                        }
                    }
                }
            }
            if (x > 0) {
                if (pixelRead[x-1][y] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                        pixelWrite[x-1][y] = Materials.ELECTRICITY;
                        pixelAgeWrite[x-1][y] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                    }
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_CHANCE_ROLL_MIN) {
                        pixelWrite[x+1][y] = Materials.ELECTRICITY;
                        pixelAgeWrite[x+1][y] = pixelAgeWrite[x][y] - ELECTRICITY_AGE_LOSS;
                    }
                }
            }
            if (pixelAgeWrite[x][y] <= 0) {
                pixelWrite[x][y] = Materials.NOTHING;
                pixelAgeWrite[x][y] = 0;
            }
        }
        else if (pixelRead[x][y] == Materials.ELECTRICITY_LIGHTNING) {   
            handleLightning(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);   
        }
        else if (pixelRead[x][y] == Materials.ELECTRICITY_SPARK) {   
            handleSpark(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);   
        }
    }
    
    
    public static void handleLightning(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        pixelWrite[x][y] = Materials.ELECTRICITY_LIGHTNING;

        if (pixelAgeRead[x][y] == 0) {
            pixelAgeWrite[x][y] = ELECTRICITY_LIGHTNING_START_AGE;
        } else {
            pixelAgeWrite[x][y] = pixelAgeRead[x][y] - ELECTRICITY_LIGHTNING_AGE_LOSS;
        }

        // Determine whether or not this lightning pixel should be extended...
        int numSurroundingLightningPixels = getNumSurroundingLightningPixels(
                x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);

        if (numSurroundingLightningPixels <= 2) {
            if (y < pixelRead[0].length - 1) {
                if (pixelRead[x][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_LIGHTNING_CHANCE_ROLL_MIN_DOWNWARDS) {
                        pixelWrite[x][y+1] = Materials.ELECTRICITY_LIGHTNING - rand.nextInt(2);
                        pixelAgeWrite[x][y+1] = pixelAgeWrite[x][y];
                    }
                    if (x > 0) {
                        if (pixelRead[x-1][y+1] == Materials.NOTHING) {
                            if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_LIGHTNING_CHANCE_ROLL_MIN_SIDEWAYS) {
                                pixelWrite[x-1][y+1] = Materials.ELECTRICITY_LIGHTNING - rand.nextInt(2);
                                pixelAgeWrite[x-1][y+1] = pixelAgeWrite[x][y];
                            }
                        }
                    }
                    if (x < pixelRead.length - 1) {
                        if (pixelRead[x+1][y+1] == Materials.NOTHING) {
                            if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_LIGHTNING_CHANCE_ROLL_MIN_SIDEWAYS) {
                                pixelWrite[x+1][y+1] = Materials.ELECTRICITY_LIGHTNING - rand.nextInt(2);
                                pixelAgeWrite[x+1][y+1] = pixelAgeWrite[x][y];
                            }
                        }
                    }
                }

            }
        }
        if (pixelAgeWrite[x][y] <= 0) {
            pixelWrite[x][y] = Materials.NOTHING;
            pixelAgeWrite[x][y] = 0;
        }
    }
    
    
    public static void handleSpark(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        pixelWrite[x][y] = Materials.ELECTRICITY_SPARK;

        pixelAgeWrite[x][y] = pixelAgeRead[x][y] - ELECTRICITY_SPARK_AGE_LOSS;

        if (y > 0) {
            if (pixelRead[x][y-1] == Materials.NOTHING) {
                if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                    pixelWrite[x][y-1] = Materials.ELECTRICITY_SPARK;
                    pixelAgeWrite[x][y-1] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                }
            }
            if (x > 0) {
                if (pixelRead[x-1][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                        pixelWrite[x-1][y-1] = Materials.ELECTRICITY_SPARK;
                        pixelAgeWrite[x-1][y-1] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                    }
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y-1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                        pixelWrite[x+1][y-1] = Materials.ELECTRICITY_SPARK;
                        pixelAgeWrite[x+1][y-1] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                    }
                }
            }
        }
        if (y < pixelRead[0].length - 1) {
            if (pixelRead[x][y+1] == Materials.NOTHING) {
                if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                    pixelWrite[x][y+1] = Materials.ELECTRICITY_SPARK;
                    pixelAgeWrite[x][y+1] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                }
            }
            if (x > 0) {
                if (pixelRead[x-1][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                        pixelWrite[x-1][y+1] = Materials.ELECTRICITY_SPARK;
                        pixelAgeWrite[x-1][y+1] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                    }
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y+1] == Materials.NOTHING) {
                    if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                        pixelWrite[x+1][y+1] = Materials.ELECTRICITY_SPARK;
                        pixelAgeWrite[x+1][y+1] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                    }
                }
            }
        }
        if (x > 0) {
            if (pixelRead[x-1][y] == Materials.NOTHING) {
                if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                    pixelWrite[x-1][y] = Materials.ELECTRICITY_SPARK;
                    pixelAgeWrite[x-1][y] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                }
            }
        }
        if (x < pixelRead.length - 1) {
            if (pixelRead[x+1][y] == Materials.NOTHING) {
                if (rand.nextInt(CHANCE_ROLL_MAX) > ELECTRICITY_SPARK_CHANCE_ROLL_MIN) {
                    pixelWrite[x+1][y] = Materials.ELECTRICITY_SPARK;
                    pixelAgeWrite[x+1][y] = pixelAgeWrite[x][y] - ELECTRICITY_SPARK_AGE_LOSS;
                }
            }
        }
        if (pixelAgeWrite[x][y] <= 0) {
            pixelWrite[x][y] = Materials.NOTHING;
            pixelAgeWrite[x][y] = 0;
        }
        
        if (pixelAgeWrite[x][y] <= 0) {
            pixelWrite[x][y] = Materials.NOTHING;
            pixelAgeWrite[x][y] = 0;
        }
    }
    
    
    
    public static int getNumSurroundingLightningPixels(int x, int y, int[][] pixelRead, int[][] pixelWrite, int[][] pixelAgeRead, int[][] pixelAgeWrite, Random rand) {
        int numSurrounding = 0;
        if (y > 0) {
            if (pixelRead[x][y-1] == Materials.ELECTRICITY_LIGHTNING
                    || pixelWrite[x][y-1] == Materials.ELECTRICITY_LIGHTNING) {
                numSurrounding++;
            }
            if (x > 0) {
                if (pixelRead[x-1][y-1] == Materials.ELECTRICITY_LIGHTNING
                        || pixelWrite[x-1][y-1] == Materials.ELECTRICITY_LIGHTNING) {
                    numSurrounding++;
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y-1] == Materials.ELECTRICITY_LIGHTNING
                    || pixelWrite[x+1][y-1] == Materials.ELECTRICITY_LIGHTNING) {
                numSurrounding++;
                }
            }
        }
        if (x > 0) {
            if (pixelRead[x-1][y] == Materials.ELECTRICITY_LIGHTNING
                    || pixelWrite[x-1][y] == Materials.ELECTRICITY_LIGHTNING) {
                numSurrounding++;
            }
        }
        if (x < pixelRead.length - 1) {
            if (pixelRead[x+1][y] == Materials.ELECTRICITY_LIGHTNING
                    || pixelWrite[x+1][y] == Materials.ELECTRICITY_LIGHTNING) {
                numSurrounding++;
            }
        }
        if (y < pixelRead[x].length - 1) {
            if (pixelRead[x][y+1] == Materials.ELECTRICITY_LIGHTNING
                    || pixelWrite[x][y+1] == Materials.ELECTRICITY_LIGHTNING) {
                numSurrounding++;
            }
            if (x > 0) {
                if (pixelRead[x-1][y+1] == Materials.ELECTRICITY_LIGHTNING
                        || pixelWrite[x-1][y+1] == Materials.ELECTRICITY_LIGHTNING) {
                    numSurrounding++;
                }
            }
            if (x < pixelRead.length - 1) {
                if (pixelRead[x+1][y+1] == Materials.ELECTRICITY_LIGHTNING
                        || pixelWrite[x+1][y+1] == Materials.ELECTRICITY_LIGHTNING) {
                    numSurrounding++;
                }
            }
        }
        return numSurrounding;
    }
}
