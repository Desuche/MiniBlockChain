import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Arrays;

public class Verifier {



    public static boolean verifyBlock(Block block, BlockChain blockchain) {
        // 1. Verify the block's own hash is valid
        if (!Arrays.equals(block.generateNewHash(), block.getStoredHash())) {
            System.out.println("Verification Failed: block hash has changed! " + block);
            return false;
        }

        // 2. Verify the previous block hash matches the actual previous block
        byte[] previousBlockHash = block.previousBlockHash;
        int blockIndex = block.getBlockNumber();

        if (blockIndex > 0) {  // If not genesis block
            Block previousBlock = blockchain.blocks.get(blockIndex - 1);
            if (!Arrays.equals(previousBlockHash, previousBlock.getStoredHash())) {
                System.out.println("Verification Failed: This block's 'prevBlockHash' breaks the blockchain sequence! " + block);
                return false;
            }
        } else {  // Genesis block should have all zeros as previous hash
            if (!Arrays.equals(previousBlockHash, new byte[32])) {
                System.out.println("Verification Failed: This block's 'prevBlockHash' breaks the blockchain sequence! " + block);
                return false;
            }
        }

        return true;
    }

    public static boolean verifyBlockTransactions(Block block) throws Exception {
        byte[] generatedMerkleRoot = MerkleTree.createFromTransactionList(block.transactions).getMerkleRoot();
        byte[] storedMerkleRoot = block.merkleRoot;

        if (! Arrays.equals(generatedMerkleRoot, storedMerkleRoot)) {
            System.out.println("Verification Failed: block has merkle root mismatch! " + block);
            return false;
        }

        for (Transaction txn : block.transactions){
            if (!verifySingleTransaction(txn)){
                return false;
            }
        }

        return true;

    }

    public static boolean verifySingleTransaction(Transaction transaction) throws Exception{

        // Get user that created this transaction
        UserManager users = UserManager.getInstance();
        User sender = users.getUserByAddress(transaction.sender_address);
        PublicKey senderPubKey = sender.publicKey;

        // Get hash of transaction amount
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] dataBytes = ByteBuffer.allocate(8).putDouble(transaction.data).array();
        byte[] generatedDataHash = digest.digest(dataBytes);


        // Decrypt transaction signature
        Cipher cipher = Cipher.getInstance("RSA");
        // use public key to decrypt the message
        cipher.init(Cipher.DECRYPT_MODE, senderPubKey);
        byte[] decryptedDataHash = cipher.doFinal(transaction.signature);

        // Verify transaction
        if (Arrays.equals(generatedDataHash, decryptedDataHash)){
            return true;
        } else {
            System.out.println("Verification Failed: transaction has been altered! " + transaction);
            return false;
        }
    }


    public static boolean verifyBlockChain(BlockChain blockChain) {
        if (blockChain.blocks.isEmpty()) {
            return true;  // Empty chain is valid
        }

        // Verify each block in sequence
        for (int i = 0; i < blockChain.blocks.size(); i++) {
            Block currentBlock = blockChain.blocks.get(i);

            // Verify block number is sequential
            if (currentBlock.getBlockNumber() != i) {
                return false;
            }

            try {
                // Verify block structure and previous hash
                if (!verifyBlock(currentBlock, blockChain)) {
                    System.err.println("Verification Failed: Block " + i + " failed structural verification");
                    return false;
                }

                // Verify transactions and merkle root
                if (!verifyBlockTransactions(currentBlock)) {
                    System.err.println("Verification Failed: Block " + i + " failed transaction verification");
                    return false;
                }

                // Verify proof of work
                byte[] blockHash = currentBlock.getStoredHash();
                if (new BigInteger(1, blockHash).compareTo(new BigInteger(1, blockChain.miningTargetValue)) >= 0) {
                    System.err.println("Verification Failed: Block " + i + " failed proof of work verification");
                    return false;
                }

            } catch (Exception e) {
                System.err.println("Verification Failed: Error verifying block " + i + ": " + e.getMessage());
                return false;
            }
        }

        return true;
    }

    // Helper method to convert a byte array to a hexadecimal string
    private static String bytesToHex(byte[] bytes) {
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
