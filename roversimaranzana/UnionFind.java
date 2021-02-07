package roversimaranzana;

public class UnionFind {
	Person[] people;
	int [] dad;
	
	public UnionFind (Person[] people) {
		this.people = people;
		this.dad = new int [people.length];
		for (int i = 0; i < dad.length; i++)
			dad[i] = i;
	}
	
	public int find_index (Person wanted) {
		int i = 0;
		for (; i < dad.length; i++)
			if (wanted.getName().equalsIgnoreCase(people[i].getName()))
				break;
		return i;
	}
	
	public Person find (Person wanted) {
		return people[dad[find_index(wanted)]];
	}
	
	public void union (Person a, Person b) {
		int dad_a = find_index(a);
		int dad_b = find_index(b);
		if (dad_a != dad_b) {
			for (int i = 0; i < dad.length; i++)
				if (dad[i] == dad_b)
					dad[i] = dad_a;
		}		
	}
}
