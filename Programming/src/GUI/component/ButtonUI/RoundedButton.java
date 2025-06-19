package GUI.component.ButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

// radius hover
public class RoundedButton extends JButton {
    private int radius;
    @SuppressWarnings("unused")
    private Color hoverColor;
    @SuppressWarnings("unused")
    private Color pressedColor;
    private Color originalColor;

    public RoundedButton(String text, int radius, Color normalColor, Color hoverColor, Color pressedColor) {
        super(text);
        this.radius = radius;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;
        this.originalColor = normalColor;

        setContentAreaFilled(false);
        setOpaque(false);
        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalColor);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nút với bo góc
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        // Vẽ text
        super.paintComponent(g);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Không vẽ border để tránh bị chồng chéo
    }
}
