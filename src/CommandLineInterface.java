import java.util.Scanner;

public class CommandLineInterface {
    /*
    MiniBlockChain System

    1 - view blockchain
    2 - create user (enter new user name)
    3 - list all users
    4 - create transaction (enter sender user name, enter recipient user name, enter amount)
    5 - view mempool
    6 - Mine new block (optionally choose max transaction count (default 5))
    7 - view block details [including txn IDs in the block] (enter block ID)
    8 - view transaction details (enter block ID and transaction ID)
    9 - Peform malicious actions
        9.1 - Change block header (enter block ID)
            - Select value to change (1 previousBlockHash; 2 timestamp; 3 nonce; 4 difficulty; 5 merkleRoot; 6 transactions; )
        9.2 - Add new transaction to block (enter block ID, enter sender user name, enter reciepient user name, enter amount)
        9.3 - Change existing transaction amount (enter block ID, enter transaction ID, enter new amount)
   10 - Verify Blockchain
        10.1 - Verify entire blockchain
        10.2 - Verify block (enter block ID)
        10.3 - Verify transaction (enter block ID, enter transaction ID)
   11 - exit


     */

    public static void main(String[] args) {
        while (true)
            try {
                new CommandLineInterface().start();
            } catch (Exception e){
                System.err.println("An Error Occurred: " + e);
            }
    }
    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println();
            System.out.println("MiniBlockChain System");
            System.out.println("1 - View blockchain");
            System.out.println("2 - Create user");
            System.out.println("3 - List all users");
            System.out.println("4 - Create transaction");
            System.out.println("5 - View mempool");
            System.out.println("6 - Mine new block");
            System.out.println("7 - View block details");
            System.out.println("8 - View transaction details");
            System.out.println("9 - Perform malicious actions");
            System.out.println("10 - Verify Blockchain");
            System.out.println("11 - Exit");
            System.out.print("Enter your choice: \n");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // View blockchain

                    System.out.println(BlockChain.getInstance());
                    break;
                case 2:
                    // Create user

                    System.out.print("Enter new user name: ");
                    String newUserName = scanner.nextLine();


                    User newUser = new User(newUserName);
                    if (UserManager.getInstance().addUser(newUser)){
                        System.out.println("User added successfully: " + newUser);
                    } else {
                        System.out.println("Duplicate username or wallet address detected!");
                    }

                    break;
                case 3:
                    // List all users

                    System.out.println(UserManager.getInstance());
                    break;
                case 4:
                    // Create new transaction

                    System.out.print("Enter sender user name: ");
                    String senderName = scanner.nextLine();
                    System.out.print("Enter recipient user name: ");
                    String recipientName = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();  // Consume newline

                    // Find users
                    User sender = UserManager.getInstance().getUserByName(senderName);
                    User recipient = UserManager.getInstance().getUserByName(recipientName);

                    if (sender == null){
                        System.out.println("Sender User not found in system!");
                        break;
                    }
                    if (recipient == null){
                        System.out.println("Recipient User not found in system!");
                        break;
                    }


                    // Send transaction to mempool
                    Transaction transaction = sender.make_transaction(amount, recipient.address);
                    MemPool.getInstance().addTransaction(transaction);

                    System.out.println("New transaction created: " + transaction);
                    System.out.println("Your transaction has been published to the MemPool. Pending for miners to pick it up.");


                    break;
                case 5:
                    // View mempool

                    System.out.println(MemPool.getInstance());
                    break;
                case 6:
                    // Mine new block

                    System.out.print("Enter max transaction count (default 5): ");
                    String maxTxnCountInput = scanner.nextLine();
                    int maxTxnCount = maxTxnCountInput.isEmpty() ? 5 : Integer.parseInt(maxTxnCountInput);

                    Miner miner = new Miner(maxTxnCount);
                    Block minedBlock = miner.mine();
                    if (minedBlock != null){
                        System.out.println("New block mined: " + minedBlock);
                    } else {
                        System.out.println("Mining failed: MemPool is empty OR Mining timeout limit is exceeded");
                    }
                    break;
                case 7:
                    // View block

                    System.out.print("Enter block Number: ");
                    int blockNumber = scanner.nextInt();
                    scanner.nextLine();

                    if (blockNumber >= BlockChain.getInstance().blocks.size()) {
                        System.out.println("Invalid block number");
                        break;
                    }

                    Block block = BlockChain.getInstance().blocks.get(blockNumber);
                    if (block != null){
                        System.out.println(block);
                    } else {
                        System.out.println("Block not found!");
                    }

                    break;
                case 8:
                    // View transaction

                    System.out.print("Enter block ID: ");
                    int blockIdForTxn = scanner.nextInt();
                    scanner.nextLine(); // consume next line

                    if (blockIdForTxn >= BlockChain.getInstance().blocks.size()) {
                        System.out.println("Invalid block number");
                        break;
                    }

                    Block txnBlock = BlockChain.getInstance().blocks.get(blockIdForTxn);
                    if (txnBlock == null){
                        System.out.println("Block not found!");
                        break;
                    } else {
                        System.out.println("Transactions in selected block: " + txnBlock.getTransactionIDs());
                    }


                    System.out.print("Enter transaction ID: ");
                    String txnId = scanner.nextLine();

                    Transaction txn = txnBlock.findTransactionByID(txnId);
                    if (txn == null){
                        System.out.println("Transaction not found in specified block!");
                        break;
                    }

                    System.out.println(txn);

                    break;
                case 9:
                    handleMaliciousActions(scanner);
                    break;
                case 10:
                    handleVerification(scanner);
                    break;
                case 11:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void handleMaliciousActions(Scanner scanner) {
        System.out.println("Choose malicious action to perform:");
        System.out.println("1 - Change block header");
        System.out.println("2 - Add new transaction to block");
        System.out.println("3 - Change existing transaction amount");
        System.out.print("Enter your choice: \n");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter block ID: ");
                int blockNumber = scanner.nextInt();
                scanner.nextLine(); // consume dangling next line


                if (blockNumber >= BlockChain.getInstance().blocks.size()) {
                    System.out.println("Invalid block number");
                    break;
                }

                Block block = BlockChain.getInstance().blocks.get(blockNumber);
                if (block == null){
                    System.out.println("Block not found!");
                    break;
                } else {
                    System.out.println(block);
                }

                System.out.println("\nSelect value to change:");
                System.out.println("1 - Previous Block Hash");
                System.out.println("2 - Timestamp");
                System.out.println("3 - Nonce");
                System.out.println("4 - Difficulty");
                System.out.println("5 - Merkle Root");
                System.out.print("Enter your choice: ");
                int valueChoice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                System.out.print("Enter updated value: ");
                String updatedValueStr = scanner.nextLine();

                try {

                    switch (valueChoice) {
                        case 1:
                            block.merkleRoot = updatedValueStr.getBytes();
                            break;
                        case 2:
                            block.timestamp = updatedValueStr;
                            break;
                        case 3:
                            block.nonce = Long.parseLong(updatedValueStr);
                            break;
                        case 4:
                            block.merkleRoot = updatedValueStr.getBytes();
                            break;
                        case 5:
                            block.merkleRoot = updatedValueStr.getBytes();
                            break;
                        default:
                            System.out.println("Invalid choice. Returning to main menu.");
                            return;
                    }
                } catch (Exception e) {
                    System.out.println("Data conversion error. Returning to main menu.");
                    return;
                }

                // Change block header logic
                break;
            case 2:
                // Add new transaction to a block

                System.out.print("Enter block ID: ");
                int targetBlockId = scanner.nextInt();
                scanner.nextLine();

                if (targetBlockId >= BlockChain.getInstance().blocks.size()){
                    System.out.println("Invalid block number");
                    break;
                }


                System.out.print("Enter sender user name: ");
                String senderName = scanner.nextLine();
                System.out.print("Enter recipient user name: ");
                String recipientName = scanner.nextLine();
                System.out.print("Enter amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();

                // Find users
                User sender = UserManager.getInstance().getUserByName(senderName);
                User recipient = UserManager.getInstance().getUserByName(recipientName);
                if (sender == null){
                    System.out.println("Sender User not found in system!");
                    break;
                }
                if (recipient == null){
                    System.out.println("Recipient User not found in system!");
                    break;
                }


                // put fake transaction into existing block
                Block targetBlock = BlockChain.getInstance().blocks.get(targetBlockId);
                Transaction fraudulentTransaction = sender.make_transaction(amount, recipient.address);
                targetBlock.transactions.add(fraudulentTransaction);


                break;
            case 3:
                System.out.print("Enter block ID: ");
                int blockId = scanner.nextInt();
                scanner.nextLine();

                if (blockId >= BlockChain.getInstance().blocks.size()){
                    System.out.println("Invalid block number");
                    break;
                }

                Block txnBlock = BlockChain.getInstance().blocks.get(blockId);
                if (txnBlock == null){
                    System.out.println("Block not found!");
                    break;
                } else {
                    System.out.println("Transactions in selected block: " + txnBlock.getTransactionIDs());
                }

                System.out.print("Enter transaction ID: ");
                String txnId = scanner.nextLine();

                Transaction targetTransaction = txnBlock.findTransactionByID(txnId);
                if (targetTransaction == null){
                    System.out.println("Transaction not found in specified block!");
                    break;
                }

                System.out.print("Enter new amount: ");
                double newAmount = scanner.nextDouble();
                scanner.nextLine();  // Consume newline


                // Alter target transaction
                targetTransaction.data = newAmount;

                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleVerification(Scanner scanner) throws Exception {
        System.out.println("Choose option to perform:");
        System.out.println("1 - Verify entire blockchain");
        System.out.println("2 - Verify a particular block");
        System.out.println("3 - Verify a particular transaction");
        System.out.print("Enter your choice: \n");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        boolean status;

        switch (choice) {
            case 1:
                // Verify entire blockchain
                System.out.println("Verifying current blockchain ... ");
                status =  Verifier.verifyBlockChain(BlockChain.getInstance());
                System.out.println(status ? "Valid" : "Invalid");
                break;
            case 2:
                // Verify a particular block
                System.out.print("Enter block ID: ");
                int blockNumber = scanner.nextInt();
                scanner.nextLine(); // consume dangling next line


                if (blockNumber >= BlockChain.getInstance().blocks.size()) {
                    System.out.println("Invalid block number");
                    break;
                }

                Block block = BlockChain.getInstance().blocks.get(blockNumber);
                System.out.println("Verifying selected block: \n" + block);
                status = Verifier.verifyBlockHeader(block, BlockChain.getInstance())
                        && Verifier.verifyBlockTransactions(block);
                System.out.println(status ? "Valid" : "Invalid");

                break;
            case 3:
                // Verify a particular transaction
                System.out.print("Enter block ID: ");
                String blockIdForTxn = scanner.nextLine();

                int txnBlockNumber = scanner.nextInt();
                scanner.nextLine(); // consume dangling next line


                if (txnBlockNumber >= BlockChain.getInstance().blocks.size()) {
                    System.out.println("Invalid block number");
                    break;
                }

                Block txnBlock = BlockChain.getInstance().blocks.get(txnBlockNumber);
                if (txnBlock == null){
                    System.out.println("Block not found!");
                    break;
                } else {
                    System.out.println("Transactions in selected block: " + txnBlock.getTransactionIDs());
                }


                System.out.print("Enter transaction ID: ");
                String txnId = scanner.nextLine();

                Transaction txn = txnBlock.findTransactionByID(txnId);
                status = Verifier.verifySingleTransaction(txn);
                System.out.println(status ? "Valid" : "Invalid");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}
