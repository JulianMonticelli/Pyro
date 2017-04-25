/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package pyro;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Julian
 */
public class FlowHandler {
    // FIX THIS PLSSSSS
    //private boolean checkLeftFirst = false;
    
    public static void checkFlow(int x, int y, int[][] pixel, ArrayList<ArrayList<Integer>> stream, Random rand) {
        // If this is the first bit of flowable material in the row
        if(stream.isEmpty()) {
            ArrayList<Integer> newStream = new ArrayList<>(); // Make new stream
            newStream.add(x); // Add x to new stream
            stream.add(newStream); // Add new stream to streams
        // If there are streams in the current stream
        } else {
            // If the last stream's last pixel is the pixel before the current pixel
            if(stream.get(stream.size()-1).get(stream.get(stream.size()-1).size()-1) == x-1) { // Should work ??
                stream.get(stream.size()-1).add(x); // Then add the current pixel to this stream
            // If this is part of a new stream
            } else {
                ArrayList<Integer> newStream = new ArrayList<>();// Make new stream
                newStream.add(x); // Add x to new stream
                stream.add(newStream); // Add new stream to streams
            }
        }
    }
    
    
    public static void flowRow(int y, int[][] pixel, ArrayList<ArrayList<Integer>> streams, Random rand) {
        // Make a place to count how many pixels we have in each stream, and boolean on which way to go
        int[] streamSize = new int[streams.size()];
        boolean[][] streamLeft = new boolean[streams.size()][];
        boolean[][] hasFlowed = new boolean[streams.size()][];
        // For each stream, count how large the stream is
        for (int i = 0; i < streams.size(); i++) {
            streamSize[i] = streams.get(i).size();
            streamLeft[i] = new boolean[streams.get(i).size()];
            hasFlowed[i] = new boolean[streams.get(i).size()];
        }
        
        // For each stream, decide which way to go
        for (int i = 0; i < streams.size(); i++) {
            int currentStreamSize = streams.get(i).size();
            int half = currentStreamSize / 2;
            for(int j  = 0; j < half; j++) {
                streamLeft[i][j] = true;
            }
            if(currentStreamSize % 2 == 1 && currentStreamSize > 2) { // Odd & at least size 3
                streamLeft[i][half + 1] = rand.nextBoolean(); // Randomize middle flow
            }
        }
        
        // Now perform stream flow
        for (int i = 0; i < streams.size(); i++) {
            int currentStreamSize = streams.get(i).size();
            for (int p = 0; p < currentStreamSize; p++) {
                int x = streams.get(i).get(p);
                int material = pixel[x][y];
                int flowLeft = Materials.getFlowIndex(pixel[x][y]);
                int flowIndex = Materials.getFlowIndex(pixel[x][y]);
                if(streamLeft[i][p]) {
                    for (int fi = 1; fi <= flowIndex; fi++) {
                        if( ((x-fi) >= 0) && pixel[x-fi][y] == Materials.NOTHING) {
                            pixel[x-fi][y] = material;
                            pixel[x-fi+1][y] = Materials.NOTHING;
                            hasFlowed[i][p] = true;
                        } else {
                            break; // Break from the loop - cannot flow left
                        }
                    }
                } else {
                    for (int fi = 1; fi <= flowIndex; fi++) {
                        if( ((x+fi) < GamePanel.WIDTH) && pixel[x+fi][y] == Materials.NOTHING) {
                            pixel[x+fi][y] = material;
                            pixel[x+fi-1][y] = Materials.NOTHING;
                            hasFlowed[i][p] = true;
                        } else {
                            break; // Break from the loop - cannot flow left
                        }
                    }
                }
            }
        }
        
    }
    
    
    
    public static void checkFlowBad2(int x, int y, int[][] pixel, boolean hasFlowed[][], Random rand) {
        
        boolean checkLeftFirst;
        
        boolean blocked = false;
        
        for (int i = Materials.getFlowIndex(pixel[x][y]); i > 0; i--) {
            checkLeftFirst = rand.nextBoolean();
            if (checkLeftFirst) {
                if (x > 0) { // if we CAN flow left (index bounds check)
                    if (y < pixel[0].length - 1) { // if we CAN go down (index bounds check)
                        if (pixel[x-1][y] == Materials.NOTHING) { // if there is nothing to the left
                            if (pixel[x-1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal left-down
                                pixel[x-1][y+1] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                hasFlowed[--x][++y] = true; // Update current pixel to hasFlowed
                                continue;
                            } else {
                                pixel[x-1][y] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                hasFlowed[--x][y] = true; // Update current pixel to hasFlowed
                                continue;
                            }
                        } else if (x < pixel.length - 1) { // if we CAN flow right (index bounds check)
                            if (y < pixel[0].length - 1) { // if we CAN go down (index bounds check)
                                if (pixel[x+1][y] == Materials.NOTHING) { // if there is nothing to the right
                                    if (pixel[x+1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal right-down
                                        pixel[x+1][y+1] = pixel[x][y];
                                        pixel[x][y] = Materials.NOTHING;
                                        hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                        hasFlowed[++x][++y] = true; // Update current pixel to hasFlowed
                                        continue;
                                    } else {
                                        pixel[x+1][y] = pixel[x][y];
                                        pixel[x][y] = Materials.NOTHING;
                                        hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                        hasFlowed[++x][y] = true; // Update current pixel to hasFlowed
                                        continue;
                                    }
                                }
                            } else { // if we current pixel is at the last step of falling
                                pixel[x][y] = Materials.NOTHING; // Delete entry (falling off)
                                hasFlowed[x][y] = false; // Delete entry (falling off)
                            }
                        } 
                    } else { // If the current pixel is at the last step of falling off-screen
                        pixel[x][y] = Materials.NOTHING; // Delete entry (falling off)
                        hasFlowed[x][y] = false; // Delete entry (falling off)
                    }
                } else if (x < pixel.length - 1) { // if we CAN flow right (index bounds check)
                    if (y < pixel[0].length - 1) { // if we CAN go down (index bounds check)
                        if (pixel[x+1][y] == Materials.NOTHING) { // if there is nothing to the right
                            if (pixel[x+1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal right-down
                                pixel[x+1][y+1] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                
                                x++;
                                y++;
                                continue;
                            } else {
                                pixel[x+1][y] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                x++;
                                continue;
                            }
                        } else if (x > 0) {
                            if (pixel[x-1][y] == Materials.NOTHING) { // if there is nothing to the left
                                if (pixel[x-1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal left-down
                                    pixel[x-1][y+1] = pixel[x][y];
                                    pixel[x][y] = Materials.NOTHING;
                                    hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                    hasFlowed[--x][++y] = true; // Update current pixel to hasFlowed
                                    continue;
                                } else {
                                    pixel[x-1][y] = pixel[x][y];
                                    pixel[x][y] = Materials.NOTHING;
                                    hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                    hasFlowed[--x][y] = true; // Update current pixel to hasFlowed
                                    continue;
                                }
                            }
                        }
                    } 
                    else { // if we current pixel is at the last step of falling
                        pixel[x][y] = Materials.NOTHING; // Delete entry (falling off)
                        hasFlowed[x][y] = false; // Delete entry (falling off)
                    }
                    break; // If you hit here, you might as well not try anymore
                }
            } else {
                if (x < pixel.length - 1) { // if we CAN flow right (index bounds check)
                    if (y < pixel[0].length - 1) { // if we CAN go down (index bounds check)
                        if (pixel[x+1][y] == Materials.NOTHING) { // if there is nothing to the right
                            if (pixel[x+1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal right-down
                                pixel[x+1][y+1] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                
                                x++;
                                y++;
                                continue;
                            } else {
                                pixel[x+1][y] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                x++;
                                continue;
                            }
                        } else if (x > 0) {
                            if (pixel[x-1][y] == Materials.NOTHING) { // if there is nothing to the left
                                if (pixel[x-1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal left-down
                                    pixel[x-1][y+1] = pixel[x][y];
                                    pixel[x][y] = Materials.NOTHING;
                                    hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                    hasFlowed[--x][++y] = true; // Update current pixel to hasFlowed
                                    continue;
                                } else {
                                    pixel[x-1][y] = pixel[x][y];
                                    pixel[x][y] = Materials.NOTHING;
                                    hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                    hasFlowed[--x][y] = true; // Update current pixel to hasFlowed
                                    continue;
                                }
                            }
                        }
                    } 
                    else { // if we current pixel is at the last step of falling
                        pixel[x][y] = Materials.NOTHING; // Delete entry (falling off)
                    }
                } else if (x > 0) { // if we CAN flow left (index bounds check)
                    if (y < pixel[0].length - 1) { // if we CAN go down (index bounds check)
                        if (pixel[x-1][y] == Materials.NOTHING) { // if there is nothing to the left
                            if (pixel[x-1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal left-down
                                pixel[x-1][y+1] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                hasFlowed[--x][++y] = true; // Update current pixel to hasFlowed
                                continue;
                            } else {
                                pixel[x-1][y] = pixel[x][y];
                                pixel[x][y] = Materials.NOTHING;
                                hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                hasFlowed[--x][y] = true; // Update current pixel to hasFlowed
                                continue;
                            }
                        } else if (x < pixel.length - 1) { // if we CAN flow right (index bounds check)
                            if (y < pixel[0].length - 1) { // if we CAN go down (index bounds check)
                                if (pixel[x+1][y] == Materials.NOTHING) { // if there is nothing to the right
                                    if (pixel[x+1][y+1] == Materials.NOTHING) { // if there is nothing to the diagonal right-down
                                        pixel[x+1][y+1] = pixel[x][y];
                                        pixel[x][y] = Materials.NOTHING;
                                        hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                        hasFlowed[++x][++y] = true; // Update current pixel to hasFlowed
                                        continue;
                                    } else {
                                        pixel[x+1][y] = pixel[x][y];
                                        pixel[x][y] = Materials.NOTHING;
                                        hasFlowed[x][y] = false; // Pixel we moved from to !hasFlowed
                                        hasFlowed[++x][y] = true; // Update current pixel to hasFlowed
                                        continue;
                                    }
                                }
                            } else { // if we current pixel is at the last step of falling
                                pixel[x][y] = Materials.NOTHING; // Delete entry (falling off)
                                hasFlowed[x][y] = false; // Delete entry (falling off)
                            }
                        } 
                    } else { // If the current pixel is at the last step of falling off-screen
                        pixel[x][y] = Materials.NOTHING; // Delete entry (falling off)
                        hasFlowed[x][y] = false; // Delete entry (falling off)
                    }
                    break; // If you hit here, you might as well not try anymore
                }
            }
        }
    }
    
    public static void checkFlowBadAlgorithm(int x, int y, int[][] pixel, boolean hasFlowed[][], Random rand) {
        // RANDOMIZE FLOW
        
        boolean checkLeftFirst = rand.nextBoolean();
        
        //if (checkLeftFirst)
        //    checkLeftFirst = false;
        //else
        //    checkLeftFirst = true;
        
        boolean blocked = false;
        for (int i = Materials.getFlowIndex(pixel[x][y]); i > 0; i--) {
            if (checkLeftFirst) {
                if (x > i - 1) {
                    for (int j = 1; j <= i; j++) {
                        if (pixel[x-j][y] != Materials.NOTHING) {
                            blocked = true;
                            //i = j;
                        }
                        if (blocked) break;
                    }
                    if (!blocked) {
                        pixel[x-i][y] = pixel[x][y];
                        hasFlowed[x-i][y] = true;
                        hasFlowed[x][y] = true;
                        pixel[x][y] = Materials.NOTHING;
                        return;
                    } else {
                        blocked = false;
                    }
                }
                if (x < GamePanel.WIDTH - (i+1)) {
                    for (int j = 1; j <= i; j++) {
                        if (pixel[x+j][y] != Materials.NOTHING) {
                            blocked = true;
                            //i = j;
                        }
                        if (blocked) break;
                    }
                    if (!blocked) {
                        pixel[x+i][y] = pixel[x][y];
                        hasFlowed[x+i][y] = true;
                        hasFlowed[x][y] = true;
                        pixel[x][y] = Materials.NOTHING;
                        return;
                    } else {
                        blocked = false;
                    }
                }
            } else {
                if (x < GamePanel.WIDTH - (i+1)) {
                    for (int j = 1; j <= i; j++) {
                        if (pixel[x+j][y] != Materials.NOTHING) {
                            blocked = true;
                            //i = j;
                        }
                        if (blocked) break;
                    }
                    if (!blocked) {
                        pixel[x+i][y] = pixel[x][y];
                        hasFlowed[x+i][y] = true;
                        hasFlowed[x][y] = true;
                        pixel[x][y] = Materials.NOTHING;
                        return;
                    } else {
                        blocked = false;
                    }
                }
                if (x > i - 1) {
                    for (int j = 1; j <= i; j++) {
                        if (pixel[x-j][y] != Materials.NOTHING) {
                            blocked = true;
                            //i = j;
                        }
                        if (blocked) break;
                    }
                    if (!blocked) {
                        pixel[x-i][y] = pixel[x][y];
                        hasFlowed[x-i][y] = true;
                        hasFlowed[x][y] = true;
                        pixel[x][y] = Materials.NOTHING;
                        return;
                    } else {
                        blocked = false;
                    }
                }
            }
        }
    }

}
