/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package pyro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
// JFrame or JApplet
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Julian
 */

// JFrame or JApplet
public class Pyro extends JFrame {
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 70;
    public static final char VERSION_TAG = 'A';
    
    GamePanel panel;
    
    public Pyro() {
        panel = new GamePanel();
        //this.setPreferredSize(new Dimension(400,400));
        
        
        JMenuBar menuBar = setupMenuBar();
        
        
        this.setJMenuBar(menuBar);
        this.add(panel);
        
        
        this.setTitle("Pyro v" + VERSION_MAJOR + "." + VERSION_MINOR + VERSION_TAG + " by Julian Monticelli");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //this.setLayout(new BorderLayout());
        
        this.pack();
        
        //this.setResizable(false);
        
        
        this.setVisible(true);
    }
    
    //if JApplet add Override
    //@Override
    // Otherwise no @Override
    public void start() {
        panel.init();
    }
    
    public static void main(String[] args) {
        Pyro game = new Pyro();
        game.start();
    }
    
    public JMenuBar setupMenuBar() {
        JMenuBar jmb = new JMenuBar();
        
        JMenu materials = new JMenu("Materials");
        JMenu file = new JMenu("File");
        JMenu options = new JMenu("Options");
        jmb.add(file);
        jmb.add(options);
        jmb.add(materials);
        
        
        
        JMenuItem clear = new JMenuItem("Clear");
        file.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.clear();
            }
        });
        
        // Options
        
        JMenu cursorSize = new JMenu("Cursor Size");
        options.add(cursorSize);
        JMenuItem small = new JMenuItem("Small");
        small.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setCursorSize(GamePanel.CURSOR_SIZE_SMALL);
            }
        });
        cursorSize.add(small);
        JMenuItem medium = new JMenuItem("Medium");
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setCursorSize(GamePanel.CURSOR_SIZE_MEDIUM);
            }
        });
        cursorSize.add(medium);
        
        // Materials
        
        JMenuItem concrete = new JMenuItem("Concrete (N)");
        materials.add(concrete);
        concrete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.CONCRETE);
            }
        });
        JMenuItem sand = new JMenuItem("Sand (S)");
        materials.add(sand);
        sand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.SAND);
            }
        });
        JMenuItem water = new JMenuItem("Water (W)");
        materials.add(water);
        water.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.WATER);
            }
        });
        JMenuItem wood = new JMenuItem("Wood (D)");
        materials.add(wood);
        wood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.WOOD);
            }
        });
        JMenuItem fire = new JMenuItem("Fire (F)");
        materials.add(fire);
        fire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.FIRE);
            }
        });
        JMenuItem electricity = new JMenuItem("Electricity (E)");
        materials.add(electricity);
        electricity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.ELECTRICITY);
            }
        });
        JMenuItem plant = new JMenuItem("Plant (P)");
        materials.add(plant);
        plant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.PLANT);
            }
        });
        JMenuItem oil = new JMenuItem("Oil (O)");
        materials.add(oil);
        oil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.OIL);
            }
        });
        JMenuItem gas = new JMenuItem("Gasoline (G)");
        materials.add(gas);
        gas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.GASOLINE);
            }
        });
        JMenuItem lava = new JMenuItem("Lava (L)");
        materials.add(lava);
        lava.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.LAVA);
            }
        });
        JMenuItem cloud = new JMenuItem("Rain Cloud (R)");
        materials.add(cloud);
        cloud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.RAIN_CLOUD);
            }
        });
        JMenuItem fuse = new JMenuItem("Fuse (U)");
        materials.add(fuse);
        fuse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.FUSE);
            }
        });
        JMenuItem gunpowder = new JMenuItem("Gunpowder (X)");
        materials.add(gunpowder);
        gunpowder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.GUNPOWDER);
            }
        });
        JMenuItem tnt = new JMenuItem("TNT (T)");
        materials.add(tnt);
        tnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.TNT);
            }
        });
        JMenuItem c4 = new JMenuItem("C4 (C)");
        materials.add(c4);
        c4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.C4);
            }
        });
        JMenuItem nitro = new JMenuItem("Nitroglycerin (Y)");
        materials.add(nitro);
        nitro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.NITROGLYCERIN);
            }
        });
        JMenuItem life = new JMenuItem("Life Seed (I)");
        materials.add(life);
        life.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.LIFE_SEED);
            }
        });
        JMenuItem antimatter = new JMenuItem("Anti Matter (A)");
        materials.add(antimatter);
        antimatter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.ANTI_MATTER);
            }
        });
        return jmb;
    }
    
}
