package Strutture_Dati;

public class Person {
	private String name;
	private int frequency;
	
	public Person(String name) {
		this.name = name;
		this.frequency = 1;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getf(){
		return this.frequency;
	}
	
	public void setf() {
		this.frequency++;
	}
	
	public void resetf() {
		this.frequency = 1;
	}
	
	public String toString() {
		return	this.name;
	}
}