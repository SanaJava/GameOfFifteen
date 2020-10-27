
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class SlidePuzzleGUI extends JFrame {

    JPanel controlPanel = new JPanel();
    JPanel puzzleGraphics = new JPanel();
    JPanel buttonPanel = new JPanel();

    JButton newGame = new JButton("Shuffle!");
    JButton solveGame = new JButton("Solve Game");
    JButton[][] puzzleButtons = new JButton[4][4];

    Font fontSize = new Font("Chalkduster", Font.BOLD, 60);
    int counter = 1;
    int emptyRow = 3;
    int emptyColumn = 3;

    SlidePuzzleGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        controlPanel.add(puzzleGraphics);
        controlPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout());

        puzzleGraphics.setLayout(new GridLayout(4, 4));

        buttonPanel.add(newGame);
        buttonPanel.add(solveGame);
        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(puzzleGraphics, BorderLayout.CENTER);
        setTitle("\u00A9 All rights reserved, made by Santana-Boukchana \u00AE");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(controlPanel);
        setSize(500, 500);
        newGame.addActionListener(shuffleForNewGame);
        solveGame.addActionListener(solvePuzzle);

        setVisible(true);

        for (int i = 0; i < puzzleButtons.length; i++)
            for (int j = 0; j < puzzleButtons[i].length; j++) {

                JButton button = new JButton("" + counter++);

                button.setForeground(Color.BLACK);
                button.setFont(fontSize);
                button.setBackground(Color.LIGHT_GRAY);

                button.addActionListener(new TileActionListener(i, j));
                puzzleGraphics.add(button);
                puzzleButtons[i][j] = button;

            }
        puzzleButtons[3][3].setText("");
        puzzleButtons[3][3].setBackground(Color.WHITE);
        puzzleButtons[3][3].setOpaque(true);
        shuffle(puzzleButtons);

        pack();
    }

    private void shuffle(JButton[][] puzzleButtons) {
        Random random = new Random();

        for (int i = puzzleButtons.length - 1; i > 0; i--) {
            for (int j = puzzleButtons[i].length - 1; j > 0; j--) {
                if (i == 3 && j == 3) {
                    continue;
                }
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                String temp = puzzleButtons[i][j].getText();
                puzzleButtons[i][j].setText(puzzleButtons[m][n].getText());
                puzzleButtons[m][n].setText(temp);

                if (puzzleButtons[i][j].getText().equals("")) {
                    emptyRow = i;
                    emptyColumn = j;
                } else if (temp.equals("")) {
                    emptyRow = m;
                    emptyColumn = n;
                }
            }
        }
    }

    ActionListener shuffleForNewGame = e -> {
        moveTile(3, 3);
        shuffle(puzzleButtons);
    };

    class TileActionListener implements ActionListener {
        private final int row;
        private final int column;

        TileActionListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (canTileBeMoved(row, column)) {
                moveTile(row, column);
            }
        }
    }

    public boolean canTileBeMoved(int tileRow, int tileColumn) {
        if (tileRow + 1 == emptyRow && tileColumn == emptyColumn) {
            return true;
        } else if (tileRow - 1 == emptyRow && tileColumn == emptyColumn) {
            return true;
        } else if (tileRow == emptyRow && tileColumn + 1 == emptyColumn) {
            return true;
        } else if (tileRow == emptyRow && tileColumn - 1 == emptyColumn) {
            return true;
        } else {
            return false;
        }
    }

    public void moveTile(int tileRow, int tileColumn) {
        JButton clickedTile = puzzleButtons[tileRow][tileColumn];
        JButton emptyTile = puzzleButtons[emptyRow][emptyColumn];

        emptyTile.setText(clickedTile.getText());
        emptyTile.setBackground(Color.LIGHT_GRAY);
        clickedTile.setText("");
        clickedTile.setBackground(Color.WHITE);

        emptyRow = tileRow;
        emptyColumn = tileColumn;

        if (isSolved()) {
           JOptionPane.showMessageDialog(null,
                    "CONGRATTIZ!",
                    "You solved the game", JOptionPane.INFORMATION_MESSAGE);
           System.exit(JOptionPane.OK_OPTION);
        }
    }

    private boolean isSolved() {
        for (int i = 0; i < puzzleButtons.length; i++) {
            for (int j = 0; j < puzzleButtons[i].length; j++) {
                String buttonText = puzzleButtons[i][j].getText();
                if (!buttonText.isEmpty() && 4 * i + j + 1 != Integer.parseInt(buttonText)) {
                    return false;
                }
            }
        }
        return true;
    }

    ActionListener solvePuzzle = e -> {
        moveTile(3, 3);
        counter = 1;
        for (int i = 0; i < puzzleButtons.length; i++)
            for (int j = 0; j < puzzleButtons[i].length; j++) {
                puzzleButtons[i][j].setText("" + counter++);
            }
        puzzleButtons[3][3].setText("");
        moveTile(3, 3);
    };

    public static void main(String[] args) {
        new SlidePuzzleGUI();
    }
}






