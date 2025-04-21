import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;

public class User {
    public String name; // Name of the user
    private final PrivateKey privateKey; // Private key for signing transactions
    public final PublicKey publicKey; // Public key for verifying transactions
    public final byte[] address; // Unique address derived from the public key
    private double wallet=100000; // Initial wallet balance

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

    // Method to create a Transaction
    public Transaction make_transaction(double data, byte[] target_address) throws Exception {
        if(data<wallet){ // Check if sufficient funds are available
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            setWallet(wallet-data); // Deduct the amount from the wallet

            //Convert double to byte array
            byte[] dataBytes = ByteBuffer.allocate(8).putDouble(data).array();
            // Hash the Transaction data
            byte[] dataHash = digest.digest(dataBytes);
            // Generate a digital signature for the Transaction
            byte[] signature = this.digital_signature_generation(dataHash);
            // Create and return a new Transaction object
            return new Transaction(this.address, target_address, data,signature);
        }
        else{
            throw new Exception("Insufficient funds: The amount exceeds the available wallet balance.");
        }


    }

    // Method to generate a digital signature using the private key
    public byte[] digital_signature_generation(byte[] plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        //using private key to encrpt the message
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(plainText);
        return cipherText;
    }

    @Override
    public String toString() {
        return String.format("User{name='%s', address='%s', publicKey='%s'}",
                name,
                bytesToHex(address),
                bytesToHex(publicKey.getEncoded())
        );
    }

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
