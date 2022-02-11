package es.uca.dss.cp1;

/** 
 * 
 * @author andres.munoz
 * To be used as example of complex identifier in StudentV2
 */
public class ComplexIdentifier implements Comparable<ComplexIdentifier>{

	// Identifer composed of two fields (int + String)
	private int id1;
	private String id2;
	
	ComplexIdentifier(int i1, String i2){
		this.id1 = i1;
		this.id2 = i2;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id1+ "-" + id2;
	}
	@Override
	/**
	 * First compare int, if equal compare string
	 */
	public int compareTo(ComplexIdentifier o) {
		
		if(this.id1 != o.id1)
			return this.id1 - o.id1;
		else
			return this.id2.compareTo(o.id2);
	}
}
