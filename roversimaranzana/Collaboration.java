package roversimaranzana;

import java.util.ArrayList;

public class Collaboration {
	Person actorA;
	Person actorB;
	ArrayList<Movie> movies;
	Boolean mark;
	
	public Collaboration(Person actorA, Person actorB) {
		this.actorA = actorA;
		this.actorB = actorB;
		this.movies = new ArrayList<Movie>();
		this.mark = false;
	}

	public Person getActorA() {
		return actorA;
	}

	public Person getActorB() {
		return actorB;
	}
	
	public Boolean getMark() {
		return this.mark;
	}

	public Double getScore(){
		Double score = 0.0;
		
		for (Movie m : movies)
			score += m.getVotes();
		
		return score / movies.size();
	}	
	
	//_________________________
	
	public void setMark() {
		this.mark = true;
	}
	
	public void unsetMark() {
		this.mark = false;
	}
	
	public String toString () {
		return("A: " + this.getActorA().toString() 
				+ "\nB: " + this.getActorB().toString()
				+ "\nScore: " + this.getScore() + "\n");
	}
}
