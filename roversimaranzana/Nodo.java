package roversimaranzana;

class Nodo {
	protected int key, height;
	protected Nodo left, right,  parent;
	protected Movie movie;
	
	/* Costruttuore usato in MovidaCore dalla funzione setAVL 
	 */
	public Nodo(int d, Movie M) { 
	  this.key = d;  
      this.height = 0;
      this.movie = M;
      this.left = null;
      this.right = null;
      this.parent = null;
	}	    
}