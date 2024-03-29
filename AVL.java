package Strutture_Dati;

class AvlTree {
	public Node root;
	
	public AvlTree() {
	  this.root = null;
	}
	
	public static int max(int a, int b) {
	  if(a > b)
	    return a;
	  return b;
	}
	
	public int height(Node n) {
	  if(n == null)
	    return -1;
	  return n.height;
	}
	
	public Node minimum(Node x) {
	  while(x.left != null)
	    x = x.left;
	  return x;
	}
	
	public void leftRotate(Node x) {
	  Node y = x.right;
	  x.right = y.left;
	  if(y.left != null) {
	    y.left.parent = x;
	  }
	  y.parent = x.parent;
	  if(x.parent == null) { //x is root
	    this.root = y;
	  }
	  else if(x == x.parent.left) { //x is left child
	    x.parent.left = y;
	  }
	  else { //x is right child
	    x.parent.right = y;
	  }
	  y.left = x;
	  x.parent = y;
	
	  x.height = 1 + max(height(x.left), height(x.right));
	  y.height = 1 + max(height(y.left), height(y.right));
	}
	
	public void rightRotate(Node x) {
	  Node y = x.left;
	  x.left = y.right;
	  if(y.right != null) {
	    y.right.parent = x;
	  }
	  y.parent = x.parent;
	  if(x.parent == null) { //x is root
	    this.root = y;
	  }
	  else if(x == x.parent.right) { //x is left child
	    x.parent.right = y;
	  }
	  else { //x is right child
	    x.parent.left = y;
	  }
	  y.right = x;
	  x.parent = y;
	
	  x.height = 1 + max(height(x.left), height(x.right));
	  y.height = 1 + max(height(y.left), height(y.right));
	}
	
	public int balanceFactor(Node n) {
	  if(n == null)
	    return 0;
	  return(height(n.left) - height(n.right));//farlo più brutto
	}
	
	public void insert(Node n) {
	  Node y = null;
	  Node temp = this.root;
	  while(temp != null) {  //ricerca posizione padre
	    y = temp;
	    if(n.data < temp.data)
	      temp = temp.left;
	    else
	      temp = temp.right;
	  }
	  n.parent = y;
	
	  if(y == null) //albero vuoto
	    this.root = n;
	  else if(n.data < y.data)
	    y.left = n;
	  else
	    y.right = n;
	
	  Node z = n;
	
	  while(y != null) {
	    y.height = 1 + max(height(y.left), height(y.right));
	
	
	    Node x = y.parent;
	
	    if(balanceFactor(x) <= -2 || balanceFactor(x) >= 2) {//nonno sbilanciato
	      if(y == x.left) {
	        if(z == x.left.left) //case SS
	          rightRotate(x);
	
	        else if(z == x.left.right) {//case SD
	          leftRotate(y);
	          rightRotate(x);
	        }
	      }
	      else if(y == x.right) {
	        if(z == x.right.right) //case DD
	          leftRotate(x);
	
	        else if(z == x.right.left) {//case DS
	          rightRotate(y);
	          leftRotate(x);
	        }
	      }
	     break;
	    }
	    y = y.parent;
	    z = z.parent;
	  }
	}
	
	public void transplant(Node u, Node v) { //u rimpiazzato da v
	  if(u.parent == null) //u is root
	    this.root = v;
	  else if(u == u.parent.left) //u is left child
	    u.parent.left = v;
	  else //u is right child
	    u.parent.right = v;
	
	  if(v != null)
	    v.parent = u.parent;
	}
	
	public void avlDeleteFixup(Node n) {
	  Node p = n;
	
	  while(p != null) {
	    p.height = 1 + max(height(p.left), height(p.right));
	
	    if(balanceFactor(p) <= -2 || balanceFactor(p) >= 2) { //grandparent is unbalanced
	      Node x, y, z;
	      x = p;
	
	      //taller child of x will be y
	      if(x.left.height > x.right.height)
	        y = x.left;
	      else
	        y = x.right;
	
	      //taller child of y will be z
	      if(y.left.height > y.right.height) {
	        z = y.left;
	      }
	      else if(y.left.height < y.right.height) {
	        z = y.right;
	      }
	      else { //same height, go for single rotation
	        if(y == x.left)
	          z = y.left;
	        else
	          z = y.right;
	      }
	
	      if(y == x.left) {
	        if(z == x.left.left) //case 1
	          rightRotate(x);
	
	        else if(z == x.left.right) {//case 3
	          leftRotate(y);
	          rightRotate(x);
	        }
	      }
	      else if(y == x.right) {
	        if(z == x.right.right) //case 2
	          leftRotate(x);
	
	        else if(z == x.right.left) {//case 4
	          rightRotate(y);
	          leftRotate(x);
	        }
	      }
	    }
	    p = p.parent;
	  }
	}
	
	public void delete(Node z) {
		if (z != null) {
		  if(z.left == null) {
		    transplant(z, z.right);
		    if(z.right != null)
		      avlDeleteFixup(z.right);
		  }
		  else if(z.right == null) {
		    transplant(z, z.left);
		    if(z.left != null)
		      avlDeleteFixup(z.left);
		  }
		  else {
		    Node y = minimum(z.right); //minimum element in right subtree
		    if(y.parent != z) {
		      transplant(y, y.right);
		      y.right = z.right;
		      y.right.parent = y;
		    }
		    transplant(z, y);
		    y.left = z.left;
		    y.left.parent = y;
		    if(y != null)
		      avlDeleteFixup(y);
		  }
		}
	}
	
	protected Node cerca (int vote, String title) {
		Node current = this.root;
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
	
	public void stamp (Node v) {
	    if (v != null) {
	        System.out.println(v.movie.getTitle());
	        stamp(v.left); 
	        stamp(v.right);
	    }
	}
	
	public void Stampa() {
		stamp(this.root);
	}
	
	public void inorder(Node n) {
	  if(n != null) {
	    inorder(n.left);
	    System.out.print(n.data + " ");
	    inorder(n.right);
	  }
	}
}