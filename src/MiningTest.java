public class MiningTest {
    public static void main(String[] args) throws Exception {


        // Create a mempool instance
        MemPool memPool = MemPool.getInstance();
        UserManager users = UserManager.getInstance();

        // Generate some dummy transactions
        users.addUser(new User("Alice"));
        users.addUser(new User("Bob"));
        users.addUser(new User("Cassy"));

        System.out.println(users);

        // Create and add transactions to the mempool
        Transaction tx1 = users.getUserByName("Alice").make_transaction(5, users.getUserByName("Bob").address);
        Transaction tx2 = users.getUserByName("Cassy").make_transaction(2, users.getUserByName("Alice").address);
        Transaction tx3 = users.getUserByName("Bob").make_transaction(1, users.getUserByName("Cassy").address);

        memPool.addTransaction(tx1);
        memPool.addTransaction(tx2);
        memPool.addTransaction(tx3);

        System.out.println(memPool);

        // Start the mining process
        Miner miner = new Miner();
        Block newBlock = miner.mine(); // Attempt to mine a new block

        if (newBlock != null) {
            System.out.println("New block mined with hash: " + bytesToHex(newBlock.getStoredHash()));
        } else {
            System.out.println("Mining failed.");
        }


        Transaction tx4 = users.getUserByName("Cassy").make_transaction(15, users.getUserByName("Bob").address);
        Transaction tx5 = users.getUserByName("Bob").make_transaction(21, users.getUserByName("Alice").address);
        Transaction tx6 = users.getUserByName("Alice").make_transaction(11, users.getUserByName("Cassy").address);

        memPool.addTransaction(tx4);
        memPool.addTransaction(tx5);
        memPool.addTransaction(tx6);

        Block newBlock2 = miner.mine(); // Attempt to mine a new block

        if (newBlock2 != null) {
            System.out.println("New block mined with hash: " + bytesToHex(newBlock2.getStoredHash()));
        } else {
            System.out.println("Mining failed.");
        }


        System.out.println("________________________BLOCKCHAIN_______________");
        System.out.println(BlockChain.getInstance());


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