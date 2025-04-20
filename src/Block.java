import java.time.Instant;
import java.util.List;

public class Block {

    public byte[] hash;
    public byte[] previousBlockHash;
    public String timestamp;
    public long nonce;
    public long difficulty;
    public byte[] merkleRoot;
    public List<Transaction> transactions;

    public byte[] generateHeaderHash(){

    }

    public Block(byte[] previousBlockHash, List<Transaction> transactions){
        this.previousBlockHash = previousBlockHash;
        this.timestamp = Instant.now().toString();
        this.nonce = 0;
        this.transactions = transactions;
        this.merkleRoot = new MerkleTree(transactions).getMerkleRoot();

    }





}
