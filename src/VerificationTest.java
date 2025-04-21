import java.time.Instant;

public class VerificationTest {

    public static void main(String[] args) throws Exception {
        testSampleBlock();

    }

    public static void testSampleTransaction() throws Exception{
        UserManager users = UserManager.getInstance();
        User alex = new User("Alex");
        User sam = new User("Sam");
        users.addUser(alex);
        users.addUser(sam);

        Transaction tx1 = alex.make_transaction(34560, sam.address);


        // Test transaction validity
        System.out.println("Test transaction validity");
        System.out.println("Before alteration: " + tx1);
        System.out.println("Valid = " + Verifier.verifySingleTransaction(tx1));

        // Alter transaction details
        tx1.data  = 2000;

        System.out.println("After alteration: ");
        System.out.println("Valid = " + Verifier.verifySingleTransaction(tx1));
    }

    public static void testSampleBlock() throws Exception{
        UserManager users = UserManager.getInstance();
        MemPool memPool = MemPool.getInstance();
        Miner miner = new Miner();

        User alex = new User("Alex");
        User sam = new User("Sam");
        users.addUser(alex);
        users.addUser(sam);

        // Test 1: Change block header

        Transaction tx1 = alex.make_transaction(34560, sam.address);
        memPool.addTransaction(tx1);

        Block testBlock1 = miner.mine();
        if (testBlock1 == null){
            System.out.println("Mining failed");
            return;
        }

        System.out.println("Test 1: Change block header");
        System.out.println("Before block header is altered");
        System.out.println(testBlock1);
        System.out.println("valid = " + Verifier.verifyBlockHeader(testBlock1, BlockChain.getInstance()));

        //Tamper with block header
        System.out.println("Changing block header");
        testBlock1.timestamp = Instant.now().toString() + "~changed~";

        System.out.println("Verifying Block header");
        System.out.println("valid = " + Verifier.verifyBlockHeader(testBlock1, BlockChain.getInstance()));


        // Test 2: add a new block transaction

        BlockChain.createNewBlockChain(); // Initialise a new, clean blockchain

        Transaction tx2 = sam.make_transaction(34560, alex.address);
        memPool.addTransaction(tx2);

        Block testBlock2 = miner.mine();
        if (testBlock2 == null){
            System.out.println("Mining failed");
            return;
        }

        System.out.println("\n\nTest 2:  add a new block transaction");
        System.out.println("Before extra transaction is added");
        System.out.println(testBlock2);
        System.out.println("valid = " + Verifier.verifyBlockTransactions(testBlock2));

        //Add a new transaction in the block
        System.out.println("Adding extra transaction");
        (testBlock2.transactions).add(alex.make_transaction(200, sam.address));

        System.out.println("Verifying Block Transactions");
        System.out.println("valid = " + Verifier.verifyBlockTransactions(testBlock2));



        // Test 3: change existing block transaction

        BlockChain.createNewBlockChain(); // Initialise a new, clean blockchain

        Transaction tx3 = sam.make_transaction(34560, alex.address);
        memPool.addTransaction(tx2);

        Block testBlock3 = miner.mine();
        if (testBlock3 == null){
            System.out.println("Mining failed");
            return;
        }

        System.out.println("\n\nTest 3: change existing block transaction");
        System.out.println("Before block transactions is altered");
        System.out.println(testBlock3);
        System.out.println("valid = " + Verifier.verifyBlockTransactions(testBlock3));

        //change an existing transaction amount in the block
        System.out.println("Changing existing transaction");
        (testBlock3.transactions).get(0).data = 50000;

        System.out.println("After block transactions is altered");
        System.out.println(testBlock3);
        System.out.println("valid = " + Verifier.verifyBlockTransactions(testBlock3));

    }
}
