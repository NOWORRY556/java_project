import javax.swing.*;

public class ViewsCalculator extends JFrame {
    JLabel a, b, result;
    JTextField txta, txtb, txtresult;
    JButton add, sub, mul, div;

    public ViewsCalculator() {
        setTitle("Calculator");
        setSize(400, 300);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        a = new JLabel("Number 1:");
        a.setBounds(30, 30, 100, 25);
        add(a);

        txta = new JTextField();
        txta.setBounds(140, 30, 180, 25);
        add(txta);

        b = new JLabel("Number 2:");
        b.setBounds(30, 70, 100, 25);
        add(b);

        txtb = new JTextField();
        txtb.setBounds(140, 70, 180, 25);
        add(txtb);

        add = new JButton("+");
        add.setBounds(30, 120, 60, 30);
        add(add);

        sub = new JButton("-");
        sub.setBounds(100, 120, 60, 30);
        add(sub);

        mul = new JButton("*");
        mul.setBounds(170, 120, 60, 30);
        add(mul);

        div = new JButton("/");
        div.setBounds(240, 120, 60, 30);
        add(div);

        result = new JLabel("Result:");
        result.setBounds(30, 170, 100, 25);
        add(result);

        txtresult = new JTextField();
        txtresult.setBounds(140, 170, 180, 25);
        txtresult.setEditable(false);
        add(txtresult);

        setVisible(true);
    }
}