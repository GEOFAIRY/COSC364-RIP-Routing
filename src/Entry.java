
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.Serializable;
public class Entry implements Serializable{
	
	private int dest;
	private int next_hop;
	private int metric;
	private LocalTime time;
	
	//CONSTRUCTOR FUNCTION
	public Entry(int dest, int next_hop, int metric, LocalTime t) {
		super();
		this.dest = dest;
		this.next_hop = next_hop;
		this.metric = metric;
		this.time = t;
	}
	
	//toString
	public String toString() {
		
			String result = "";
			
			//String.valueOf()
			result +="dest: " + String.valueOf(this.dest) + " ";
			result +="next hop: " + String.valueOf(this.next_hop) + " ";
			result +="metric: " + String.valueOf(this.metric) + " ";
			result +="time: " + this.time.toString() + " ";
			
			return result;
	}
	
	
	public long timer() {
		LocalTime now = LocalTime.now();
		return SECONDS.between(now, this.time);
	}

	public int getDest() {
		return dest;
	}

	public int getNextHop() {
		return next_hop;
	}

	public int getMetric() {
		return metric;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public void setNextHop(int next_hop) {
		this.next_hop = next_hop;
	}

	public void setMetric(int metric) {
		this.metric = metric;
	}
}
