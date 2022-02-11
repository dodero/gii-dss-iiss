package es.uca.dss.cp1;

import java.util.Arrays;

public class StudentV1 implements Comparable<StudentV1> {
	
	Comparable<Object> id;
	
	public StudentV1(Object id) throws ClassCastException {
		this.id = (Comparable<Object>) id;
	}
	
	Comparable<Object> getId(){
		return id;
	}
	
	public String toString() {
		return id.toString();
	}
	
	public int compareTo(StudentV1 otro) {
		if(this.id.getClass() != otro.id.getClass()) {
			System.out.println("Incomparables");
			throw new ClassCastException();
		}
		
		return this.id.compareTo(otro.id);				
	}
	
	
	public static void main(String[] args) {
		
		StudentV1 a = new StudentV1(Integer.valueOf(1));
		StudentV1 b = new StudentV1(new String("B"));
		StudentV1 c = new StudentV1(Double.valueOf(123.5));
		StudentV1 d = new StudentV1(Integer.valueOf(300));
		
		System.out.println("Name a " + a.toString());
		System.out.println("Name b " + b.toString());
		System.out.println("Name c " + c.toString());
		
		try {
		System.out.println("Comp a<-->b: " + a.compareTo(b));
		
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("Comp a<-->d: " + a.compareTo(d));
			
		} catch (ClassCastException e) {
				e.printStackTrace();
		}
				
				
		// Example of ordering of student with comparable ID
		StudentV1 [] students = { new StudentV1("cs01"),
	            new StudentV1("cs21"),
	            new StudentV1("cs11"),
	            new StudentV1("cs08") };

	      System.out.println("\n\nBefore sorting");
	      System.out.println(Arrays.toString(students));
	      Arrays.sort(students);
	      System.out.println("\n\nAfter sorting by Comparable");
	      System.out.println(Arrays.toString(students));
	}
	
}
