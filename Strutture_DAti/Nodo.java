package Strutture_DAti;

class Nodo { 
    protected int key, height; 
    protected Nodo left, right; 
    protected Movie movie;
  
    Nodo(int d, Movie M) { 
        key = d; 
        height = 1;
        movie = M;
        left = null;
        right = null;
    } 
} 