
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
public class User {
    public String name;
    private final PrivateKey privateKey;
    public final PublicKey publicKey;
    public final byte[] address;

    public User(String name) throws NoSuchAlgorithmException {
        this.name=name;
        //generate public key and private key for the user
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        address = digest.digest(publicKey.getEncoded());

    }



    public transaction make_transaction(String data,byte[]target_address) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] dataBytes = data.getBytes();
        byte[] dataHash = digest.digest(data.getBytes());
        //hash the message and generate the signature
        byte[] signature = this.digital_signature_generation(new String(dataHash));
        transaction transaction=new transaction(this.address,target_address,data,signature);

        return transaction;
    }
    public byte[] digital_signature_generation(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        //using private key to encrpt the message
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return cipherText;
    }



}
