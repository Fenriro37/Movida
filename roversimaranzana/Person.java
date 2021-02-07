package roversimaranzana;

public class Person {
	private String name;
	private int frequency;	//numero di incarichi che la persona ha avuto
	
	public Person(String name) {
		this.name = name;
		this.frequency = 1;
	}
	
	public String getName(){
		return this.name;
	}

	//#################################
	
	public int getf(){
		return this.frequency;
	}
	
	public void increasef() {
		this.frequency++;
	}
	
	public void resetf() {
		this.frequency = 1;
	}
	
	public String toString() {
		if (this.name == "")
			return "Errore, persona non trovata";
		else
			return	this.name;
	}
}