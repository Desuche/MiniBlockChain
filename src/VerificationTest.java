public class VerificationTest {

    public static void main(String[] args) throws Exception {

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

        System.out.println("After alteration: " + tx1);
        System.out.println("Valid = " + Verifier.verifySingleTransaction(tx1));
    }
}
