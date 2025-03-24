import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Generate dummy users
        User[] users = new User[3];
        users[0] = new User("Alice");
        users[1] = new User("Bob");
        users[2] = new User("Chris");

        Random random = new Random();

        // Generate a random number of transactions (power of 2)
        transaction[] transactions = new transaction[(int) Math.pow(2, 1 + random.nextInt(4))];

        // Generate random transactions between users
        for (int i = 0; i < transactions.length; i++) {
            int input = random.nextInt(3);
            int output;
            // Ensure the sender and receiver are not the same
            do {
                output = random.nextInt(3);
            } while (input == output);

            // Create a transaction with a random amount between 1 and 10
            transactions[i] = users[input].make_transaction(random.nextInt(10), users[output].address);
            System.out.println(transactions[i]);

            // Update the receiver's wallet balance
            users[output].setWallet(transactions[i].getData() + users[output].getWallet());
        }

        // Compare transaction IDs for equality
        System.out.println(Arrays.equals(transactions[0].transactionID, transactions[0].transactionID));
        System.out.println(Arrays.equals(transactions[0].transactionID, transactions[1].transactionID));

        // Collect transaction IDs for the Merkle Tree
        List<byte[]> transactionIDs = new ArrayList<>();
        for (transaction tx : transactions) {
            transactionIDs.add(tx.transactionID);
            System.out.printf("TX: %s%n", bytesToHex(tx.transactionID)); // Print transaction ID in hex
        }

        // Create a Merkle Tree and calculate the Merkle Root
        MerkleTree merkleTree = new MerkleTree(transactionIDs);
        byte[] merkleRoot = merkleTree.getMerkleRoot();
        System.out.println("Merkle Root: " + bytesToHex(merkleRoot)); // Print the Merkle Root

        // Print all transactions under the Merkle Tree
        merkleTree.printTransactions();
    }

    // Helper method to convert a byte array to a hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
