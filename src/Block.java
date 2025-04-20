import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Block {

    public byte[] hash;
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
        List<Transaction> transactionList = Arrays.asList(transactions);
        this.merkleRoot = MerkleTree.createFromTransactionList(transactionList).getMerkleRoot();
        this.transactions = transactionList;
    }

    public byte[] getHeaderHash() {
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
        this.hash = this.getHeaderHash();

    }

    public byte[] getHash(){
        return this.hash;
    }






}
