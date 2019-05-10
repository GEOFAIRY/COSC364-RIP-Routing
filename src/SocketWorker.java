import java.net.DatagramSocket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * class to handle deconstructing an entry table to be merged with this routers one
 */
public class SocketWorker implements Runnable {

    private EntryTable newTable;
    private DatagramSocket socket;

    /**
     * constructor method to get the information needed to work
     * @param newTable EntryTable the table that we received
     * @param socket DatagramSocket the socket that received the data
     */
    SocketWorker(EntryTable newTable, DatagramSocket socket) {
        this.newTable = newTable;
        this.socket = socket;
    }

    /**
     * runnable method to interperate the incoming entry table
     */
    public void run() {
        LocalTime now = LocalTime.now();
        //System.out.println("RECIEVED \tPort: " + socket.getLocalPort() + "\t" + now.toString());

        Integer nextHop = newTable.getSourceRouter();

        ArrayList<Entry> entriesToAdd = new ArrayList<>();
        for (Entry newEntry : newTable.getEntries()) {
            boolean flag = false;
            for (Entry currentEntry : Runner.entryTable.getEntries()) {
                if (newEntry.getDest() == currentEntry.getDest()) { // we have an existing record to update!
                    flag = true;
                    if (newEntry.getMetric() < currentEntry.getMetric() && currentEntry.getMetric() != Runner.INFINITY) { // the new entry has a smaller metric then our one
                        Runner.entryTable.removeEntry(currentEntry.getDest());
                        newEntry.setNextHop(nextHop);
                        newEntry.setTime(LocalTime.now());
                        entriesToAdd.add(newEntry);
                    } else if (currentEntry.getNextHop() == nextHop) { //the next hops and destination are the same
                        currentEntry.setMetric(newEntry.getMetric());
                    }
                }
                if (nextHop == currentEntry.getDest() && currentEntry.getMetric() == Runner.INFINITY) { //router has come back from the dead, re add its data
                    currentEntry.setTime(LocalTime.now());
                    for (List<String> output : Runner.routerConfig.outputs) {
                        if (socket.getLocalPort() == Integer.parseInt(output.get(0))) {
                            currentEntry.setMetric(Integer.parseInt(output.get(1)));
                        }
                    }
                }
            }
            if (!flag && newEntry.getMetric() < Runner.INFINITY) { // this is a new entry to add to our table
                newEntry.setNextHop(nextHop);
                newEntry.setTime(LocalTime.now());
                entriesToAdd.add(newEntry);
            }
        }
        for (Entry entryToAdd : entriesToAdd) { // add the new entries
            Runner.entryTable.update(entryToAdd);
        }

        System.out.println("\n Router ID: " + Runner.routerConfig.routerId);
        System.out.println(Runner.entryTable.toString()); //output for user to see

    }
}
