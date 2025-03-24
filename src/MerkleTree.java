import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private final List<byte[]> transactions;

    public MerkleTree(List<byte[]> transactions) {
        this.transactions = transactions;
    }

    public byte[] getMerkleRoot() throws Exception {
        List<byte[]> currentLevel = new ArrayList<>(transactions);

        while (currentLevel.size() > 1) {
            List<byte[]> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                byte[] left = currentLevel.get(i);
                byte[] right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left;
                nextLevel.add(hashPair(left, right));
            }
            currentLevel = nextLevel;
        }

        return currentLevel.get(0);
    }

    public void printTransactions() throws Exception {
        System.out.println("Transactions under the Merkle Tree:");
        for (byte[] transaction : transactions) {
            System.out.println(bytesToHex(transaction));
        }
        System.out.println("Merkle Root: " + bytesToHex(getMerkleRoot()));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private byte[] hashPair(byte[] left, byte[] right) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(left);
        digest.update(right);
        return digest.digest();
    }
}
