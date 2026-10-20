package com.shopsphere.desktop;
import javax.swing.*; import java.awt.*;
public class AdminDesktopApp {
 public static void main(String[] args){SwingUtilities.invokeLater(()->{JFrame f=new JFrame("ShopSphere Admin");f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);f.setSize(600,400);JPanel p=new JPanel(new BorderLayout());p.add(new JLabel("ShopSphere Desktop Admin Console",SwingConstants.CENTER),BorderLayout.NORTH);JTextArea log=new JTextArea("Desktop administration module ready.\nConnect DAO operations here.");p.add(new JScrollPane(log),BorderLayout.CENTER);f.add(p);f.setLocationRelativeTo(null);f.setVisible(true);});}
}