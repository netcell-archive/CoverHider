/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author tebaomang
 */
public class Launcher {
    
    private static String AppName = "CoverHider";
    
    public static void main(String args[]) {
        try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppName);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(ClassNotFoundException e) {
                    System.out.println("ClassNotFoundException: " + e.getMessage());
            }
            catch(InstantiationException e) {
                    System.out.println("InstantiationException: " + e.getMessage());
            }
            catch(IllegalAccessException e) {
                    System.out.println("IllegalAccessException: " + e.getMessage());
            }
            catch(UnsupportedLookAndFeelException e) {
                    System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Embeder().setVisible(true);
            }
        });
    }
    
    public static void embe(){
        try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppName);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(ClassNotFoundException e) {
                    System.out.println("ClassNotFoundException: " + e.getMessage());
            }
            catch(InstantiationException e) {
                    System.out.println("InstantiationException: " + e.getMessage());
            }
            catch(IllegalAccessException e) {
                    System.out.println("IllegalAccessException: " + e.getMessage());
            }
            catch(UnsupportedLookAndFeelException e) {
                    System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Embeder().setVisible(true);
            }
        });
    }
    
    public static void psnr(){
        try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppName);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(ClassNotFoundException e) {
                    System.out.println("ClassNotFoundException: " + e.getMessage());
            }
            catch(InstantiationException e) {
                    System.out.println("InstantiationException: " + e.getMessage());
            }
            catch(IllegalAccessException e) {
                    System.out.println("IllegalAccessException: " + e.getMessage());
            }
            catch(UnsupportedLookAndFeelException e) {
                    System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PSNR().setVisible(true);
            }
        });
    }
    
    public static void extract(){
        try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppName);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(ClassNotFoundException e) {
                    System.out.println("ClassNotFoundException: " + e.getMessage());
            }
            catch(InstantiationException e) {
                    System.out.println("InstantiationException: " + e.getMessage());
            }
            catch(IllegalAccessException e) {
                    System.out.println("IllegalAccessException: " + e.getMessage());
            }
            catch(UnsupportedLookAndFeelException e) {
                    System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Extracter().setVisible(true);
            }
        });
    }
    
    public static void psnrembe(){
        try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppName);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(ClassNotFoundException e) {
                    System.out.println("ClassNotFoundException: " + e.getMessage());
            }
            catch(InstantiationException e) {
                    System.out.println("InstantiationException: " + e.getMessage());
            }
            catch(IllegalAccessException e) {
                    System.out.println("IllegalAccessException: " + e.getMessage());
            }
            catch(UnsupportedLookAndFeelException e) {
                    System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PSNREmber().setVisible(true);
            }
        });
    }
}
