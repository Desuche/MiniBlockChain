import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;

public class Block {

    public byte[] hash;
    public byte[] previousBlockHash;
    public String timestamp;
    public Long nonce;
    public Long difficulty;
    public byte[] merkleRoot;
    public List<Transaction> transactions;



    public Block(byte[] previousBlockHash, List<Transaction> transactions, long miningTargetValue) throws Exception {
        this.previousBlockHash = previousBlockHash;
        this.timestamp = Instant.now().toString();
        this.nonce = 0L;
        this.difficulty = miningTargetValue;
        this.merkleRoot = MerkleTree.createFromTransactionList(transactions).getMerkleRoot();
        this.transactions = transactions;
    }

    public byte[] generateHeaderHash() throws NoSuchAlgorithmException {
        MessageDigest headerHash = MessageDigest.getInstance("SHA-256");
        headerHash.update(previousBlockHash);
        headerHash.update(timestamp.getBytes());
        headerHash.update(nonce.byteValue());
        headerHash.update(difficulty.byteValue());
        headerHash.update(merkleRoot);
        return headerHash.digest();
    }

    public void incrementNonce(){
        this.nonce++;
    }

    public void setHash(byte[] hash){
        this.hash = hash;
    }

    public byte[] getHash(){
        return this.hash;
    }






}
