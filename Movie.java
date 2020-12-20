package Strutture_Dati;

public class Movie {

    private String title;
    private Integer year;
    private Integer votes;
    private Person[] cast;
    private Person director;

    public Movie(String title, Integer year, Integer votes,
            Person[] cast, Person director) {
        this.title = title;
        this.year = year;
        this.votes = votes;
        this.cast = cast;
        this.director = director;
    }

    //Costruttore usato per lo scan in IMovidaDB
    public Movie(String title) {
        this.title = title;
        this.year = 0;
        this.votes = 0;
        this.cast = new Person[0];
        this.director = new Person("");
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getYear() {
        return this.year;
    }

    public Integer getVotes() {
        return this.votes;
    }

    public Person[] getCast() {
        return this.cast;
    }

    public Person getDirector() {
        return this.director;
    }

    /////////////////////////////////////////

    public void setTitle (String T) {
        this.title = T;
    }

    public void setYear (Integer Y) {
        this.year = Y;
    }

    public void setVotes (Integer V) {
        this.votes = V;
    }

    public void setCast (String C) {
        String[] cast = C.split(",");
        Person [] tmp = new Person [cast.length];
        for(int i = 0; i < cast.length; i++) {
            tmp[i] = new Person(cast[i].trim());
        }
        this.cast = tmp;
    }

    public void setDirector (String D) {
        this.director = new Person(D);
    }
    
    private String stampCast () {
    	String s = "";
    	int i = 0;
    	for (; i < cast.length - 1; i++) {
    		s += cast[i].getName() + ", ";
    	}
    	return s += cast[i].getName();    	
    }
    
    public String toString() {
		return	"Title: " + this.title 
				+ "\nYear: " + this.year
				+ "\nDirector: " + this.director
				+ "\nCast: " + stampCast()
				+ "\nVotes: " + this.votes;
	}
}