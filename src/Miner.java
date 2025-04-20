import java.math.BigInteger;

public class Miner {
    final long TIMEOUT_MS = 30000; // Mining timeout in milliseconds

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
        long startTime = System.currentTimeMillis();

        while (true) {
            // Check if the hash meets the mining target
            if (new BigInteger(block.getHeaderHash()).compareTo(new BigInteger(blockChain.miningTargetValue)) < 0) {
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



}
