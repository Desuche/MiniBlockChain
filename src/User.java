import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.*;
public class User {
    public String name; // Name of the user
    private final PrivateKey privateKey; // Private key for signing transactions
    public final PublicKey publicKey; // Public key for verifying transactions
    public final byte[] address; // Unique address derived from the public key
    private double wallet=100000; // Initial wallet balance for mining

    // Constructor to initialize the user with a name and generate keys
    public User(String name) throws NoSuchAlgorithmException {
        this.name=name;
        //generate public key and private key for the user
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        // Generate address by hashing the public key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        address = digest.digest(publicKey.getEncoded());

    }

    // Getter for wallet balance
    public double getWallet() {
        return wallet;
    }

    // Setter for wallet balance
    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    // Method to create a transaction
    public transaction make_transaction(double data, byte[] target_address) throws Exception {
        if(data<wallet){ // Check if sufficient funds are available
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            setWallet(wallet-data); // Deduct the amount from the wallet

            //Convert double to byte array
            byte[] dataBytes = ByteBuffer.allocate(8).putDouble(data).array();

            // Hash the transaction data
            byte[] dataHash = digest.digest(dataBytes);
            // Generate a digital signature for the transaction
            byte[] signature = this.digital_signature_generation(new String(dataHash));
            // Create and return a new transaction object
            transaction transaction=new transaction(this.address, target_address, data,signature);
            return transaction;
        }
        else{
            throw new Exception("Insufficient funds: The amount exceeds the available wallet balance.");
        }


    }

    // Method to generate a digital signature using the private key
    public byte[] digital_signature_generation(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        //using private key to encrpt the message
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return cipherText;
    }



}
