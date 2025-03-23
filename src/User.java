
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
public class User {
    public String name;
    private final PrivateKey privateKey;
    public final PublicKey publicKey;
    public final byte[] address;
    private int money=0;

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
    public byte[] encrypt(String plainText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return cipherText;
    }



}
