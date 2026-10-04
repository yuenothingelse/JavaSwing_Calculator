import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SwingCalculator extends JFrame {

    private final JTextField display = new JTextField("0");
    private String currentInput = "0";
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    private final Color BG = Color.BLACK;
    private final Color NUMBER = new Color(48, 48, 48);
    private final Color FUNCTION = new Color(105, 105, 105);
    private final Color ORANGE = new Color(255, 149, 0);
    private final Color TEXT = new Color(245, 245, 245);

    public SwingCalculator() {
        setTitle("Kalkulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(25, 35, 30, 35));

        // display panel
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);

        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("SansSerif", Font.PLAIN, 64));
        display.setForeground(TEXT);
        display.setBackground(BG);
        display.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        display.setCaretColor(BG);
        top.add(display, BorderLayout.CENTER);

        // menu button 
        JButton menuButton = createCircleButton("⚙", 58, FUNCTION);
        menuButton.setToolTipText("Mode kalkulator");
        menuButton.addActionListener(e -> showModeMenu(menuButton));

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        menuPanel.setOpaque(false);
        menuPanel.add(menuButton);
        top.add(menuPanel, BorderLayout.WEST);

        root.add(top, BorderLayout.NORTH);

        // button grid
        JPanel grid = new JPanel(new GridLayout(4, 5, 12, 12));
        grid.setBackground(BG);

        addButton(grid, "7", NUMBER, e -> number("7"));
        addButton(grid, "8", NUMBER, e -> number("8"));
        addButton(grid, "9", NUMBER, e -> number("9"));
        addButton(grid, "÷", FUNCTION, e -> setOperator("/"));
        addButton(grid, "×", ORANGE, e -> setOperator("*"));

        addButton(grid, "4", NUMBER, e -> number("4"));
        addButton(grid, "5", NUMBER, e -> number("5"));
        addButton(grid, "6", NUMBER, e -> number("6"));
        addButton(grid, "AC", FUNCTION, e -> clear());
        addButton(grid, "−", ORANGE, e -> setOperator("-"));

        addButton(grid, "1", NUMBER, e -> number("1"));
        addButton(grid, "2", NUMBER, e -> number("2"));
        addButton(grid, "3", NUMBER, e -> number("3"));
        addButton(grid, "%", FUNCTION, e -> setOperator("%"));
        addButton(grid, "+", ORANGE, e -> setOperator("+"));

        addButton(grid, "+/−", NUMBER, e -> toggleSign());
        addButton(grid, "0", NUMBER, e -> number("0"));
        addButton(grid, ",", NUMBER, e -> decimal());
        addButton(grid, "=", ORANGE, e -> calculate());
        addButton(grid, "⌫", ORANGE, e -> backspace());

        root.add(grid, BorderLayout.CENTER);
        setContentPane(root);

        // keyboard support
        setupKeyboard();
    }

    private void addButton(JPanel panel, String text, Color color, ActionListener listener) {
        RoundedButton button = new RoundedButton(text, color);
        button.addActionListener(listener);
        panel.add(button);
    }

    private JButton createCircleButton(String text, int size, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(size, size));
        button.setFont(new Font("SansSerif", Font.PLAIN, 30));
        button.setForeground(TEXT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    private void showModeMenu(Component parent) {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(new Color(30, 28, 25));

        JMenuItem basic = createMenuItem("✓   Dasar", true);
        JMenuItem scientific = createMenuItem("f(x)   Ilmiah", false);
        JMenuItem mathNotes = createMenuItem("Y  X   Catatan Matematika", false);
        JMenuItem conversion = createMenuItem("↔   Konversi", false);

        basic.addActionListener(e -> {});
        scientific.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Mode Ilmiah belum diaktifkan.",
                        "Ilmiah", JOptionPane.INFORMATION_MESSAGE));
        mathNotes.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Catatan Matematika belum diaktifkan.",
                        "Catatan Matematika", JOptionPane.INFORMATION_MESSAGE));
        conversion.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Konversi belum diaktifkan.",
                        "Konversi", JOptionPane.INFORMATION_MESSAGE));

        popup.add(basic);
        popup.add(scientific);
        popup.add(mathNotes);
        popup.addSeparator();
        popup.add(conversion);

        popup.show(parent, parent.getWidth() + 5, -5);
    }

    private JMenuItem createMenuItem(String text, boolean selected) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("SansSerif", Font.PLAIN, 18));
        item.setForeground(TEXT);
        item.setBackground(new Color(30, 28, 25));
        item.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 25));
        return item;
    }

    // logic methods

    private void number(String n) {
        if (startNewNumber || currentInput.equals("0")) {
            currentInput = n;
            startNewNumber = false;
        } else {
            currentInput += n;
        }
        display.setText(currentInput);
    }

    private void decimal() {
        if (startNewNumber) {
            currentInput = "0,";
            startNewNumber = false;
        } else if (!currentInput.contains(",")) {
            currentInput += ",";
        }
        display.setText(currentInput);
    }

    private void setOperator(String op) {
        try {
            if (!operator.isEmpty() && !startNewNumber) {
                calculate();
            }
            firstNumber = parse(currentInput);
            operator = op;
            startNewNumber = true;
        } catch (Exception ignored) {}
    }

    private void calculate() {
        if (operator.isEmpty()) return;

        try {
            double secondNumber = parse(currentInput);
            double result;

            switch (operator) {
                case "+" -> result = firstNumber + secondNumber;
                case "-" -> result = firstNumber - secondNumber;
                case "*" -> result = firstNumber * secondNumber;
                case "/" -> {
                    if (secondNumber == 0) {
                        display.setText("Tidak terdefinisi");
                        operator = "";
                        startNewNumber = true;
                        return;
                    }
                    result = firstNumber / secondNumber;
                }
                case "%" -> result = firstNumber * secondNumber / 100.0;
                default -> { return; }
            }

            currentInput = format(result);
            display.setText(currentInput);
            operator = "";
            startNewNumber = true;

        } catch (Exception ignored) {}
    }

    private void clear() {
        currentInput = "0";
        firstNumber = 0;
        operator = "";
        startNewNumber = true;
        display.setText("0");
    }


    private void toggleSign() {
        try {
            double value = -parse(currentInput);
            currentInput = format(value);
            display.setText(currentInput);
        } catch (Exception ignored) {}
    }

    private void backspace() {
        if (startNewNumber) return;

        if (currentInput.length() <= 1) {
            currentInput = "0";
            startNewNumber = true;
        } else {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }

        display.setText(currentInput);
    }

    private double parse(String value) {
        return Double.parseDouble(value.replace(',', '.'));
    }

    private String format(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }

        String result = String.format(java.util.Locale.US, "%.10f", value)
                .replaceAll("0+$", "")
                .replace(".", ",");

        return result;
    }

    // keyboard

    private void setupKeyboard() {
        JRootPane pane = getRootPane();

        pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("ENTER"), "equals");
        pane.getActionMap().put("equals", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });

        for (int i = 0; i <= 9; i++) {
            final String n = String.valueOf(i);
            pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(n), "number" + n);
            pane.getActionMap().put("number" + n, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    number(n);
                }
            });
        }
    }

    // rounded button
    static class RoundedButton extends JButton {
        private final Color color;

        RoundedButton(String text, Color color) {
            super(text);
            this.color = color;

            setFont(new Font("SansSerif", Font.PLAIN, 28));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color paintColor = color;

            if (getModel().isPressed()) {
                paintColor = color.darker();
            } else if (getModel().isRollover()) {
                paintColor = color.brighter();
            }

            g2.setColor(paintColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 55, 55);

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}

            new SwingCalculator().setVisible(true);
        });
    }
}
