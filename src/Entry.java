package myrip;
import java.util.Date;
public class Entry {
	
	private int dest;
	private int first;
	private int metric;
	private long time;
	
	//CONSTRUCTOR FUNCTION
	public Entry(int dest, int first, int metric, long t) {
		super();
		this.dest = dest;
		this.first = first;
		this.metric = metric;
		this.time = t;
	}
	
	//toString
	public String toString() {
		
			String result = "";
			
			//String.valueOf()
			result +="dest: " + String.valueOf(this.dest) + " ";
			result +="first: " + String.valueOf(this.first) + " ";
			result +="metric: " + String.valueOf(this.metric) + " ";
			result +="time: " + String.valueOf(this.timer()) + " ";
			
			return result;
	}
	
	
	public long timer() {
		long now = new Date().getTime();
		return now - this.time;
	}

	public int getDest() {
		return dest;
	}

	public int getFirst() {
		return first;
	}

	public int getMetric() {
		return metric;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setMetric(int metric) {
		this.metric = metric;
	}
	

	public static void main(String [] args) {
		Entry et = new Entry(1,1,1,new Date().getTime());
		System.out.println(et);
	}
}
