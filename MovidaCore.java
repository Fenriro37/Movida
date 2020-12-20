package Strutture_Dati;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch {	
	private SortingAlgorithm sort;
	private MapImplementation map;
	public Movie[] mArray;
	public AvlTree tree;
	public tabella_hash table;
	private int NF;
	private Person[] actors;
	private Person[] directors;
	
	public MovidaCore() {
		this.NF = 0;
		this.sort = SortingAlgorithm.BubbleSort;
		this.map = MapImplementation.AVL;
	}
	
	public void setmArray(Movie[] a) {
		this.mArray = a;
	}
	
	@Override
	public boolean setSort(SortingAlgorithm a) {
		if(a == SortingAlgorithm.BubbleSort) {
			this.sort = a;
			return true;
		}
		else if(a == SortingAlgorithm.QuickSort) {
			this.sort = a;
			return true;
		}
		return false;
	}

	@Override
	public boolean setMap(MapImplementation m) {
		if(m == MapImplementation.AVL) {
			this.map = m;
			return true;
		}
		else if(m == MapImplementation.HashIndirizzamentoAperto) {
			this.map = m;
			return true;
		}
		return false;
	}
	
	private void setAVL() {
		AvlTree a = new AvlTree(); 
		for(int i=0; i<this.mArray.length; i++) {
			Node tmp = new Node(mArray[i].getVotes(), mArray[i]);
			a.insert(tmp);
		}	
		this.tree = a;
	}
	
	private void setHash() {
		tabella_hash a = new tabella_hash(this.mArray.length + 10);
		for(int i=0; i<this.mArray.length; i++) {
			a.insert(mArray[i].getVotes(), mArray[i]);
		}
		this.table = a;
	}

	@Override
	public void loadFromFile(File F) throws MovidaFileException { //non sappiamo fare l'eccezzzione
		try {
			  clear();
		      File myObj = new File("es0.txt");
		      Scanner myReader = new Scanner(myObj);
		      String data = "";
		      NumberFilms();
		      int nFilms = this.NF; 
		      Movie [] Films = new Movie [nFilms];
		      int i = 0;	//i iteratore per Films
		      for(; i < nFilms; i++){
		    	  Films[i] = new Movie(""); 
		    	  data = myReader.nextLine();	//riga del Title
		    	  Films[i].setTitle(data.substring(7).trim()); 
//		    	  System.out.println(Films[i].getTitle());
		    	  data = myReader.nextLine();
		    	  Films[i].setYear(Integer.parseInt(data.substring(6, 10)));
		    	  data = myReader.nextLine();
		    	  Films[i].setDirector(data.substring(9).trim());
		    	  data = myReader.nextLine();
		    	  Films[i].setCast(data.substring(6).trim());
		    	  data = myReader.nextLine();
		    	  Films[i].setVotes(Integer.parseInt((data.substring(7)).trim()));	//stringa.trim() permette di ignorare gli spazi bianchi
		    	  if(myReader.hasNext())
		    		  data = myReader.nextLine(); // -> riga vuota		      
		      }
		      myReader.close();
		      setmArray(Films);
		      getAllPeople();
		      if(this.map == MapImplementation.AVL) 
		    	  setAVL();		      	  
		      else if(this.map == MapImplementation.HashIndirizzamentoAperto) 
		    	  setHash();
		}
		catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
				}
	}
	
	@Override
	public void saveToFile(File f) {
		try {
	      FileWriter myWriter = new FileWriter("txt.txt");
	      String s = "";
	      int i = 0;
	      for (; i < mArray.length - 1; i++) {
	    	  s += mArray[i].toString() + "\n\n";
	      }
	      s += mArray[i].toString();
	      myWriter.write(s);
	      myWriter.close();
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		
	}

	@Override
	public void clear() {
		this.mArray = null;
		this.tree = null;
		this.table = null;
	}

	public void NumberFilms() {
		int i = 0;		
		try {
			File myObj = new File("es0.txt");
			Scanner myReader = new Scanner(myObj);
			
			String data = "";
			while (myReader.hasNextLine()) {
		        data = myReader.nextLine();
		        //supponiamo non ci siano nomi o cognomi "Title"
		        if (data.contains("Title"))
		        	i++;
			}
			myReader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		this.NF = i;
	}
	
	@Override
	public int countMovies() {
		return this.mArray.length;
	}
	
	@Override
	public int countPeople() {
		return this.actors.length + this.directors.length;
	}

	@Override
	public boolean deleteMovieByTitle(String title) {
		Boolean find = false;
		int vote = 0;
		//mArray
		for (int i = 0; i < mArray.length; i++) {
			if (mArray[i].getTitle().equalsIgnoreCase(title)) {
				find = true;
				vote = mArray[i].getVotes();
				mArray[i] = null;
				this.NF--;
				Movie[] tmp = new Movie[NF];
				for (int w = 0, j = 0; w < mArray.length; w++) {
					if (mArray[w] != null) {
						tmp[j] = mArray[w];
						j++;
					}
				}
				this.mArray = tmp;
			}
		}		
		//strutture dati
		if (find){
			if (this.map == MapImplementation.AVL) {
				Node n = tree.cerca(vote, title);
				tree.delete(n);
			}
			else if (this.map == MapImplementation.HashIndirizzamentoAperto) {
				this.table.delete(vote, title);
			}
			getAllPeople();
			return true;
		}
		return false;
	}

	@Override
	public Movie getMovieByTitle(String title) {
		Movie m = new Movie ("");
		for (int i = 0; i < mArray.length; i++) {
			if (mArray[i].getTitle().equalsIgnoreCase(title)){
				m = mArray[i];
				break;
			}					
		}
		return m;
	}

	@Override
	public Person getPersonByName(String name) {
		Person m = new Person ("");
		for (int i = 0; i < actors.length; i++) {
			if (actors[i].getName().equalsIgnoreCase(name)){
				m = actors[i];
				break;
			}					
		}
		if (m.getName() == "") {
			for (int i = 0; i < directors.length; i++) {
				if (directors[i].getName().equalsIgnoreCase(name)){
					m = directors[i];
					break;
				}	
			}
		}
		return m;
	}

	@Override
	public Movie[] getAllMovies() {
		return this.mArray;
	}

	public Boolean cegia (ArrayList<Person> L, Person P) {
		for (int i = 0; i < L.size(); i++) {
			if (L.get(i).getName().equalsIgnoreCase(P.getName())) {
				L.get(i).setf();
				return true;	
			}
		}
		return false;
	}
	
	private Person[] PtoArray(ArrayList<Person> L) {
		Person[] A = new Person[L.size()];
		for (int i = 0; i < L.size(); i++) {
			 A[i] = L.get(i);
		}
		return A;
	}
	
	private void resetallf() {
		for (int i = 0; i < mArray.length; i++) {
			//regista			
			mArray[i].getDirector().resetf();
			//cast
			Person [] p = mArray[i].getCast();
			for(int ii = 0; ii < p.length; ii++) {
				p[ii].resetf();
			}
		}
	}
	
	@Override
	public Person[] getAllPeople() {
		ArrayList<Person> act = new ArrayList();
		ArrayList<Person> dir = new ArrayList();
		resetallf();	//reset a 1 delle frequenze di tutte le persone in mArray
		for (int i = 0; i < mArray.length; i++) {
			//regista			
			if (cegia(dir, mArray[i].getDirector()))
				;
			else
				dir.add(mArray[i].getDirector());
			//cast
			Person [] p = mArray[i].getCast();
			for(int ii = 0; ii < p.length; ii++) {
				if (cegia(act, p[ii]))
					;
				else
					act.add(p[ii]);
			}
		}
		this.actors = PtoArray(act);
		this.directors = PtoArray(dir);
		dir.addAll(act);
		return PtoArray(dir);
	}	
	
	private void searchMoviesByTitle_inAVL (ArrayList<Movie> m, String substring, Node n) {
		if (n != null) {
			if (n.movie.getTitle().toUpperCase().indexOf(substring.toUpperCase()) != -1)
				m.add(n.movie);
			searchMoviesByTitle_inAVL (m, substring, n.left);
			searchMoviesByTitle_inAVL (m, substring, n.right);
		}
	}
	
	private void searchMoviesByTitle_inHash (ArrayList<Movie> m, String substring, tabella_hash h) {
		Movie[] s = h.getTable();
		for (int i = 0; i < h.getSize(); i++) {
			if (s[i] != null && s[i].getTitle().toUpperCase().indexOf(substring.toUpperCase()) != -1)
				m.add(s[i]);
		}
	}
	
	private Movie[] MtoArray(ArrayList<Movie> L) {
		Movie[] A = new Movie[L.size()];
		for (int i = 0; i < L.size(); i++) {
			 A[i] = L.get(i);
		}
		return A;
	}
	
	@Override
	public Movie[] searchMoviesByTitle(String title) {
		ArrayList<Movie> m = new ArrayList();
		if (map == MapImplementation.AVL) {
			searchMoviesByTitle_inAVL(m, title, tree.root);
		}
		else if (map == MapImplementation.HashIndirizzamentoAperto) {
			searchMoviesByTitle_inHash(m, title, table);
		}
		return MtoArray(m);
	}

	@Override
	public Movie[] searchMoviesInYear(Integer year) { //ordinamento su films 
		if (this.sort == SortingAlgorithm.BubbleSort) {
			bubbleSort_year();
		}
		else if (this.sort == SortingAlgorithm.QuickSort) {
			quickSort_year();
		}
		int m = binarySearch(0, mArray.length - 1, year);
		ArrayList<Movie> a = new ArrayList();
		if (m != -1) {
			if (m == 0) {
				while (mArray[m].getYear().equals(year)) {
					a.add(mArray[m]);
					m++;
				}
			} else if (m == mArray.length - 1) {
				while (mArray[m].getYear().equals(year)) {
					a.add(mArray[m]);
					m--;
				}				
			} else {
				int n = m - 1;
				while (mArray[m].getYear().equals(year) && m < mArray.length) {
					a.add(mArray[m]);
					m++;
				}
				while (mArray[n].getYear().equals(year) && n >= 0) {
					a.add(mArray[n]);				
					n--;
				}
			}
		}
		return MtoArray(a);
	}

	@Override
	public Movie[] searchMoviesDirectedBy(String name) {
		int ingaggi = 0;
		for (int i = 0; i < directors.length; i++) {
			if (directors[i].getName().equalsIgnoreCase(name)) {
				ingaggi = directors[i].getf();
				break;
			}			
		}
		Movie[] m = new Movie[ingaggi];
		if (ingaggi != 0) {
			for (int i = 0, ii = 0; i < mArray.length && ii < ingaggi; i++) {
				if (mArray[i].getDirector().getName().equalsIgnoreCase(name)) {
					m[ii] = mArray[i];
					ii++;
				}					
			}
		}
		return m;
	}

	@Override
	public Movie[] searchMoviesStarredBy(String name) {
		int ingaggi = 0;
		for (int i = 0; i < actors.length; i++) {
			if (actors[i].getName().equalsIgnoreCase(name)) {
				ingaggi = actors[i].getf();
				break;
			}			
		}
		Movie[] m = new Movie[ingaggi];
		if (ingaggi != 0) {
			for (int i = 0, ii = 0; i < mArray.length && ii < ingaggi; i++) {
				Person[] f = mArray[i].getCast();
				for (int j = 0; j < f.length; j++) {
					if(f[j].getName().equalsIgnoreCase(name)) {
						m[ii] = mArray[i];
						ii++;
						break;
					}
				}				
			}
		}
		return m;
	}

	@Override
	public Movie[] searchMostVotedMovies(Integer N) {
		if (this.sort == SortingAlgorithm.BubbleSort) {
			bubbleSort_votes();
			}
		else if (this.sort == SortingAlgorithm.QuickSort) {
			quickSort_votes();
		}
		if (N >= mArray.length) {
			return Mribalta(mArray);
		} 
		else {
			Movie[] m = new Movie[N];
			for (int i = mArray.length - 1, j = 0; j < N; i--, j++)
				m[j] = mArray[i];
			return m;
		}
	}

	@Override
	public Movie[] searchMostRecentMovies(Integer N) {
		if (this.sort == SortingAlgorithm.BubbleSort) {
			bubbleSort_year();
			}
		else if (this.sort == SortingAlgorithm.QuickSort) {
			quickSort_year();
		}
		if (N >= mArray.length) {
			return Mribalta(mArray);
		} 
		else {
			Movie[] m = new Movie[N];
			for (int i = mArray.length - 1, j = 0; j < N; i--, j++)
				m[j] = mArray[i];
			return m;
		}
	}

	@Override
	public Person[] searchMostActiveActors(Integer N) { //ordinamento su getallppl
		if (this.sort == SortingAlgorithm.BubbleSort) {
			bubbleSort_freq();
			}
		else if (this.sort == SortingAlgorithm.QuickSort) {
			quickSort_freq();
		}
		if (N >= mArray.length) {
			return Pribalta(actors);
		} 
		else {
			Person[] m = new Person[N];
			for (int i = actors.length - 1, j = 0; j < N; i--, j++)
				m[j] = actors[i];
			return m;
		}
	}
	
	/*	UTILI...
	 * ____________________________________________
	 */
	
	
	public void stampaArrayMovies (Movie[] m) {
		for (int i = 0; i < m.length; i++)
			System.out.println(m[i].toString() + "\n");
	}
	
	public void stampaArrayPerson (Person[] m) {
		for (int i = 0; i < m.length; i++)
			System.out.println(m[i].toString() + "\n");
	}
	
	private Movie[] Mribalta (Movie[] m) {
		Movie[] tmp = new Movie[m.length];
		for (int i = 0, j = m.length - 1; i < tmp.length; i++, j--)
			tmp[i] = m[j];			
		return tmp;
	}
	
	private Person[] Pribalta (Person[] m) {
		Person[] tmp = new Person[m.length];
		for (int i = 0, j = m.length - 1; i < tmp.length; i++, j--)
			tmp[i] = m[j];			
		return tmp;
	}
	
	private void bubbleSort_votes() {  
	        int n = mArray.length;  
	        Movie temp = null;  
	         for(int i=0; i < n; i++){  
	                 for(int j=1; j < (n-i); j++){  
	                          if(mArray[j-1].getVotes() > mArray[j].getVotes()){  
	                                 //swap 
	                                 temp = mArray[j-1];  
	                                 mArray[j-1] = mArray[j];  
	                                 mArray[j] = temp;  
	                         }  	                          
	                 }  
	         }  	  
	    }  
	
	private int partition_votes(int low, int high){
		Movie pivot = mArray[high];  
        int i = (low-1); // index of smaller element 
        for (int j=low; j<high; j++){
            // If current element is smaller than the pivot 
            if (mArray[j].getVotes() < pivot.getVotes()) {
                i++;   
                // swap mArray[i] and mArray[j] 
                Movie temp = mArray[i]; 
                mArray[i] = mArray[j]; 
                mArray[j] = temp; 
            } 
        }   
        // swap mArray[i+1] and mArray[high] (or pivot) 
        Movie temp = mArray[i+1]; 
        mArray[i+1] = mArray[high]; 
        mArray[high] = temp; 
  
        return i+1; 
    }   
  
    /* The main function that implements QuickSort() 
      arr[] --> Array to be sorted, 
      low  --> Starting index, 
      high  --> Ending index */
    private void quickSort_votes_util(int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int pi = partition_votes(low, high); 
  
            // Recursively sort elements before 
            // partition and after partition 
            quickSort_votes_util(low, pi-1); 
            quickSort_votes_util(pi+1, high); 
        } 
    } 
    
    private void quickSort_votes() {
    	quickSort_votes_util(0, mArray.length - 1);
    }
    
    private void bubbleSort_year() {  
        int n = mArray.length;  
        Movie temp = null;  
         for(int i=0; i < n; i++){  
                 for(int j=1; j < (n-i); j++){  
                          if(mArray[j-1].getYear() > mArray[j].getYear()){  
                                 //swap 
                                 temp = mArray[j-1];  
                                 mArray[j-1] = mArray[j];  
                                 mArray[j] = temp;  
                         }  	                          
                 }  
         }  	  
    }  

	private int partition_year(int low, int high){
		Movie pivot = mArray[high];  
	    int i = (low-1); // index of smaller element 
	    for (int j=low; j<high; j++){
	        // If current element is smaller than the pivot 
	        if (mArray[j].getYear() < pivot.getYear()) {
	            i++;   
	            // swap mArray[i] and mArray[j] 
	            Movie temp = mArray[i]; 
	            mArray[i] = mArray[j]; 
	            mArray[j] = temp; 
	        } 
	    }   
	    // swap mArray[i+1] and mArray[high] (or pivot) 
	    Movie temp = mArray[i+1]; 
	    mArray[i+1] = mArray[high]; 
	    mArray[high] = temp; 
	
	    return i+1; 
	}   
	
	/* The main function that implements QuickSort() 
	  arr[] --> Array to be sorted, 
	  low  --> Starting index, 
	  high  --> Ending index */
	private void quickSort_year_util(int low, int high) {
	    if (low < high) {
	        /* pi is partitioning index, arr[pi] is  
	          now at right place */
	        int pi = partition_year(low, high); 
	
	        // Recursively sort elements before 
	        // partition and after partition 
	        quickSort_year_util(low, pi-1); 
	        quickSort_year_util(pi+1, high); 
	    } 
	} 
	
	private void quickSort_year() {
		quickSort_year_util(0, mArray.length - 1);
	}	
	
	private void bubbleSort_freq() {  
        int n = actors.length;  
        Person temp = null;  
         for(int i=0; i < n; i++){  
                 for(int j=1; j < (n-i); j++){  
                          if(actors[j-1].getf() > actors[j].getf()){  
                                 //swap 
                                 temp = actors[j-1];  
                                 actors[j-1] = actors[j];  
                                 actors[j] = temp;  
                         }  	                          
                 }  
         }  	  
    }  

	private int partition_freq(int low, int high){
		Person pivot = actors[high];  
	    int i = (low-1); // index of smaller element 
	    for (int j=low; j<high; j++){
	        // If current element is smaller than the pivot 
	        if (actors[j].getf() < pivot.getf()) {
	            i++;   
	            // swap mArray[i] and mArray[j] 
	            Person temp = actors[i]; 
	            actors[i] = actors[j]; 
	            actors[j] = temp; 
	        } 
	    }   
	    // swap mArray[i+1] and mArray[high] (or pivot) 
	    Person temp = actors[i+1]; 
	    actors[i+1] = actors[high]; 
	    actors[high] = temp; 
	
	    return i+1; 
	}   
	
	/* The main function that implements QuickSort() 
	  arr[] --> Array to be sorted, 
	  low  --> Starting index, 
	  high  --> Ending index */
	private void quickSort_freq_util(int low, int high) {
	    if (low < high) {
	        /* pi is partitioning index, arr[pi] is  
	          now at right place */
	        int pi = partition_year(low, high); 
	
	        // Recursively sort elements before 
	        // partition and after partition 
	        quickSort_freq_util(low, pi-1); 
	        quickSort_freq_util(pi+1, high); 
	    } 
	} 
	
	private void quickSort_freq() {
		quickSort_freq_util(0, mArray.length - 1);
	}
	 
    // Returns index of x if it is present in arr[l..
    // r], else return -1 
    private int binarySearch(int l, int r, int x) {
        if (r >= l) { 
            int mid = l + (r - l) / 2; 
            if (mArray[mid].getYear() == x) 
                return mid;   
            if (mArray[mid].getYear() > x) 
                return binarySearch(l, mid - 1, x);  
            return binarySearch(mid + 1, r, x); 
        } 
        return -1; 
    } 
}