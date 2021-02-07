package roversimaranzana;

import java.util.ArrayList;

class AVL {
	//unico parametro è la radice
	public Nodo root;
	
	public AVL() {
		this.root = null;
	}
	
	public int max(int a, int b) {
		if(a > b)
			return a;
		return b;
	}
	
	public int height(Nodo n) {
		if(n == null)
			return -1;
		return n.height;
	}
	
	public Nodo massimo(Nodo x) {
		while(x.right != null)
			x = x.right;
		return x;
	}
	
	public void rotazioneSinistra(Nodo x) {
		Nodo y = x.right;
		x.right = y.left;
		if(y.left != null) {
			y.left.parent = x;
		}
		y.parent = x.parent;
		if(x.parent == null) {
			this.root = y;
		}
		else if(x == x.parent.left) {
			x.parent.left = y;
		}
		else {
			x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
	
		x.height = 1 + max(height(x.left), height(x.right));
		y.height = 1 + max(height(y.left), height(y.right));
	}
	
	public void rotazioneDestra(Nodo x) {
		Nodo y = x.left;
		x.left = y.right;
		if(y.right != null) {
			y.right.parent = x;
		}
		y.parent = x.parent;
		if(x.parent == null) {
			this.root = y;
		}
		else if(x == x.parent.right) {
			x.parent.right = y;
		}
		else {
			x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
	
		x.height = 1 + max(height(x.left), height(x.right));
		y.height = 1 + max(height(y.left), height(y.right));
	}
	
	public int sbilanciamento(Nodo n) {
		if(n == null)
			return 0;
		return(height(n.left) - height(n.right));
	}
	
	public void insert(Nodo n) {
		Nodo y = null;
		Nodo temp = this.root;
		while(temp != null) {  //ricerca posizione padre
			y = temp;
			if(n.key < temp.key)
				temp = temp.left;
			else
				temp = temp.right;
		}
		n.parent = y; //y è la foglia a cui attacchiamo il nuovo nodo
	
		if(y == null) //albero vuoto
			this.root = n;
		else if(n.key < y.key)
			y.left = n; //n diventa figlio sinistro
		else
			y.right = n; //n diventa figlio destro
	
		Nodo z = n;
	
		while(y != null) {
			y.height = 1 + max(height(y.left), height(y.right));	
	
			Nodo x = y.parent;
	
		    if(sbilanciamento(x) <= -2 || sbilanciamento(x) >= 2) { //nonno sbilanciato
		    	if(y == x.left) {
		    		if(z == x.left.left) //caso SS
		    			rotazioneDestra(x);
		    		else if(z == x.left.right) { //caso SD
		    			rotazioneSinistra(y);
		    			rotazioneDestra(x);
		    		}
		    	}
			    else if(y == x.right) {
			        if(z == x.right.right) //caso DD
			        	rotazioneSinistra(x);
			        else if(z == x.right.left) {//caso DS
			        	rotazioneDestra(y);
			        	rotazioneSinistra(x);
			        }
			    }
			    break;
		    }
		    y = y.parent;
		    z = z.parent;
		}
	} 
	
	public void sostituisci(Nodo u, Nodo v) { //u rimpiazzato da v, sostituzione
		if(u.parent == null) //u è la radice
			this.root = v;
		else if(u == u.parent.left) //u è il figlio sinistro
			u.parent.left = v;
		else //u è il figlio destro
			u.parent.right = v;
	
		if(v != null)
			v.parent = u.parent;
	}
	
	public void aggiustaDelete(Nodo n) {
		Nodo p = n;
	
		while(p != null) {
			p.height = 1 + max(height(p.left), height(p.right));
			
			if(sbilanciamento(p) <= -2 || sbilanciamento(p) >= 2) { //il nonno è sbilanciato
				Nodo x, y, z;
				x = p;
				
				if(x.right == null)
					rotazioneDestra(x);
				else if (x.left == null)
					rotazioneSinistra(x);
				else {
					//y diventa il sottoalbero più alto di x
					if(x.left.height > x.right.height)
						y = x.left;
					else
						y = x.right;
			
					//z diventa il sottoalbero più alto di y
					if(y.left.height > y.right.height) {
							z = y.left;
					}
					else if(y.left.height < y.right.height) {
						z = y.right;
					}
					else { //se hanno la stessa altezza creo la situazione per una rotazione semplice: allineo z con x e y
						if(y == x.left)
							z = y.left;
						else
							z = y.right;
					}
			
					if(y == x.left) {
						if(z == x.left.left)
							rotazioneDestra(x);
			
						else if(z == x.left.right) {
							rotazioneSinistra(y);
							rotazioneDestra(x);
						}
					}
					else if(y == x.right) {
						if(z == x.right.right)
							rotazioneSinistra(x);
				        else if(z == x.right.left) {
				        	rotazioneDestra(y);
				        	rotazioneSinistra(x);
				        }
					}
				}
			}
			p = p.parent;
		}
	}
		
	public void delete(Nodo z) {
		if (z != null) { //controllo l'input
			if(z.left == null) {
				sostituisci(z, z.right);
				if(z.right != null)
					aggiustaDelete(z.right);
				else 
					aggiustaDelete(z.parent); //fix nel caso di una foglia
			}
			else if(z.right == null) {
				sostituisci(z, z.left);
				if(z.left != null)
					aggiustaDelete(z.left);
			}
			else {
				Nodo y = massimo(z.left); //cerco il predecessore di z (il più grande elemento del sottoalbero sinistro)
					//per le proprietà del predecessore, y non ha figlio destro
					//controllo il sinistro
				if(y.parent != z) {
					sostituisci(y, y.left);
					y.left = z.left;
					y.left.parent = y;
				}
				sostituisci(z, y);
				y.right = z.right;
				y.right.parent = y;
				if(y != null)
					aggiustaDelete(y);
			}
		}
	}
	
	/*	Funzioni supplementari ########################################################
	 * 	
	 *  ###############################################################################
	 */	
	
	/* Usata dalla funzione: searchMostVotedMovies
	 * Visita simmetrica con precedenza a destra
	 */
	public ArrayList<Movie> visitaDestra (Nodo iter, ArrayList<Movie> m) {
		if (iter != null) {
			visitaDestra(iter.right, m);
			m.add(iter.movie);
			visitaDestra(iter.left, m);			
		}			
		return m;		
	}
	
	/* funzione usata dalla funzione: Delete in MovidaCore
	 * ritorna il Nodo cercato oppure null
	 */
	public Nodo cerca (int vote, String title) {
		Nodo current = this.root;
	    while (current != null) {
	        if (current.movie.getTitle().equalsIgnoreCase(title)) {
	            break;
	        }
	        if (current.movie.getVotes() < vote)
	        	current = current.right;
	        else
	        	current = current.left;
	    }
	    return current;
	}
	
	public void Stampa_util (Nodo v) {
	    if (v != null) {
//	        System.out.println(v.movie.getTitle());
	    	System.out.println(v.key);
	        Stampa_util(v.left); 
	        Stampa_util(v.right);
	    }
	}
	
	public void Stampa() {
		Stampa_util(this.root);
	}
	
	public void inorder(Nodo n) {
	  if(n != null) {
	    inorder(n.left);
	    System.out.print(n.key + " ");
	    inorder(n.right);
	  }
	}
}