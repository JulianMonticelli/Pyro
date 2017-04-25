/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package pyro;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import javax.swing.JPanel;

/**
 * @author Julian
 */
class GamePanel extends JPanel {
        
    // Constants
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;
    public static final int CURSOR_SIZE_SMALL = 1;
    public static final int CURSOR_SIZE_MEDIUM = 2;
    
    
    
    
    // Panel globals
    private boolean mouseHeldIn;
    private int tickSpeed;
    private boolean paused;
    private boolean running = true;
    private boolean isDrag;
    private boolean rowHasFlow;
    private ArrayList<ArrayList<Integer>> streams;
    // May not need hasFlowed anymore but keeping it just incase
    private boolean[][] hasFlowed;
    
    private final int[][] pixel;
    private final int[][] pixelAge; // ONLY to be used for Fire
    private final int[][] explosionTable;
    private int prevXDrag, prevYDrag;
    private int cursorSize;
    
    private int currentMaterial = Materials.SAND; // test for sand
    
    private final Random rand;
    
    public void setCursorSize(int size) {
        cursorSize = size;
    }
    
    public void setMaterial(int material) {
        currentMaterial = material;
    }
    
    public int getMaterial() {
        return currentMaterial;
    }
    
    public GamePanel() {
        cursorSize = CURSOR_SIZE_MEDIUM;
        rand = new Random(System.currentTimeMillis()); // Use currentTimeMillis() as seed
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        rowHasFlow = false;
        streams = new ArrayList<>();
        hasFlowed = new boolean[WIDTH][HEIGHT];
        pixel = new int[WIDTH][HEIGHT];
        pixelAge = new int[WIDTH][HEIGHT];
        explosionTable = new int[WIDTH][HEIGHT];
        mouseHeldIn = false;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_N:
                        currentMaterial = Materials.CONCRETE;
                        break;
                    case KeyEvent.VK_S:
                        currentMaterial = Materials.SAND;
                        break;
                    case KeyEvent.VK_W:
                        currentMaterial = Materials.WATER;
                        break;
                    case KeyEvent.VK_D:
                        currentMaterial = Materials.WOOD;
                        break;
                    case KeyEvent.VK_F:
                        currentMaterial = Materials.FIRE;
                        break;
                    case KeyEvent.VK_P:
                        currentMaterial = Materials.PLANT;
                        break;
                    case KeyEvent.VK_O:
                        currentMaterial = Materials.OIL;
                        break;
                    case KeyEvent.VK_L:
                        currentMaterial = Materials.LAVA;
                        break;
                    case KeyEvent.VK_R:
                        currentMaterial = Materials.RAIN_CLOUD;
                        break;
                    case KeyEvent.VK_X:
                        currentMaterial = Materials.GUNPOWDER;
                        break;
                    case KeyEvent.VK_T:
                        currentMaterial = Materials.TNT;
                        break;
                    case KeyEvent.VK_U:
                        currentMaterial = Materials.FUSE;
                        break;
                    case KeyEvent.VK_Y:
                        currentMaterial = Materials.NITROGLYCERIN;
                        break;
                    case KeyEvent.VK_C:
                        currentMaterial = Materials.C4;
                        break;
                    case KeyEvent.VK_E:
                        currentMaterial = Materials.ELECTRICITY;
                        break;
                    case KeyEvent.VK_I:
                        currentMaterial = Materials.LIFE_SEED;
                        break;
                    case KeyEvent.VK_G:
                        currentMaterial = Materials.GASOLINE;
                        break;
                    case KeyEvent.VK_A:
                        currentMaterial = Materials.ANTI_MATTER;
                        break;
                    default:
                        break; // Do nothing
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                createPoint(e.getX(), e.getY());
                mouseHeldIn = true;
                prevXDrag = e.getX();
                prevYDrag = e.getY();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // Mouse clicked logic
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                //isDrag = false;
                mouseHeldIn = false;
                prevXDrag = -1;
                prevYDrag = -1;
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (prevXDrag >= 0) {
                    createSlopeFromPoints(prevXDrag, prevYDrag, e.getX(), e.getY());
                } else
                    createPoint(e.getX(), e.getY());
                
                prevXDrag = e.getX();
                prevYDrag = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Mouse moved logic
            }
            
        });
    }
    private void createSlopeFromPoints(int prevX, int prevY, int currX, int currY) {
        // Bresenham's algorithm - taken from tomasjaneck.cz
        // (I did jack this code - why reinvent the wheel?)
        int dx = Math.abs(currX - prevX);
        int dy = Math.abs(currY - prevY);
        int rozdil = dx - dy;

        int posun_x, posun_y;

        if (prevX < currX) posun_x = 1; else posun_x = -1;
        if (prevY < currY) posun_y = 1; else posun_y = -1;

        while ((prevX != currX) || (prevY != currY)) {  

            int p = 2 * rozdil;

            if (p > -dy) {
                rozdil = rozdil - dy;
                prevX = prevX + posun_x;
            }
            if (p < dx) {
                rozdil = rozdil + dx;
                prevY = prevY + posun_y;
            }
            createPoint(prevX, prevY);
        } 
    }
    
    private void createPoint(int x, int y) {
        // Is our draw origin offscreen? If so then return
        if (x < 0 || x >= pixel.length || y < 0 || y >= pixel[0].length)
            return;
        pixel[x][y] = currentMaterial;  // No need to check if we passed above check
        pixelAge[x][y] = 0;  // No need to check if we passed above check
        if(cursorSize == CURSOR_SIZE_MEDIUM) {
            if (x > 0) { // If our x is > 1
                pixel[x-1][y] = currentMaterial; // This is okay
                pixelAge[x-1][y] = 0; // This is okay
                if (y > 0) {
                    pixel[x-1][y-1] = currentMaterial;
                    pixelAge[x-1][y-1] = 0;
                }
                if (y < pixel[0].length - 1) {
                    pixel[x-1][y+1] = currentMaterial;
                    pixelAge[x-1][y+1] = 0;
                }
                if (x > 1) {
                    pixel[x-2][y] = currentMaterial;
                    pixelAge[x-2][y] = 0;
                }
            }
            if (x < pixel.length - 1) {
                pixel[x+1][y] = currentMaterial; // This is okay
                pixelAge[x+1][y] = 0; // This is okay
                if (y > 0) {
                    pixel[x+1][y-1] = currentMaterial;
                    pixelAge[x+1][y-1] = 0;
                }
                if (y < pixel[0].length - 1) {
                    pixel[x+1][y+1] = currentMaterial;
                    pixelAge[x+1][y+1] = 0;
                }
                if (x < pixel.length - 2) {
                    pixel[x+2][y] = currentMaterial;
                    pixelAge[x+2][y] = 0;
                }
            }
            if (y > 0) {
                pixel[x][y-1] = currentMaterial;
                pixelAge[x][y-1] = 0;
                if (y > 1) {
                    pixel[x][y-2] = currentMaterial;
                    pixelAge[x][y-2] = 0;
                }
            }
            if (y < pixel[0].length - 1) {
                pixel[x][y+1] = currentMaterial;
                pixelAge[x][y+1] = 0;
                if (y < pixel[0].length - 2) {
                    pixel[x][y+2] = currentMaterial;
                    pixelAge[x][y+2] = 0;
                }
            }
        }
        else if (cursorSize == CURSOR_SIZE_SMALL) {
            pixel[x][y] = currentMaterial;
            pixelAge[x][y] = 0;
        }
        
        //repaint(x-2,y-2,x+2,y+2);
    }
    
    public void init() {
        Materials.init();
        repaint();
        prevXDrag = -1;
        prevYDrag = -1;
        tickSpeed = 30;
        paused = false;
        running = true;
        run();
    }
    
    public void clear() {
        paused = true;
        // Wait for thread to finish draw & tick (30ms)
        try {
            Thread.sleep(30L);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        for (int x = pixel.length-1; x > -1;  x--) {
            for (int y = pixel[x].length-1; y > -1;  y--) {
                pixel[x][y] = 0;
            }
        }
        paused = false;
    }
    
    private void tick() {
        // Handle explosions
        if (mouseHeldIn) {
            createPoint(prevXDrag, prevYDrag);
        }
        long random = rand.nextLong();
        rand.setSeed(System.currentTimeMillis() + random);
        
        // RESET HASFLOWED BOOLEAN
        for (int y = pixel[0].length-1; y > -1;  y--) {
            for (int x = pixel.length-1; x > -1;  x--) {
                hasFlowed[x][y] = false;
            }
        }
        
        Stack<int[]> antiGravStack = new Stack<>();
        
        // LOGIC
        for (int y = pixel[0].length-1; y > -1;  y--) {
            //for (int x = pixel.length-1; x > -1;  x--) {
            for (int x = 0; x < pixel.length;  x++) {
                // First, deal with any and all explosions
                ExplosionHandler.handleExplosionTable(x, y, pixel, pixelAge, explosionTable, rand);
                
                // Second, skip doing anything if the current pixel is nothing
                if(pixel[x][y] == Materials.NOTHING)
                {
                    continue;
                }
                
                // Handle fire
                if (pixel[x][y] >= Materials.FIRE && pixel[x][y] <= Materials.FIRE_ORANGE_2) { // if any instance of fire
                    FireHandler.handleFire(x, y, pixel, pixelAge, rand); // Handle dat fire
                }
                
                // Handle burnables
                if (Materials.isBurnable(pixel[x][y])) {
                    FireHandler.handleBurnable(x, y, pixel, pixelAge); // Handle dat burnable yo
                }
                
                // Handle fire negation
                if (Materials.negatesFire(pixel[x][y])) {
                    FireHandler.handleFireNegation(x, y, pixel, pixelAge);
                }
                
                // Handle plant growth
                if (pixel[x][y] == Materials.PLANT) {
                    AliveHandler.handlePlant(x, y, pixel, pixelAge);
                }
                
                // Handle water spawning
                if (Materials.spawnsWater(pixel[x][y])) {
                    SpawnHandler.handleSpawnsWater(x, y, pixel, rand);
                }
                
                // Handle explosive materials
                if (Materials.getExplosiveRadius(pixel[x][y]) > 0) {
                    ExplosionHandler.handleExplosive(x, y, pixel, pixelAge, explosionTable);
                }
                
                // Handle electricity
                if (Materials.isElectric(pixel[x][y])) {
                    ElectricityHandler.handleElectricity(x, y, pixel, pixelAge, rand);
                }
                
                // Handle life seed
                if (pixel[x][y] == Materials.LIFE_SEED) {
                    AliveHandler.handleLifeSeed(x, y, pixel, pixelAge, rand);
                }
                
                // Falling & Offscreen correction
                if (y == pixel[x].length-1) { // are we at the bottom of the screen?
                    if (Materials.falls(pixel[x][y])) {
                        pixel[x][y] = Materials.NOTHING;
                    }
                }
                else {
                    if (Materials.falls(pixel[x][y])) {
                        if (pixel[x][y+1] == Materials.NOTHING || Materials.canFallThrough(pixel[x][y+1])) {
                            pixel[x][y+1] = pixel[x][y];
                            pixel[x][y] = Materials.NOTHING;
                        } else if (Materials.getFlowIndex(pixel[x][y]) > 0 && !hasFlowed[x][y]) { // If there's something impeding falling and this pixel hasn't flown already, check flow
                            FlowHandler.checkFlow(x, y, pixel, streams, rand);
                            rowHasFlow = true; // Make sure we tell the program that this row has some flow to go!
                        }
                    }
                }
                
                if(Materials.isAntiGravity(pixel[x][y])) {
                    int[] currXY = {x, y};
                    antiGravStack.push(currXY);
                }
                
            } // End row
            
            
            
            
            // Check if this row has any flow, and if so, perform a flow algorithm
            if(rowHasFlow) {
                FlowHandler.flowRow(y, pixel, streams, rand);
                // Reset state variables...
                streams = new ArrayList<>(); // Create new Streams object
                rowHasFlow = false;
            }
            
        }
        
        if(!antiGravStack.isEmpty()) {
            while(!antiGravStack.isEmpty()) {
                int[] xy = antiGravStack.pop();
                if(xy[1] == 0) {
                    if(Materials.isAntiGravity(pixel[xy[0]][xy[1]])) {
                        pixel[xy[0]][xy[1]] = Materials.NOTHING;
                    }
                }
                else {
                    if(Materials.isAntiGravity(pixel[xy[0]][xy[1]])) {
                        if (pixel[xy[0]][xy[1]-1] == Materials.NOTHING || pixel[xy[0]][xy[1]-1] == Materials.ANTI_MATTER) {
                            pixel[xy[0]][xy[1]-1] = pixel[xy[0]][xy[1]];
                            pixel[xy[0]][xy[1]] = Materials.NOTHING;
                        } else {
                            ExplosionHandler.markExplosion(xy[0], xy[1], 3, pixel, pixelAge, explosionTable);       
                        }
                    }
                }
            }
        }
    }
    
    private void run() {
        while(running) {
            // Get before time
            long beforeTime = System.currentTimeMillis();
            
            // Game logic
            tick();
            
            // Graphics
            repaint();
            
            
            // Handle pausing
            while(paused) {
                try {
                    Thread.sleep(50L); // Attempt to sleep
                } catch (InterruptedException ex) {
                    // Do nothing if it fails bc we don't care
                }
            }
            
            // Calculate game step time
            long timeSpent = System.currentTimeMillis() - beforeTime;
            
            // FPS correction
            if (timeSpent < 1000L/tickSpeed) {
                try {
                    Thread.sleep((long)(1000/tickSpeed) - timeSpent); // Sleep for duration of rest of tick time
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            
        }
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Need this call before painting logic
        // Graphic stuff goes after this comment
        g.setColor(Materials.getColor(Materials.NOTHING));
        g.fillRect(0, 0, WIDTH-1, HEIGHT-1);
        for (int x = 0; x < pixel.length; x++) {
            for (int y = 0; y < pixel[x].length; y++) {
                if (pixel[x][y] > Materials.NOTHING) {
                    g.setColor(Materials.getColor(pixel[x][y]));
                    g.drawLine(x, y, x, y);
                }
            }
        }
        //repaint();
    }
    
}
