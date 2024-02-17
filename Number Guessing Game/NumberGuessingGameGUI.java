import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {
    private final Random random = new Random();
    private int targetNumber;
    private int maxAttempts = 5;
    private int attempts = 0;
    private int totalScore = 0;

    private JLabel backgroundLabel; // Background image label
    private JLabel imageLabel; // Small image label
    private JLabel promptLabel;
    private JTextField guessTextField;
    private JButton guessButton;
    private JLabel totalScoreLabel;
    private JLabel attemptsLabel;

    // Constructor
    public NumberGuessingGameGUI() {
        setTitle("Number Guessing Game");
        setSize(800, 600);  // Set window size to 800x600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Add background image label
        backgroundLabel = new JLabel(new ImageIcon("background.jpg"));
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        // Add small image label with FlowLayout to center it
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setOpaque(false); // Make the small image panel transparent
        imageLabel = new JLabel();
        imagePanel.add(imageLabel);
        backgroundLabel.add(imagePanel, BorderLayout.NORTH);

        try {
            // Read the image and set it to the imageLabel
            Image image = ImageIO.read(new File("image.png"));
            Image scaledImage = image.getScaledInstance(400, 150, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a panel to hold components using GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Make panel transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Labels, Text Field, Button, Score Label
        promptLabel = new JLabel("Guess the number between 1 and 100:");
        guessTextField = new JTextField(20);
        guessButton = new JButton("Guess");
        totalScoreLabel = new JLabel("Total Score: 0");
        attemptsLabel = new JLabel("Total Attempts: 5");

        // Increase font size for components
        Font bigFont = new Font("Comic Sans MS", Font.PLAIN, 20);
        promptLabel.setFont(bigFont);
        guessTextField.setFont(bigFont);
        guessButton.setFont(bigFont);
        totalScoreLabel.setFont(bigFont);
        attemptsLabel.setFont(bigFont);

        // ActionListener for the Guess Button
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });

        // Add components to the panel using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(promptLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(guessTextField, gbc);

        gbc.gridy = 2;
        mainPanel.add(guessButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(totalScoreLabel, gbc);

        gbc.gridy = 4;
        mainPanel.add(attemptsLabel, gbc);

        // Add the panel to the content pane
        backgroundLabel.add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        setupGame();
    }

    // Setup the game by initializing variables
    private void setupGame() {
        targetNumber = random.nextInt(100) + 1;
        attempts = 0;
        promptLabel.setText("Guess the number between 1 and 100:");
        guessTextField.setText("");
        guessButton.setEnabled(true);
    }

    // Check user's guess against the target number
    private void checkGuess() {
        attempts++;

        try {
            int userGuess = Integer.parseInt(guessTextField.getText());

            if (userGuess == targetNumber) {
                // User guessed correctly
                JOptionPane.showMessageDialog(this,
                        "Congratulations! You guessed the correct number in " + attempts + " attempts.");
                int roundScore = maxAttempts - attempts + 1; // Assigning points based on attempts
                totalScore += roundScore;
                totalScoreLabel.setText("Total Score: " + totalScore); // Update total score
                JOptionPane.showMessageDialog(this, "Round Score: " + roundScore + " | Total Score: " + totalScore);
                setupGame();
            } else {
                if (attempts == maxAttempts) {
                    // User ran out of attempts
                    JOptionPane.showMessageDialog(this,
                            "Sorry, you've run out of attempts. The correct number was: " + targetNumber);
                    endRound();
                } else {
                    // Incorrect guess, provide a hint
                    if (userGuess < targetNumber) {
                        JOptionPane.showMessageDialog(this,
                                "Try again! The number is higher. Remaining attempts: " + (maxAttempts - attempts));
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Try again! The number is lower. Remaining attempts: " + (maxAttempts - attempts));
                    }
                    // Clear the text box after displaying the dialog
                    guessTextField.setText("");
                }
            }
        } catch (NumberFormatException ex) {
            // Invalid input
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    // End the current round
    private void endRound() {
        JOptionPane.showMessageDialog(this, "End of Round. Your Total Score: " + totalScore);
        setupGame(); // Start a new round
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NumberGuessingGameGUI();
            }
        });
    }
}