package Strutture_DAti;

public class AVL {
	private Nodo root;
	
	public AVL (Nodo R) {
		root = R;
	};
	
	//funzioni da implementare
	
	public Nodo search (int k) {
		Nodo v = root;
		while(v != null) {
			if (k == v.key) {
				return v;
			}
			if (k < v.key) 
				v = v.left;	
			else 
				v = v.right;
		};
		return null;
	}
	
	public void Rotazione_SS (Nodo N) {
		Nodo t = N.left;
		N.left = t.right;
		t.right = N;
	}
	
	public void Rotazione_DD (Nodo N) {
		Nodo t = N.right;
		N.right = t.left;
		t.left = N;
	}
}
