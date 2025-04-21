import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Miner {
    long TIMEOUT_MS = 150000; // Mining timeout in milliseconds
    int NUMBER_OF_TRANSACTIONS_TO_MINE = 5;

    public Miner(){}
    public Miner(int max_transactions_per_mining_attempt){
        this.NUMBER_OF_TRANSACTIONS_TO_MINE = max_transactions_per_mining_attempt;
    }

    public Block mine() throws Exception {
        Transaction[] transactions = MemPool.getInstance().collectTransactions(NUMBER_OF_TRANSACTIONS_TO_MINE); // Mine five earliest transactions

        // Filter valid transactions
        List<Transaction> validTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (Verifier.verifySingleTransaction(transaction)) {
                validTransactions.add(transaction);
            }
        }

        // If no valid transactions were found, return null
        if (validTransactions.isEmpty()) {
            return null;
        }

        // Create a new block with the valid transactions
        BlockChain blockchain = BlockChain.getInstance();
        Block newBlock = new Block(blockchain.getLastBlockHash(), validTransactions.toArray(new Transaction[0]), blockchain.miningTargetValue);

        // Attempt Proof of Work
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
            byte[] blockHash = block.generateNewHash();
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



}
