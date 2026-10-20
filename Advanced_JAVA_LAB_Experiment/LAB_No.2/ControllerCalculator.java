import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerCalculator implements ActionListener {
    ViewsCalculator vc;
    ModelsCalculator mc;

    public ControllerCalculator(ViewsCalculator vc, ModelsCalculator mc) {
        this.mc = mc;
        this.vc = vc;

        this.vc.add.addActionListener(this);
        this.vc.sub.addActionListener(this);
        this.vc.mul.addActionListener(this);
        this.vc.div.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double a = Double.parseDouble(vc.txta.getText());
            double b = Double.parseDouble(vc.txtb.getText());
            double res = 0;

            if (e.getSource() == vc.add) {
                res = mc.add(a, b);
            } else if (e.getSource() == vc.sub) {
                res = mc.sub(a, b);
            } else if (e.getSource() == vc.mul) {
                res = mc.mul(a, b);
            } else if (e.getSource() == vc.div) {
                res = mc.div(a, b);
            }

            vc.txtresult.setText(String.valueOf(res));
        } catch (NumberFormatException ex) {
            vc.txtresult.setText("Invalid Input");
        } catch (ArithmeticException ex) {
            vc.txtresult.setText(ex.getMessage());
        }
    }
}