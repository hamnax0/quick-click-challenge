import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NewGame {
    private static final int GAME_DURATION_MS = 60000;
    private static int clickCount = 0;
    private static final Random random = new Random();
    private static Timer moveTimer;
    private static Timer gameTimer;
    private static boolean showStartMessage = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Speed Tap Game");
        frame.setSize(1250, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Left menu panel
        JPanel menu = new JPanel();
        menu.setBounds(0, 0, 200, 650);
        menu.setBackground(Color.LIGHT_GRAY);
        menu.setLayout(null);

        JLabel title = new JLabel("Speed Tap");
        title.setBounds(25, 30, 150, 50);
        title.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 25));
        menu.add(title);

        JButton btn1 = new JButton("Easy Level");
        JButton btn2 = new JButton("Medium Level");
        JButton btn3 = new JButton("Hard Level");
        JButton btn4 = new JButton("Score");

        btn1.setBounds(25, 100, 125, 30);
        btn2.setBounds(25, 150, 125, 30);
        btn3.setBounds(25, 200, 125, 30);
        btn4.setBounds(25, 300, 125, 30);

        menu.add(btn1);
        menu.add(btn2);
        menu.add(btn3);
        menu.add(btn4);

        JLabel scoreLabel = new JLabel("0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 35));
        scoreLabel.setBounds(25, 350, 125, 50);
        menu.add(scoreLabel);

        // Right game panel with custom paint
        JPanel panel1 = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (showStartMessage) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("SansSerif", Font.BOLD, 36));
                    g2d.drawString("I bet you can't click me", 250, 320);
                }
            }
        };
        panel1.setBounds(200, 0, 1050, 650);
        panel1.setBackground(Color.BLACK);

        JButton clickButton = new JButton("Click Me");
        clickButton.setBounds(100, 100, 125, 60);
        clickButton.setBackground(Color.ORANGE);
        clickButton.setFont(new Font("Georgia", Font.BOLD, 20));
        clickButton.setVisible(false);
        panel1.add(clickButton);

        frame.add(menu);
        frame.add(panel1);
        frame.setVisible(true);

        // Start game logic
        ActionListener startGame = (ActionEvent e) -> {
            int delay;
            if (e.getSource() == btn1) {
                delay = 1000;
            } else if (e.getSource() == btn2) {
                delay = 750;
            } else {
                delay = 500;
            }

            clickCount = 0;
            scoreLabel.setText("0");
            clickButton.setVisible(true);
            clickButton.setEnabled(true);
            showStartMessage = true;
            panel1.repaint();

            if (moveTimer != null) moveTimer.stop();
            if (gameTimer != null) gameTimer.stop();

            moveTimer = new Timer(delay, ev -> moveButton(clickButton, panel1));
            moveTimer.start();

            for (ActionListener al : clickButton.getActionListeners()) {
                clickButton.removeActionListener(al); 
            }

            clickButton.addActionListener(ev -> {
                clickCount++;
                scoreLabel.setText(String.valueOf(clickCount));
                moveButton(clickButton, panel1);
            });

            gameTimer = new Timer(GAME_DURATION_MS, ev -> {
                moveTimer.stop();
                clickButton.setEnabled(false);
                showStartMessage = false;
                panel1.repaint();
                JOptionPane.showMessageDialog(frame, "Time's up! Your score: " + clickCount, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            });
            gameTimer.setRepeats(false);
            gameTimer.start();
        };

        btn1.addActionListener(startGame);
        btn2.addActionListener(startGame);
        btn3.addActionListener(startGame);

        btn4.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Current Score: " + clickCount, "Score", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static void moveButton(JButton button, JPanel panel) {
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();
        int maxX = panel.getWidth() - buttonWidth;
        int maxY = panel.getHeight() - buttonHeight;

        int x = random.nextInt(Math.max(maxX, 1));
        int y = random.nextInt(Math.max(maxY, 1));
        button.setLocation(x, y);
    }
}

