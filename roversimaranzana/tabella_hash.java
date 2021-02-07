package roversimaranzana;

public class tabella_hash {
	private Integer size;
	private Movie table[];
	
	public tabella_hash(int s) {
		if(s > 0){
			this.size = s;
			this.table = new Movie[this.size];
		}
		else
			System.out.println("Inserire una dimensione positiva");
	}
	
	public int getSize() {
		return this.size;
	}
	
	public Movie[] getTable() {
		return this.table;
	}

	public void setTable(Movie table[]) {
		this.table = table;
		this.size = table.length;
	}
	
	public int hash(int key) { //funzione hash data una chiave
		return key % size;
	}
	
	public int scan_quadratica(int key, int i) { 
		int k = hash(key);
		return (k + i + i * i) % size;
	}
	
	public int indexof(int key, String m) {
		int i = 0;
		int j;
		do {
			j = scan_quadratica(key, i);
			if(getTable()[j] != null && getTable()[j].getYear() == key && getTable()[j].getTitle().equalsIgnoreCase(m))
				return j;
			i++;			
		} while(i != size); //table[j] != null
		
		return -1;
	}
	
	/* Usata nella ricerca dei film per Anno 
	 */
	public int indexof_year(int year) {
		int i = 0;
		int j;
		do {
			j = scan_quadratica(year, i);
			if(getTable()[j] != null && getTable()[j].getYear() == year)
				return j;
			i++;			
		} while(i != size); //table[j] != null
		
		return -1;
	}
	
	public void search(int key, String m) {
		int index = indexof(key, m);
		if(index != -1) 
			System.out.println(getTable()[index]);
		else
			System.out.println("Film non trovato");
	}
	
	public boolean insert(int key, Movie m) {
			int i = 0;
			int j; 
			while(i != size) {
				j = scan_quadratica(key, i);
				if(getTable()[j] == null) {
					getTable()[j] = m;
					return true;
				}
				else
					i++;
			}
			return false;
		}

	public void delete(Integer key, String m) {
		int x = indexof(key, m);
		if (x != -1) {
			getTable()[x] = null;
		}
	}

	public void stampall() {
		for(int i = 0; i < size; i++)
			if(getTable()[i] != null)
				System.out.println( i + " " + getTable()[i].getTitle());
	}
}