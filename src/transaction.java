import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Transaction {
    public byte[] transactionID; // Unique ID of the Transaction
    public double data; // Amount being transferred
    public byte[] signature; // Digital signature of the Transaction
    public final byte[] sender_address; // Address of the sender
    public final byte[] receiver_address; // Address of the receiver

    // Constructor to initialize a Transaction
    public Transaction(byte[] sender_address, byte[] receiver_address, double data, byte[] signature) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        this.sender_address = sender_address;
        this.receiver_address = receiver_address;
        this.signature = signature;
        this.data = data;

        // Convert the Transaction data to a byte array
        byte[] dataBytes = ByteBuffer.allocate(8).putDouble(data).array();

        // Calculate the total length of the Transaction header
        int header_length = dataBytes.length + signature.length + this.sender_address.length + this.receiver_address.length;

        // Combine all Transaction components into a single byte array
        byte[] headerContent = ByteBuffer.allocate(header_length).put(dataBytes).put(signature).put(this.sender_address).put(this.receiver_address).array();

        // Generate the Transaction ID by hashing the header content
        this.transactionID = digest.digest(headerContent);
    }

    // Getter for the Transaction data
    public double getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "ID=" + bytesToHex(transactionID) +
                ", sender=" + bytesToHex(sender_address) +
                ", receiver=" + bytesToHex(receiver_address) +
                ", amount=" + data +
                '}';
    }

    // Helper method to convert a byte array to a hexadecimal string
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
