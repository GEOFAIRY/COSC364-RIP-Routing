package myrip;
import java.util.*;
public class EntryTable {
	private Map<Integer,Entry> entries;
	
	
	public final static String LOCALHOST = "127.0.0.1";
	
	//public static String CONFIGFILE = "";
	public final static int BUFSIZE = 1023;
	public final static int TIMER = 6;
	public final static int TIMEROUT = TIMER/6;
	public final static int ENTRY_TIMEOUT = TIMER*6;
	public final static int GARBAGE = TIMER * 6;
	public final static int INFINITY = 30;
	
	
	public EntryTable() {
		super();
		this.entries = new HashMap<Integer,Entry>();
	}
	

	public String tostr() {
		String result = "";
		for(Entry entry : this.entries.values()){
			result += entry.toString() + "\n";
		}
		
		return result;
	}
	
	public String toString() {
		return this.tostr();
	}
	

	public List<Integer> destinations() {
		List<Integer> dest = new ArrayList<Integer>(this.entries.keySet());
		Collections.sort(dest);
		return dest;
	}
	

	public Entry getEntry(int dest) {
		Entry item = this.entries.get(dest);
		return item;
	}
	

	public List<Entry> getEntries(){
		List<Entry> result = new ArrayList<Entry>();
		for(int dest : this.destinations()) {
			result.add(this.entries.get(dest));
		}
		return result;
	}
	

	public Entry update(Entry entry) {
		Entry current = this.getEntry(entry.getDest());
		
		if(current == null) {
			if(entry.getMetric() <EntryTable.INFINITY) {
				this.entries.put(entry.getDest(), entry);
			}
			return entry;
		}
		else if(current.getFirst() == entry.getFirst()) {
			if(entry.getMetric() > EntryTable.INFINITY) {
				entry.setMetric(EntryTable.INFINITY);
			}
			this.entries.put(entry.getDest(), entry);
			return entry;
		}
		else if(entry.getMetric() < current.getMetric()) {
			this.entries.put(entry.getDest(), entry);
		}
		
		return null;
	}
	

	public Entry removeEntry(int dest) {
		if(this.entries.get(dest) == null) {
			return null;
		}else {
			return this.entries.remove(dest);
		}
	}
	

	public static void main(String [] args) {
		EntryTable et_tb = new EntryTable();
		Entry et_a = new Entry(1,10,1,new Date().getTime());
		Entry et_b = new Entry(2,20,2,new Date().getTime());
		Entry et_c = new Entry(3,30,3,new Date().getTime());
		Entry et_d = new Entry(4,40,4,new Date().getTime());
		et_tb.update(et_a);
		System.out.println("插入一个数据后:");
		System.out.println(et_tb);
		et_tb.update(et_b);
		et_tb.update(et_c);
		et_tb.update(et_d);
		System.out.println("插入四个数据后:");
		System.out.println(et_tb);
		et_tb.removeEntry(et_d.getDest());
		System.out.println("删除最后一个数据后:");
		System.out.println(et_tb);
		System.out.println("显示所有键(也就是dest):");
		List<Integer> dests = et_tb.destinations();
		System.out.println(dests);
		System.out.println("显示所有entry:");
		List<Entry> ets = et_tb.getEntries();
		System.out.println(ets);
		System.out.println("显示第三个entry:");
		System.out.println(et_tb.getEntry(et_c.getDest()));
	}
}
