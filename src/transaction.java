import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class transaction {
    public byte[] transactionID; // Unique ID of the transaction
    public double data; // Amount being transferred
    public byte[] signature; // Digital signature of the transaction
    public final byte[] sender_address; // Address of the sender
    public final byte[] receiver_address; // Address of the receiver

    // Constructor to initialize a transaction
    public transaction(byte[] sender_address, byte[] receiver_address, double data, byte[] signature) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        this.sender_address = sender_address;
        this.receiver_address = receiver_address;
        this.signature = signature;
        this.data = data;

        // Convert the transaction data to a byte array
        byte[] dataBytes = ByteBuffer.allocate(8).putDouble(data).array();

        // Calculate the total length of the transaction header
        int header_length = dataBytes.length + signature.length + this.sender_address.length + this.receiver_address.length;

        // Combine all transaction components into a single byte array
        byte[] headerContent = ByteBuffer.allocate(header_length).put(dataBytes).put(signature).put(this.sender_address).put(this.receiver_address).array();

        // Generate the transaction ID by hashing the header content
        this.transactionID = digest.digest(headerContent);
    }

    // Getter for the transaction data
    public double getData() {
        return data;
    }
}
