import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static UserManager instance = null;

    // Two maps for efficient lookup by either name or address
    private Map<String, User> usersByName = new HashMap<>();
    private Map<String, User> usersByAddress = new HashMap<>();

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager() {} // Private constructor for singleton

    public boolean addUser(User user) {
        if (user == null || user.name == null || user.address == null) {
            return false;
        }

        // Check if user already exists
        if (usersByName.containsKey(user.name) ||
                usersByAddress.containsKey(Arrays.toString(user.address))) {
            return false;
        }

        usersByName.put(user.name, user);
        usersByAddress.put(Arrays.toString(user.address), user);
        return true;
    }

    public User getUserByName(String name) {
        return usersByName.get(name);
    }

    public User getUserByAddress(String address) {
        return usersByAddress.get(address);
    }

    public User getUserByAddress(byte[] address) {
        return getUserByAddress(Arrays.toString(address));
    }

    public boolean removeUser(String name) {
        User user = usersByName.get(name);
        if (user == null) {
            return false;
        }

        usersByName.remove(name);
        usersByAddress.remove(Arrays.toString(user.address));
        return true;
    }

    public int getUserCount() {
        return usersByName.size();
    }

    public void clear() {
        usersByName.clear();
        usersByAddress.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserManager{\n");
        sb.append("Total Users: ").append(getUserCount()).append("\n");

        for (User user : usersByName.values()) {
            sb.append("  ").append(user.toString()).append("\n");
        }
        sb.append("}");

        return sb.toString();
    }
}