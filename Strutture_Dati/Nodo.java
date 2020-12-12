package Strutture_Dati;

class Nodo { 
    protected int key, height; 
    protected Nodo left, right,parent; 
    protected Movie movie;
  
    Nodo(int d, Movie M) { 
        key = d; 
        height = 0;
        movie = M;
        left = null;
        right = null;
        parent = null;
    }
    
    public int sbilanciamento() {
        if(this.left == null && this.right == null)
            return 0;

        if(this.left == null)
            return -(this.right.height + 1);

        if(this.right == null)
            return this.left.height + 1;

        else return this.left.height - this.right.height;
    }
}