/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package pyro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
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
    
    // Locks
    private static final Object TICK_LOCK = new Object();
    
    // Panel globals
    private boolean mouseHeldIn;
    private int tickSpeed;
    private boolean paused;
    private boolean running = true;
    private boolean isDrag;
    
    // Drawing line variables
    private boolean willStartDrawingLine = false;
    private boolean isDrawingLine = false;
    private boolean isControlDown = false;
    private boolean drawingHasBeenCancelled = false;
    private int drawStartX = -1;
    private int drawStartY = -1;
    
    // Double multi-dimensional arrays for each table so that we can appropriately
    // perform reads and writes. Adds a little extra overhead, but it's totally
    // worth it.
    volatile private int[][] pixelWrite;
    volatile private int[][] pixelRead;
    volatile private int[][] pixelAgeWrite;
    volatile private int[][] pixelAgeRead;
    volatile private int[][] explosionTableWrite;
    volatile private int[][] explosionTableRead;
    
    private final int[][] pixel1;
    private final int[][] pixel2;
    private final int[][] pixelAge1; // ONLY to be used for Fire
    private final int[][] pixelAge2; // ONLY to be used for Fire
    private final int[][] explosionTable1;
    private final int[][] explosionTable2;
    private int prevXDrag, prevYDrag;
    private int cursorSize;
    
    private int currentMaterial = Materials.FIRE; // The initial material is fire
    
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
        this.setBackground(Color.black);
        cursorSize = CURSOR_SIZE_MEDIUM;
        rand = new Random(System.currentTimeMillis()); // Use currentTimeMillis() as seed
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        
        pixel1 = new int[WIDTH][HEIGHT];
        pixel2 = new int[WIDTH][HEIGHT];
        pixelWrite = pixel1;
        pixelRead = pixel2;
        
        
        pixelAge1 = new int[WIDTH][HEIGHT];
        pixelAge2 = new int[WIDTH][HEIGHT];
        pixelAgeWrite = pixelAge1;
        pixelAgeRead = pixelAge2;
        
        explosionTable1 = new int[WIDTH][HEIGHT];
        explosionTable2 = new int[WIDTH][HEIGHT];
        explosionTableWrite = explosionTable1;
        explosionTableRead = explosionTable2;
        
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
                        currentMaterial = Materials.ROCK;
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
                    case KeyEvent.VK_1:
                        currentMaterial = Materials.RAIN_CLOUD;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (isDrawingLine) {
                            drawingHasBeenCancelled = true;
                        }
                        clearDrawSelection();
                        break;
                    case KeyEvent.VK_CONTROL:
                        isControlDown = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        paused = !paused;
                    default:
                        break; // Do nothing
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        isControlDown = false;
                        break;
                    default:
                        break;
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (paused) return;
                if (isControlDown) {
                    isDrawingLine = true;
                }
                if (!isDrawingLine) {
                    createPoint(e.getX(), e.getY());
                }
                if (isDrawingLine) {
                    drawStartX = e.getX();
                    drawStartY = e.getY();
                }
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
                mouseHeldIn = false;
                if (isDrawingLine) {
                    if (!paused) {
                        createSlopeFromPoints(drawStartX, drawStartY,
                            prevXDrag, prevYDrag);
                    }
                }
                drawingHasBeenCancelled = false;
                clearDrawSelection();
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (paused) return;
                if (!isDrawingLine) {
                    synchronized (TICK_LOCK) {
                        if (prevXDrag >= 0) {
                            createSlopeFromPoints(prevXDrag, prevYDrag, e.getX(), e.getY());
                        } else {
                            createPoint(e.getX(), e.getY());
                        }
                    }
                }
                
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
        if (x < 0 || x >= pixelRead.length || y < 0 || y >= pixelRead[0].length)
            return;
        pixelWrite[x][y] = currentMaterial;  // No need to check if we passed above check
        pixelAgeWrite[x][y] = 0;  // No need to check if we passed above check
        if(cursorSize == CURSOR_SIZE_MEDIUM) {
            if (x > 0) { // If our x is > 1
                pixelWrite[x-1][y] = currentMaterial; // This is okay
                pixelAgeWrite[x-1][y] = 0; // This is okay
                if (y > 0) {
                    pixelWrite[x-1][y-1] = currentMaterial;
                    pixelAgeWrite[x-1][y-1] = 0;
                }
                if (y < pixelRead[0].length - 1) {
                    pixelWrite[x-1][y+1] = currentMaterial;
                    pixelAgeWrite[x-1][y+1] = 0;
                }
                if (x > 1) {
                    pixelWrite[x-2][y] = currentMaterial;
                    pixelAgeWrite[x-2][y] = 0;
                }
            }
            if (x < pixelRead.length - 1) {
                pixelWrite[x+1][y] = currentMaterial; // This is okay
                pixelAgeWrite[x+1][y] = 0; // This is okay
                if (y > 0) {
                    pixelWrite[x+1][y-1] = currentMaterial;
                    pixelAgeWrite[x+1][y-1] = 0;
                }
                if (y < pixelRead[0].length - 1) {
                    pixelWrite[x+1][y+1] = currentMaterial;
                    pixelAgeWrite[x+1][y+1] = 0;
                }
                if (x < pixelRead.length - 2) {
                    pixelWrite[x+2][y] = currentMaterial;
                    pixelAgeWrite[x+2][y] = 0;
                }
            }
            if (y > 0) {
                pixelWrite[x][y-1] = currentMaterial;
                pixelAgeWrite[x][y-1] = 0;
                if (y > 1) {
                    pixelWrite[x][y-2] = currentMaterial;
                    pixelAgeWrite[x][y-2] = 0;
                }
            }
            if (y < pixelRead[0].length - 1) {
                pixelWrite[x][y+1] = currentMaterial;
                pixelAgeWrite[x][y+1] = 0;
                if (y < pixelRead[0].length - 2) {
                    pixelWrite[x][y+2] = currentMaterial;
                    pixelAgeWrite[x][y+2] = 0;
                }
            }
        }
        else if (cursorSize == CURSOR_SIZE_SMALL) {
            pixelWrite[x][y] = currentMaterial;
            pixelAgeWrite[x][y] = 0;
        }
        
        //repaint(x-2,y-2,x+2,y+2);
    }
    
    public void init() {
        Materials.init();
        repaint();
        prevXDrag = -1;
        prevYDrag = -1;
        tickSpeed = 45;
        paused = false;
        running = true;
        run();
    }
    
    public void clear() {
        synchronized (TICK_LOCK) {
            for (int x = pixelWrite.length-1; x > -1;  x--) {
                for (int y = pixelWrite[x].length-1; y > -1;  y--) {
                    pixelWrite[x][y] = 0;
                    pixelRead[x][y] = 0;
                    pixelAgeWrite[x][y] = 0;
                    pixelAgeRead[x][y] = 0;
                    explosionTableWrite[x][y] = 0;
                    explosionTableRead[x][y] = 0;
                }
            }
            clearDrawSelection();
        }
    }
    
    
    
    
    private void clearDrawSelection() {
        
        isDrawingLine = false;
        
        drawStartX = -1;
        drawStartY = -1;
        prevXDrag = -1;
        prevYDrag = -1;
    }
    
    private void swapFrames() {
        // Swap read frames to write frames
        // Since we do these flips at the same time, we need not worry about
        // any special cases
        if (pixelWrite == pixel1) {
            // Write arrays are now suffix-2
            pixelWrite = pixel2;
            explosionTableWrite = explosionTable2;
            pixelAgeWrite = pixelAge2;
            // Read arrays are now suffix-1
            pixelRead = pixel1;
            pixelAgeRead = pixelAge1;
            explosionTableRead = explosionTable1;
        } else {
            // Write arrays are now suffix-1
            pixelWrite = pixel1;
            pixelAgeWrite = pixelAge1;
            explosionTableWrite = explosionTable1;
            // Read arrays are now suffix-2
            pixelRead = pixel2;
            pixelAgeRead = pixelAge2;
            explosionTableRead = explosionTable2;
        }
        
        // Clear writing frames. We want nothing to be carried over from the
        // read frame unless it is explicitly transferred over from the read
        // frame.
        // WARNING: The current ordering of the for-loop is the most efficient.
        // The other way incurs a considerable performance penalty
        for (int i = 0; i < pixelWrite.length; i++) {
            for (int j = 0; j < pixelWrite[i].length; j++) {
                pixelWrite[i][j] = 0;
                pixelAgeWrite[i][j] = 0;
                explosionTableWrite[i][j] = 0;
            }
        }
    }
    
    private void tick() {
        // Swap int state frames and wipe write frames
        swapFrames();
        
        // Handle explosions
        if (mouseHeldIn && !isDrawingLine && !drawingHasBeenCancelled) {
            createPoint(prevXDrag, prevYDrag);
        }
        long random = rand.nextLong();
        rand.setSeed(System.currentTimeMillis() + random);
        
        
        Stack<int[]> antiGravStack = new Stack<>();
        
        // This boolean determines whether a material has been handled
        // so we know whether or not to make a direct copy of it from one
        // array to another.
        boolean handled = false;
        
        
        // LOGIC
        for (int y = 0; y < pixelRead[0].length;  y++) {
            //for (int x = pixel.length-1; x > -1;  x--) {
            for (int x = 0; x < pixelRead.length;  x++) {
                
                // First, deal with any and all explosions -- this MAY cause some problems with the new "handled" boolean
                if (explosionTableRead[x][y] > 0) {
                    if (ExplosionHandler.handleExplosionTable(x, y,
                            pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite,
                            explosionTableRead, explosionTableWrite, rand)) {
                        continue;
                    }
                }
                    
                    
                // Second, skip doing anything if the current pixel is nothing
                if(pixelRead[x][y] == Materials.NOTHING)
                {
                    continue;
                }
                
                // Handle fire
                if (pixelRead[x][y] >= Materials.FIRE && pixelRead[x][y] <= Materials.FIRE_ORANGE_2) { // if any instance of fire
                    FireHandler.handleFire(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);
                    continue;
                }
                
                // Handle sparkables
                if (Materials.isSparkable(pixelRead[x][y])) {
                    if (FireHandler.handleSparkable(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite)) {
                        continue;
                    }
                }
                
                // Handle burnables
                if (Materials.isBurnable(pixelRead[x][y])) {
                    if (FireHandler.handleBurnable(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite)) {
                        continue; // Continue because the burnable has become fire
                    }
                }
                
                // Handle fire negation
                if (Materials.negatesFire(pixelRead[x][y])) {
                    FireHandler.handleFireNegation(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite);
                    // Don't continue - water may need to flow
                }
                
                // Handle plant growth
                if (pixelRead[x][y] == Materials.PLANT) {
                    AliveHandler.handlePlant(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);
                    continue;
                }
                
                // Handle water spawning
                if (Materials.spawnsWater(pixelRead[x][y])) {
                    SpawnHandler.handleSpawnsWater(x, y, pixelRead, pixelWrite, rand);
                    continue;
                }
                
                // Handle explosive materials
                if (Materials.isExplosive(pixelRead[x][y])) {
                    ExplosionHandler.handleExplosive(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);
                    
                    // There's a special case to consider before continuing - gasoline flows!
                    if (!Materials.flows(pixelRead[x][y])) {
                        continue;
                    }
                }
                
                // Handle electricity
                if (Materials.isElectric(pixelRead[x][y])) {
                    ElectricityHandler.handleElectricity(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);
                    continue;
                }
                
                // Handle life seed
                if (pixelRead[x][y] == Materials.LIFE_SEED) {
                    AliveHandler.handleLifeSeed(x, y, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, rand);
                    continue;
                }
                
                if (Materials.flows(pixelRead[x][y])) {
                    FlowHandler.flow(x, y, pixelRead, pixelWrite, rand);
                    continue;
                }
                
                // Falling & Offscreen correction
                if (y == pixelRead[x].length-1) { // are we at the bottom of the screen?
                    if (Materials.falls(pixelRead[x][y])) {
                        pixelWrite[x][y] = Materials.NOTHING;
                    } else {
                        pixelWrite[x][y] = pixelRead[x][y];
                    }
                    handled = true;
                    continue;
                }
                else {
                    if (Materials.falls(pixelRead[x][y])) {
                        if (pixelRead[x][y+1] == Materials.NOTHING || Materials.canFallThrough(pixelRead[x][y+1])) {
                            pixelWrite[x][y+1] = pixelRead[x][y];
                            pixelWrite[x][y] = Materials.NOTHING;
                        }
                    }
                }
                
                if(Materials.isAntiGravity(pixelRead[x][y])) {
                    int[] currXY = {x, y};
                    antiGravStack.push(currXY);
                    continue;
                }
                
                if (pixelWrite[x][y] == 0) pixelWrite[x][y] = pixelRead[x][y];
                
            } // End row
            
            
        }
        
        if(!antiGravStack.isEmpty()) {
            while(!antiGravStack.isEmpty()) {
                int[] xy = antiGravStack.pop();
                if(xy[1] == 0) {
                    if(Materials.isAntiGravity(pixelRead[xy[0]][xy[1]])) {
                        pixelWrite[xy[0]][xy[1]] = Materials.NOTHING;
                    }
                }
                else {
                    if(Materials.isAntiGravity(pixelRead[xy[0]][xy[1]])) {
                        if (pixelRead[xy[0]][xy[1]-1] == Materials.NOTHING
                                || pixelRead[xy[0]][xy[1]-1] == Materials.ANTI_MATTER
                                ||  
                                (pixelRead[xy[0]][xy[1]-1] >= Materials.ANTI_MATTER_EXPLOSION_FLASH_1
                              &&  pixelRead[xy[0]][xy[1]-1] <= Materials.ANTI_MATTER_EXPLOSION_FLASH_8)
                                ) {
                            pixelWrite[xy[0]][xy[1]-1] = pixelRead[xy[0]][xy[1]];
                            pixelWrite[xy[0]][xy[1]] = Materials.NOTHING;
                        } else {
                            ExplosionHandler.markExplosion(xy[0], xy[1], Materials.EXPLOSION_ANTI_MATTER, pixelRead, pixelWrite, pixelAgeRead, pixelAgeWrite, explosionTableRead, explosionTableWrite);       
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
            synchronized (TICK_LOCK) {
               tick();
            }
            
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
        for (int x = 0; x < pixelRead.length; x++) {
            for (int y = 0; y < pixelRead[x].length; y++) {
                if (pixelRead[x][y] > Materials.NOTHING) {
                    g.setColor(Materials.getColor(pixelRead[x][y]));
                    g.drawLine(x, y, x, y);
                }
            }
        }
        
        if (isDrawingLine) {
            drawLine(g);
        }
        
        if (paused) {
            drawPaused(g);
        }
        
        //repaint();
    }
    
    private void drawLine(Graphics g) {
        Color cmColor  = Materials.getColor(currentMaterial);
        g.setColor(new Color(cmColor.getRed(), cmColor.getGreen(),
                cmColor.getBlue(), 128));
        if (cursorSize == CURSOR_SIZE_SMALL) {
            g.drawLine(drawStartX, drawStartY, prevXDrag, prevYDrag);
        } else if (cursorSize == CURSOR_SIZE_MEDIUM) {
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    if ((Math.abs(x) + Math.abs(y)) <= 2) {
                        g.drawLine(drawStartX + x, drawStartY + y,
                                prevXDrag + x, prevYDrag + y);
                    }
                }
            }
        }
    }
    
    private void drawPaused(Graphics g) {
        g.setFont(new Font("Verdana", Font.PLAIN, 24));
        g.setColor(Color.decode("#696969"));
        g.drawString("PAUSED", 0, 20);
        g.setColor(Color.BLACK);
        g.drawString("PAUSED", 1, 21);
        g.setColor(Color.RED);
        g.drawString("PAUSED", 2, 22);
    }
    
    public int[][] getPixelArray() {
        return pixelRead;
    }
    
}
