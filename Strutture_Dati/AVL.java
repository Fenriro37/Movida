package Strutture_Dati;

public class AVL {
	private Nodo root;
	
	public AVL (Nodo R) {
		if (R != null) {
			root = R;
			R.parent = R;
		}
		else 
			System.out.println("Nodo deve essere diverso da null");
	}

	//Max tra interi
	public int max (int a, int b) {
		if (a>=b)
			return a;
		else 
			return b;
	}

	//Max figlio dato un nodo
	//NB: non gestiamo il caso di v == null
	public Nodo max(Nodo v) {
			while (v.right != null)
				v = v.right;
			return v;			
	}
	
	public int maxHeight (Nodo a, Nodo b) {
		if(a == null && b == null)
			return 0;
		
		if(a == null)
			return b.height;
		
		if(b == null)
			return a.height;
		
		else return max(a.height, b.height);
	}

    public void AggiustaAltezza(Nodo N) {
		N.height = maxHeight(N.left, N.right) + 1;
	}
	
	//Aggiornamento height cammino nodo radice
	public void AggiustaCammino(Nodo N) {
		while (N!=N.parent) { 
			AggiustaAltezza(N.parent); 
			N = N.parent;
		} 
	}
	
	public void Rotazione_SS (Nodo V) {
		
		//aggiornamento puntatori
		Nodo U = V.left; 
		
		//controllo sulla presenza del figlio destro di U
		if (U.right != null) {
			V.left = U.right;
			V.left.parent = V;
		}
		else 
			V.left = null;
		
		U.right = V;
		
		//controllo sul padre di V
		if (V == V.parent) {
			U.parent = U;
			root = U;
		}
		else if (V == V.parent.left) {
			U.parent = V.parent;
			U.parent.left = U;
		}
		else {
			U.parent = V.parent;
			U.parent.right = U;
		}

		V.parent = U;
		//aggiornamento altezza
		AggiustaAltezza(V);
		AggiustaAltezza(U);
		AggiustaCammino(U);
	}
	
	public void Rotazione_DD (Nodo V) {
		//aggiornamento puntatori
		Nodo U = V.right;
		
		//controllo sulla presenza del figlio sinistro di U
		if (U.left != null) {
			V.right = U.left;
			V.right.parent = V;
		}	
		else 
			V.right = null;
				
		U.left = V;
		
		//controllo sul padre di V
		if (V == V.parent) {
			U.parent = U;
			root = U;
		}
		else if (V == V.parent.left) {
			U.parent = V.parent;
			U.parent.left = U;
		}
		else {
			U.parent = V.parent;
			U.parent.right = U;
		}
		
		V.parent = U;
		//aggiornamento altezza
		AggiustaAltezza(V);
		AggiustaAltezza(U);
		AggiustaCammino(U);
	}
	
	public void Rotazione_SD (Nodo V) {
		Nodo Z = V.left;  
		Nodo W = Z.right;
		
		//controllo figlio sinistro di W
		if (W.left != null)	{
			Z.right = W.left;
			W.left.parent = Z;
		}
		else
			Z.right = null;
		
		V.left = W;
		W.parent = V;
		W.left = Z;
		Z.parent = W;
		
		AggiustaAltezza(Z);	//rotazione sd -> lo height in Z è negativo 
							//-> se Z scende il suo height aumenta
		
		Rotazione_SS(V);
	}
	
	public void Rotazione_DS (Nodo V) {
		Nodo Z = V.right;
		Nodo W = Z.left;
		
		//controllo figlio destro di W
		if (W.right != null) {
			Z.left = W.right;	
			W.right.parent = Z;
		}
		else 
			Z.left = null;
		
		V.right = W;
		W.parent = V;
		W.right = Z;
		Z.parent = W;
		
		AggiustaAltezza(Z);
		Rotazione_DD(V);
	}
	
	//Aggiusta sbilanciamento di un cammino nodo-radice
	public void AggiustaSbilanciamento(Nodo I) {
		if(I != null) {
			I = I.parent;
			while (I!=I.parent) {
				if	(I.sbilanciamento() > 2 || I.sbilanciamento() < -2) {
					System.out.println("Sbilanciamento anomalo");
					break;
				}
				
				if (I.sbilanciamento() == 2) { //sbilanciamento a sinistra
					if (I.left.sbilanciamento() == 1)
						Rotazione_SS(I);
					else if(I.left.sbilanciamento() == -1)
						Rotazione_SD(I);
				}
					
				else if (I.sbilanciamento() == -2) { //sbilanciamento destra
					if (I.right.sbilanciamento() == 1)
						Rotazione_DS(I);
					else if(I.right.sbilanciamento() == -1)
						Rotazione_DD(I);
				}
				
				I = I.parent;
				
			}
	 	}
	}
	
	//usato solo in delete
	protected Nodo search (int k, String S) {
		Nodo v = root;
		while(v != null) {
			if (k == v.key && v.movie.getTitle().equals(S)) {
				break;
			}
			if (k < v.key) 
				v = v.left;	
			else 
				v = v.right;
		}
		return v;
	}
	
	public void insert (int key, Movie m) {
		Nodo tmp = null; 
		Nodo stmp = root;
		while( stmp!= null) {
			tmp = stmp;
			if (stmp.key>key) stmp=stmp.left; 
			else stmp = stmp.right;
		}
		Nodo N = new Nodo (key, m);
		N.parent = tmp;
		
		if (tmp == null) { //caso albero vuoto
			root = N;
			N.parent = N;
		}
		else if (key<tmp.key) tmp.left=N;      //qui come nel while se il valore coincide con quello del padre viene messo a destra
		else tmp.right=N;
				
		AggiustaCammino(N);
		AggiustaSbilanciamento(N);
		
//		prova ad usare tmp come puunattore
	}
	
	public Nodo predecessore(Nodo v) {
		if (v == null) return null;
		
		if(v.left != null) return max(v.left);
		else   {	
	    Nodo tmp = v.parent;
		while (tmp!=null && v == tmp.left) {
			v = tmp;
			tmp = tmp.parent;
			}
		return tmp;
		}
	}
	
	public void delete (int key, Movie m) { //poichè ci possono essere più chiavi uguali bisogna passare anche il film
		Nodo I = search(key, m.getTitle());
		if(I != null) {
			
			if(I == root) {
				//caso 00
				if(I.left == null && I.right == null)
					root = null;
				//caso 01  //non cambia le altezze eliminare la radice 
				else if(I.left == null) {
					root = I.right;
					I.right.parent = I.right;
				}
				//caso 10 //non cambia le altezze eliminare la radice
				else if(I.right == null) {
					root = I.left;
					I.left.parent = I.left;
				}
				//caso 11
				Nodo P = predecessore(I);
				if(P == I.left) { //caso predecessore adiacente
					P.right = I.right;
					I.right.parent = P; 
					//eventuali figli sinistri rimangono attaccati a P
					root = P;
					P.parent = P;
					AggiustaAltezza(P);
					
				}
				else {   //caso predecessore non adiacente
					if(P.left!=null) { //collegamento di eventuale figlio sinistro
						P.parent.right = P.left;
						P.left.parent = P.parent;
						AggiustaCammino(P.left);
					}
					P.right = I.right;
					I.right.parent = P;
					P.left = I.left;
					I.left.parent = P;
					root = P;
					P.parent = P;
					AggiustaAltezza(P);
				}
				
				
			}
			else { //nodo da eliminare non è root
			
			//caso 00
			if(I.left == null && I.right == null) {
				
				if(I.key < I.parent.key) {
					I.parent.left = null;
					I.parent = null;
					AggiustaAltezza(I.parent);
				}
				else{
					I.parent.right = null;
					I.parent = null;
					AggiustaAltezza(I.parent);
					}
				}
			//caso 01
			else if(I.left == null) {
				if(I.key < I.parent.key) {  //I è figlio sinistro
					I.parent.left = I.right;
					I.right.parent = I.parent;
				}
				else {						//I è figlio destro
					I.parent.right = I.right;
					I.right.parent = I.parent;
				}
				AggiustaCammino(I.right);
			}
			//caso 10
			else if(I.right == null) {
				if(I.key < I.parent.key) {  //I è figlio sinistro
					I.parent.left = I.left;
					I.left.parent = I.parent;
				}
				else {						//I è figlio destro
					I.parent.right = I.left;
					I.left.parent = I.parent;
				}
				AggiustaCammino(I.left);
				
			}
			//caso 11
			else {
				Nodo P = predecessore(I);
				
				if(I.left == P) { //predecessore è figlio sinistro di I
					P.right = I.right;
					I.right.parent= P;
					P.parent = I.parent;
					if(I.key<I.parent.key) 
						I.parent.left = P;
					else	
						I.parent.right = P;	
					AggiustaCammino(P.right);	
				}
				
				else { 
					if(P.left != null) {  //eventuale figlio sinistro riagganciato
						P.parent.right = P.left;
						P.left.parent= P.parent;
						AggiustaCammino(P.left);
						P.height = I.height;
						P.left = I.left; //sostituzione di I con P
						I.left.parent = P;
						P.right = I.right;
						I.right.parent = P;
						P.parent = I.parent;
						if(I.key<I.parent.key) 
							I.parent.left = P;
						else
							I.parent.right = P;		
					}	
					else { //nessun figlio sinistro di P
						P.parent.height = maxHeight(P.parent.left, null);
						AggiustaCammino(P.parent);
						P.height = I.height;
						P.left = I.left; //sostituzione di I con P
						I.left.parent = P;
						P.right = I.right;
						I.right.parent = P;
						P.parent = I.parent;
						if(I.key<I.parent.key) 
							I.parent.left = P;
						else
							I.parent.right = P;	
						}
					}
				}
			}
			}
		}
	
	public int numnodi(Nodo N) {
		if(N == null)
			return 0;
		else 
			return 1 + numnodi(N.left) + numnodi(N.right);
		
	}
	
	public String stamp (Nodo v) {
		if (v != null)
			return " [" + v.key + " " + v.movie.getTitle() + stamp(v.left) + stamp(v.right) + "]";
		else 
			return " []";
	}
	
	public void Stampa() {
		System.out.println(stamp(root));	
//		stamp(root);
	}
	
	public Movie crea (String S) {
		return new Movie (S);
	}
	
	public static void main(String[] args) {
		Movie S = new Movie ("uno");
		AVL loco = new AVL (new Nodo (3,S));
		loco.insert(2, loco.crea("due"));
		loco.insert(1, loco.crea("tre"));
//		loco.insert(4, loco.crea("quattro"));
//		loco.insert(5, loco.crea("cinque"));
		loco.Stampa();
	}
}
