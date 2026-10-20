package com.shopsphere.desktop;

import javax.swing.*;
import java.awt.*;

public final class SwingAdminMvcDemo {
    private SwingAdminMvcDemo() {}
    static final class Model {
        private String status="Ready";
        String getStatus(){return status;}
        void setStatus(String value){status=value;}
    }
    static final class View extends JFrame {
        final JLabel status=new JLabel();
        final JButton products=new JButton("Products");
        final JButton customers=new JButton("Customers");
        final JButton orders=new JButton("Orders");
        View(){
            super("ShopSphere Swing MVC Admin");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(560,220);
            JPanel buttons=new JPanel(new FlowLayout());
            buttons.add(products); buttons.add(customers); buttons.add(orders);
            add(buttons,BorderLayout.CENTER);
            add(status,BorderLayout.SOUTH);
        }
    }
    static final class Controller {
        private final Model model; private final View view;
        Controller(Model model,View view){
            this.model=model; this.view=view;
            view.products.addActionListener(e->setStatus("ProductFrame action"));
            view.customers.addActionListener(e->setStatus("CustomerFrame action"));
            view.orders.addActionListener(e->setStatus("OrderFrame action"));
        }
        private void setStatus(String value){model.setStatus(value);view.status.setText(model.getStatus());}
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}
            Model model=new Model(); View view=new View(); new Controller(model,view); view.setLocationRelativeTo(null); view.setVisible(true);
        });
    }
}
