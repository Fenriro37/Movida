package roversimaranzana;

class Nodo {
	protected int key, height;
	protected Nodo left, right,  parent;
	protected Movie movie;

	public Nodo(int key) {
	  this.key = key;
	  this.left = null;
	  this.right = null;
	  this.parent = null;
	  this.height = 0;
	  this.movie = null;
	}
	
	public Nodo() {
		  this.key = 0;
		  this.left = null;
		  this.right = null;
		  this.parent = null;
		  this.height = 0;
		  this.movie = null;
		}
	
	public Nodo(int d, Movie M) { 
	  this.key = d;  
      this.height = 0;
      this.movie = M;
      this.left = null;
      this.right = null;
      this.parent = null;
	}
	    
}