import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class transaction {
    byte[] transactionID;
    String data;
    byte[] signature;
    public final byte[] sender_address;
    public final byte[] receiver_address;
    public transaction( byte[]sender_address,byte[]receiver_address,String data,byte []signature) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        this.sender_address=sender_address;
        this.receiver_address=receiver_address;
        this.signature=signature;
        this.data=data;
        byte[] dataBytes = data.getBytes();
        int header_length=dataBytes.length + signature.length + this.sender_address.length + this.receiver_address.length;
        //put all the transaction content into the header
        byte[] headerContent = ByteBuffer.allocate(header_length).put(dataBytes).put(signature).put(this.sender_address).put(this.receiver_address).array();
        // the transaction ID is calculated by taking a hash of the transaction contents.
        this.transactionID = digest.digest(headerContent);
        //using sha256 to hash the message


    }

}
