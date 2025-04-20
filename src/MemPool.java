import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MemPool {

    private static MemPool instance = null;

    public static MemPool getInstance() {
        if (instance == null){
            instance = new MemPool();
        }
        return instance;
    }



    private Queue<Transaction> pendingTransactions;


    private MemPool(){
        this.pendingTransactions = new LinkedList<>();
    }

    public void addTransaction(Transaction transaction){
        this.pendingTransactions.add(transaction);
    }

    public Transaction[] collectTransactions(int count){
        /* Removes and returns a specified number of transactions from the mempool.
        *  If the mempool has fewer than requested, it returns as many as possible.
        */

        count = Math.min(this.pendingTransactions.size(), count);
        Transaction[] transactions = new Transaction[count];

        for (int i = 0; i < count; i ++){
            transactions[i] = this.pendingTransactions.poll();
        }

        return transactions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MemPool{\n");
        for (Transaction tx : pendingTransactions) {
            sb.append("  ").append(tx.toString()).append(",\n");
        }
        sb.append("}");
        return sb.toString();
    }



}
