/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package pyro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Julian
 */

// JFrame or JApplet
public class Pyro extends JFrame {
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 7;
    public static final int VERSION_REVISION = 3;
    public static final char VERSION_TAG = 'A';
    
    GamePanel panel;
    
    public Pyro() {
        panel = new GamePanel();
        
        
        JMenuBar menuBar = setupMenuBar();
        
        
        this.setJMenuBar(menuBar);
        this.add(panel);
        
        
        this.setTitle("Pyro v" + VERSION_MAJOR + '.' + VERSION_MINOR + '.'
                + VERSION_REVISION + VERSION_TAG + " by Julian Monticelli");
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
        JMenu help = new JMenu("Help");
        jmb.add(file);
        jmb.add(options);
        jmb.add(materials);
        jmb.add(help);
        
        
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
        
        JMenu skybox = new JMenu("Skybox");
        options.add(skybox);
        JMenuItem skyboxBlack = new JMenuItem("Black");
        skybox.add(skyboxBlack);
        skyboxBlack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(Color.black);
            }
        });
        JMenuItem skyboxSkyBlue = new JMenuItem("Sky Blue");
        skybox.add(skyboxSkyBlue);
        skyboxSkyBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(Color.decode("#77abff"));
            }
        });
        
        
        
        // Materials Menus
        JMenu standard = new JMenu("Standard");
        JMenu living = new JMenu("Living");
        JMenu flammables = new JMenu("Flammables");
        JMenu explosives = new JMenu("Explosives");
        JMenu entities = new JMenu("Entities");
        JMenu special = new JMenu("Special");
        
        materials.add(standard);
        materials.add(living);
        materials.add(flammables);
        materials.add(explosives);
        materials.add(entities);        
        materials.add(special);
        
        // Materials
        JMenuItem concrete = new JMenuItem("Concrete (N)");
        standard.add(concrete);
        concrete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.CONCRETE);
            }
        });
        
        JMenuItem sand = new JMenuItem("Sand (S)");
        standard.add(sand);
        sand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.SAND);
            }
        });
        JMenuItem water = new JMenuItem("Water (W)");
        standard.add(water);
        water.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.WATER);
            }
        });
        JMenuItem wood = new JMenuItem("Wood (D)");
        standard.add(wood);
        wood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.WOOD);
            }
        });
        JMenuItem rock = new JMenuItem("Rock (R)");
        standard.add(rock);
        rock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.ROCK);
            }
        });
        JMenuItem fire = new JMenuItem("Fire (F)");
        flammables.add(fire);
        fire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.FIRE);
            }
        });
        JMenuItem electricity = new JMenuItem("Electricity (E)");
        flammables.add(electricity);
        electricity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.ELECTRICITY);
            }
        });
        JMenuItem plant = new JMenuItem("Plant (P)");
        living.add(plant);
        plant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.PLANT);
            }
        });
        JMenuItem oil = new JMenuItem("Oil (O)");
        flammables.add(oil);
        oil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.OIL);
            }
        });
        JMenuItem gas = new JMenuItem("Gasoline (G)");
        flammables.add(gas);
        gas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.GASOLINE);
            }
        });
        JMenuItem lava = new JMenuItem("Lava (L)");
        flammables.add(lava);
        lava.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.LAVA);
            }
        });
        JMenuItem cloud = new JMenuItem("Rain Cloud (1)");
        entities.add(cloud);
        cloud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.RAIN_CLOUD);
            }
        });
        JMenuItem fuse = new JMenuItem("Fuse (U)");
        explosives.add(fuse);
        fuse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.FUSE);
            }
        });
        JMenuItem gunpowder = new JMenuItem("Gunpowder (X)");
        explosives.add(gunpowder);
        gunpowder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.GUNPOWDER);
            }
        });
        JMenuItem tnt = new JMenuItem("TNT (T)");
        explosives.add(tnt);
        tnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.TNT);
            }
        });
        JMenuItem c4 = new JMenuItem("C4 (C)");
        explosives.add(c4);
        c4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.C4);
            }
        });
        JMenuItem nitro = new JMenuItem("Nitroglycerin (Y)");
        explosives.add(nitro);
        nitro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.NITROGLYCERIN);
            }
        });
        JMenuItem life = new JMenuItem("Life Seed (I)");
        living.add(life);
        life.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.LIFE_SEED);
            }
        });
        JMenuItem antimatter = new JMenuItem("Anti Matter (A)");
        special.add(antimatter);
        antimatter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setMaterial(Materials.ANTI_MATTER);
            }
        });
        
        // Help
        
        JMenuItem controls = new JMenuItem("Controls");
        help.add(controls);
        controls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame helpFrame = new JFrame();
                helpFrame.setSize(350, 350);
                
                helpFrame.setTitle("Help");
                
                JLabel helpLabel = new JLabel();
                helpLabel.setText("Help");
                
                JTextArea helpText = new JTextArea();
                helpText.setPreferredSize(new Dimension(350, 350));
                helpText.setEditable(false);
                helpText.setText(
                
                "Pyro is a simple game, where you can build and destroy\n" +
                "scenes that you make with various materials. Every material\n" +
                "has a hotkey as listed in the materials menu. \n\n" +
                "It is possible to draw lines by holding in CTRL before\n" +
                "dragging a material. Upon releasing a line, it will be\n" +
                "created. To cancel the creation of a line, one can release\n" +
                "control and hit escape before releasing the mouse.\n\n" +
                "The game can be paused and unpaused by hitting space\n"
                
                );
                
                helpFrame.add(helpLabel);
                helpFrame.add(helpText);
                helpFrame.setVisible(true);       
                helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        
        return jmb;
    }
    
}
