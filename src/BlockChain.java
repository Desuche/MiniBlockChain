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

    public static BlockChain createNewBlockChain(){
        instance = null;
        return getInstance();
    }

    public byte[] miningTargetValue = new
            BigInteger("0aFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF",16)
            .toByteArray();

    public List<Block> blocks = new ArrayList<>();


    public byte[] getLastBlockHash(){
        if (blocks.size() == 0){
            return new byte[32]; // 32 bytes of zeros
        } else{
            Block lastBlock = blocks.get(blocks.size() - 1);
            return lastBlock.getStoredHash();
        }
    }

    public boolean addBlock(Block block){
        byte[] blockHashValue = block.generateNewHash();

        boolean isProofOfWorkSatisfied = new BigInteger(blockHashValue).compareTo(new BigInteger(miningTargetValue)) < 0;
        if (!isProofOfWorkSatisfied){
            System.err.println("Block rejected! Block does not satisfy proof of work.");
            return false;
        }

        block.setBlockNumber(this.blocks.size());
        this.blocks.add(block);
        return true;

    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BlockChain:\n");
        sb.append("Number of Blocks: ").append(blocks.size()).append("\n\n");

        for (int i = 0; i < blocks.size(); i++) {
            sb.append("Block #").append(i).append(":\n");
            sb.append(blocks.get(i).toString()).append("\n");
        }

        return sb.toString();
    }

    // Helper method to convert a byte array to a hexadecimal string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        // Pad with leading zeros if necessary to ensure the length is 32
        while (hexString.length() < 64) { // 32 bytes = 64 hex characters
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }



}
