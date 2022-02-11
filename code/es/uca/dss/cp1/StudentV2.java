package es.uca.dss.cp1;

import java.util.Arrays;

public class StudentV2<K extends Comparable<K>> 
                       implements Comparable<StudentV2<K>> {

	private K id;

	public StudentV2(K id) {
		this.id = id;
	}

	K getId() {
		return this.id;
	}
	
	public String toString() {
		return id.toString();
	}

	public int compareTo(StudentV2<K> other) {
		return this.id.compareTo(other.id);				
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StudentV2<Integer> a = new StudentV2<>(Integer.valueOf(2));
		StudentV2<String>  b = new StudentV2<>(new String("C"));
		StudentV2<Integer> c = new StudentV2<>(223);
		
		System.out.println("Name " + a.toString());
		System.out.println("Name " + b.toString());
		System.out.println("Name " + c.toString());
		
		//System.out.println("Comp " + a.compareTo(b));
		
		
		System.out.println("Comp " + a.compareTo(c));
		

		StudentV2[] students = { new StudentV2<String>("cs01"),
	            new StudentV2<String>("cs21"),
	            new StudentV2<String>("cs11"),
	            new StudentV2<String>("cs08") };

	      System.out.println("Before sorting");
	      System.out.println(Arrays.toString(students));

	      // Note this is for the array
	      Arrays.sort(students);

	      System.out.println("\n\nAfter sorting by Comparable");
	      System.out.println(Arrays.toString(students));
		
		
	

	}

}
