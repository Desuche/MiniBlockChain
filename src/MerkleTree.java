import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private final List<byte[]> transactions; // List of transaction IDs

    // Constructor to initialize the Merkle Tree with a list of transactions
    public MerkleTree(List<byte[]> transactions) {
        this.transactions = transactions;
    }

    // Method to calculate the Merkle Root
    public byte[] getMerkleRoot() throws Exception {
        List<byte[]> currentLevel = new ArrayList<>(transactions);

        // Iteratively hash pairs of nodes until a single root is obtained
        while (currentLevel.size() > 1) {
            List<byte[]> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                byte[] left = currentLevel.get(i);
                byte[] right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left; // Handle odd nodes
                nextLevel.add(hashPair(left, right));
            }
            currentLevel = nextLevel;
        }

        return currentLevel.get(0); // Return the final root
    }

    // Method to print all transactions and the Merkle Root
    public void printTransactions() throws Exception {
        System.out.println("Transactions under the Merkle Tree:");
        for (byte[] transaction : transactions) {
            System.out.println(bytesToHex(transaction)); // Print each transaction ID in hex
        }
        System.out.println("Merkle Root: " + bytesToHex(getMerkleRoot())); // Print the Merkle Root
    }

    // Helper method to convert a byte array to a hexadecimal string
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Helper method to hash a pair of byte arrays
    private byte[] hashPair(byte[] left, byte[] right) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(left);
        digest.update(right);
        return digest.digest();
    }
}
