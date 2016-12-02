/**************************************************************
 *  File: PointAndClick.java
 *  Author: Nabor Palomera
 *          Ubaldo Jimenez Prieto
 *          Shun Lu
 *          WeiYing Lee
 * 
 * Assignment: Point and Click Game Version 1.2
 * Date Last Modified: 10/25/2016
 * 
 * Purpose: This programs purpose is to create a Hangman game.
 * The user will be able to interact with certain features such
 * as playing the game, checking the high scores, checking the 
 * creators of the game, etc. The user will have six chances to
 * guess the word correctly before losing the round. In addition, 
 * we have another game following up the Hangman game. In the color
 * game the user will have five rounds to pick the correct color 
 * corresponding to the text color. Each round consists of 100
 * points for every correct choice they make, otherwise no points 
 * are given. In the sodoku game, we will ask the user to fill out 
 * blank spaces with the correct numbers. User must complete the game
 * in order for the user to get partial/full points from this particular
 * game. Also the user thas the option to skip the sodoku game. 
 * The total highest score consists of these three games. If 
 * the user reaches a new high score, we prompt the user for their
 * initials to update the high scores table. In addition, we provided 
 * tooltips for every interactive component to help user navigate 
 * throughout the program. 
 *  
 *************************************************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs245;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.Clock;
import javax.swing.Timer;
import java.util.Random;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;


public class PointAndClick extends javax.swing.JFrame 
{
    /**
     * Creates new form PointAndClick
     */
    
    private String tmp;
    private int gameScore;
    private int colorGameRounds;
    private ArrayList<Color> colorBucket;
    private ArrayList<String> colorWords;
    private ArrayList<UserScore> scores;
    private javax.swing.JTextField[][] cells;
    private String[][] sudokuSolution;
    private boolean[][] sudokuCellChecked;
    private int sudokuListenerCount;
    
    //method: constructor 
    //purpose: to initialize all the global variables
    public PointAndClick() throws IOException 
    {
        getRootPane().getInputMap(getRootPane().WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "show credit");
        getRootPane().getInputMap(getRootPane().WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "close program");

        getRootPane().getActionMap().put("show credit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.JOptionPane.showMessageDialog(rootPane, name1.getText() + "\n" + name2.getText()
                        + "\n" + name3.getText() + "\n" + name4.getText() + "\n\n"
                        + "          Point And Click" + "\n" + "          FALL 2016",
                        "Credits", javax.swing.JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        getRootPane().getActionMap().put("close program", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });     

        tmp = null;
        gameScore = 100;
        colorGameRounds = 5;
        colorBucket = new ArrayList<>();
        colorWords = new ArrayList<>();
        scores = new ArrayList<>(5);
        cells = new javax.swing.JTextField[9][9];
        sudokuSolution = new String[9][9];
        sudokuCellChecked = new boolean[9][9];
        sudokuListenerCount = 0;
        
        scores.add(new UserScore());
        scores.add(new UserScore());
        scores.add(new UserScore());
        scores.add(new UserScore());
        scores.add(new UserScore());
        
        File f = new File("src/cs245/scores.txt");
        if (!f.exists()) 
        {
            PrintWriter pw = new PrintWriter(new FileWriter("src/cs245/scores.txt"));
            for (int i = 0; i < scores.size(); ++i) {
                pw.println(scores.get(i));
            }
            
            pw.flush();
            pw.close();
        }
        
        BufferedReader reader = new BufferedReader(new FileReader("src/cs245/scores.txt"));

        String line = reader.readLine();
        scores.add(0, new UserScore(line));
        scores.remove(scores.size() - 1);
        
        for (int i = 1; i < 5; ++i) {
            line = reader.readLine();
            scores.add(i, new UserScore(line));
            scores.remove(scores.size() - 1);
        }
        
        initializeColor();
        initializeWord();
        initComponents();    
        
        javax.swing.JPanel[] boxes = {box1, box2, box3, box4, box5,
                                      box6, box7, box8, box9};
        //This cells 2D array has row for each element in one box
        //has column for each box
        
        for (int i = 0; i < 9; ++i) 
        {
            //better check inside type
            Component[] tmp = boxes[i].getComponents(); 
            int count = 0;
            for (Component c : tmp) {
                if (c instanceof javax.swing.JTextField) {
                    cells[i][count] = (javax.swing.JTextField) c;
                    sudokuSolution[i][count] = ((javax.swing.JTextField) c).getText();
                    ++count;
                } else {
                    System.out.println("Not JTextField?");
                }
            }
        }
        
        reader.close();
    }
    
    //method: letterChecked
    //purpose: this method checks whether the user guess 
    //the the correct letter from the given word. It would also
    //update the score & the hangman diagram accordingly whether 
    //user guessed is correct or incorrect. When user loses/wins
    //it will direct to the endScore page.
    private void letterChecked(String temp, char letter)
    {   
        boolean answer= false;
        int num = temp.indexOf(letter);
        
        while(num >= 0)
        {
            answer = true;
            
            if(num == 0)
                char0.setVisible(true);
                
            else if(num == 1)
                char1.setVisible(true);
            else if(num == 2)
                char2.setVisible(true);
            else if(num == 3)
                char3.setVisible(true);
            else if(num == 4)
                char4.setVisible(true);
            else if(num == 5)
                char5.setVisible(true);
            else if(num == 6)
                char6.setVisible(true);
            else if(num == 7)
                char7.setVisible(true);
            
            num = temp.indexOf(letter, num + 1);
        }
        
        if (answer == false)
        {
            gameScore -= 10;
            value.setText(Integer.toString(gameScore));
            
            if(gameScore == 90)
                hangman1.setVisible(true);
            else if(gameScore == 80)
                hangman2.setVisible(true);
            else if(gameScore == 70)
                hangman3.setVisible(true);
            else if(gameScore == 60)
                hangman4.setVisible(true);
            else if(gameScore == 50)
                hangman5.setVisible(true);
            else if(gameScore == 40)
                hangman6.setVisible(true);
                        
        }
        
        if(gameScore <= 40)
        {   
               playScreen.setVisible(false);
               playColorScreen.setVisible(true);
               playRandomizeCircles();
               //points1.setText(gameScore + " PTS");   
        }
        
        if (temp.length() == 5) 
        {
            if (char0.isVisible() == true &&
                char1.isVisible() == true &&
                char2.isVisible() == true &&
                char3.isVisible() == true &&
                char4.isVisible() == true) 
            {
                playScreen.setVisible(false);
                playColorScreen.setVisible(true);
                playRandomizeCircles();
                //points1.setText(gameScore + " PTS");     
            }
        }
        else if(temp.length() == 8) 
        { 
                if (char0.isVisible() == true &&
                char1.isVisible() == true &&
                char2.isVisible() == true &&
                char3.isVisible() == true &&
                char4.isVisible() == true &&
                char5.isVisible() == true &&
                char6.isVisible() == true &&
                char7.isVisible() == true)
                {
                    playScreen.setVisible(false);
                    playColorScreen.setVisible(true);
                    playRandomizeCircles();
                    //points1.setText(gameScore + " PTS");                    
                }
        }
    }
    
    //method: afterColorSelected 
    //purpose: this method updates the score after each correct 
    //in the color game. Otherwise, score is not updated. 
    private void afterColorSelected(java.awt.Color color) {
        
        //check the color
        boolean isThisColor = colorText.getForeground().equals(color);
        
        popupPanel.setVisible(false);
        
        //if rounds are less than 5 rounds, game can proceed
        if(colorGameRounds > 1){
            if(isThisColor) {
                //score! get 100 pts :)
                gameScore += 100;
            }
            
            //to prevent the last round shows the next round
            //if(colorGameRounds < 4) {
            //design for next round
            changeTextColor();
            changeTextWord();
            playRandomizeCircles();
            //}
            --colorGameRounds;  
        }
        else {
            if(isThisColor) {
                //score! get 100 pts :)
                gameScore += 100;
            }
            //there are five rounds, game should end
            //update the point
            //also needs to reset the score for colorGameRounds for re-start
            colorGameRounds = 5;
            //goes to end screen
            playColorScreen.setVisible(false);
            playSudokuScreen.setVisible(true);
            sudoku();
            
            //check if the popup panel should come out after the game over
        }
    }
    
    //method: isIntersected
    //purpose: This method checks if a button intersects any remaining buttons.
    private boolean isIntersected(java.awt.Component button, ArrayList<java.awt.Component> al) 
    {
        //check whole panel if it has any button intersected    
        if (!al.isEmpty()) {
            for (int i = 0; i < al.size(); i++) {
                if (al.get(i).getBounds().intersects(button.getBounds()))
                    return true;
            }
        }
        return false;
    }
    
    //method: setRandomLocation
    //purpose: This method sets a new random location for button.
    private void setRandomLocation(Random rdm, javax.swing.JButton button) {

        button.setLocation(rdm.nextInt(500), rdm.nextInt(300));
    }
    
    //method: changeTextColor
   //purpose: This method randomly changes color of colorText.
    private void changeTextColor() {
        Random rdm = new Random();
        colorText.setForeground(colorBucket.get(rdm.nextInt(colorBucket.size())));
    }
    
    //method: changeTextWord
    //purpose: change the the color of the text and also
    //change the word name to a different color name
    private void changeTextWord() {
        Random rdm = new Random();
        colorText.setText(colorWords.get(rdm.nextInt(colorWords.size())));
    }

    //method: initializeColor
    //purpose: This method adds all RGB values of listed colors into arraylist
    private void initializeColor() {
        colorBucket.add(Color.RED);
        colorBucket.add(Color.BLUE);
        colorBucket.add(Color.GREEN);
        colorBucket.add(Color.YELLOW);
        colorBucket.add(Color.MAGENTA);
    }
    
    //method: initializeWord
    //purposE: This method adds all listed words of colors into arraylist.
    private void initializeWord() {
        colorWords.add("RED");
        colorWords.add("BLUE");
        colorWords.add("GREEN");
        colorWords.add("YELLOW");
        colorWords.add("PURPLE");
    }
        
    //method: playRandomizeCircles
    //purpose: Randomized the circles locations and if any of them
    //intersect with each other, re-locate them into a new position
    private void playRandomizeCircles() {
        Random rdm = new Random();
        ArrayList<java.awt.Component> circles = new ArrayList<>();
        Stack<javax.swing.JButton> circlesStack = new Stack<>();
        
        circlesStack.push(blueButton);
        circlesStack.push(redButton);
        circlesStack.push(greenButton);
        circlesStack.push(purpleButton);
        circlesStack.push(yellowButton);
        circles.add(colorText);
        circles.add(displayColorScreenTime);
        
        javax.swing.JButton curButton;
        while (!circlesStack.empty()) {
            curButton = circlesStack.pop();
            setRandomLocation(rdm, curButton);
            while (isIntersected(curButton, circles)) {
                setRandomLocation(rdm, curButton);
            }
            circles.add(curButton);
        }
    }
    
    //method: sudoku
    //purpose: Create the sudoku gameBoard squares
    private void sudoku() 
    {

        if (sudokuListenerCount == 0) {
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {
                    if (i == 0 && j == 0); else {
                        cells[i][j].addFocusListener(new java.awt.event.FocusAdapter() {
                            public void focusLost(java.awt.event.FocusEvent evt) {
                                cellFocusLost(evt);
                            }
                        });
                        cells[i][j].addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                cellActionPerformed(evt);
                            }
                        });
                    }
                }
            }
            sudokuListenerCount = 1;
        }
        //Begin the game
        clearBoard();
    }

    //method: clearBoard
    //purpose: This method clears/resets the board to the original state. This
    //will happen when user wants to play the program again
    private void clearBoard() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (cells[i][j].isEditable()) {
                    cells[i][j].setText(null);
                }
                sudokuCellChecked[i][j] = true;
            }
        }
    }

    //method: checkBoard
    //purpose: Checks if the user inputted the correct numbers to their 
    //appropiate square spots. If the user completed the whole board
    //correctly we send the user to the end game panel and provide the user
    //with their total score for all three games. If score is higher than any
    //previous high scores, we prompt the user to enter their initials.
    private void checkBoard() {
        int sudokuScore = 540;
        boolean isIncomplete = false;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (!cells[i][j].getText().equals(sudokuSolution[i][j])) {
                    sudokuCellChecked[i][j] = false;
                    isIncomplete = true;
                }
            }
        }
       
        if (isIncomplete) {
            javax.swing.JOptionPane.showMessageDialog(rootPane, "Some solutions are incorrect, try again.");
        } else {
            for (boolean[] box : sudokuCellChecked) {
                for (boolean cell : box) {
                    if (cell == false && sudokuScore > 0) {
                        sudokuScore -= 10;
                    }
                }
            }
            gameScore += sudokuScore;
            points1.setText(gameScore + "");
            endGamePanel.setVisible(true);
            playSudokuScreen.setVisible(false);

            if (gameScore >= scores.get(0).getScore()) {
                //popup come out
                popupPanel.setVisible(true);
                //save to file
            }
            //disable save button
            initSaveButton.setEnabled(false);
        }
    }                                      

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleScreen = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        team = new javax.swing.JLabel();
        mainMenu = new javax.swing.JPanel();
        playButtom = new javax.swing.JButton();
        highscoresButtom = new javax.swing.JButton();
        creditsButtom = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        scorePanel = new javax.swing.JPanel();
        scoreTitle = new javax.swing.JLabel();
        scoreFirst = new javax.swing.JLabel();
        scoreSecond = new javax.swing.JLabel();
        scoreThird = new javax.swing.JLabel();
        scoreFourth = new javax.swing.JLabel();
        scoreFifth = new javax.swing.JLabel();
        firstData = new javax.swing.JLabel();
        secondData = new javax.swing.JLabel();
        thirdData = new javax.swing.JLabel();
        fourthData = new javax.swing.JLabel();
        fifthData = new javax.swing.JLabel();
        scoreBackButtom = new javax.swing.JButton();
        line1 = new javax.swing.JLabel();
        line2 = new javax.swing.JLabel();
        line3 = new javax.swing.JLabel();
        line4 = new javax.swing.JLabel();
        line5 = new javax.swing.JLabel();
        creditPanel = new javax.swing.JPanel();
        creditTitle = new javax.swing.JLabel();
        name1 = new javax.swing.JLabel();
        name2 = new javax.swing.JLabel();
        name3 = new javax.swing.JLabel();
        name4 = new javax.swing.JLabel();
        creditBackButtom = new javax.swing.JButton();
        playScreen = new javax.swing.JPanel();
        displayTime = new javax.swing.JLabel();
        P = new javax.swing.JButton();
        Q = new javax.swing.JButton();
        S = new javax.swing.JButton();
        R = new javax.swing.JButton();
        Z = new javax.swing.JButton();
        O = new javax.swing.JButton();
        Y = new javax.swing.JButton();
        T = new javax.swing.JButton();
        U = new javax.swing.JButton();
        V = new javax.swing.JButton();
        W = new javax.swing.JButton();
        X = new javax.swing.JButton();
        K = new javax.swing.JButton();
        J = new javax.swing.JButton();
        N = new javax.swing.JButton();
        I = new javax.swing.JButton();
        H = new javax.swing.JButton();
        M = new javax.swing.JButton();
        L = new javax.swing.JButton();
        A = new javax.swing.JButton();
        B = new javax.swing.JButton();
        C = new javax.swing.JButton();
        D = new javax.swing.JButton();
        E = new javax.swing.JButton();
        F = new javax.swing.JButton();
        G = new javax.swing.JButton();
        skipGameButton = new javax.swing.JButton();
        playTitle = new javax.swing.JLabel();
        hangmanTree = new javax.swing.JLabel();
        dashedLine1 = new javax.swing.JLabel();
        dashedLine2 = new javax.swing.JLabel();
        dashedLine3 = new javax.swing.JLabel();
        dashedLine4 = new javax.swing.JLabel();
        dashedLine5 = new javax.swing.JLabel();
        dashedLine6 = new javax.swing.JLabel();
        dashedLine7 = new javax.swing.JLabel();
        dashedLine8 = new javax.swing.JLabel();
        char0 = new javax.swing.JLabel();
        char1 = new javax.swing.JLabel();
        char2 = new javax.swing.JLabel();
        char3 = new javax.swing.JLabel();
        char4 = new javax.swing.JLabel();
        char5 = new javax.swing.JLabel();
        char6 = new javax.swing.JLabel();
        char7 = new javax.swing.JLabel();
        value = new javax.swing.JLabel();
        score = new javax.swing.JLabel();
        hangman6 = new javax.swing.JLabel();
        hangman5 = new javax.swing.JLabel();
        hangman4 = new javax.swing.JLabel();
        hangman3 = new javax.swing.JLabel();
        hangman2 = new javax.swing.JLabel();
        hangman1 = new javax.swing.JLabel();
        playColorScreen = new javax.swing.JPanel();
        redButton = new javax.swing.JButton();
        blueButton = new javax.swing.JButton();
        greenButton = new javax.swing.JButton();
        yellowButton = new javax.swing.JButton();
        purpleButton = new javax.swing.JButton();
        colorText = new javax.swing.JLabel();
        displayColorScreenTime = new javax.swing.JLabel();
        playSudokuScreen = new javax.swing.JPanel();
        sudokuBoard = new javax.swing.JPanel();
        box1 = new javax.swing.JPanel();
        cell1 = new javax.swing.JTextField();
        cell2 = new javax.swing.JTextField();
        cell3 = new javax.swing.JTextField();
        cell4 = new javax.swing.JTextField();
        cell5 = new javax.swing.JTextField();
        cell6 = new javax.swing.JTextField();
        cell7 = new javax.swing.JTextField();
        cell8 = new javax.swing.JTextField();
        cell9 = new javax.swing.JTextField();
        box2 = new javax.swing.JPanel();
        cell10 = new javax.swing.JTextField();
        cell11 = new javax.swing.JTextField();
        cell12 = new javax.swing.JTextField();
        cell13 = new javax.swing.JTextField();
        cell14 = new javax.swing.JTextField();
        cell15 = new javax.swing.JTextField();
        cell16 = new javax.swing.JTextField();
        cell17 = new javax.swing.JTextField();
        cell18 = new javax.swing.JTextField();
        box3 = new javax.swing.JPanel();
        cell19 = new javax.swing.JTextField();
        cell20 = new javax.swing.JTextField();
        cell21 = new javax.swing.JTextField();
        cell22 = new javax.swing.JTextField();
        cell23 = new javax.swing.JTextField();
        cell24 = new javax.swing.JTextField();
        cell25 = new javax.swing.JTextField();
        cell26 = new javax.swing.JTextField();
        cell27 = new javax.swing.JTextField();
        box4 = new javax.swing.JPanel();
        cell28 = new javax.swing.JTextField();
        cell29 = new javax.swing.JTextField();
        cell30 = new javax.swing.JTextField();
        cell31 = new javax.swing.JTextField();
        cell32 = new javax.swing.JTextField();
        cell33 = new javax.swing.JTextField();
        cell34 = new javax.swing.JTextField();
        cell35 = new javax.swing.JTextField();
        cell36 = new javax.swing.JTextField();
        box5 = new javax.swing.JPanel();
        cell37 = new javax.swing.JTextField();
        cell38 = new javax.swing.JTextField();
        cell39 = new javax.swing.JTextField();
        cell40 = new javax.swing.JTextField();
        cell41 = new javax.swing.JTextField();
        cell42 = new javax.swing.JTextField();
        cell43 = new javax.swing.JTextField();
        cell44 = new javax.swing.JTextField();
        cell45 = new javax.swing.JTextField();
        box6 = new javax.swing.JPanel();
        cell46 = new javax.swing.JTextField();
        cell47 = new javax.swing.JTextField();
        cell48 = new javax.swing.JTextField();
        cell49 = new javax.swing.JTextField();
        cell50 = new javax.swing.JTextField();
        cell51 = new javax.swing.JTextField();
        cell52 = new javax.swing.JTextField();
        cell53 = new javax.swing.JTextField();
        cell54 = new javax.swing.JTextField();
        box7 = new javax.swing.JPanel();
        cell55 = new javax.swing.JTextField();
        cell56 = new javax.swing.JTextField();
        cell57 = new javax.swing.JTextField();
        cell58 = new javax.swing.JTextField();
        cell59 = new javax.swing.JTextField();
        cell60 = new javax.swing.JTextField();
        cell61 = new javax.swing.JTextField();
        cell62 = new javax.swing.JTextField();
        cell63 = new javax.swing.JTextField();
        box8 = new javax.swing.JPanel();
        cell64 = new javax.swing.JTextField();
        cell65 = new javax.swing.JTextField();
        cell66 = new javax.swing.JTextField();
        cell67 = new javax.swing.JTextField();
        cell68 = new javax.swing.JTextField();
        cell69 = new javax.swing.JTextField();
        cell70 = new javax.swing.JTextField();
        cell71 = new javax.swing.JTextField();
        cell72 = new javax.swing.JTextField();
        box9 = new javax.swing.JPanel();
        cell73 = new javax.swing.JTextField();
        cell74 = new javax.swing.JTextField();
        cell75 = new javax.swing.JTextField();
        cell76 = new javax.swing.JTextField();
        cell77 = new javax.swing.JTextField();
        cell78 = new javax.swing.JTextField();
        cell79 = new javax.swing.JTextField();
        cell80 = new javax.swing.JTextField();
        cell81 = new javax.swing.JTextField();
        quitButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();
        displayTime3 = new javax.swing.JLabel();
        playTitle1 = new javax.swing.JLabel();
        endGamePanel = new javax.swing.JPanel();
        totalScore1 = new javax.swing.JLabel();
        gameOver1 = new javax.swing.JLabel();
        points1 = new javax.swing.JLabel();
        endButton1 = new javax.swing.JButton();
        popupPanel = new javax.swing.JPanel();
        initLabel = new javax.swing.JLabel();
        initSaveButton = new javax.swing.JButton();
        initInputField = new javax.swing.JTextField();
        congrazLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMaximumSize(new java.awt.Dimension(600, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        titleScreen.setBackground(new java.awt.Color(0, 0, 0));
        titleScreen.setPreferredSize(new java.awt.Dimension(600, 400));
        titleScreen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title.setFont(new java.awt.Font("Stencil", 1, 48)); // NOI18N
        title.setForeground(new java.awt.Color(0, 0, 255));
        title.setText("CS245 FALL");
        titleScreen.add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, -1, -1));

        team.setFont(new java.awt.Font("Stencil", 3, 24)); // NOI18N
        team.setForeground(new java.awt.Color(0, 0, 255));
        team.setText("By: Team Logic VISUALS");
        titleScreen.add(team, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 285, -1, -1));

        getContentPane().add(titleScreen, "card2");
        titleScreen.getAccessibleContext().setAccessibleName("");

        mainMenu.setBackground(new java.awt.Color(0, 0, 0));
        mainMenu.setPreferredSize(new java.awt.Dimension(600, 400));

        playButtom.setText("PLAY");
        playButtom.setToolTipText("Click to play!");
        playButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtomActionPerformed(evt);
            }
        });

        highscoresButtom.setText("HIGHSCORES");
        highscoresButtom.setToolTipText("Click to view highscores!");
        highscoresButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highscoresButtomActionPerformed(evt);
            }
        });

        creditsButtom.setText("CREDITS");
        creditsButtom.setToolTipText("Click to show credit!");
        creditsButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditsButtomActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/team_logic_logo_by_zinch21_edited.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout mainMenuLayout = new javax.swing.GroupLayout(mainMenu);
        mainMenu.setLayout(mainMenuLayout);
        mainMenuLayout.setHorizontalGroup(
            mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(highscoresButtom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playButtom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditsButtom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        mainMenuLayout.setVerticalGroup(
            mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuLayout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addGroup(mainMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainMenuLayout.createSequentialGroup()
                        .addComponent(playButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(highscoresButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(creditsButtom, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        getContentPane().add(mainMenu, "card3");

        scorePanel.setBackground(new java.awt.Color(204, 204, 204));
        scorePanel.setPreferredSize(new java.awt.Dimension(600, 400));
        scorePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scoreTitle.setFont(new java.awt.Font("Stencil", 2, 48)); // NOI18N
        scoreTitle.setForeground(new java.awt.Color(0, 204, 0));
        scoreTitle.setText("HIGHSCORES");
        scorePanel.add(scoreTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 78, -1, -1));

        scoreFirst.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        scoreFirst.setText("ABC");
        scorePanel.add(scoreFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 139, -1, -1));

        scoreSecond.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        scoreSecond.setText("ABC");
        scorePanel.add(scoreSecond, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 178, -1, -1));

        scoreThird.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        scoreThird.setText("ABC");
        scorePanel.add(scoreThird, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 217, -1, -1));

        scoreFourth.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        scoreFourth.setText("ABC");
        scorePanel.add(scoreFourth, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 258, -1, -1));

        scoreFifth.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        scoreFifth.setText("ABC");
        scorePanel.add(scoreFifth, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 297, -1, -1));

        firstData.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        firstData.setText("00000");
        scorePanel.add(firstData, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 139, -1, -1));

        secondData.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        secondData.setText("00000");
        scorePanel.add(secondData, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 178, -1, -1));

        thirdData.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        thirdData.setText("00000");
        scorePanel.add(thirdData, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 217, -1, -1));

        fourthData.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        fourthData.setText("00000");
        scorePanel.add(fourthData, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 258, -1, -1));

        fifthData.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        fifthData.setText("00000");
        scorePanel.add(fifthData, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 297, -1, -1));

        scoreBackButtom.setText("Back");
        scoreBackButtom.setToolTipText("Click to go main menu");
        scoreBackButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreBackButtomActionPerformed(evt);
            }
        });
        scorePanel.add(scoreBackButtom, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 333, -1, -1));

        line1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        line1.setText("-----");
        scorePanel.add(line1, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 139, -1, -1));

        line2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        line2.setText("-----");
        scorePanel.add(line2, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 178, -1, -1));

        line3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        line3.setText("-----");
        scorePanel.add(line3, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 217, -1, -1));

        line4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        line4.setText("-----");
        scorePanel.add(line4, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 258, -1, -1));

        line5.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        line5.setText("-----");
        scorePanel.add(line5, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 297, -1, -1));

        getContentPane().add(scorePanel, "card4");
        scorePanel.getAccessibleContext().setAccessibleName("");

        creditPanel.setBackground(new java.awt.Color(204, 204, 204));
        creditPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        creditTitle.setFont(new java.awt.Font("Stencil", 2, 48)); // NOI18N
        creditTitle.setForeground(new java.awt.Color(204, 0, 0));
        creditTitle.setText("CREDITS");

        name1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        name1.setText("Nabor Palomera ID# 009457864");

        name2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        name2.setText("Ubaldo Jimenez ID# 008231301");

        name3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        name3.setText("Shun Lu ID# 009423986");

        name4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        name4.setText("WeiYing Lee ID# 009546069");

        creditBackButtom.setText("Back");
        creditBackButtom.setToolTipText("Click to go back main menu");
        creditBackButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditBackButtomActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout creditPanelLayout = new javax.swing.GroupLayout(creditPanel);
        creditPanel.setLayout(creditPanelLayout);
        creditPanelLayout.setHorizontalGroup(
            creditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(creditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditPanelLayout.createSequentialGroup()
                        .addComponent(name4, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(131, 131, 131))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creditPanelLayout.createSequentialGroup()
                        .addComponent(name3, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100))))
            .addGroup(creditPanelLayout.createSequentialGroup()
                .addGroup(creditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(creditPanelLayout.createSequentialGroup()
                        .addGap(202, 202, 202)
                        .addComponent(creditTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(creditPanelLayout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addGroup(creditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(name1)
                            .addComponent(name2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(creditPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(creditBackButtom)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        creditPanelLayout.setVerticalGroup(
            creditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creditPanelLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(creditTitle)
                .addGap(18, 18, 18)
                .addComponent(name1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(name2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(name4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(name3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(creditBackButtom)
                .addGap(32, 32, 32))
        );

        getContentPane().add(creditPanel, "card5");

        playScreen.setBackground(new java.awt.Color(204, 204, 204));
        playScreen.setPreferredSize(new java.awt.Dimension(600, 400));
        playScreen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        displayTime.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        displayTime.setToolTipText("Current time");
        java.awt.event.ActionListener taskperformed = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayTime.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            }
        };
        Timer timer = new Timer(1000, taskperformed);
        //timer.setInitialDelay(1);
        timer.start();
        playScreen.add(displayTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 28, -1, -1));

        P.setText("P");
        P.setToolTipText("Click to select");
        P.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PMouseClicked(evt);
            }
        });
        playScreen.add(P, new org.netbeans.lib.awtextra.AbsoluteConstraints(474, 323, -1, -1));

        Q.setText("Q");
        Q.setToolTipText("Click to select");
        Q.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                QMouseClicked(evt);
            }
        });
        playScreen.add(Q, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 359, -1, -1));

        S.setText("S");
        S.setToolTipText("Click to select");
        S.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SMouseClicked(evt);
            }
        });
        playScreen.add(S, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 359, -1, -1));

        R.setText("R");
        R.setToolTipText("Click to select");
        R.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RMouseClicked(evt);
            }
        });
        playScreen.add(R, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 359, -1, -1));

        Z.setText("Z");
        Z.setToolTipText("Click to select");
        Z.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ZMouseClicked(evt);
            }
        });
        playScreen.add(Z, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 359, -1, -1));

        O.setText("O");
        O.setToolTipText("Click to select");
        O.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OMouseClicked(evt);
            }
        });
        playScreen.add(O, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 323, -1, -1));

        Y.setText("Y");
        Y.setToolTipText("Click to select");
        Y.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                YMouseClicked(evt);
            }
        });
        playScreen.add(Y, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 359, -1, -1));

        T.setText("T");
        T.setToolTipText("Click to select");
        T.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TMouseClicked(evt);
            }
        });
        playScreen.add(T, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 359, -1, -1));

        U.setText("U");
        U.setToolTipText("Click to select");
        U.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UMouseClicked(evt);
            }
        });
        playScreen.add(U, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 359, -1, -1));

        V.setText("V");
        V.setToolTipText("Click to select");
        V.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VMouseClicked(evt);
            }
        });
        playScreen.add(V, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 359, -1, -1));

        W.setText("W");
        W.setToolTipText("Click to select");
        W.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WMouseClicked(evt);
            }
        });
        playScreen.add(W, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 359, -1, -1));

        X.setText("X");
        X.setToolTipText("Click to select");
        X.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                XMouseClicked(evt);
            }
        });
        playScreen.add(X, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 359, -1, -1));

        K.setText("K");
        K.setToolTipText("Click to select");
        K.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KMouseClicked(evt);
            }
        });
        playScreen.add(K, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 323, -1, -1));

        J.setText("J");
        J.setToolTipText("Click to select");
        J.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JMouseClicked(evt);
            }
        });
        playScreen.add(J, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 323, -1, -1));

        N.setText("N");
        N.setToolTipText("Click to select");
        N.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NMouseClicked(evt);
            }
        });
        playScreen.add(N, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 323, -1, -1));

        I.setText("I");
        I.setToolTipText("Click to select");
        I.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IMouseClicked(evt);
            }
        });
        playScreen.add(I, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 323, -1, -1));

        H.setText("H");
        H.setToolTipText("Click to select");
        H.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HMouseClicked(evt);
            }
        });
        playScreen.add(H, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 285, -1, -1));

        M.setText("M");
        M.setToolTipText("Click to select");
        M.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MMouseClicked(evt);
            }
        });
        playScreen.add(M, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 323, -1, -1));

        L.setText("L");
        L.setToolTipText("Click to select");
        L.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LMouseClicked(evt);
            }
        });
        playScreen.add(L, new org.netbeans.lib.awtextra.AbsoluteConstraints(231, 323, -1, -1));

        A.setText("A");
        A.setToolTipText("Click to select");
        A.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AMouseClicked(evt);
            }
        });
        playScreen.add(A, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 285, -1, -1));

        B.setText("B");
        B.setToolTipText("Click to select");
        B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BMouseClicked(evt);
            }
        });
        playScreen.add(B, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 285, -1, -1));

        C.setText("C");
        C.setToolTipText("Click to select");
        C.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CMouseClicked(evt);
            }
        });
        playScreen.add(C, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 285, -1, -1));

        D.setText("D");
        D.setToolTipText("Click to select");
        D.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DMouseClicked(evt);
            }
        });
        playScreen.add(D, new org.netbeans.lib.awtextra.AbsoluteConstraints(229, 285, -1, -1));

        E.setText("E");
        E.setToolTipText("Click to select");
        E.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EMouseClicked(evt);
            }
        });
        playScreen.add(E, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 285, -1, -1));

        F.setText("F");
        F.setToolTipText("Click to select");
        F.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FMouseClicked(evt);
            }
        });
        playScreen.add(F, new org.netbeans.lib.awtextra.AbsoluteConstraints(352, 285, -1, -1));

        G.setText("G");
        G.setToolTipText("Click to select");
        G.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GMouseClicked(evt);
            }
        });
        playScreen.add(G, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 285, -1, -1));

        skipGameButton.setText("SKIP");
        skipGameButton.setToolTipText("Click to skip this game");
        skipGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipGameButtonActionPerformed(evt);
            }
        });
        playScreen.add(skipGameButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 211, -1, -1));

        playTitle.setFont(new java.awt.Font("Tempus Sans ITC", 0, 30)); // NOI18N
        playTitle.setText("HANGMAN");
        playScreen.add(playTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        hangmanTree.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/gioco_impiccato_0.gif"))); // NOI18N
        playScreen.add(hangmanTree, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 204, 199));

        dashedLine1.setText("___");
        playScreen.add(dashedLine1, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 249, -1, 27));

        dashedLine2.setText("___");
        playScreen.add(dashedLine2, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 249, -1, 27));

        dashedLine3.setText("___");
        playScreen.add(dashedLine3, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 249, -1, 27));

        dashedLine4.setText("___");
        playScreen.add(dashedLine4, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 246, -1, 30));

        dashedLine5.setText("___");
        playScreen.add(dashedLine5, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 249, -1, 27));

        dashedLine6.setText("___");
        playScreen.add(dashedLine6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 250, -1, 27));

        dashedLine7.setText("___");
        playScreen.add(dashedLine7, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 249, -1, 27));

        dashedLine8.setText("___");
        playScreen.add(dashedLine8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 250, -1, 27));

        char0.setText("A");
        playScreen.add(char0, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, -1, -1));

        char1.setText("B");
        playScreen.add(char1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, -1, -1));

        char2.setText("S");
        playScreen.add(char2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, -1, -1));

        char3.setText("T");
        playScreen.add(char3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 240, -1, -1));

        char4.setText("R");
        playScreen.add(char4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, -1, -1));

        char5.setText("A");
        playScreen.add(char5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, -1, -1));

        char6.setText("C");
        playScreen.add(char6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 240, -1, -1));

        char7.setText("T");
        playScreen.add(char7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, -1, -1));

        value.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        value.setForeground(new java.awt.Color(255, 51, 51));
        value.setText("100");
        value.setToolTipText("Current score");
        playScreen.add(value, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, -1, -1));

        score.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        score.setForeground(new java.awt.Color(255, 51, 51));
        score.setText("Score:");
        playScreen.add(score, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        hangman6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/6wrong.png"))); // NOI18N
        hangman6.setText("jLabel3");
        playScreen.add(hangman6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 150, 190));

        hangman5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/5wrong.png"))); // NOI18N
        hangman5.setText("jLabel3");
        playScreen.add(hangman5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 150, 190));

        hangman4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/4wrong.png"))); // NOI18N
        hangman4.setText("jLabel4");
        playScreen.add(hangman4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 150, 190));

        hangman3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/3wrong.png"))); // NOI18N
        hangman3.setText("jLabel3");
        playScreen.add(hangman3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 150, 190));

        hangman2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/2wrong.png"))); // NOI18N
        hangman2.setText("jLabel2");
        playScreen.add(hangman2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 160, 180));

        hangman1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/1wrong.png"))); // NOI18N
        hangman1.setText("jLabel2");
        playScreen.add(hangman1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, 130, 160));

        getContentPane().add(playScreen, "card6");

        playColorScreen.setBackground(new java.awt.Color(204, 204, 204));
        playColorScreen.setPreferredSize(new java.awt.Dimension(600, 400));
        playColorScreen.setLayout(null);

        redButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/RED.png"))); // NOI18N
        redButton.setToolTipText("Select to match up word's color!");
        redButton.setPreferredSize(new java.awt.Dimension(100, 100));
        redButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                redButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                redButtonMouseExited(evt);
            }
        });
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });
        playColorScreen.add(redButton);
        redButton.setBounds(0, 0, 100, 100);

        blueButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/BLUE.png"))); // NOI18N
        blueButton.setToolTipText("Select to match up word's color!");
        blueButton.setPreferredSize(new java.awt.Dimension(100, 100));
        blueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                blueButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                blueButtonMouseExited(evt);
            }
        });
        blueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButtonActionPerformed(evt);
            }
        });
        playColorScreen.add(blueButton);
        blueButton.setBounds(0, 0, 100, 100);

        greenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/GREEN.png"))); // NOI18N
        greenButton.setToolTipText("Select to match up word's color!");
        greenButton.setPreferredSize(new java.awt.Dimension(100, 100));
        greenButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                greenButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                greenButtonMouseExited(evt);
            }
        });
        greenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenButtonActionPerformed(evt);
            }
        });
        playColorScreen.add(greenButton);
        greenButton.setBounds(0, 0, 100, 100);

        yellowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/YELLOW.png"))); // NOI18N
        yellowButton.setToolTipText("Select to match up word's color!");
        yellowButton.setPreferredSize(new java.awt.Dimension(100, 100));
        yellowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                yellowButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                yellowButtonMouseExited(evt);
            }
        });
        yellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yellowButtonActionPerformed(evt);
            }
        });
        playColorScreen.add(yellowButton);
        yellowButton.setBounds(0, 0, 100, 100);

        purpleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/PURPLE.png"))); // NOI18N
        purpleButton.setToolTipText("Select to match up word's color!");
        purpleButton.setPreferredSize(new java.awt.Dimension(100, 100));
        purpleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                purpleButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                purpleButtonMouseExited(evt);
            }
        });
        purpleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purpleButtonActionPerformed(evt);
            }
        });
        playColorScreen.add(purpleButton);
        purpleButton.setBounds(0, 0, 100, 100);

        colorText.setFont(new java.awt.Font("Sylfaen", 0, 36)); // NOI18N
        changeTextColor();
        colorText.setText(null);
        changeTextWord();
        colorText.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playColorScreen.add(colorText);
        colorText.setBounds(230, 80, 192, 48);

        displayColorScreenTime.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        displayColorScreenTime.setToolTipText("Current time");
        java.awt.event.ActionListener taskperformed2 = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayColorScreenTime.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            }
        };
        Timer timer2 = new Timer(1000, taskperformed2);
        //timer.setInitialDelay(1);
        timer2.start();
        playColorScreen.add(displayColorScreenTime);
        displayColorScreenTime.setBounds(460, 28, 130, 16);

        getContentPane().add(playColorScreen, "card9");

        playSudokuScreen.setBackground(new java.awt.Color(204, 204, 204));
        playSudokuScreen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sudokuBoard.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBoard.setPreferredSize(new java.awt.Dimension(306, 306));
        sudokuBoard.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        box1.setBackground(new java.awt.Color(255, 255, 255));
        box1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell1.setEditable(false);
        cell1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell1.setText("8");
        cell1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell1.setEnabled(false);
        cell1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cellFocusLost(evt);
            }
        });
        cell1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cellActionPerformed(evt);
            }
        });
        box1.add(cell1);

        cell2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell2.setText("3");
        cell2.setToolTipText("click to set number");
        cell2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell2);

        cell3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell3.setText("5");
        cell3.setToolTipText("click to set number");
        cell3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell3);

        cell4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell4.setText("2");
        cell4.setToolTipText("click to set number");
        cell4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell4);

        cell5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell5.setText("9");
        cell5.setToolTipText("click to set number");
        cell5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell5);

        cell6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell6.setText("6");
        cell6.setToolTipText("click to set number");
        cell6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell6);

        cell7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell7.setText("4");
        cell7.setToolTipText("click to set number");
        cell7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell7);

        cell8.setEditable(false);
        cell8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell8.setText("1");
        cell8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell8.setEnabled(false);
        box1.add(cell8);

        cell9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell9.setText("7");
        cell9.setToolTipText("click to set number");
        cell9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box1.add(cell9);

        sudokuBoard.add(box1);

        box2.setBackground(new java.awt.Color(255, 255, 255));
        box2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell10.setEditable(false);
        cell10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell10.setText("4");
        cell10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell10.setEnabled(false);
        box2.add(cell10);

        cell11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell11.setText("1");
        cell11.setToolTipText("click to set number");
        cell11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell11);

        cell12.setEditable(false);
        cell12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell12.setText("6");
        cell12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell12.setEnabled(false);
        box2.add(cell12);

        cell13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell13.setText("8");
        cell13.setToolTipText("click to set number");
        cell13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell13);

        cell14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell14.setText("5");
        cell14.setToolTipText("click to set number");
        cell14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell14);

        cell15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell15.setText("7");
        cell15.setToolTipText("click to set number");
        cell15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell15);

        cell16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell16.setText("2");
        cell16.setToolTipText("click to set number");
        cell16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell16);

        cell17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell17.setText("9");
        cell17.setToolTipText("click to set number");
        cell17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell17);

        cell18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell18.setText("3");
        cell18.setToolTipText("click to set number");
        cell18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box2.add(cell18);

        sudokuBoard.add(box2);

        box3.setBackground(new java.awt.Color(255, 255, 255));
        box3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box3.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell19.setText("9");
        cell19.setToolTipText("click to set number");
        cell19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box3.add(cell19);

        cell20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell20.setText("2");
        cell20.setToolTipText("click to set number");
        cell20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box3.add(cell20);

        cell21.setEditable(false);
        cell21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell21.setText("7");
        cell21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell21.setEnabled(false);
        box3.add(cell21);

        cell22.setEditable(false);
        cell22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell22.setText("4");
        cell22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell22.setEnabled(false);
        box3.add(cell22);

        cell23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell23.setText("3");
        cell23.setToolTipText("click to set number");
        cell23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box3.add(cell23);

        cell24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell24.setText("1");
        cell24.setToolTipText("click to set number");
        cell24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box3.add(cell24);

        cell25.setEditable(false);
        cell25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell25.setText("6");
        cell25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell25.setEnabled(false);
        box3.add(cell25);

        cell26.setEditable(false);
        cell26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell26.setText("5");
        cell26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell26.setEnabled(false);
        box3.add(cell26);

        cell27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell27.setText("8");
        cell27.setToolTipText("click to set number");
        cell27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box3.add(cell27);

        sudokuBoard.add(box3);

        box4.setBackground(new java.awt.Color(255, 255, 255));
        box4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box4.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell28.setEditable(false);
        cell28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell28.setText("5");
        cell28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell28.setEnabled(false);
        box4.add(cell28);

        cell29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell29.setText("6");
        cell29.setToolTipText("click to set number");
        cell29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box4.add(cell29);

        cell30.setEditable(false);
        cell30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell30.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell30.setText("9");
        cell30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell30.setEnabled(false);
        box4.add(cell30);

        cell31.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell31.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell31.setText("1");
        cell31.setToolTipText("click to set number");
        cell31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box4.add(cell31);

        cell32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell32.setText("2");
        cell32.setToolTipText("click to set number");
        cell32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box4.add(cell32);

        cell33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell33.setText("3");
        cell33.setToolTipText("click to set number");
        cell33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box4.add(cell33);

        cell34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell34.setText("7");
        cell34.setToolTipText("click to set number");
        cell34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box4.add(cell34);

        cell35.setEditable(false);
        cell35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell35.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell35.setText("4");
        cell35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell35.setEnabled(false);
        box4.add(cell35);

        cell36.setEditable(false);
        cell36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell36.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell36.setText("8");
        cell36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell36.setEnabled(false);
        box4.add(cell36);

        sudokuBoard.add(box4);

        box5.setBackground(new java.awt.Color(255, 255, 255));
        box5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell37.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell37.setText("1");
        cell37.setToolTipText("click to set number");
        cell37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.add(cell37);

        cell38.setEditable(false);
        cell38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell38.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell38.setText("3");
        cell38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell38.setEnabled(false);
        box5.add(cell38);

        cell39.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell39.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell39.setText("4");
        cell39.setToolTipText("click to set number");
        cell39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.add(cell39);

        cell40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell40.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell40.setText("6");
        cell40.setToolTipText("click to set number");
        cell40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.add(cell40);

        cell41.setEditable(false);
        cell41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell41.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell41.setText("7");
        cell41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell41.setEnabled(false);
        box5.add(cell41);

        cell42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell42.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell42.setText("8");
        cell42.setToolTipText("click to set number");
        cell42.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.add(cell42);

        cell43.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell43.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell43.setText("5");
        cell43.setToolTipText("click to set number");
        cell43.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.add(cell43);

        cell44.setEditable(false);
        cell44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell44.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell44.setText("2");
        cell44.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell44.setEnabled(false);
        box5.add(cell44);

        cell45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell45.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell45.setText("9");
        cell45.setToolTipText("click to set number");
        cell45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box5.add(cell45);

        sudokuBoard.add(box5);

        box6.setBackground(new java.awt.Color(255, 255, 255));
        box6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box6.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell46.setEditable(false);
        cell46.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell46.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell46.setText("7");
        cell46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell46.setEnabled(false);
        box6.add(cell46);

        cell47.setEditable(false);
        cell47.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell47.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell47.setText("8");
        cell47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell47.setEnabled(false);
        box6.add(cell47);

        cell48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell48.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell48.setText("2");
        cell48.setToolTipText("click to set number");
        cell48.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box6.add(cell48);

        cell49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell49.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell49.setText("5");
        cell49.setToolTipText("click to set number");
        cell49.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box6.add(cell49);

        cell50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell50.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell50.setText("4");
        cell50.setToolTipText("click to set number");
        cell50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box6.add(cell50);

        cell51.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell51.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell51.setText("9");
        cell51.setToolTipText("click to set number");
        cell51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box6.add(cell51);

        cell52.setEditable(false);
        cell52.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell52.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell52.setText("1");
        cell52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell52.setEnabled(false);
        box6.add(cell52);

        cell53.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell53.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell53.setText("6");
        cell53.setToolTipText("click to set number");
        cell53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box6.add(cell53);

        cell54.setEditable(false);
        cell54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell54.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell54.setText("3");
        cell54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell54.setEnabled(false);
        box6.add(cell54);

        sudokuBoard.add(box6);

        box7.setBackground(new java.awt.Color(255, 255, 255));
        box7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box7.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell55.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell55.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell55.setText("6");
        cell55.setToolTipText("click to set number");
        cell55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box7.add(cell55);

        cell56.setEditable(false);
        cell56.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell56.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell56.setText("5");
        cell56.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell56.setEnabled(false);
        box7.add(cell56);

        cell57.setEditable(false);
        cell57.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell57.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell57.setText("2");
        cell57.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell57.setEnabled(false);
        box7.add(cell57);

        cell58.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell58.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell58.setText("9");
        cell58.setToolTipText("click to set number");
        cell58.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box7.add(cell58);

        cell59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell59.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell59.setText("8");
        cell59.setToolTipText("click to set number");
        cell59.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box7.add(cell59);

        cell60.setEditable(false);
        cell60.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell60.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell60.setText("1");
        cell60.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell60.setEnabled(false);
        box7.add(cell60);

        cell61.setEditable(false);
        cell61.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell61.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell61.setText("3");
        cell61.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell61.setEnabled(false);
        box7.add(cell61);

        cell62.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell62.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell62.setText("7");
        cell62.setToolTipText("click to set number");
        cell62.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box7.add(cell62);

        cell63.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell63.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell63.setText("4");
        cell63.setToolTipText("click to set number");
        cell63.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box7.add(cell63);

        sudokuBoard.add(box7);

        box8.setBackground(new java.awt.Color(255, 255, 255));
        box8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell64.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell64.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell64.setText("7");
        cell64.setToolTipText("click to set number");
        cell64.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell64);

        cell65.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell65.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell65.setText("8");
        cell65.setToolTipText("click to set number");
        cell65.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell65);

        cell66.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell66.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell66.setText("1");
        cell66.setToolTipText("click to set number");
        cell66.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell66);

        cell67.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell67.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell67.setText("3");
        cell67.setToolTipText("click to set number");
        cell67.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell67);

        cell68.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell68.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell68.setText("4");
        cell68.setToolTipText("click to set number");
        cell68.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell68);

        cell69.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell69.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell69.setText("5");
        cell69.setToolTipText("click to set number");
        cell69.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell69);

        cell70.setEditable(false);
        cell70.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell70.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell70.setText("9");
        cell70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell70.setEnabled(false);
        box8.add(cell70);

        cell71.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell71.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell71.setText("6");
        cell71.setToolTipText("click to set number");
        cell71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box8.add(cell71);

        cell72.setEditable(false);
        cell72.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell72.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell72.setText("2");
        cell72.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell72.setEnabled(false);
        box8.add(cell72);

        sudokuBoard.add(box8);

        box9.setBackground(new java.awt.Color(255, 255, 255));
        box9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.setLayout(new java.awt.GridLayout(3, 3, -1, -1));

        cell73.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell73.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell73.setText("3");
        cell73.setToolTipText("click to set number");
        cell73.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell73);

        cell74.setEditable(false);
        cell74.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell74.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell74.setText("9");
        cell74.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell74.setEnabled(false);
        box9.add(cell74);

        cell75.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell75.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell75.setText("4");
        cell75.setToolTipText("click to set number");
        cell75.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell75);

        cell76.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell76.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell76.setText("2");
        cell76.setToolTipText("click to set number");
        cell76.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell76);

        cell77.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell77.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell77.setText("7");
        cell77.setToolTipText("click to set number");
        cell77.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell77);

        cell78.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell78.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell78.setText("6");
        cell78.setToolTipText("click to set number");
        cell78.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell78);

        cell79.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell79.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell79.setText("8");
        cell79.setToolTipText("click to set number");
        cell79.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell79);

        cell80.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell80.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell80.setText("1");
        cell80.setToolTipText("click to set number");
        cell80.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        box9.add(cell80);

        cell81.setEditable(false);
        cell81.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cell81.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cell81.setText("5");
        cell81.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cell81.setEnabled(false);
        box9.add(cell81);

        sudokuBoard.add(box9);

        playSudokuScreen.add(sudokuBoard, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        quitButton.setText("Quit");
        quitButton.setToolTipText("Click to quit this game");
        quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                quitButtonMouseReleased(evt);
            }
        });
        playSudokuScreen.add(quitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 350, -1, -1));

        submitButton.setText("Submit");
        submitButton.setToolTipText("Click to submit solution");
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                submitButtonMouseReleased(evt);
            }
        });
        playSudokuScreen.add(submitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, -1, -1));

        displayTime3.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        displayTime3.setToolTipText("Current time");
        java.awt.event.ActionListener taskperformed3 = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayTime3.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            }
        };
        Timer timer3 = new Timer(1000, taskperformed3);
        //timer.setInitialDelay(1);
        timer3.start();
        playSudokuScreen.add(displayTime3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 28, -1, -1));

        playTitle1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 24)); // NOI18N
        playTitle1.setText("SUDOKU");
        playSudokuScreen.add(playTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        getContentPane().add(playSudokuScreen, "card10");

        endGamePanel.setPreferredSize(new java.awt.Dimension(600, 400));
        endGamePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalScore1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 36)); // NOI18N
        totalScore1.setForeground(new java.awt.Color(255, 0, 51));
        totalScore1.setText("Total Score");
        endGamePanel.add(totalScore1, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 152, -1, -1));

        gameOver1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 60)); // NOI18N
        gameOver1.setForeground(new java.awt.Color(255, 0, 51));
        gameOver1.setText("GAME OVER");
        endGamePanel.add(gameOver1, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 41, -1, -1));

        points1.setFont(new java.awt.Font("Stencil", 2, 24)); // NOI18N
        points1.setText("0 PTS");
        points1.setToolTipText("Total score");
        endGamePanel.add(points1, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 218, 110, 34));

        endButton1.setText("END");
        endButton1.setToolTipText("Click to go back to main menu");
        endButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endButton1ActionPerformed(evt);
            }
        });
        endGamePanel.add(endButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 373, -1, -1));

        popupPanel.setBackground(new java.awt.Color(204, 204, 204));
        popupPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        popupPanel.setLayout(null);

        initLabel.setFont(new java.awt.Font("Tekton Pro", 1, 24)); // NOI18N
        initLabel.setText("Enter your initials");
        popupPanel.add(initLabel);
        initLabel.setBounds(10, 50, 204, 32);

        initSaveButton.setText("Save");
        initSaveButton.setToolTipText("Click to save highscore");
        initSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initSaveButtonActionPerformed(evt);
            }
        });
        popupPanel.add(initSaveButton);
        initSaveButton.setBounds(80, 140, 61, 25);

        initInputField.setToolTipText("Enter up to three letters");
        initInputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                initInputFieldKeyReleased(evt);
            }
        });
        popupPanel.add(initInputField);
        initInputField.setBounds(50, 90, 130, 30);

        congrazLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        congrazLabel.setForeground(new java.awt.Color(255, 0, 0));
        congrazLabel.setText("YOU GOT A HIGH SCORE!!");
        popupPanel.add(congrazLabel);
        congrazLabel.setBounds(20, 10, 210, 30);

        endGamePanel.add(popupPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, 220, 180));

        getContentPane().add(endGamePanel, "card8");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //method: formWindowOpened
    //purpose: Display splash screen for only 3 seconds
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        long initTime, finalTime, waitTime;
        initTime = System.currentTimeMillis();
        finalTime = initTime;
        while (finalTime - initTime < 3000) {
            finalTime = System.currentTimeMillis();
        }
        getContentPane().remove(titleScreen);
        getContentPane().revalidate();
        getContentPane().repaint();
        
    }//GEN-LAST:event_formWindowOpened

    //method: playButtom
    //purpose: When the play button is pressed, we move to the playScreen panel.
    //We randomly select a word for the user to begin playing the game and
    //determine how many letters the user needs to guess.
    private void playButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtomActionPerformed
        
        mainMenu.setVisible(false);
        playScreen.setVisible(true);
        hangman1.setVisible(false);
        hangman2.setVisible(false);
        hangman3.setVisible(false);
        hangman4.setVisible(false);
        hangman5.setVisible(false);
        hangman6.setVisible(false);
        
        String wordList[] = {"ABSTRACT", "CEMETERY", "NURSE", 
            "PHARMACY", "CLIMBING"};
        Random rand = new Random();
        
       int index = rand.nextInt(5);
        
        if(index == 0)
            tmp = wordList[index];
        else if(index == 1)
            tmp = wordList[index];
        else if(index == 2)
            tmp = wordList[index];
        else if(index == 3)
            tmp = wordList[index];
        else if(index == 4)
            tmp = wordList[index];
        
        if (tmp.length() == 5)
        {
            dashedLine6.setVisible(false);
            dashedLine7.setVisible(false);
            dashedLine8.setVisible(false);
            
            char0.setText(tmp.charAt(0) + "");
            char1.setText(tmp.charAt(1) + "");
            char2.setText(tmp.charAt(2) + "");
            char3.setText(tmp.charAt(3) + "");
            char4.setText(tmp.charAt(4) + "");
            
            char0.setVisible(false);
            char1.setVisible(false);
            char2.setVisible(false);
            char3.setVisible(false);
            char4.setVisible(false);
            char5.setVisible(false);
            char6.setVisible(false);
            char7.setVisible(false);
        }
        else 
        {
            dashedLine6.setVisible(true);
            dashedLine7.setVisible(true);
            dashedLine8.setVisible(true);
            
            char0.setText(tmp.charAt(0) + "");
            char1.setText(tmp.charAt(1) + "");
            char2.setText(tmp.charAt(2) + "");
            char3.setText(tmp.charAt(3) + "");
            char4.setText(tmp.charAt(4) + "");
            char5.setText(tmp.charAt(5) + "");
            char6.setText(tmp.charAt(6) + "");
            char7.setText(tmp.charAt(7) + "");
            
            char0.setVisible(false);
            char1.setVisible(false);
            char2.setVisible(false);
            char3.setVisible(false);
            char4.setVisible(false);
            char5.setVisible(false);
            char6.setVisible(false);
            char7.setVisible(false);
        }
    }//GEN-LAST:event_playButtomActionPerformed
    //method: creditsButton
    //Purpose: To show the creators of the program
    private void creditsButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditsButtomActionPerformed
        mainMenu.setVisible(false);
        creditPanel.setVisible(true);
    }//GEN-LAST:event_creditsButtomActionPerformed
    
    //method: highscoresButtomActionPerformed
    //purpose: Display the top five scores 
    private void highscoresButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highscoresButtomActionPerformed
        mainMenu.setVisible(false);
        scorePanel.setVisible(true);
        
        //set high score
        scoreFirst.setText(scores.get(4).getInitial() + "");
        scoreSecond.setText(scores.get(3).getInitial() + "");
        scoreThird.setText(scores.get(2).getInitial() + "");
        scoreFourth.setText(scores.get(1).getInitial() + "");
        scoreFifth.setText(scores.get(0).getInitial() + "");
        firstData.setText(scores.get(4).getStringScore() + "");
        secondData.setText(scores.get(3).getStringScore() + "");
        thirdData.setText(scores.get(2).getStringScore() + "");
        fourthData.setText(scores.get(1).getStringScore() + "");
        fifthData.setText(scores.get(0).getStringScore() + "");
      
    }//GEN-LAST:event_highscoresButtomActionPerformed

    private void scoreBackButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreBackButtomActionPerformed
        scorePanel.setVisible(false);
        mainMenu.setVisible(true);
    }//GEN-LAST:event_scoreBackButtomActionPerformed

    private void creditBackButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditBackButtomActionPerformed
        creditPanel.setVisible(false);
        mainMenu.setVisible(true);
    }//GEN-LAST:event_creditBackButtomActionPerformed

    private void AMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AMouseClicked
        A.setEnabled(false);
        letterChecked(tmp, 'A');
    }//GEN-LAST:event_AMouseClicked

    //method: skipHangmanGame
    //Purpose: this methods entirely skips the first game and displays
    //the second game.
    private void skipGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipGameButtonActionPerformed
        playColorScreen.setVisible(true);
        playScreen.setVisible(false);
        gameScore = 0;
       
        playRandomizeCircles();
    }//GEN-LAST:event_skipGameButtonActionPerformed

    private void BMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BMouseClicked
        B.setEnabled(false);
        letterChecked(tmp, 'B');
    }//GEN-LAST:event_BMouseClicked

    private void CMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CMouseClicked
        C.setEnabled(false);
        letterChecked(tmp, 'C');
    }//GEN-LAST:event_CMouseClicked

    private void DMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DMouseClicked
        D.setEnabled(false);
        letterChecked(tmp, 'D');
    }//GEN-LAST:event_DMouseClicked

    private void EMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EMouseClicked
        E.setEnabled(false);
        letterChecked(tmp, 'E');
    }//GEN-LAST:event_EMouseClicked

    private void FMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FMouseClicked
        F.setEnabled(false);
        letterChecked(tmp, 'F');
    }//GEN-LAST:event_FMouseClicked

    private void GMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GMouseClicked
        G.setEnabled(false);
        letterChecked(tmp, 'G');
    }//GEN-LAST:event_GMouseClicked

    private void HMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HMouseClicked
        H.setEnabled(false);
        letterChecked(tmp, 'H');
    }//GEN-LAST:event_HMouseClicked

    private void IMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IMouseClicked
        I.setEnabled(false);
        letterChecked(tmp, 'I');
    }//GEN-LAST:event_IMouseClicked

    private void JMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JMouseClicked
        J.setEnabled(false);
        letterChecked(tmp, 'J');
    }//GEN-LAST:event_JMouseClicked

    private void KMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KMouseClicked
        K.setEnabled(false);
        letterChecked(tmp, 'K');
    }//GEN-LAST:event_KMouseClicked

    private void LMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LMouseClicked
        L.setEnabled(false);
        letterChecked(tmp, 'L');
    }//GEN-LAST:event_LMouseClicked

    private void MMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MMouseClicked
        M.setEnabled(false);
        letterChecked(tmp, 'M');
    }//GEN-LAST:event_MMouseClicked

    private void NMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NMouseClicked
        N.setEnabled(false);
        letterChecked(tmp, 'N');
    }//GEN-LAST:event_NMouseClicked

    private void OMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OMouseClicked
        O.setEnabled(false);
        letterChecked(tmp, 'O');
    }//GEN-LAST:event_OMouseClicked

    private void PMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PMouseClicked
        P.setEnabled(false);
        letterChecked(tmp, 'P');
    }//GEN-LAST:event_PMouseClicked

    private void QMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QMouseClicked
        Q.setEnabled(false);
        letterChecked(tmp, 'Q');
    }//GEN-LAST:event_QMouseClicked

    private void RMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RMouseClicked
        R.setEnabled(false);
        letterChecked(tmp, 'R');
    }//GEN-LAST:event_RMouseClicked

    private void SMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SMouseClicked
        S.setEnabled(false);
        letterChecked(tmp, 'S');
    }//GEN-LAST:event_SMouseClicked

    private void TMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TMouseClicked
        T.setEnabled(false);
        letterChecked(tmp, 'T');
    }//GEN-LAST:event_TMouseClicked

    private void UMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UMouseClicked
        U.setEnabled(false);
        letterChecked(tmp, 'U');
    }//GEN-LAST:event_UMouseClicked

    private void VMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VMouseClicked
        V.setEnabled(false);
        letterChecked(tmp, 'V');
    }//GEN-LAST:event_VMouseClicked

    private void WMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WMouseClicked
        W.setEnabled(false);
        letterChecked(tmp, 'W');
    }//GEN-LAST:event_WMouseClicked

    private void XMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XMouseClicked
        X.setEnabled(false);
        letterChecked(tmp, 'X');
    }//GEN-LAST:event_XMouseClicked

    private void YMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_YMouseClicked
        Y.setEnabled(false);
        letterChecked(tmp, 'Y');
    }//GEN-LAST:event_YMouseClicked

    private void ZMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZMouseClicked
        Z.setEnabled(false);
        letterChecked(tmp, 'Z');
    }//GEN-LAST:event_ZMouseClicked

    //method: endButton1
    //purpose: Go back to main Menu and reset everything to 
    //default values/commands.
    private void endButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endButton1ActionPerformed
        A.setEnabled(true);
        B.setEnabled(true);
        C.setEnabled(true);
        D.setEnabled(true);
        E.setEnabled(true);
        F.setEnabled(true);
        G.setEnabled(true);
        H.setEnabled(true);
        I.setEnabled(true);
        J.setEnabled(true);
        K.setEnabled(true);
        L.setEnabled(true);
        M.setEnabled(true);
        N.setEnabled(true);
        O.setEnabled(true);
        P.setEnabled(true);
        Q.setEnabled(true);
        R.setEnabled(true);
        S.setEnabled(true);
        T.setEnabled(true);
        U.setEnabled(true);
        V.setEnabled(true);
        W.setEnabled(true);
        X.setEnabled(true);
        Y.setEnabled(true);
        Z.setEnabled(true);
        mainMenu.setVisible(true);
        endGamePanel.setVisible(false);
        gameScore = 100;
        value.setText(gameScore + "");
    }//GEN-LAST:event_endButton1ActionPerformed

    private void redButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redButtonMouseEntered
        
        redButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/REDHILIGHTED.png"))); // NOI18N
        
    }//GEN-LAST:event_redButtonMouseEntered

    private void redButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redButtonMouseExited
    
        redButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/RED.png"))); // NOI18N

    }//GEN-LAST:event_redButtonMouseExited

    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButtonActionPerformed
        
        afterColorSelected(Color.RED);
    }//GEN-LAST:event_redButtonActionPerformed

    private void blueButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blueButtonMouseEntered
    
        blueButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/BLUEHILIGHTED.png"))); // NOI18N

    }//GEN-LAST:event_blueButtonMouseEntered

    private void blueButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blueButtonMouseExited

        blueButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/BLUE.png"))); // NOI18N

    }//GEN-LAST:event_blueButtonMouseExited

    private void blueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButtonActionPerformed
        
        afterColorSelected(Color.BLUE);
    }//GEN-LAST:event_blueButtonActionPerformed

    private void greenButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_greenButtonMouseEntered
       
        greenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/GREENHILIGHTED.png"))); // NOI18N
    }//GEN-LAST:event_greenButtonMouseEntered

    private void greenButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_greenButtonMouseExited

        greenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/GREEN.png"))); // NOI18N
    }//GEN-LAST:event_greenButtonMouseExited

    private void greenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_greenButtonActionPerformed

        afterColorSelected(Color.GREEN);
    }//GEN-LAST:event_greenButtonActionPerformed

    private void yellowButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yellowButtonMouseEntered

        yellowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/YELLOWHILIGHTED.png"))); // NOI18N
    }//GEN-LAST:event_yellowButtonMouseEntered

    private void yellowButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yellowButtonMouseExited

        yellowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/YELLOW.png"))); // NOI18N
    }//GEN-LAST:event_yellowButtonMouseExited

    private void yellowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yellowButtonActionPerformed
     
        afterColorSelected(Color.YELLOW);
    }//GEN-LAST:event_yellowButtonActionPerformed

    private void purpleButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purpleButtonMouseEntered

        purpleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/PURPLEHILIGHTED.png"))); // NOI18N
    }//GEN-LAST:event_purpleButtonMouseEntered

    private void purpleButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purpleButtonMouseExited

        purpleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cs245/PURPLE.png"))); // NOI18N
    }//GEN-LAST:event_purpleButtonMouseExited

    private void purpleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purpleButtonActionPerformed

        afterColorSelected(Color.MAGENTA);
    }//GEN-LAST:event_purpleButtonActionPerformed

    //method: SaveButton
    //purpose: Save the higherscore from the user and goes back to main Menu 
    // and reset everything to default values/commands.
    private void initSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initSaveButtonActionPerformed

        mainMenu.setVisible(true);
        popupPanel.setVisible(false);
        endGamePanel.setVisible(false);

        A.setEnabled(true);
        B.setEnabled(true);
        C.setEnabled(true);
        D.setEnabled(true);
        E.setEnabled(true);
        F.setEnabled(true);
        G.setEnabled(true);
        H.setEnabled(true);
        I.setEnabled(true);
        J.setEnabled(true);
        K.setEnabled(true);
        L.setEnabled(true);
        M.setEnabled(true);
        N.setEnabled(true);
        O.setEnabled(true);
        P.setEnabled(true);
        Q.setEnabled(true);
        R.setEnabled(true);
        S.setEnabled(true);
        T.setEnabled(true);
        U.setEnabled(true);
        V.setEnabled(true);
        W.setEnabled(true);
        X.setEnabled(true);
        Y.setEnabled(true);
        Z.setEnabled(true);

        //save the actual high score
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("src/cs245/scores.txt"));

            //user input
            String input = initInputField.getText();

            if (input.length() == 1) {
                input = input + " " + " ";
            }
            if (input.length() == 2) {
                input = input + " ";
            }

            String newHighestScore = input + String.format("%05d", gameScore);

            scores.remove(0);
            scores.add(new UserScore(newHighestScore));
            Collections.sort(scores);

            for (int i = 0; i < scores.size(); ++i) {
                pw.println(scores.get(i));
            }
            pw.flush();

            pw.close();
            gameScore = 100;
            value.setText(gameScore + " PTS");
            initInputField.setText("");
        } catch (IOException e) {
        }
    }//GEN-LAST:event_initSaveButtonActionPerformed
    
    //method: initInputFieldKeyReleased
    //purpose: The user is only allowed to put thier initials from 
    //length of 1-3. Otherwise, disable the save button.
    private void initInputFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_initInputFieldKeyReleased
       
        if(initInputField.getText().trim().length() <= 0 || initInputField.getText().trim().length() > 3) {
            initSaveButton.setEnabled(false);
        }else{
            initSaveButton.setEnabled(true);
        }
        
    }//GEN-LAST:event_initInputFieldKeyReleased

    private void cellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cellActionPerformed
        playSudokuScreen.requestFocusInWindow();
        
    }//GEN-LAST:event_cellActionPerformed
    
    //method: cellFocusLost
    //purpose: Check whether or not a number 1-9 is entered in each cell field
    private void cellFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cellFocusLost
        javax.swing.JTextField curCell = (javax.swing.JTextField) evt.getComponent();
        if (!curCell.getText().equals("1") && !curCell.getText().equals("2") &&
            !curCell.getText().equals("3") && !curCell.getText().equals("4") &&
            !curCell.getText().equals("5") && !curCell.getText().equals("6") &&
            !curCell.getText().equals("7") && !curCell.getText().equals("8") &&
            !curCell.getText().equals("9") && !curCell.getText().equals("")) {
            javax.swing.JOptionPane.showMessageDialog(rootPane, "Please input a number 1-9 only.");
            curCell.requestFocusInWindow();
        }
    }//GEN-LAST:event_cellFocusLost
    
    //method: quitButtonMouseReleased
    //purpose: takes the user to the end screen if decides to quit sudoku game
    private void quitButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitButtonMouseReleased
        points1.setText(gameScore + "");
        endGamePanel.setVisible(true);
        playSudokuScreen.setVisible(false);

        if (gameScore >= scores.get(0).getScore()) {
            //popup come out
            popupPanel.setVisible(true);
            //save to file
        }
        //disable save button
        initSaveButton.setEnabled(false);
    }//GEN-LAST:event_quitButtonMouseReleased

    private void submitButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitButtonMouseReleased

        checkBoard();
    }//GEN-LAST:event_submitButtonMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PointAndClick.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PointAndClick.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PointAndClick.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PointAndClick.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try {
                    new PointAndClick().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(PointAndClick.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A;
    private javax.swing.JButton B;
    private javax.swing.JButton C;
    private javax.swing.JButton D;
    private javax.swing.JButton E;
    private javax.swing.JButton F;
    private javax.swing.JButton G;
    private javax.swing.JButton H;
    private javax.swing.JButton I;
    private javax.swing.JButton J;
    private javax.swing.JButton K;
    private javax.swing.JButton L;
    private javax.swing.JButton M;
    private javax.swing.JButton N;
    private javax.swing.JButton O;
    private javax.swing.JButton P;
    private javax.swing.JButton Q;
    private javax.swing.JButton R;
    private javax.swing.JButton S;
    private javax.swing.JButton T;
    private javax.swing.JButton U;
    private javax.swing.JButton V;
    private javax.swing.JButton W;
    private javax.swing.JButton X;
    private javax.swing.JButton Y;
    private javax.swing.JButton Z;
    private javax.swing.JButton blueButton;
    private javax.swing.JPanel box1;
    private javax.swing.JPanel box2;
    private javax.swing.JPanel box3;
    private javax.swing.JPanel box4;
    private javax.swing.JPanel box5;
    private javax.swing.JPanel box6;
    private javax.swing.JPanel box7;
    private javax.swing.JPanel box8;
    private javax.swing.JPanel box9;
    private javax.swing.JTextField cell1;
    private javax.swing.JTextField cell10;
    private javax.swing.JTextField cell11;
    private javax.swing.JTextField cell12;
    private javax.swing.JTextField cell13;
    private javax.swing.JTextField cell14;
    private javax.swing.JTextField cell15;
    private javax.swing.JTextField cell16;
    private javax.swing.JTextField cell17;
    private javax.swing.JTextField cell18;
    private javax.swing.JTextField cell19;
    private javax.swing.JTextField cell2;
    private javax.swing.JTextField cell20;
    private javax.swing.JTextField cell21;
    private javax.swing.JTextField cell22;
    private javax.swing.JTextField cell23;
    private javax.swing.JTextField cell24;
    private javax.swing.JTextField cell25;
    private javax.swing.JTextField cell26;
    private javax.swing.JTextField cell27;
    private javax.swing.JTextField cell28;
    private javax.swing.JTextField cell29;
    private javax.swing.JTextField cell3;
    private javax.swing.JTextField cell30;
    private javax.swing.JTextField cell31;
    private javax.swing.JTextField cell32;
    private javax.swing.JTextField cell33;
    private javax.swing.JTextField cell34;
    private javax.swing.JTextField cell35;
    private javax.swing.JTextField cell36;
    private javax.swing.JTextField cell37;
    private javax.swing.JTextField cell38;
    private javax.swing.JTextField cell39;
    private javax.swing.JTextField cell4;
    private javax.swing.JTextField cell40;
    private javax.swing.JTextField cell41;
    private javax.swing.JTextField cell42;
    private javax.swing.JTextField cell43;
    private javax.swing.JTextField cell44;
    private javax.swing.JTextField cell45;
    private javax.swing.JTextField cell46;
    private javax.swing.JTextField cell47;
    private javax.swing.JTextField cell48;
    private javax.swing.JTextField cell49;
    private javax.swing.JTextField cell5;
    private javax.swing.JTextField cell50;
    private javax.swing.JTextField cell51;
    private javax.swing.JTextField cell52;
    private javax.swing.JTextField cell53;
    private javax.swing.JTextField cell54;
    private javax.swing.JTextField cell55;
    private javax.swing.JTextField cell56;
    private javax.swing.JTextField cell57;
    private javax.swing.JTextField cell58;
    private javax.swing.JTextField cell59;
    private javax.swing.JTextField cell6;
    private javax.swing.JTextField cell60;
    private javax.swing.JTextField cell61;
    private javax.swing.JTextField cell62;
    private javax.swing.JTextField cell63;
    private javax.swing.JTextField cell64;
    private javax.swing.JTextField cell65;
    private javax.swing.JTextField cell66;
    private javax.swing.JTextField cell67;
    private javax.swing.JTextField cell68;
    private javax.swing.JTextField cell69;
    private javax.swing.JTextField cell7;
    private javax.swing.JTextField cell70;
    private javax.swing.JTextField cell71;
    private javax.swing.JTextField cell72;
    private javax.swing.JTextField cell73;
    private javax.swing.JTextField cell74;
    private javax.swing.JTextField cell75;
    private javax.swing.JTextField cell76;
    private javax.swing.JTextField cell77;
    private javax.swing.JTextField cell78;
    private javax.swing.JTextField cell79;
    private javax.swing.JTextField cell8;
    private javax.swing.JTextField cell80;
    private javax.swing.JTextField cell81;
    private javax.swing.JTextField cell9;
    private javax.swing.JLabel char0;
    private javax.swing.JLabel char1;
    private javax.swing.JLabel char2;
    private javax.swing.JLabel char3;
    private javax.swing.JLabel char4;
    private javax.swing.JLabel char5;
    private javax.swing.JLabel char6;
    private javax.swing.JLabel char7;
    private javax.swing.JLabel colorText;
    private javax.swing.JLabel congrazLabel;
    private javax.swing.JButton creditBackButtom;
    private javax.swing.JPanel creditPanel;
    private javax.swing.JLabel creditTitle;
    private javax.swing.JButton creditsButtom;
    private javax.swing.JLabel dashedLine1;
    private javax.swing.JLabel dashedLine2;
    private javax.swing.JLabel dashedLine3;
    private javax.swing.JLabel dashedLine4;
    private javax.swing.JLabel dashedLine5;
    private javax.swing.JLabel dashedLine6;
    private javax.swing.JLabel dashedLine7;
    private javax.swing.JLabel dashedLine8;
    private javax.swing.JLabel displayColorScreenTime;
    private javax.swing.JLabel displayTime;
    private javax.swing.JLabel displayTime3;
    private javax.swing.JButton endButton1;
    private javax.swing.JPanel endGamePanel;
    private javax.swing.JLabel fifthData;
    private javax.swing.JLabel firstData;
    private javax.swing.JLabel fourthData;
    private javax.swing.JLabel gameOver1;
    private javax.swing.JButton greenButton;
    private javax.swing.JLabel hangman1;
    private javax.swing.JLabel hangman2;
    private javax.swing.JLabel hangman3;
    private javax.swing.JLabel hangman4;
    private javax.swing.JLabel hangman5;
    private javax.swing.JLabel hangman6;
    private javax.swing.JLabel hangmanTree;
    private javax.swing.JButton highscoresButtom;
    private javax.swing.JTextField initInputField;
    private javax.swing.JLabel initLabel;
    private javax.swing.JButton initSaveButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel line1;
    private javax.swing.JLabel line2;
    private javax.swing.JLabel line3;
    private javax.swing.JLabel line4;
    private javax.swing.JLabel line5;
    private javax.swing.JPanel mainMenu;
    private javax.swing.JLabel name1;
    private javax.swing.JLabel name2;
    private javax.swing.JLabel name3;
    private javax.swing.JLabel name4;
    private javax.swing.JButton playButtom;
    private javax.swing.JPanel playColorScreen;
    private javax.swing.JPanel playScreen;
    private javax.swing.JPanel playSudokuScreen;
    private javax.swing.JLabel playTitle;
    private javax.swing.JLabel playTitle1;
    private javax.swing.JLabel points1;
    private javax.swing.JPanel popupPanel;
    private javax.swing.JButton purpleButton;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton redButton;
    private javax.swing.JLabel score;
    private javax.swing.JButton scoreBackButtom;
    private javax.swing.JLabel scoreFifth;
    private javax.swing.JLabel scoreFirst;
    private javax.swing.JLabel scoreFourth;
    private javax.swing.JPanel scorePanel;
    private javax.swing.JLabel scoreSecond;
    private javax.swing.JLabel scoreThird;
    private javax.swing.JLabel scoreTitle;
    private javax.swing.JLabel secondData;
    private javax.swing.JButton skipGameButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JPanel sudokuBoard;
    private javax.swing.JLabel team;
    private javax.swing.JLabel thirdData;
    private javax.swing.JLabel title;
    private javax.swing.JPanel titleScreen;
    private javax.swing.JLabel totalScore1;
    private javax.swing.JLabel value;
    private javax.swing.JButton yellowButton;
    // End of variables declaration//GEN-END:variables
}
