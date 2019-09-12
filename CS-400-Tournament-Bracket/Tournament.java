///////////////////////////////////////////////////////////////////////////////
// assignment name: Tournament Bracket
// Author: Theodore Montalbano Shashwat Srivastava
// Partner: Griff Zhang, Jerry Yu, Tianyuan(Rainer) Yuan
// Email : tmontalbano@wisc.edu, ssrivastav26@wisc.edu
// due date: April 15th 2018
// CS Login: shashwat@cs.wisc.edu, montalbano@cs.wisc.edu
// Credits: Maulin Vasavada from: https://coderanch.com/t/371069/java/arrange-numbers
// known bugs: none
//////////////////////////////////////////////////////////////////////////////
package application;
/**
 * The Tournament class represents the tournament that is being played. 
 * The tournament class contains the name of the tournament,
 *  Team class instances (which represent each team), an array of matches to be played 
 *  (which acts like our main data structure) 
 *  and specific instances of the Team class which represent the champion, runner-up and the third place holder.
 *  This class is responsible for taking in the input from the file, seeding and storing it in the main data structure.
 *  The seeding algorithm taken from online source 
 * @author Shashwat Srivastava, Theodore Montalbano
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tournament {
	/**
	 * Array that represents the tournament bracket
	 */
	private Match[] matches;
	/**
	 * Array that holds all of the teams participating in tournament.
	 */
	private Team[] teams;
	/**
	 * Constructor to call all the respective methods that 
	 * 	read the file, seed the teams and store them in the
	 * 	respective teams and matches arrays.
	 * @param filePath the path of the file that needs to
	 * 	be read
	 * @throws IOException if the file is not read properly
	 */
	public Tournament (String filePath) throws IOException {
		readInput(filePath);
	}
	/**
	 * The method is responsible for reading the input from
	 * 	the file preferably using streams
	 * the method then calls seed to seed all the teams 
	 *	and store 
	 * @param filePath the name of the file from which the
	 * 	names of the teams needs to be extracted
	 * @throws IOException if the file is not accessed properly
	 */
	 private void readInput(String filePath) throws IOException {
		 Stream<String> teamStream;
		 List<String> teamsList = new ArrayList<String>();
		 teamStream = Files.lines(Paths.get(filePath)).map(String::trim);
		//Remove empty lines
		teamStream = teamStream.filter(x -> x != null && !x.equals(""));
		teamsList = teamStream.collect(Collectors.toList());
		int rank = 1;
		teams = new Team[teamsList.size()];
		for(String teamName: teamsList) {
			teams[rank - 1] = new Team(teamName, rank);
			rank++;
		}
		if(teamsList.size() < 2) {
			if(teams.length != 1)
				teams = null;
			}
		else
			seed();
	}
	 /**
	  * The recursive method helper method gotten from online source that help 
	  * 	algorithm seed the teams
	  * @author Maulin Vasavada
	  * from: https://coderanch.com/t/371069/java/arrange-numbers
	  * @param N the number of teams in the tournament
	  * @param n the position where the teams of a respective rank will be indexed
	  * @return the particular rank of a team with a particular rank
	  */
	 private static double seedHelper(int N, int n) {
		 double d = Math.pow(2,N) + 1;
		 if ( n == 2) { // the base case
		 return d - 1;
		 	}
		 // the rest of the two cases adhere to the 
		 // formula function that the source provides
		 // to seed the teams
		 else if ( n % 2 == 1 ) {
		 return  d - seedHelper(N-1, (n+1)/2);
		 	} 
		 else {
		 return seedHelper(N-1,n/2);
		 	}
		 }
	 /**
	  * Returns log base 2 of input as long as the input is a power of two and greater than 0.
	  * 
	  * @param x - method takes the log base 2 of this number
	  * @return log base 2 of the input
	  */
	 private int log(int x) {
		 int count = 0;
		 while(x > 1) {
			 x = x/2;
			 count++;
		 }
		 return count;
	 }
	 /**
	  * Responsible for seeding the teams according to their team ranks and
	  *  store them into the data structure (Array)
	  * The seeding algorithm taken from online source 
	  * @author Maulin Vasavada
	  * from: https://coderanch.com/t/371069/java/arrange-numbers
	  *  
	  */
	private void seed() {
		/*
		 * seeding algorithm gotten from online source
		 * cited in the java docs of method and class
		 */
		// seeding algorithm (from online source) begins		
		int N = log(teams.length);
		double d = Math.pow(2,N);
		double d1 = d +1;
		/*
		 * initializes data to teams.length + 1
		 */
		double[] data = new double[(int)d+1];
		//System.out.println("d="+d);
		data[0] = -1;
		/*
		 * intializes first element in data to be the first seed and the second
		 * element to be the seed of the team the first seed is playing against:
		 * teams.length
		 */
		data[1] = 1;
		data[2] = d;
		/*
		 * initializes second to last element of data to be 2 seed
		 * and the second to last element to be the seed of the team the two seed is
		 * playing against which will always be teams.length - 1
		 */
		data[(int)d] = d-1;
		data[(int)d-1] = 2;
		/*
		 * seeds the rest of the teams utilizing seedHelper()
		 */
		for(int i=4; i < (int)d; i+= 2 ) {
		data[i-1] = seedHelper(N,i);
		data[i] = d1 - data[i-1];
		}
		// seeding algorithm (from online source) ends
		/*
		 * this is the number of matches in the first round 
		 */
		matches = new Match[teams.length - 1];
		/*
		 * keeps track of how many matches have been my initializes
		 */
		int b = matches.length - 1;
		/*
		 * loop initializes the matches by separating the two closest teams to the two ends of the first round
		 *  subsection of the array and the two last place teams to the two ends of the first round subsection
		 *  of the first round array. As the middle of the first round subsection of the array is approached
		 *  the seeds of the teams playing in the matches approach each other.
		 */
		for(int i = 0; i < teams.length; i=i+2) {
			matches[matches.length-1-i/2] = new Match(teams[(int) data[i+1]-1],teams[(int) data[i+2]-1]);
			b--;
		}
		/*
		 * initializes the rest of the matches
		 */
		while(b >= 0) {
			matches[b] = new Match();
			b--;
		}
		/*
		 * sets the next match for each match besides the one at index 0 which has no next match
		 */
		for(int i = matches.length-1; i > 0; i--) {
			matches[i].setNext(matches[(i-1)/2]);
			
		}
	}
	
	/**
	 * @return an array containing all the teams
	 */
	public Team[] getTeam(){
		return teams;	
	}
	/**
	 * @return an array containing all the matches
	 */
	public Match[] getMatch(){
		return matches;
	}
}
