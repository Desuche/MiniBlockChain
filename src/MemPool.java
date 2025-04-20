import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class MemPool {

    private static MemPool instance = null;

    public static MemPool getInstance() {
        if (instance == null){
            instance = new MemPool();
        }
        return instance;
    }



    private List<Transaction> pendingTransactions;


    private MemPool(){
        this.pendingTransactions = new ArrayList<>();
    }

    public Transaction[] collectTransactions(int count){
        /* Removes and returns a specified number of transactions from the mempool.
        *  If the mempool has fewer than is requested, it returns as many as possible.
        */

        Transaction[] transactions;
        if (pendingTransactions.size() <= count){
             transactions = pendingTransactions.toArray(new Transaction[0]);
             pendingTransactions = new ArrayList<>();
        } else {
            transactions = new Transaction[count];
            for (int i = 0; i < count; i ++){
                transactions[i] = pendingTransactions.get(0);
                pendingTransactions.remove(0);
            }
        }
        return transactions;
    }

}
