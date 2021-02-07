package roversimaranzana;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch, IMovidaCollaborations {	
	private SortingAlgorithm sort;
	private MapImplementation map;
	
	private Movie[] mArray;	//array che contiene i film
	private AVL tree;
	private tabella_hash table;						
	
	private Person[] actors;						
	private Person[] directors;
	private ArrayList<Collaboration> collab;
	
	public MovidaCore() {
		this.sort = SortingAlgorithm.QuickSort;
		this.map = MapImplementation.AVL;
	}
	
	public Movie[] getMovies () {
		return this.mArray;
	}
	
	public Person[] getActors () {
		return this.actors;
	}
	
	public Person[] getDirectors () {
		return this.directors;
	}
	
	public tabella_hash getTabella_hash () {
		return this.table;
	}
	
	public AVL getTree () {
		return this.tree;
	}
	
	public Collaboration[] getCollaborations () {
		return CtoArray(this.collab);
	}
	
	private void setmArray(Movie[] a) {
		this.mArray = a;
	}
	
	 /* IMovidaConfig_________________________________________________________________
     * 
     * _______________________________________________________________________________
     */
	
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
		System.out.println("Algoritmo non disponible");
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
		System.out.println("Mappatura non disponible");
		return false;
	}
	
	 /* IMovidaDB__________________________________________________________________
     * 																			   
     * ____________________________________________________________________________
     */
	
	/* chiamata da loadFromFile per inizializzare la mappatura
	 * creo un albero vuoto, lo riempio con i film presenti in mArray, setto this.tree
	 */
	private void setAVL() {
		AVL a = new AVL(); 
		for(int i=0; i < this.mArray.length; i++) {
			Nodo tmp = new Nodo(mArray[i].getVotes(), mArray[i]);
			a.insert(tmp);
		}
		this.tree = a;
	}

	/* chiamata da loadFromFile per inizializzare la mappatura
	 * creo la tabella_hash, la riempio con in film presenti in mArray, setto this.table
	 * implementato anche il raddoppiamento della table  
	 */
	private void setHash() {
		tabella_hash a = new tabella_hash(this.mArray.length);
		for(int i = 0; i < this.mArray.length; i++) {
			if (!a.insert(mArray[i].getYear(), mArray[i])) {
				Movie[] x = new Movie[a.getSize()*2 + 1];
				a.setTable(x);
				i = -1; //fix: nella prossima iterazione si ha: i++ = 0
			}
		}
		this.table = a;
	}
	
	/* funzione chiamata in loadFromFile 
	 * per sapere quanti film ci sono nel file
	 */
	private int NumberFilms(File f) {
		int i = 0;		
		try {
			File myObj = f;
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
		return i;
	}

	@Override
	public void loadFromFile(File F) throws MovidaFileException { //non sappiamo fare l'eccezzzione
		try {
			//evito confusione con chiamate successive alla prima
			clear();
			
			//creo lo Scanner
		    File myObj = F;
		    Scanner myReader = new Scanner(myObj);
		    
		    //riga vuota per leggere le righe
		    String data = "";
		    
		    //creo l'array per settare mArray
		    
		    int nFilms = NumberFilms(F); 
		    Movie [] Films = new Movie[nFilms];
		    
		    //lettura del file nel formato:
		    //   Title: Pulp Fiction
		    //	 Year: 1994
		    //	 Director: Quentin Tarantino
		    //	 Cast: John Travolta, Uma Thurman
		    //	 Votes: 1743616
		    for(int i = 0; i < nFilms; i++){
		    	Films[i] = new Movie(""); 
		    	data = myReader.nextLine();	//riga del titolo
		    	Films[i].setTitle(data.substring(7).trim()); 
		    	data = myReader.nextLine();
		    	Films[i].setYear(Integer.parseInt(data.substring(6, 10)));
		    	data = myReader.nextLine();
		    	Films[i].setDirector(data.substring(9).trim());
		    	data = myReader.nextLine();
		    	Films[i].setCast(data.substring(6).trim());
		    	data = myReader.nextLine();
		    	Films[i].setVotes(Integer.parseInt((data.substring(7)).trim()));	//stringa.trim() permette di ignorare gli spazi bianchi
		    	//controllo per l'ultimo film, che non avrà una riga bianca vuota dopo i voti
		    	if(myReader.hasNext())
		    		data = myReader.nextLine(); // -> riga vuota
		    }		    
		    myReader.close();
		    
		    //setto mArray con l'array appena creato
		    setmArray(Films);
		    
		    //chiamata a getAllPeople per inizializzare gli array actors e directors				
		    getAllPeople();
		    
		    //setto Collab
		    setCollab();
		    
		    //setto la mappatura
		    if(this.map == MapImplementation.AVL)
		    	setAVL();		      	  
		    else if(this.map == MapImplementation.HashIndirizzamentoAperto) 
		    	setHash();
		}
		catch (FileNotFoundException e) {
			throw new MovidaFileException();
		}
	}
	
	@Override
	public void saveToFile(File f) {
		try {
	      FileWriter myWriter = new FileWriter(f);
	      //creo la riga vuota da riempire
	      String s = "";
	      int i = 0;
	      
	      //leggo ogni film in mArray tranne l'ultimo e lo aggiungo a s
	      for (; i < mArray.length - 1; i++) {
	    	  s += mArray[i].toString() + "\n\n";
	      }
	      
	      //aggiungo l'ultimo, evitando righe vuote alla fine
	      s += mArray[i].toString();
	      myWriter.write(s);
	      myWriter.close();
	    } 
		catch (IOException e) {
				throw new MovidaFileException();
	    }		
	}

	@Override
	public void clear() {
		this.mArray = null;
		this.tree = null;
		this.table = null;
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
		//traccio il caso in cui trovo il film
		Boolean find = false;
		
		//traccio il voto e l'anno del film per il delete nella struttura dati
		int vote = 0;
		int year = 0;
		
		//delete da mArray
		for (int i = 0; i < mArray.length; i++) {
			if (mArray[i].getTitle().equalsIgnoreCase(title)) {
				find = true;
				vote = mArray[i].getVotes();
				year = mArray[i].getYear();
				mArray[i] = null;
				Movie[] tmp = new Movie[this.mArray.length - 1];
				for (int w = 0, j = 0; w < mArray.length; w++) {
					if (mArray[w] != null) {
						tmp[j] = mArray[w];
						j++;
					}
				}
				this.mArray = tmp;
			}
		}		
		
		//delete nella struttura dati
		if (find){
			if (this.map == MapImplementation.AVL) {
				Nodo n = tree.cerca(vote, title);
				this.tree.delete(n);
			}
			else if (this.map == MapImplementation.HashIndirizzamentoAperto) {
				this.table.delete(year, title);
			}
			
			//se ho fatto il delete setto di nuovo gli array actors e directors
			getAllPeople();
			
			return true;
		}
		
		//il film non è stato trovato
		return false;
	}

	@Override
	public Movie getMovieByTitle(String title) {
		//creo il movie da restituire
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
		//creo la Person da restituire
		Person m = new Person ("");
		for (int i = 0; i < directors.length; i++) {
			if (directors[i].getName().equalsIgnoreCase(name)){
				m = directors[i];
				break;
			}	
		}
		//controllo che il nome sia diverso da "" 
		//per sapere se è già stato trovato 
		if (m.getName() == "") {
			for (int i = 0; i < actors.length; i++) {
				if (actors[i].getName().equalsIgnoreCase(name)){
					m = actors[i];
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

	/*	ricerca sequenziale in ArrayList
	 *  restituisce 1 se P c'è già e ne incrementa la frequency
	 *  0 altrimenti
	 */
	private Boolean cegia (ArrayList<Person> L, Person P) {
		for (int i = 0; i < L.size(); i++) {
			if (L.get(i).getName().equalsIgnoreCase(P.getName())) {
				L.get(i).increasef();
				return true;	
			}
		}
		return false;
	}
	
	/* reset a 1 degli incarichi di ogni persona 
	 * 1 perchè ogni attore presente in uno dei due array ha almeno un incarico
	 */
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
		//array per attori e direttori
		ArrayList<Person> act = new ArrayList<Person>();
		ArrayList<Person> dir = new ArrayList<Person>();
		
		resetallf();	//reset a 1 delle frequenze di tutte le persone in mArray
						//per gestire successive chiamate di getAllPeople
		
		//itero su ogni film in mArray
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
		
		//setto i due array
		this.actors = PtoArray(act);
		this.directors = PtoArray(dir);
		
		//append delle due ArrayList
		dir.addAll(act);
		
		//restituisco
		return PtoArray(dir);
	}	
	
	/* IMovidaSearch______________________________________________________________
     * 																			   
     * ___________________________________________________________________________
     */
	
	/* ricerca preordine su ogni nodo 
	 * parametri: 
	 * 	ArrayList m in cui metto le ricorrenze della sottostringa
	 * 	Stringa che vado a cercare nei titoli
	 * 	Nodo n, che nella prima chiamata è la radice
	 */
	private void searchMoviesByTitle_inAVL (ArrayList<Movie> m, String substring, Nodo n) {
		if (n != null) {
			//.indexOf() restituisce -1 se la sottostringa in input non è presente
			if (n.movie.getTitle().toUpperCase().indexOf(substring.toUpperCase()) != -1)
				m.add(n.movie);
			searchMoviesByTitle_inAVL (m, substring, n.left);
			searchMoviesByTitle_inAVL (m, substring, n.right);
		}
	}
	
	/* ricerca lineare nella tabella_hash 
	 * parametri: 
	 * 	ArrayList m in cui metto le ricorrenze della sottostringa
	 * 	Stringa che vado a cercare nei titoli
	 * 	tabella_hash su cui faccio la ricerca
	 */
	private void searchMoviesByTitle_inHash (ArrayList<Movie> m, String substring, tabella_hash h) {
		Movie[] s = h.getTable();
		for (int i = 0; i < h.getSize(); i++) {
			//.indexOf() restituisce -1 se la sottostringa in input non è presente
			if (s[i] != null && s[i].getTitle().toUpperCase().indexOf(substring.toUpperCase()) != -1)
				m.add(s[i]);
		}
	}
	
	/* controllo quale struttura dati sto usando
	 * e chiamo una delle due funzioni ausiliarie
	 */
	@Override
	public Movie[] searchMoviesByTitle(String title) {
		ArrayList<Movie> m = new ArrayList<Movie>();
		if (map == MapImplementation.AVL) {
			searchMoviesByTitle_inAVL(m, title, tree.root);
		}
		else if (map == MapImplementation.HashIndirizzamentoAperto) {
			searchMoviesByTitle_inHash(m, title, table);
		}
		return MtoArray(m);
	}

	@Override
	public Movie[] searchMoviesInYear(Integer year) {
		
		//creo l'ArrayList da riempire
		ArrayList<Movie> a = new ArrayList<Movie>();
		
		/* Caso con tabella_hash
		 * sfrutto la struttura dati che usa come chiave l'anno 
		 */		
		if (this.map == MapImplementation.HashIndirizzamentoAperto) {
			//creo una tabella_hash da modificare
			tabella_hash H = table;	
			//trovo un film pubblicato nell'anno cercato
			int index = H.indexof_year(year);
			//X contiene i film della tabella_hash
			Movie[] X = H.getTable();
			//se trovo un film valido lo aggiungo ad a e lo tolgo da H e da X
			while (index != -1) {
				a.add(X[index]);
				H.delete(year, X[index].getTitle());
				X[index] = null;
				index = H.indexof_year(year);
			}
		}
		
		/* Caso con AVL
		 * non posso sfruttare la struttura dati, che usa come chiave i voti
		 */
		else {		
			//ordinamento su mArray 
			if (this.sort == SortingAlgorithm.BubbleSort) {
				bubbleSort_year();
			}
			else if (this.sort == SortingAlgorithm.QuickSort) {
				quickSort_year();
			}
			
			//trovo un film pubblicato nell'anno cercato
			int m = binarySearch(0, mArray.length - 1, year);
			
			if (m != -1) {
				//caso 1: è il primo film
				if (m == 0) {
					while (mArray[m].getYear().equals(year)) {
						a.add(mArray[m]);
						m++;
					}
				} else 
				//caso 2: è l'ultimo film
				if (m == mArray.length - 1) {
					while (mArray[m].getYear().equals(year)) {
						a.add(mArray[m]);
						m--;
					}				
				} 
				//caso 3: è un film nel mezzo 
				else {
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
		}		
		return MtoArray(a);
	}

	@Override
	public Movie[] searchMoviesDirectedBy(String name) {
		int ingaggi = 0;
		
		//itero su directors per sapere la frequency: i film diretti dal regista cercato
		for (int i = 0; i < directors.length; i++) {
			if (directors[i].getName().equalsIgnoreCase(name)) {
				ingaggi = directors[i].getf();
				break;
			}
		}
		
		//cerco in mArray, i iteratore per mArray
		//ii iteratore per l'array da restituire
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
		
		//itero su actors per sapere la frequency: i film a cui l'attore ha partecipato
		for (int i = 0; i < actors.length; i++) {
			if (actors[i].getName().equalsIgnoreCase(name)) {
				ingaggi = actors[i].getf();
				break;
			}			
		}
		
		//cerco in mArray, i iteratore per mArray
		//ii iteratore per l'array da restituire
		//j iteratore per il cast dell i-esimo film
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

	/* restituisco gli N film più votati//
	 */	
	@Override
	public Movie[] searchMostVotedMovies(Integer N) {
		//creo l'array da riempire
		Movie[] m = new Movie[0];

		if (N < 0) {
			System.out.println("Inserisci un numero positivo");
			return m;
		}
		
		/* Caso con AVL
		 * sfrutto la struttura dati con chiave voti
		 */
		if (this.map == MapImplementation.AVL) {
			if (N >= this.mArray.length) {
				ArrayList<Movie> mm = new ArrayList<Movie>();
				tree.visitaDestra(this.tree.root, mm);
				m = this.MtoArray(mm);
			}
			else {
				AVL copy = this.tree;
				m = new Movie[N];
				for (int i = 0; i < N; i++) {
					Nodo x = copy.massimo(copy.root);
					m[i] = x.movie;
					copy.delete(x);
				}
			}
		}
		
		/* Caso con Tabella_hash
		 * non posso sfruttare la struttura dati che usa come chiave l'anno
		 */
		else {
			//sort in ordine crescente di voti
			if (this.sort == SortingAlgorithm.BubbleSort)
				bubbleSort_votes();
			else if (this.sort == SortingAlgorithm.QuickSort) 
				quickSort_votes();
			
			if (N >= this.mArray.length) //caso N maggiore del totale dei film
				return Mribalta(mArray);
			else {
				m = new Movie[N];
				for (int i = mArray.length - 1, j = 0; j < N; i--, j++)
					m[j] = mArray[i];
			}
		}
		return m;
	}

	@Override
	public Movie[] searchMostRecentMovies(Integer N) {
		Movie[] m = new Movie[0];
		if (N < 0)
			System.out.println("Inserisci un numero positivo");
		else {
			//sort in ordine crescente di anno
			if (this.sort == SortingAlgorithm.BubbleSort) {
				bubbleSort_year();
				}
			else if (this.sort == SortingAlgorithm.QuickSort) 
				quickSort_year();
					
			if (N >= mArray.length) //caso N maggiore del totale dei film
				return Mribalta(mArray);
			else {
				m = new Movie[N];
				for (int i = mArray.length - 1, j = 0; j < N; i--, j++)
					m[j] = mArray[i];
			}
		}
		return m;
	}

	@Override
	public Person[] searchMostActiveActors(Integer N) {
		Person[] m = new Person[0];
		if (N < 0)
			System.out.println("Inserisci un numero positivo");
		else {
			//sort su actors
			if (this.sort == SortingAlgorithm.BubbleSort)
				bubbleSort_freq();
			else if (this.sort == SortingAlgorithm.QuickSort) 
				quickSort_freq();
			
			if (N >= actors.length) //caso N maggiore del totale degli attori
				return Pribalta(actors);
			else {
				m = new Person[N];
				for (int i = actors.length - 1, j = 0; j < N; i--, j++)
					m[j] = actors[i];
			}
		}
		return m;
	}
	
	/* IMovidaCollaborations_______________________________________________________
     * 																			   
     * ____________________________________________________________________________
     */
    
    /* controllo se la collaborazione tra a e b è già presente nell'ArrayLIst c
     * se non è presente la aggiungo
     * se è presente aggiungo il film m alla lista dei film in cui hanno collaborato
     */
    private Boolean cegia_setCollab (ArrayList<Collaboration> c, Person a, Person b, Movie m) {
    	//itero su c
    	for (int i = 0; i < c.size(); i++) {
    		//controllo se la collab è già presente
    		if (c.get(i).getActorA().getName().equalsIgnoreCase(a.getName())
    				&& c.get(i).getActorB().getName().equalsIgnoreCase(b.getName())
    				|| c.get(i).getActorA().getName().equalsIgnoreCase(b.getName())
    					&& c.get(i).getActorB().getName().equalsIgnoreCase(a.getName())) {
    			//aggiungo l'i-esimo film alla lista dei film in cui hanno collaborato
    			c.get(i).movies.add(m);
    			return true;
    		}    			
    	}
    	//altrimenti 
    	return false;
    }
    
    /* creo l'ArrayList delle collaborazioni
     */
    private void setCollab () {
    	ArrayList<Collaboration> m = new ArrayList<Collaboration>();
    	Collaboration s;
    	//scansione dei film
    	for (int i = 0; i < mArray.length; i++) {
    		Person[] c = mArray[i].getCast();
    		//scansione nel cast
    		for (int i2 = 0; i2 < c.length - 1; i2++) {
    			//scansione alla destra dell'attore [i2]
    			for (int i3 = i2 + 1; i3 < c.length; i3++) {
        			//se la collaborazione tra i2 e i3 non è ancora presente la aggiungo
    				if (!cegia_setCollab(m, c[i2], c[i3], mArray[i])) {
    					s = new Collaboration(c[i2], c[i3]);
    					//aggiungo ai film in cui hanno collaborato il film i-esimo
    					s.movies.add(mArray[i]);
    					m.add(s);
    				}
    			}
    		}
    	}
    	this.collab = m;
    }

    /* controllo che la persona P sia presente nell'ArrayList L
     */
    private Boolean cegia_person (ArrayList<Person> L, Person P) {
		for (int i = 0; i < L.size(); i++) {
			if (L.get(i).getName().equalsIgnoreCase(P.getName()))
				return true;	
		}
		return false;
	}
    
	@Override
	public Person[] getDirectCollaboratorsOf(Person actor) {
		ArrayList<Person> m = new ArrayList<Person>();
		//itero nell'array collab
		for (int i = 0; i < collab.size(); i++) {
			//controllo che actor sia uno dei due membri della i-esima collab
			if (collab.get(i).getActorA().getName().equalsIgnoreCase(actor.getName())
					|| collab.get(i).getActorB().getName().equalsIgnoreCase(actor.getName())) {
				//caso 1: è l'attore A
				//controllo se B è già presente nell'ArrayList m
				if (collab.get(i).getActorA().getName().equalsIgnoreCase(actor.getName())) {
					if (!cegia_person(m, collab.get(i).getActorB()))
						//se non è presente lo aggiungo
						m.add(collab.get(i).getActorB());
				}
				//caso 2: è l'attore B
				//controllo se A è già presente nell'ArrayList m
				else if (!cegia_person(m, collab.get(i).getActorA()))
					//se non è presente lo aggiungo
					m.add(collab.get(i).getActorA());
			}
		}
		//cast a Person[]
		return PtoArray(m);
	}

	@Override
	public Person[] getTeamOf(Person actor) {
		ArrayList<Person> p = new ArrayList<Person>();
		//se stesso
		p.add(actor);	
		
		//collaboratori diretti
		Person[] c = getDirectCollaboratorsOf(actor);
		for (int i = 0; i < c.length; i++)
			p.add(c[i]);
		
		//collaboratori indiretti
		//itero su ogni persona presente nel Team, cercandone i collaboratori diretti
		for (int i = 1; i < p.size(); i++) {	//p.size() aumenta ad ogni add eseguito
			Person[] d = getDirectCollaboratorsOf(p.get(i));
			//itero sui collaboratori diretti
			for (int ii = 0; ii < d.length; ii++) {
				//se ne trovo uno non ancora presente, lo aggiungo al Team
				if (!cegia_person(p, d[ii])) {
					p.add(d[ii]);
				}
			}
		}
		//cast a Person[]
		return PtoArray(p);
	}
	
	/* classe usata per confrontare le Collab nella PriorityQueue
	 * ordinamento non crescente
	 */
	class collab_comparator implements Comparator<Collaboration> { 
	    public int compare(Collaboration c1, Collaboration c2) 
	    { 
	    	if (c1.getScore() < c2.getScore()) 
                return 1; 
            else if (c1.getScore() > c2.getScore()) 
                return -1; 
            return 0; 
	    } 
	} 

	/*cerca in collab, una collaboration tra a e b
	 */
	private Boolean find (Person a, Person b, PriorityQueue<Collaboration> queue) {
		for (int i = 0; i < collab.size(); i++) {
			if (collab.get(i).getActorA().getName().equalsIgnoreCase(a.getName())
					&& collab.get(i).getActorB().getName().equalsIgnoreCase(b.getName())
					|| collab.get(i).getActorA().getName().equalsIgnoreCase(b.getName())
						&& collab.get(i).getActorB().getName().equalsIgnoreCase(a.getName()) 
							&& !collab.get(i).getMark()) {
				//la aggiungo alla priority queue
				queue.add(collab.get(i));
				collab.get(i).setMark(); //evito doppioni nella queue
				return true;
			}			
		}
		return false;
	}
	
	/* Algoritmo di Kruskal modificato
	 */
	@Override
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
		//creo il Team di actor
		Person[] p = getTeamOf(actor);
		
		//creo una PriorityQueue, ordinata in base agli score delle Collab 
		PriorityQueue<Collaboration> PRIO = new PriorityQueue<Collaboration>(new collab_comparator());
		
		//per ogni attore nel Team di actor, cerco tra le sue Collab e inserisco quelle nuove nella PriorityQueue
		for (int i = 0; i < p.length; i++) {
			Person[] c = getDirectCollaboratorsOf(p[i]);
			for (int ii = 0; ii < c.length; ii++) {
				find(p[i], c[ii], PRIO);
			}
		}
		
		//tutti i check delle collab ritornano a false
		for (Collaboration i : this.collab)
			i.unsetMark();
		
		ArrayList<Collaboration> T = new ArrayList<Collaboration>();
		UnionFind UF = new UnionFind(p);
		while (!PRIO.isEmpty()) {
			Person Tu = UF.find(PRIO.peek().getActorA()); 
			Person Tv = UF.find(PRIO.peek().getActorB()); 
			if (!Tu.getName().equalsIgnoreCase(Tv.getName())) { //se diversi
				T.add(PRIO.poll()); // aggiungi arco
				UF.union(Tu, Tv); // unisci componenti
			}
			else {
				PRIO.poll();
			}
		}
		return CtoArray(T);
	}
	
	/* Utili_______________________________________________________________________
     * 																			   
     * ____________________________________________________________________________
     */
	
	public Movie[] MtoArray(ArrayList<Movie> L) {
		Movie[] A = new Movie[L.size()];
		for (int i = 0; i < L.size(); i++) {
			 A[i] = L.get(i);
		}
		return A;
	}
	
	public Person[] PtoArray(ArrayList<Person> L) {
		Person[] A = new Person[L.size()];
		for (int i = 0; i < L.size(); i++) {
			 A[i] = L.get(i);
		}
		return A;
	}
	
	public Collaboration[] CtoArray(ArrayList<Collaboration> L) {
		Collaboration[] A = new Collaboration[L.size()];
		for (int i = 0; i < L.size(); i++) {
			 A[i] = L.get(i);
		}
		return A;
	}
	
	public void stampaArrayMovies (Movie[] m) {
		for (int i = 0; i < m.length-1; i++)
			System.out.println(m[i].toString() + "\n");
		System.out.println(m[m.length-1].toString());
	}
	
	public void stampaArrayPerson (Person[] m) {
		for (int i = 0; i < m.length-1; i++)
			System.out.println(m[i].toString() + "\n");
		System.out.println(m[m.length-1].toString());
	}
	
	public void stampaArrayCollab (Collaboration[] m) {
		for (int i = 0; i < m.length-1; i++) {
			System.out.println("A: " + m[i].getActorA().toString() 
					+ "\nB: " + m[i].getActorB().toString()
					+ "\nScore: " + m[i].getScore() + "\n");			
		}
		System.out.println("A: " + m[m.length-1].getActorA().toString() 
				+ "\nB: " + m[m.length-1].getActorB().toString()
				+ "\nScore: " + m[m.length-1].getScore());	
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
        int i = (low-1); 
        for (int j=low; j<high; j++){
            if (mArray[j].getVotes() < pivot.getVotes()) {
                i++;   
                Movie temp = mArray[i]; 
                mArray[i] = mArray[j]; 
                mArray[j] = temp; 
            } 
        }
        Movie temp = mArray[i+1]; 
        mArray[i+1] = mArray[high]; 
        mArray[high] = temp; 
  
        return i+1; 
    }   
	
    private void quickSort_votes_util(int low, int high) {
        if (low < high) {
            int pi = partition_votes(low, high); 
  
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
                                 temp = mArray[j-1];  
                                 mArray[j-1] = mArray[j];  
                                 mArray[j] = temp;  
                         }  	                          
                 }  
         }  	  
    }  

	private int partition_year(int low, int high){
		Movie pivot = mArray[high];  
	    int i = (low-1);
	    for (int j=low; j<high; j++){
	        if (mArray[j].getYear() < pivot.getYear()) {
	            i++;   
	            Movie temp = mArray[i]; 
	            mArray[i] = mArray[j]; 
	            mArray[j] = temp; 
	        } 
	    }   
	    Movie temp = mArray[i+1]; 
	    mArray[i+1] = mArray[high]; 
	    mArray[high] = temp; 
	
	    return i+1; 
	}   
	
	private void quickSort_year_util(int low, int high) {
	    if (low < high) {
	        int pi = partition_year(low, high); 
	        
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
                                 temp = actors[j-1];  
                                 actors[j-1] = actors[j];  
                                 actors[j] = temp;  
                         }  	                          
                 }  
         }  	  
    }  

	private int partition_freq(int low, int high){
		Person pivot = actors[high];  
	    int i = (low-1);  
	    for (int j=low; j<high; j++){ 
	        if (actors[j].getf() < pivot.getf()) {
	            i++;   
	            Person temp = actors[i]; 
	            actors[i] = actors[j]; 
	            actors[j] = temp; 
	        } 
	    }   
	    Person temp = actors[i+1]; 
	    actors[i+1] = actors[high]; 
	    actors[high] = temp; 
	
	    return i+1; 
	}   
	
	private void quickSort_freq_util(int low, int high) {
	    if (low < high) {
	        int pi = partition_freq(low, high); 
	
	        quickSort_freq_util(low, pi-1); 
	        quickSort_freq_util(pi+1, high); 
	    } 
	} 
	
	private void quickSort_freq() {
		quickSort_freq_util(0, actors.length - 1);
	}
	 
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