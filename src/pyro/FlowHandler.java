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
public class FlowHandler {
    
    public static void flow(int x, int y, int[][] pixelRead, int[][] pixelWrite,
            Random rand)
    {
        if (y < pixelRead[x].length - 1) {
            if (pixelRead[x][y+1] == Materials.NOTHING 
                    && pixelWrite[x][y+1] == Materials.NOTHING)
            {
                pixelWrite[x][y+1] = pixelRead[x][y];
            } else {        
                
                boolean handled = false;
        
                // Can we flow to left/right?
                
                boolean flowLeft = rand.nextBoolean();
                if (flowLeft) {
                    handled = handleFlowLeft(x, y, pixelRead, pixelWrite, rand);
                    if (!handled) {
                        handled = handleFlowRight(x, y, pixelRead, pixelWrite, rand);
                    }
                } else {
                    handled = handleFlowRight(x, y, pixelRead, pixelWrite, rand);
                    if (!handled) {
                        handled = handleFlowLeft(x, y, pixelRead, pixelWrite, rand);
                    }
                }
                
                // If the pixel was entirely unable to flow, just copy it over
                if (!handled) {
                    pixelWrite[x][y] = pixelRead[x][y];
                }
            }
        }
        // If the pixel is about to fall of the screen, don't worry about it
    }
    
    public static boolean handleFlowLeft(int x, int y, int[][] pixelRead,
            int[][] pixelWrite, Random rand)
    {
        int flowIndex = Materials.getFlowIndex(pixelRead[x][y]);
        
        // Bounds check that we can go left at all before looping
        // through potential flow
        if (x > 0) {
            for (int i = flowIndex; i > 0; i--) {
                if (x >= i) {
                    boolean canFlow = true;
                    for (int j = i-1; j > 0; j--) {
                        // WARNING: 
                        if (pixelRead[x-j][y] != Materials.NOTHING
                                || pixelRead[x-j][y] != Materials.NOTHING)
                        {
                            canFlow = false;
                        }
                    }
                    if (canFlow && pixelRead[x-i][y] == Materials.NOTHING
                            && pixelWrite[x-i][y] == Materials.NOTHING) {
                        pixelWrite[x-i][y] = pixelRead[x][y];
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public static boolean handleFlowRight(int x, int y, int[][] pixelRead,
            int[][] pixelWrite, Random rand)
    {
        int flowIndex = Materials.getFlowIndex(pixelRead[x][y]);
        
        // Bounds check that we can go right at all before looping
        // through potential flow
        if (x < pixelRead.length - 1) {
            for (int i = flowIndex; i > 0; i--) {
                if (x <= pixelRead.length - (i + 1) ) {
                    boolean canFlow = true;
                    for (int j = i-1; j > 0; j--) {
                        // WARNING: 
                        if (pixelRead[x+j][y] != Materials.NOTHING
                                || pixelRead[x+j][y] != Materials.NOTHING)
                        {
                            canFlow = false;
                            break;
                        }
                    }
                    if (canFlow && pixelRead[x+i][y] == Materials.NOTHING
                            && pixelWrite[x+i][y] == Materials.NOTHING) {
                        pixelWrite[x+i][y] = pixelRead[x][y];
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
}
