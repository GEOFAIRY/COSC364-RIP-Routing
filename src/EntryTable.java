
import java.io.Serializable;
import java.util.*;

/**
 * class to hold all entries of routers in the network
 */
public class EntryTable implements Serializable {
    static final long serialVersionUID = 42L;
    private Map<Integer, Entry> entries;
    private Integer sourceRouter;


    /**
     * constructor to create a new entry table to hold all the link entries
     */
    EntryTable() {
        super();
        this.entries = new HashMap<>();
        this.sourceRouter = Runner.routerConfig.routerId;
    }

    Integer getSourceRouter() {
        return this.sourceRouter;
    }

    /**
     * method to stringify the entry table
     * @return String a string representing the entry table
     */
    private String tostr() {
        StringBuilder result = new StringBuilder();
        for (Entry entry : this.entries.values()) {
            result.append(entry.toString()).append("\n");
        }

        return result.toString();
    }

    public String toString() {
        return this.tostr();
    }

    /**
     * method to return all destinations in the entry table
     * @return List list of destinations
     */
    private List<Integer> destinations() {
        List<Integer> dest = new ArrayList<>(this.entries.keySet());
        Collections.sort(dest);
        return dest;
    }

    private Entry getEntry(int dest) {
        return this.entries.get(dest);
    }

    /**
     * method to get a list of all entries
     * @return List list of all entries
     */
    List<Entry> getEntries() {
        List<Entry> result = new ArrayList<Entry>();
        for (int dest : this.destinations()) {
            result.add(this.entries.get(dest));
        }
        return result;
    }

    /**
     * method to add a new entry to the entry table
     * @param entry the new entry to add to the entry table
     */
    void update(Entry entry) {
        Entry current = this.getEntry(entry.getDest());

        if (current == null) {
            if (entry.getMetric() < Runner.INFINITY) {
                this.entries.put(entry.getDest(), entry);
            }
        } else if (current.getNextHop() == entry.getNextHop()) {
            if (entry.getMetric() > Runner.INFINITY) {
                entry.setMetric(Runner.INFINITY);
            }
            this.entries.put(entry.getDest(), entry);
        } else if (entry.getMetric() < current.getMetric()) {
            this.entries.put(entry.getDest(), entry);
        }

    }

    /**
     * method to remove an entry by destination
     * @param dest int the destination to remove from the table
     */
    void removeEntry(int dest) {
        if (!(this.entries.get(dest) == null)) {
            this.entries.remove(dest);
        }
    }

    /**
     * method to duplicate the entry table
     * @return EntryTable a new clone of the entry table
     */
    EntryTable duplicateTable() {
        EntryTable clone = new EntryTable();
        for (Entry entry : getEntries()) {
            clone.update(entry.duplicateEntry());
        }
        return clone;
    }
}
