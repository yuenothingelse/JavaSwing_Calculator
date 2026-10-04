import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingCalculator extends JFrame {

    private final JTextField display = new JTextField("0");

    public SwingCalculator() {
        setTitle("Kalkulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(Color.BLACK);
        root.setBorder(
            BorderFactory.createEmptyBorder(25, 35, 30, 35)
        );

        setContentPane(root);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SwingCalculator().setVisible(true);
        });
    }
}
