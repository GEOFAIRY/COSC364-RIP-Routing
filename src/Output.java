package myrip;
import java.util.*;
import java.lang.String;
public class Output {
	private int port;
	private int metric;
	private int dest;
	
	public Output(String pmd) {
		super();
		String [] elements = pmd.split("-");
		this.port = Integer.parseInt(elements[0]);
		this.metric = Integer.parseInt(elements[1]);
		this.dest = Integer.parseInt(elements[2]);
	}
	
	public String toString() {
		String result = "";
		result += "port:" + String.valueOf(this.port) + ",";
		result += "metric:" + String.valueOf(this.metric) + ",";
		result += "dest:" + String.valueOf(this.dest) + ",";
		return result;
	}
}
