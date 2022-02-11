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
		
		StudentV2<Integer> a = new StudentV2<>(Integer.valueOf(2));
		StudentV2<String>  b = new StudentV2<>(new String("C"));
		StudentV2<Double> c = new StudentV2<>(223.5);
		StudentV2<Integer> d = new StudentV2<>(200);
		
		System.out.println("Name a " + a.toString());
		System.out.println("Name b " + b.toString());
		System.out.println("Name c " + c.toString());
		
		// Now the following line produces an error, why?
		//System.out.println("Comp a<-->b: " + a.compareTo(b));
				
		System.out.println("Comp a<-->d: " + a.compareTo(d));
		
		// Example of ordering of student with comparable ID
		StudentV2[] students = { new StudentV2<String>("cs01"),
	            new StudentV2<String>("cs21"),
	            new StudentV2<String>("cs11"),
	            new StudentV2<String>("cs08") };

	     System.out.println("\n\nBefore sorting");
	     System.out.println(Arrays.toString(students));
	     Arrays.sort(students);
	     System.out.println("\n\nAfter sorting by Comparable");
	     System.out.println(Arrays.toString(students));
	     System.out.println("\n\n");
	     
	     // Now examples of using complex identifier
	     StudentV2<ComplexIdentifier> sci1 = 
					new StudentV2<>(new ComplexIdentifier(3, "tres"));
			
		 StudentV2<ComplexIdentifier> sci2 = 
					new StudentV2<>(new ComplexIdentifier(3, "cuatro"));
		
		 System.out.println("Name sci1 " + sci1.toString());
		 System.out.println("Name sci2 " + sci2.toString());
		 System.out.println("Comp sci1<-->sci2:" + sci1.compareTo(sci2));					
	}

}