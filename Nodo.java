package Strutture_Dati;

class Node {
	protected int data, height;
	protected Node left, right,  parent;
	protected Movie movie;

	public Node(int data) {
	  this.data = data;
	  this.left = null;
	  this.right = null;
	  this.parent = null;
	  this.height = 0;
	  this.movie = null;
	}
	
	public Node() {
		  this.data = 0;
		  this.left = null;
		  this.right = null;
		  this.parent = null;
		  this.height = 0;
		  this.movie = null;
		}
	
	public Node(int d, Movie M) { 
	  this.data = d;  
      this.height = 0;
      this.movie = M;
      this.left = null;
      this.right = null;
      this.parent = null;
	}
	    
}