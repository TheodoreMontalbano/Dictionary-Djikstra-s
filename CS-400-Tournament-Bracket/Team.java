///////////////////////////////////////////////////////////////////////////////
// Assignment Name: Tournament Bracket
// Author: Shashwat Srivastava, Theodore Montalbano
// Partners: Jerry Yu, Tianyuan Yuan, Griff Zhang
// Email : ssrivastav26@wisc.edu, tmontalbano@wisc.edu
// CS Login: shashwat, montalbano
// Credits: None
// Due Date: May 3, 2018
// Known Bugs: None
//////////////////////////////////////////////////////////////////////////////

package application;
/**
 * The Team class represents each team in the tournament. 
 * It contains the name and rank (for seeding) of the team.
 */
public class Team {
    private String name;
    private int rank;
    /**
     *  getter for initial rank of the team
     *  setter for round is not required because it is set only once when the constructor is called
     *  according to the position in the file
     */
    public Team(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }
    public int getRank() {
        return rank;
    }
    /**
     *  getter for round
     */
    public String getName() {
        return name;
    }
}
