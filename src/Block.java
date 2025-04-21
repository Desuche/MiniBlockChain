import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block {

    private int blockNumber = -1; // -1 represents a pending block
    private byte[] hash; // hash is private so it cannot be changed after it is set by the miner;
    public byte[] previousBlockHash;
    public String timestamp;
    public Long nonce;
    public byte[] difficulty;
    public byte[] merkleRoot;
    public List<Transaction> transactions;



    public Block(byte[] previousBlockHash, Transaction[] transactions, byte[] miningTargetValue) throws Exception {
        this.previousBlockHash = previousBlockHash;
        this.timestamp = Instant.now().toString();
        this.nonce = 0L;
        this.difficulty = miningTargetValue;
        List<Transaction> transactionList = new ArrayList<>(Arrays.asList(transactions));
        this.merkleRoot = MerkleTree.createFromTransactionList(transactionList).getMerkleRoot();
        this.transactions = transactionList;
    }

    public byte[] generateNewHash() {
        MessageDigest headerHash = null;
        try {
            headerHash = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e){
            System.err.println("NoSuchAlgorithmException when generating block hash: " + e);
            System.exit(1);
        }

        headerHash.update(previousBlockHash);
        headerHash.update(timestamp.getBytes());
        headerHash.update(nonce.byteValue());
        headerHash.update(difficulty);
        headerHash.update(merkleRoot);
        return headerHash.digest();
    }

    public void incrementNonce(){
        this.nonce++;
    }


    public void setBlockHash(){

        // Only set the block hash once; further attempts to change the block hash will fail

        if (this.hash == null){
            this.hash = this.generateNewHash();
        }

    }

    public byte[] getStoredHash(){
        return this.hash;
    }


    public int getBlockNumber(){
        return this.blockNumber;
    }

    public void setBlockNumber(int blockNumber){
        if (this.blockNumber == -1){
            this.blockNumber = blockNumber;
        }
    }

    public Transaction findTransactionByID(String txnIdinHexFormat){
        for ( Transaction t : this.transactions){
            if (t.transactionIDInHexFormat.equals(txnIdinHexFormat)) return t;
        }

        return null;
    }

    public String getTransactionIDs(){
        StringBuilder sb = new StringBuilder("[ ");
        for (Transaction t: this.transactions){
            sb.append(t.transactionIDInHexFormat + " ; ");
        }
        sb.append(" ] ");
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Block{\n");
        sb.append("  hash=").append(BlockChain.bytesToHex(hash)).append(",\n");
        sb.append("  previousBlockHash=").append(BlockChain.bytesToHex(previousBlockHash)).append(",\n");
        sb.append("  timestamp='").append(timestamp).append("',\n");
        sb.append("  nonce=").append(nonce).append(",\n");
        sb.append("  difficulty=").append(BlockChain.bytesToHex(difficulty)).append(",\n");
        sb.append("  merkleRoot=").append(BlockChain.bytesToHex(merkleRoot)).append(",\n");
        sb.append("  number of transactions=").append(transactions.size()).append(",\n");
        sb.append("}");
        return sb.toString();
    }




}
