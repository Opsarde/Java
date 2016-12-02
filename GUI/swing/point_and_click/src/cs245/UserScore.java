/**************************************************************
 *  File: UserScore.java
 *  Author: Nabor Palomera
 *          Ubaldo Jimenez Prieto
 *          Shun Lu
 *          WeiYing Lee
 * 
 * Assignment: Point and Click Game Version 1.2
 * Date Last Modified: 10/25/2016
 * 
 * Purpose: This programs purpose is to get the user score and 
 * initials when a new high score is reached. We implemented all 
 * the getters to make it easier to retrieve the score when 
 * needed.
 *  
 *************************************************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs245;

/**
 * UserScore class that store user's initial with score.
 */
public class UserScore implements Comparable<UserScore>{

    //This field stores user initial.
    private String initial;
    //This field stores user score.
    private int score;
    //This field store strings of initial and score.
    private String stringLine;
    
    //method: Constructor
    //Purpose: constructor that initialize empty score.
    public UserScore() {
        stringLine = "ABC00000";
        initial = stringLine.substring(0, 3);
        score = Integer.parseInt(stringLine.substring(3));
    }
    
    //method: initialization constructor 
    //purpose: constructor that initialize score and initial.
    public UserScore(String stringLine) {
        this.stringLine = stringLine;
        initial = stringLine.substring(0, 3);
        score = Integer.parseInt(stringLine.substring(3));
    }
    
    //method: getter for initial
    //purpose: get initial from this.
    public String getInitial() {
        return initial;
    }
    
    //method: getter for string score
    //purpose: get score in string format from this.
    public String getStringScore() {
        return stringLine.substring(3);
    }
    
    //method: getter for int score
    //purpose: get score from this.
    public int getScore() {
        return score;
    }
    
    //method: gets the whole line which includes initial and score
    //purpose: get stringLine from this.
    public String getStringLine() {
        return stringLine;
    }
    
    //method: compareScores
    //purpose: override compareTo method in Comparable
    @Override
    public int compareTo(UserScore o) {
        if (this.score > o.getScore()) {
            return 1;
        }
        else if (this.score < o.getScore()) {
            return -1;
        }
        else
            return 0;
    }
    
    //method: toString
    //purpose: override toString method & return stringLine.
    @Override
    public String toString() {
        return stringLine;
    }
}