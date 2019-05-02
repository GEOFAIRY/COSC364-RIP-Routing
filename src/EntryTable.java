
import java.util.*;

public class EntryTable {
	private Map<Integer, Entry> entries;


	public EntryTable() {
		super();
		this.entries = new HashMap<Integer, Entry>();
	}

	public String tostr() {
		String result = "";
		for (Entry entry : this.entries.values()) {
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

	public List<Entry> getEntries() {
		List<Entry> result = new ArrayList<Entry>();
		for (int dest : this.destinations()) {
			result.add(this.entries.get(dest));
		}
		return result;
	}

	public Entry update(Entry entry) {
		Entry current = this.getEntry(entry.getDest());

		if (current == null) {
			if (entry.getMetric() < Runner.INFINITY) {
				this.entries.put(entry.getDest(), entry);
			}
			return entry;
		} else if (current.getNextHop() == entry.getNextHop()) {
			if (entry.getMetric() > Runner.INFINITY) {
				entry.setMetric(Runner.INFINITY);
			}
			this.entries.put(entry.getDest(), entry);
			return entry;
		} else if (entry.getMetric() < current.getMetric()) {
			this.entries.put(entry.getDest(), entry);
		}

		return null;
	}

	public Entry removeEntry(int dest) {
		if (this.entries.get(dest) == null) {
			return null;
		} else {
			return this.entries.remove(dest);
		}
	}
}
