import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    private static BlockChain instance = null;

    public static BlockChain getInstance(){
        if (instance == null){
            instance = new BlockChain();
        }
        return instance;
    }

    public byte[] miningTargetValue = new
            BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF",16)
            .toByteArray();

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
        byte[] blockHashValue = block.getHeaderHash();

        boolean isProofOfWorkSatisfied = new BigInteger(blockHashValue).compareTo(new BigInteger(miningTargetValue)) < 0;
        if (!isProofOfWorkSatisfied){
            System.err.println("Block rejected! Block does not satisfy proof of work.");
            return false;
        }

        this.blocks.add(block);
        return true;

    }



}
