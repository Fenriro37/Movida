package roversimaranzana;

import java.io.File;

public class MAIN {
	
	public static void main(String[] args) {
		System.out.println("1. Creazione di una nuova istanza della Classe MovidaCore");
		MovidaCore M = new MovidaCore();
		System.out.println("2. Creazione dell'oggetto File");
		File txt = new File("Data.txt");
		System.out.println("3. Inizializzazione delle Strutture Dati");
		M.loadFromFile(txt);
		
		System.out.println("\nAlcune funzioni di IMovidaDB:");
		System.out.println("\n->Funzione countMovies, output: " + M.countMovies());
		System.out.println("\n->Esecuzione di deleteMovieByTitle con input: \"Pulp Fiction\"");
		M.deleteMovieByTitle("Pulp Fiction");
		System.out.println("\te nuova esecuzione di countMovies: " + M.countMovies());
		System.out.println("\n->Esecuzione di getPersonByName con input: \"Harrison Ford\" " 
				+ M.getPersonByName("Harrison Ford"));
		System.out.println("\n->Esecuzione di getPersonByName con input: \"John Travolta\" " 
				+ M.getPersonByName("John Travolta"));
		
		System.out.println("\nAlcune funzioni di IMovidaSearch:");
		System.out.println("\n->Esecuzione di searchMostVotedMovies con input 3:\n");
		M.stampaArrayMovies(M.searchMostVotedMovies(3));
		System.out.println("_______________________________________________");
		System.out.println("\n->Esecuzione di searchMostActiveActors con input 2:\n");
		M.stampaArrayPerson(M.searchMostActiveActors(2));
		System.out.println("_______________________________________________");
		
		System.out.println("\nAlcune funzioni di IMovidaCollaborations:");
		System.out.println("\n->Esecuzione di getTeamOf con input l'attore Robert De Niro:\n");
		M.stampaArrayPerson(M.getTeamOf(M.getPersonByName("Robert De Niro")));
		System.out.println("_______________________________________________");
		System.out.println("\n->Esecuzione di maximizeCollaborationsInTheTeamOf "
				+ "con input l'attore Robert De Niro:\n");
		M.stampaArrayCollab(M.maximizeCollaborationsInTheTeamOf(M.getPersonByName("Robert De Niro")));
		System.out.println("_______________________________________________");
		
		System.out.println("\n4. Cambio delle strutture dati e dell'algoritmo di ordinamento");
		M.setMap(MapImplementation.HashIndirizzamentoAperto);
		M.setSort(SortingAlgorithm.BubbleSort);
		System.out.println("5. Inizializzazione delle Strutture Dati");
		M.loadFromFile(txt);
		
		System.out.println("\n->Funzione countMovies, output: " + M.countMovies());
		System.out.println("\n->Esecuzione di searchMoviesInYear con input: 1997\n");
		M.stampaArrayMovies(M.searchMoviesInYear(1997));		
	}
}