package Strutture_Dati;

import java.util.Scanner; 
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

public class MAIN {
	/* Scan lineare del file alla ricerca della parola "Title"
	 * Costo lineare al numero dei film
	 */
	public static int countMovies() {
		//i contatore per i movies
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
		return i;
	}

public static void main(String[] args) {
		try {
		      File myObj = new File("es0.txt");
		      Scanner myReader = new Scanner(myObj);
		      String data = "";
		      int nFilms = countMovies();
		      Movie [] Films = new Movie [nFilms];
		      int i = 0;	//i iteratore per Films
		      for(; i < nFilms; i++){
		    	  Films[i] = new Movie(""); 
		    	  data = myReader.nextLine();	//riga del Title
		    	  Films[i].setTitle(data.substring(7)); 
//		    	  System.out.println(Films[i].getTitle());
		    	  data = myReader.nextLine();
		    	  Films[i].setYear(Integer.parseInt(data.substring(6, 9)));
		    	  data = myReader.nextLine();
		    	  Films[i].setDirector(data.substring(9));
		    	  data = myReader.nextLine();
		    	  Films[i].setCast(data.substring(6));
		    	  data = myReader.nextLine();
		    	  Films[i].setVotes(Integer.parseInt((data.substring(7)).trim()));	//stringa.trim() permette di ignorare gli spazi bianchi
		    	  if(myReader.hasNext())
		    		  data = myReader.nextLine(); // -> riga vuota		      
		      }
		      
		      System.out.println(Films[6].getTitle());
		      
		      myReader.close();
			} 
		catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
				}
		}
}