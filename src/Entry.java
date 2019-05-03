
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.Serializable;

/**
 * class to hold a single entry about a router in the network
 */
public class Entry implements Serializable {
    static final long serialVersionUID = 42L;
    private int dest;
    private int next_hop;
    private int metric;
    private LocalTime time;

    /**
     * constructor to create a new entry for a router
     * @param dest int the destination router
     * @param next_hop int the next hop to get to the destination
     * @param metric int the cost to get to the destination
     * @param t LocalTime the time the link was last reported
     */
    Entry(int dest, int next_hop, int metric, LocalTime t) {
        super();
        this.dest = dest;
        this.next_hop = next_hop;
        this.metric = metric;
        this.time = t;
    }

    /**
     * method to stringify the entry
     * @return String a string representing the entry
     */
    public String toString() {

        String result = "";
        result += "dest: " + String.valueOf(this.dest) + " ";
        result += "next hop: " + String.valueOf(this.next_hop) + " ";
        result += "metric: " + String.valueOf(this.metric) + " ";
        result += "time: " + this.time.toString() + " ";

        return result;
    }


    /**
     * method to check how long it has been since the last update of the entry
     * @return long the amount of seconds since the entry was reported
     */
    long timer() {
        LocalTime now = LocalTime.now();
        return SECONDS.between(this.time, now);
    }

    void setTime(LocalTime time) {
        this.time = time;
    }

    int getDest() {
        return dest;
    }

    int getNextHop() {
        return next_hop;
    }

    int getMetric() {
        return metric;
    }

    private LocalTime getTime() {
        return time;
    }

    void setNextHop(int next_hop) {
        this.next_hop = next_hop;
    }

    void setMetric(int metric) {
        this.metric = metric;
    }

    /**
     * method to duplicate the entry
     * @return Entry a new clone of the entry
     */
    Entry duplicateEntry() {
        return new Entry(getDest(), getNextHop(), getMetric(), getTime());
    }
}
