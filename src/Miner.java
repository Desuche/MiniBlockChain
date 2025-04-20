import java.math.BigInteger;

public class Miner {
    final long TIMEOUT_MS = 120000; // Mining timeout in milliseconds

    public Block mine() throws Exception {
        Transaction[] transactions = MemPool.getInstance().collectTransactions(5); // Mine five earliest transactions
        BlockChain blockchain = BlockChain.getInstance();
        Block newBlock = new Block(blockchain.getLastBlockHash(), transactions, blockchain.miningTargetValue);

        if (this.solveProofOfWork(newBlock, blockchain)){
            blockchain.addBlock(newBlock);
            return newBlock;
        } else {
            return null;
        }

    }

    private boolean solveProofOfWork(Block block, BlockChain blockChain) {
        if (block == null){
            return false;
        }

        long startTime = System.currentTimeMillis();

        while (true) {
            // Check if the hash meets the mining target
            byte[] blockHash = block.getHeaderHash();
            BigInteger hashValue = new BigInteger(1, blockHash);
            BigInteger target = new BigInteger(1, blockChain.miningTargetValue);

            // Check if hash is below target (meaning valid solution found)
            if (hashValue.compareTo(target) < 0) {
                block.setBlockHash();
                return true; // Solution found
            }

            // Increment nonce for the next attempt
            block.incrementNonce();

            // Check for timeout
            if (System.currentTimeMillis() - startTime > TIMEOUT_MS) {
                System.out.println("Mining timed out.");
                return false; // Give up if timeout reached
            }
        }
    }

    private String bytesToHex(byte[] bytes) {
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

    public static void main(String[] args) throws Exception {
        System.out.println(new
                BigInteger("01a60d8893786c5d678b769c079e8e2d081d1729cf6177bd418c7aa6dd099efc", 16)
                .compareTo(new
                        BigInteger("0FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF",16)
                )
        );
    }


}
