public class MiningTest {

    public static void main(String[] args) throws Exception{
        miningTest2();
    }

    public static void miningTest1() throws Exception{

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
        System.out.println();

        // Start the mining process
        Miner miner = new Miner();
        Block newBlock = miner.mine(); // Attempt to mine a new block

        if (newBlock != null) {
            System.out.println("New block mined with hash: " + BlockChain.bytesToHex(newBlock.getStoredHash()));
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
            System.out.println("New block mined with hash: " + BlockChain.bytesToHex(newBlock2.getStoredHash()));
        } else {
            System.out.println("Mining failed.");
        }


        System.out.println("________________________BLOCKCHAIN_______________");
        System.out.println(BlockChain.getInstance());




    }

    public static void miningTest2 () throws Exception{
        UserManager users = UserManager.getInstance();
        MemPool memPool = MemPool.getInstance();
        Miner miner = new Miner();

        // Generate multiple users
        for (int i = 0; i < 5; i++) {
            users.addUser(new User("User" + i));
        }

        // Create and add multiple transactions to the mempool
        for (int i = 0; i < 12; i++) {
            User sender = users.getUserByName("User" + (i % 5)); // Cycle through users
            User receiver = users.getUserByName("User" + ((i + 1) % 5)); // Next user as receiver
            Transaction transaction = sender.make_transaction(i + 1, receiver.address);
            memPool.addTransaction(transaction);
        }

        // Print initial mempool
        System.out.println("Initial MemPool: " + memPool + "\n\n");

        // Mine three times
        for (int i = 0; i < 3; i++) {
            Block newBlock = miner.mine(); // Attempt to mine a new block

            if (newBlock != null) {
                System.out.println("New block mined with hash: " + BlockChain.bytesToHex(newBlock.getStoredHash()));
            } else {
                System.out.println("Mining failed.");
            }

            // Print the mempool after each mining attempt
            System.out.println("MemPool after mining attempt " + (i + 1) + ": " + memPool + "\n\n");

        }

        System.out.println("Mining finished");
        System.out.println("______________BLOCKCHAIN STATUS_______________");
        System.out.println(BlockChain.getInstance());
    }

}