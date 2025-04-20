public class MiningTest {
    public static void main(String[] args) throws Exception {


        // Create a mempool instance
        MemPool memPool = MemPool.getInstance();

        // Generate some dummy transactions
        User alice = new User("Alice");
        User bob = new User("Bob");

        // Create and add transactions to the mempool
        Transaction tx1 = alice.make_transaction(5, bob.address);
        Transaction tx2 = alice.make_transaction(2, bob.address);
        Transaction tx3 = bob.make_transaction(1, alice.address);

        memPool.addTransaction(tx1);
        memPool.addTransaction(tx2);
        memPool.addTransaction(tx3);

        System.out.println(memPool);

        // Start the mining process
        Miner miner = new Miner();
        Block newBlock = miner.mine(); // Attempt to mine a new block

        if (newBlock != null) {
            System.out.println("New block mined with hash: " + bytesToHex(newBlock.getHash()));
            System.out.println(newBlock);
        } else {
            System.out.println("Mining failed.");
        }


        Transaction tx4 = alice.make_transaction(5, bob.address);
        Transaction tx5 = alice.make_transaction(2, bob.address);
        Transaction tx6 = bob.make_transaction(1, alice.address);

        memPool.addTransaction(tx4);
        memPool.addTransaction(tx5);
        memPool.addTransaction(tx6);

        Block newBlock2 = miner.mine(); // Attempt to mine a new block

        if (newBlock2 != null) {
            System.out.println("New block mined with hash: " + bytesToHex(newBlock2.getHash()));
            System.out.println(newBlock2);
        } else {
            System.out.println("Mining failed.");
        }



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