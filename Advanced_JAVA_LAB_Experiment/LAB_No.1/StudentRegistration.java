import javax.swing.*;
import java.awt.event.*;

public class StudentRegistration extends JFrame implements ActionListener {
    // Components Declare
    JLabel labelName, labelRoll, labelGender, labelBranch;
    JTextField textName, textRoll, textBranch;
    JRadioButton radioMale, radioFemale;
    ButtonGroup genderGroup;
    JCheckBox checkTerms;
    JButton btnSubmit, btnReset;

    // Constructor
    public StudentRegistration() {
        setTitle("Student Registration Form");
        setSize(400, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Name
        labelName = new JLabel("Name:");
        labelName.setBounds(50, 30, 100, 30);
        add(labelName);

        textName = new JTextField();
        textName.setBounds(150, 30, 180, 30);
        add(textName);

        // Roll Number
        labelRoll = new JLabel("Roll No:");
        labelRoll.setBounds(50, 80, 100, 30);
        add(labelRoll);

        textRoll = new JTextField();
        textRoll.setBounds(150, 80, 180, 30);
        add(textRoll);

        // Gender
        labelGender = new JLabel("Gender:");
        labelGender.setBounds(50, 130, 100, 30);
        add(labelGender);

        radioMale = new JRadioButton("Male");
        radioMale.setBounds(150, 130, 80, 30);
        radioFemale = new JRadioButton("Female");
        radioFemale.setBounds(230, 130, 80, 30);

        genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);

        add(radioMale);
        add(radioFemale);

        // Branch
        labelBranch = new JLabel("Branch:");
        labelBranch.setBounds(50, 180, 100, 30);
        add(labelBranch);

        textBranch = new JTextField();
        textBranch.setBounds(150, 180, 180, 30);
        add(textBranch);

        // Terms & Conditions
        checkTerms = new JCheckBox("Accept Terms & Conditions");
        checkTerms.setBounds(50, 230, 250, 30);
        add(checkTerms);

        // Buttons
        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(50, 280, 100, 30);
        btnSubmit.addActionListener(this);
        add(btnSubmit);

        btnReset = new JButton("Reset");
        btnReset.setBounds(180, 280, 100, 30);
        btnReset.addActionListener(this);
        add(btnReset);

        setVisible(true);
    }

    // Action Logic
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnReset) {
            textName.setText("");
            textRoll.setText("");
            textBranch.setText("");
            genderGroup.clearSelection();
            checkTerms.setSelected(false);
            return;
        }

        if (e.getSource() == btnSubmit) {
            // Validations
            if (textName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Name!");
                return;
            }
            if (textRoll.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Roll Number!");
                return;
            }
            if (!radioMale.isSelected() && !radioFemale.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please select Gender!");
                return;
            }
            if (textBranch.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Branch!");
                return;
            }
            if (!checkTerms.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please accept Terms & Conditions!");
                return;
            }

            // Success Output
            String gender = radioMale.isSelected() ? "Male" : "Female";
            String msg = "Registration Successful!\n" +
                    "Name: " + textName.getText() + "\n" +
                    "Roll No: " + textRoll.getText() + "\n" +
                    "Gender: " + gender + "\n" +
                    "Branch: " + textBranch.getText();
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    public static void main(String[] args) {
        new StudentRegistration();
    }
}