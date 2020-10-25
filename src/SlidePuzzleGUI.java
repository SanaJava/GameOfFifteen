
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

class SlidePuzzleGUI extends JFrame {

    JPanel controlPanel = new JPanel(); // bottenpanelen, ligger längst ner
    JPanel puzzleGraphics = new JPanel(); // panel för spelknapparna med siffrorna 0-15
    JPanel buttonPanel = new JPanel(); // panel för knapparna newGame och SolvePuzzle, ligger i controlPanel

    JButton newGame = new JButton("Shuffle!"); // shufflar knapparna till en random ordning
    JButton solveGame = new JButton("Solve Game"); // lösa spelet
    JButton[][] puzzleButtons = new JButton[4][4]; // multidimisionell array med fyra rader och fyra columner

    Font fontSize = new Font("Chalkduster", Font.BOLD, 60); // storleken på texten på spelknapparna
    int counter = 1; // en räknare som används vid "instansieringen" av puzzleButtons
    int emptyRow = 3;
    int emptyColumn = 3;

    SlidePuzzleGUI() {
        try { // denna try-Catch används för att få färgerna på knapparna att funka.
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        controlPanel.add(puzzleGraphics); // lägger till panel
        controlPanel.setLayout(new BorderLayout()); // sätter layout för innehållet i panelen
        buttonPanel.setLayout(new FlowLayout()); // sätter layout för innehållet i panelen

        puzzleGraphics.setLayout(new GridLayout(4, 4)); // sätter rutnätslayout för spelknapparna i panelen

        buttonPanel.add(newGame); // newGame knappen adderas till buttonPanel
        buttonPanel.add(solveGame); // solveGame knappen adderas till buttonPanel
        controlPanel.add(buttonPanel, BorderLayout.NORTH); // sätter ButtonPanel högst upp i controlPanel
        controlPanel.add(puzzleGraphics, BorderLayout.CENTER); // sätter puzzleGraphics panelen i mitten på controlPanel
        setTitle("\u00A9 All rights reserved, made by Santana-Boukchana \u00AE");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // exit på kryss
        add(controlPanel); // skapa / adda Controlpanel i konstruktorn
        setSize(500, 500); // sätter storleken på fönstret
        newGame.addActionListener(shuffleForNewGame); // lägger till en actionListener på knappen newGame - shuffleForNewGame så att alla siffror blandas
        //solveGame.addActionListener(); // ingen metod klar än för denna actionListener

        setVisible(true); // sätter GUIn som synlig

        for (int i = 0; i < puzzleButtons.length; i++)  //dessa loopar körs för att skapa upp spelknapparna, finns beskrivet hur man gör i boken på sid 350 tror jag
            for (int j = 0; j < puzzleButtons[i].length; j++) {
                // Update: Store button in local variable, and set all configuration before setting it to the matrix
                JButton button = new JButton("" + counter++); // här används counter så varje ruta får texten av vad counter++ har för värde tex 1,2,3. counter loopas tills den blir 16 iom vi loopas längden på arrayen (har längden 4*4)
                //button.addActionListener(); // här kan vi adda actionlisteners till alla knappar men har kommenterat ut detta då jag inte vet hur metoden för "flytta knapp" ska funka
                button.setForeground(Color.BLACK); // sätter vit färg på texten på knapparna
                button.setFont(fontSize); // sätter storlek på siffrorna i knapparna
                button.setBackground(Color.LIGHT_GRAY); // bakgrundsfärg på knapparna blir grå

                button.addActionListener(new TileActionListener(i, j));
                puzzleGraphics.add(button);
                puzzleButtons[i][j] = button;

            }
        puzzleButtons[3][3].setText(""); // [3][3] är det sista "elementet" eller vad man säger i arrayen och skulle egentligen ha namnet 16 men iom att knapparna ska vara 1-15 så tar jag knapp 16 och döper om den till ingenting. Därav är den tom
        puzzleButtons[3][3].setBackground(Color.WHITE); // gör knappen helt rosa
        puzzleButtons[3][3].setOpaque(true); // gör så att färgerna funkar typ..
        shuffle(puzzleButtons);

        pack(); // packar allt

    }

    private void shuffle(JButton[][] puzzleButtons) { // bröt ut koden i ShuffleForNewGame och skapade en egen metod för att shuffla. Den implementerades på rad 75 och 114
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

    // lyssnare för newGame - ska shuffla spelknapparna
    ActionListener shuffleForNewGame = e -> {
        moveTile(3, 3); // Reset empty tile to bottom right
        shuffle(puzzleButtons);

    };

    public static void main(String[] args) {
        new SlidePuzzleGUI(); // kör programmet
    }

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
    }

}






