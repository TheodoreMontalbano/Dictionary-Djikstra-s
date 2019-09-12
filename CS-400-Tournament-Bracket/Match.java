///////////////////////////////////////////////////////////////////////////////
// Assignment Name: Tournament Bracket
// Author: Jerry Yu
// Partners: Shashwat Srivastava, Tianyuan Yuan, Griff Zhang, Theodore Montalbano
// Email : jyu283@wisc.edu
// CS Login: jyu
// Credits: None
// Due Date: May 3, 2018
// Known Bugs: None
//////////////////////////////////////////////////////////////////////////////

package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * The Match class stores data of individual matches, and handles user interaction
 * including entering and submitting scores. Tie games are not allowed. After valid
 * scores are entered, a winner is generated and the next match is updated. <br>
 * Special cases: semifinal and final games are handled differently. Semi-final games
 * will be stored in a static array to help determine the third place winner of the 
 * tournament. The final game will produce a pop-up alert box informing the user of 
 * the champion, second place, and third place winner of the tournament. 
 * 
 * @author Jerry Yu
 *
 */
public class Match {
    private Team teamOne;
    private Team teamTwo;
    private int scoreOne = -999;
    private int scoreTwo = -999;

    private Button submitButton = new Button("OK");
    // Labels for team names are defaulted to TBD
    private Label teamOneLabel = new Label("TBD"); 
    private Label teamTwoLabel = new Label("TBD");
    private TextField teamOneIn = new TextField();
    private TextField teamTwoIn = new TextField();

    private Match next;

    // stores the two semifinal games in order to determine the third place winner.
    static Match[] semifinals = new Match[2];

    /**
     * Create a new Match object provided the two teams. Initialize the two labels
     * accordingly. Scores for both teams are initialized to -999 to differentiate
     * from actual zero scores in games.
     * @param t1    Team One
     * @param t2    Team Two
     */
    public Match(Team t1, Team t2) {
    	teamOneLabel.setAlignment(Pos.CENTER_RIGHT);
    	teamTwoLabel.setAlignment(Pos.CENTER_RIGHT);

        setTeamOne(t1);
        setTeamTwo(t2);
        // Event listeners and handlers
        teamOneIn.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                try {
                                    scoreTwo = Integer.parseInt(teamTwoIn.getText());
                                } catch (Exception ex) {
                                    invalidEntry();
                                    return;
                                }}});
        teamTwoIn.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                try {
                                    scoreTwo = Integer.parseInt(teamTwoIn.getText());
                                } catch (Exception ex) {
                                    invalidEntry();
                                    return;
                                }
                            }});
        submitButton.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) { 
                            	try {
                            		submit(); 
                            	} catch (NumberFormatException numE) {
                            		numberFormat();
                            		return;
                            	} catch (Exception e) {
                            		invalidEntry();
                            		return;
                            	}
                            }});
    }
    

    /**
     * Create a new Match object with no team information. Initialize the two labels
     * to "TBD" (To Be Decided). Scores for both teams are initialized to -999.
     */
    public Match() {
    	teamOneLabel.setAlignment(Pos.CENTER_RIGHT);
    	teamTwoLabel.setAlignment(Pos.CENTER_RIGHT);

        // Event listeners and handlers
        teamOneIn.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                try { scoreOne = Integer.parseInt(teamOneIn.getText()); }
                                catch (Exception ex) { 
                                    invalidEntry(); 
                                    return;
                                }
                            }});
        teamTwoIn.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                try {
                                    scoreTwo = Integer.parseInt(teamTwoIn.getText());
                                } catch (Exception ex) {
                                    invalidEntry();
                                    return;
                                }
                            }});
        submitButton.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) { 
                            	try {
                            		submit(); 
                            	} catch (NumberFormatException numE) {
                            		numberFormat();
                            		return;
                            	} catch (Exception e) {
                            		invalidEntry();
                            		return;
                            	}
                            }});
    }

    private static void invalidEntry() {
        Alert invalid = new Alert(AlertType.WARNING);
        invalid.setTitle("WARNING");
        invalid.setHeaderText(null);
        invalid.setContentText("Invalid entry.");
        invalid.showAndWait();
    }

    private static void numberFormat() {
    	Alert format = new Alert(AlertType.WARNING);
    	format.setTitle("WARNING");
    	format.setHeaderText(null);
    	format.setContentText("Invalid entry: Numbers only.");
    	format.showAndWait();
    }
    
    /**
     * Event handling for the submit button.
     */
    private void submit() {
        // If either team in the current game has not been set, warn the user.
        if (teamOne == null || teamTwo == null) {
            Alert noTeam = new Alert(AlertType.WARNING);
            noTeam.setTitle("WARNING");
            noTeam.setHeaderText(null);
            noTeam.setContentText("No team information.");
            noTeam.showAndWait();
            return;
        }
        // if either text field is empty, warn the user.
        if (teamOneIn.getText().isEmpty() || teamTwoIn.getText().isEmpty()) {
            invalidEntry();
            return;
        }
        // if the two score inputs are the same, warn the user (no tie allowed).
        if (teamOneIn.getText().equals(teamTwoIn.getText())) {
            Alert sameScore = new Alert(AlertType.WARNING);
            sameScore.setTitle("WARNING");
            sameScore.setHeaderText(null);
            sameScore.setContentText("Invalid score: Ties not permitted.");
            sameScore.showAndWait();
            return;
        }
        // no error, read in the scores.
        scoreOne = Integer.parseInt(teamOneIn.getText());
        scoreTwo = Integer.parseInt(teamTwoIn.getText());
        if (next == null) { // if this game is the final game
            // if the semi-final games have not been recorded, warn the user.
            Team champion = getWinner();
            Team secondPlace;
            if (teamOne.equals(champion)) secondPlace = teamTwo;
            else secondPlace = teamOne;
            // Compute third place team
            Alert results = new Alert(AlertType.INFORMATION);
            results.setTitle("Tournament Results");
            results.setHeaderText(null);
            if (semifinals[0] == null && semifinals[1] == null) {
            	results.setContentText("Champion: " + champion.getName() + "\n"
            			+ "Second place: " + secondPlace.getName());
            } else if (semifinals[0] == null || semifinals[1] == null) {
            	Alert incompSemi = new Alert(AlertType.WARNING);
            	incompSemi.setTitle("WARNING");
            	incompSemi.setHeaderText(null);
            	incompSemi.setContentText("Incomplete semi-final data.");
            	incompSemi.showAndWait();
            	return;
            } else if (semifinals[0] != null && semifinals[1] != null) {
            	Team thirdPlace = getThirdPlace();
                results.setContentText("Champion: " + champion.getName() + "\n"
                        + "Second place: " + secondPlace.getName() + "\n"
                        + "Third place: " + thirdPlace.getName());
            }
            results.showAndWait();
        } else if (next.next == null) { // if this game is semi-final
            if (next.teamOneLabel.getText().equals("TBD")) 
                next.setTeamOne(getWinner());
            else next.setTeamTwo(getWinner());
            // store game info to determine third place after grand final
            storeSemifinal();
        } else { // if this is a normal game
            if (next.teamOneLabel.getText().equals("TBD")) 
                next.setTeamOne(getWinner());
            else next.setTeamTwo(getWinner());
        }
        // after scores have been entered, disable the submit button.
        submitButton.setDisable(true);
    }

    /** 
     * If current match is a semi-final game, store the match info in the static array.
     */
    private void storeSemifinal() {
        if (semifinals[0] == null) semifinals[0] = this;
        else semifinals[1] = this;
    }

    /**
     * Get the third place winner after the semi-final is complete. Third place winner is 
     * the one of the two semi-final losers with the higher score.
     */
    private static Team getThirdPlace() {
        Team thirdPlace;
        // determine the loser of semifinal one
        Team semiOneLoser;
        int loserOneScore;
        if (semifinals[0].scoreOne > semifinals[0].scoreTwo) {
            semiOneLoser = semifinals[0].teamTwo;
            loserOneScore = semifinals[0].scoreTwo;
        } else {
            semiOneLoser = semifinals[0].teamOne;
            loserOneScore = semifinals[0].scoreOne;
        }
        // determine the loser of semifinal two
        Team semiTwoLoser;
        int loserTwoScore;
        if (semifinals[1].scoreOne > semifinals[1].scoreTwo) {
            semiTwoLoser = semifinals[1].teamTwo;
            loserTwoScore = semifinals[1].scoreTwo;
        } else { 
            semiTwoLoser = semifinals[1].teamOne;
            loserTwoScore = semifinals[1].scoreOne;
        }
        // compare the scores of two losing teams, the team that scored more is the third place
        if (loserOneScore > loserTwoScore) thirdPlace = semiOneLoser;
        else  thirdPlace = semiTwoLoser;
        return thirdPlace;
    }

    /**
     * Determine the winner of the match based on the scores. Throws an
     * IllegalArgumentException if the two scores are the same.
     * @return  The winning team.
     */
    public Team getWinner() {
        // if either score is empty, return null
        if (scoreOne == -999 || scoreTwo == -999) return null;
        if (scoreOne == scoreTwo) throw new IllegalArgumentException();
        return (scoreOne > scoreTwo) ? teamOne : teamTwo;
    }

    public void setNext(Match n) { next = n; }


    public void setTeamOne(Team t1) {
        teamOne = t1;
        teamOneLabel.setText(t1.getName());
    }

    public void setTeamTwo(Team t2) {
        teamTwo = t2;
        teamTwoLabel.setText(t2.getName());
    }

    public Match getNext() { return next; }

    /**
     * Set the scores for both teams.
     * @param score1    Score of Team One.
     * @param score2    Score of Team Two.
     */
    public void setScore(int score1, int score2) {
        scoreOne = score1;
        scoreTwo = score2;
    }

    /** Return TeamOne's score. */
    public int getScore1() { return scoreOne; }

    /** Return TeamTwo's score. */
    public int getScore2() { return scoreTwo; }

    public Label getLabelOne() { return teamOneLabel; }

    public Label getLabelTwo() { return teamTwoLabel; }

    public TextField getTextOne() { return teamOneIn; }

    public TextField getTextTwo() { return teamTwoIn; }

    public Button getSubmitButton() { return submitButton; }

}
