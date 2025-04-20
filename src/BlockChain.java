import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    public static byte[] miningTargetValue = new
            BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF",16)
            .toByteArray();


    private static BlockChain instance = null;

    public static BlockChain getInstance(){
        if (instance == null){
            instance = new BlockChain();
        }
        return instance;
    }


    public List<Block> blocks = new ArrayList<>();


    public byte[] getLastBlockHash(){
        if (blocks.size() == 0){
            return new byte[32]; // 32 bytes of zeros
        } else{
            Block lastBlock = blocks.get(blocks.size() - 1);
            return lastBlock.getHash();
        }
    }

    public boolean addBlock(Block block){
        byte[] blockHashValue;
        try{
            blockHashValue = block.getHeaderHash();
        } catch (
        NoSuchAlgorithmException e){
            System.err.println("Failed to add block: Error generating block header hash: " + e.toString());
            return false;
        }

        boolean isProofOfWorkSatisfied = new BigInteger(blockHashValue).compareTo(new BigInteger(miningTargetValue)) < 0;
        if (!isProofOfWorkSatisfied){
            System.err.println("Block rejected! Block does not satisfy proof of work.");
            return false;
        }

        this.blocks.add(block);
        return true;

    }



}
