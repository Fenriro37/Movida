package Strutture_Dati;

public class tabella_hash {
	private Integer size;
	private Movie table[];
	//private Boolean full = false;
	
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
	
	public int hash(int key) { //funzione hash data una chiave
		return key%size;
	}
	
	public int scan_quadratica(int key, int i) { 
		int k = hash(key);
		return (k + i + i*i) % size; 
		
	}
	
	public void raddoppia() {
		Movie tmp[] = new Movie[size*2];
		for(int i=0; i < size; i++) {
			if(table[i]!= null && table[i].getYear()!= 0)
				tmp[i] = table[i];
		}
		table = tmp;
	}
	
	public int indexof(int key, String m) {
		int i = 0;
		int j;
		do {
			j = scan_quadratica(key, i);
			if(table[j]!= null && table[j].getVotes() == key && table[j].getTitle().equalsIgnoreCase(m)) //key = voti film
				return j;
			i++;
			
		} while(i != size); //table[j] != null
		
		return -1;
	}
	
	public void search(int key, String m) {
		int index = indexof(key, m);
		if(index != -1) 
			System.out.println(table[index]);
		else
			System.out.println("Film non trovato");
		
	}
	
	public void insert(int key, Movie m) {
			int i = 0;
			int j; 
			while(i != size){
				j = scan_quadratica(key, i);
				if(table[j] == null || table[j].getYear() == 0) { //giostrare il deleted con qualcosa tipo anno -1000
					table[j] = m;
					break;
				}
				 
				else
					i++;
			}
			if(i == size)
			System.out.println("mission failed");
		}

	public void delete(Integer key, String m) {
		int x = indexof(key, m);
		System.out.println(x);
		if (x != -1) {
			table[x].setYear(0);
		}
	}

	public void stampall() {
		for(int i = 0; i < size; i++)
			if(table[i] != null && table[i].getYear() != 0)
				System.out.println( i + " " + table[i].getTitle());
	}
}