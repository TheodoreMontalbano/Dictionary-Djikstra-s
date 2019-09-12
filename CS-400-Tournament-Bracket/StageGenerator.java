///////////////////////////////////////////////////////////////////////////////
// assignment name: Tournament Bracket
// Author: Tianyuan(Rainer) Yuan
// Partner:THEODORE MONTALBANO SHASHWAT SRIVASTAVA GRIFF ZHANG
// Email : tyuan22@wisc.edu
// due date: April 15th 2018
// CS Login: tyuan22
// Credits: none
// known bugs: none
//////////////////////////////////////////////////////////////////////////////

package application;

import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
/**
 * Generate stage for the whole program
 */
public class StageGenerator {
    
    /**
     * Default constructor for creating stagegenerator object
     */
    public StageGenerator() {

    };

    Pane outside = new Pane();//field for overall display
    
    /**
     * take in a filepath and do all the pre-calculation for displaying 
     * @param filepath
     * @return return a Pane for displaying
     * @throws IOException
     */

    public Scene Scene(String filepath) throws IOException {
        Tournament myTournament = new Tournament(filepath);//generate a tournament object
        if (myTournament.getMatch() != null) {
            Match matches[] = myTournament.getMatch(); // get the matches list in order to display 
            HBox matchbox[] = new HBox[matches.length];// generate a HBox for each match            
            for (int i = 0; i < matches.length; i++) {
                HBox top = new HBox(10);
                HBox bootom = new HBox(10);
                VBox holds = new VBox(10);
                HBox bigbox = new HBox(10);
                matches[i].getTextOne().setMaxWidth(40);
                matches[i].getTextTwo().setMaxWidth(40);
                matches[i].getSubmitButton().setMinHeight(62);
                matches[i].getLabelOne().setMinWidth(65);
                matches[i].getLabelTwo().setMinWidth(65);
                matches[i].getLabelOne().setMaxWidth(65);
                matches[i].getLabelOne().setAlignment(Pos.BASELINE_RIGHT);
                matches[i].getLabelTwo().setAlignment(Pos.BASELINE_RIGHT);
                matches[i].getLabelTwo().setMaxWidth(65);
                matches[i].getLabelOne().setFont(Font.font ("Verdana", 14));
                matches[i].getLabelTwo().setFont(Font.font ("Verdana", 14));
                top.getChildren().addAll(matches[i].getLabelOne(), matches[i].getTextOne());
                bootom.getChildren().addAll(matches[i].getLabelTwo(), matches[i].getTextTwo());
                holds.getChildren().addAll(top, bootom);
                bigbox.getChildren().addAll(holds, matches[i].getSubmitButton());
                matchbox[i] = bigbox;
            }
            //generate the GUI for 16 team
            HBox center = new HBox();
            if (matches.length == 15) { 
                VBox left = new VBox(50);
                VBox right = new VBox(50);
                VBox left2 = new VBox(150);
                VBox right2 = new VBox(150);
                VBox left3 = new VBox(50);
                VBox right3 = new VBox(50);
                left.getChildren().addAll(matchbox[14], matchbox[13], matchbox[12], matchbox[11]);
                left.setLayoutX(20);
                left.setLayoutY(20);
                right.getChildren().addAll(matchbox[7], matchbox[8], matchbox[9], matchbox[10]);
                right.setLayoutX(1200);
                right.setLayoutY(20);
                left2.getChildren().addAll(matchbox[6], matchbox[5]);
                left2.setLayoutX(220);
                left2.setLayoutY(85);
                right2.getChildren().addAll(matchbox[3], matchbox[4]);
                right2.setLayoutX(1000);
                right2.setLayoutY(85);
                left3.getChildren().addAll(matchbox[2]);
                left3.setLayoutX(400);
                left3.setLayoutY(190);
                right3.getChildren().addAll(matchbox[1]);
                right3.setLayoutX(800);
                right3.setLayoutY(190);
                center.getChildren().addAll(matchbox[0]);
                center.setLayoutX(600);
                center.setLayoutY(190);
                outside.getChildren().addAll(left, left2, left3, center, right3, right2, right);
            } else if (matches.length == 7) {//generate the GUI for 8 team
                VBox left = new VBox(300);
                VBox right = new VBox(300);
                VBox left2 = new VBox(150);
                VBox right2 = new VBox(150);
                left.getChildren().addAll(matchbox[6], matchbox[5]);
                left.setLayoutY(20);
                right.getChildren().addAll(matchbox[3], matchbox[4]);
                right.setLayoutX(1200);
                right.setLayoutY(20);
                left2.getChildren().addAll(matchbox[2]);
                left2.setLayoutX(300);
                left2.setLayoutY(190);
                right2.getChildren().addAll(matchbox[1]);
                right2.setLayoutX(900);
                right2.setLayoutY(190);
                center.getChildren().addAll(matchbox[0]);
                center.setLayoutX(600);
                center.setLayoutY(190);
                outside.getChildren().addAll(left, left2, center, right2, right);
            } else if (matches.length == 3) { //generate the GUI for 4 team
                VBox left = new VBox(100);
                VBox right = new VBox(100);
                left.getChildren().addAll(matchbox[2]);
                left.setLayoutX(400);
                left.setLayoutY(170);
                right.getChildren().addAll(matchbox[1]);
                right.setLayoutX(800);
                right.setLayoutY(170);
                center.getChildren().addAll(matchbox[0]);
                center.setLayoutX(600);
                center.setLayoutY(170);
                outside.getChildren().addAll(left, center, right);
            } else if (matches.length == 1) {//generate the GUI for 2 team
                center.getChildren().addAll(matchbox[0]);
                center.setLayoutX(600);
                center.setLayoutY(170);
                outside.getChildren().addAll(center);
            }
        } else {// handle the problem when there is no match
            
            if (myTournament.getTeam() == null) { // when there no team in the file 
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText(
                                "Please check you importing file, there is no team entered in the contest");
                alert.showAndWait();
            } else if (myTournament.getTeam().length == 1) { // when there is one team in file and display the winner
                Team Teams[] = myTournament.getTeam();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Result");
                alert.setHeaderText(null);
                alert.setContentText("The Winner is: " + Teams[0].getName());
                alert.showAndWait();
            }
        }
        //label for operation tips 
        Label operation = new Label("Please enter scores in the coresponding text field :)");
        operation.setLayoutX(450);
        operation.setLayoutY(400);
        operation.setFont(Font.font ("Verdana", 20));
        //final team label
        Label finalgame = new Label("Final game");
        finalgame.setLayoutY(150);
        finalgame.setLayoutX(650);
        finalgame.setFont(Font.font ("Verdana", 20));
        outside.getChildren().addAll(finalgame,operation);
        Scene scene1 = new Scene(outside, 1380, 470);
        return scene1;
    };
}
