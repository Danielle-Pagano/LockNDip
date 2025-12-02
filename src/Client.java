import java.io.*;
import java.net.*;
import javafx.application.Platform;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private SubstitutionCipher cipher;
    private ClientMessageListener messageListener;
    private boolean connected = false;

    public interface ClientMessageListener {
        void onMessageReceived(String message);
        void onConnected();
        void onDisconnected();
        void onConnectionFailed(String error);
    }

    public Client(ClientMessageListener listener) {
        this.messageListener = listener;
    }

    public void connect(String host, int port) {
        System.out.println("=== CLIENT CONNECT CALLED: " + host + ":" + port + " ===");
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("✅ Connected to server at " + host + ":" + port);

                // Receive encryption key
                String keyString = in.readLine();
                if (keyString == null) {
                    notifyConnectionFailed("Did not receive key from server");
                    return;
                }

                cipher = new SubstitutionCipher(keyString);
                System.out.println("Key received and cipher initialized.");
                connected = true;

                // Notify UI of successful connection
                if (messageListener != null) {
                    Platform.runLater(() -> messageListener.onConnected());
                }

                // Start listening for incoming messages
                startListening();

            } catch (IOException e) {
                System.err.println("❌ Unable to connect to server: " + e.getMessage());
                notifyConnectionFailed(e.getMessage());
            }
        }).start();
    }

    private void startListening() {
        new Thread(() -> {
            String incoming;
            try {
                while ((incoming = in.readLine()) != null) {
                    final String message = incoming;
                    System.out.println(message);
                    
                    // Notify UI on JavaFX thread
                    if (messageListener != null) {
                        Platform.runLater(() -> messageListener.onMessageReceived(message));
                    }
                }
            } catch (IOException e) {
                System.out.println("❌ Disconnected from server.");
            } finally {
                connected = false;
                if (messageListener != null) {
                    Platform.runLater(() -> messageListener.onDisconnected());
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (!connected || out == null) {
            System.err.println("❌ Not connected to server");
            return;
        }

        new Thread(() -> {
            try {
                String encrypted = cipher.encrypt(message);
                out.println(encrypted);
                
                if (message.equalsIgnoreCase("exit")) {
                    disconnect();
                }
            } catch (Exception e) {
                System.err.println("❌ Error sending message: " + e.getMessage());
            }
        }).start();
    }

    public void disconnect() {
        connected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("👋 You left the chat.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    private void notifyConnectionFailed(String error) {
        if (messageListener != null) {
            Platform.runLater(() -> messageListener.onConnectionFailed(error));
        }
    }

    public boolean isConnected() {
        return connected;
    }

    // Keep the main method for testing purposes
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Client <server_ip> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // Console-based version for testing
        try (
                Socket socket = new Socket(host, port);
                BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("✅ Connected to server at " + host + ":" + port);

            String keyString = in.readLine();
            if (keyString == null) {
                System.err.println("❌ Did not receive key from server. Disconnecting.");
                return;
            }

            SubstitutionCipher myCipher = new SubstitutionCipher(keyString);
            System.out.println("Key received and cipher initialized.");

            new Thread(() -> {
                String incoming;
                try {
                    while ((incoming = in.readLine()) != null) {
                        System.out.println(incoming);
                    }
                } catch (IOException e) {
                    System.out.println("❌ Disconnected from server.");
                }
            }).start();

            String userMsg;
            while ((userMsg = userIn.readLine()) != null) {
                String encrypted = myCipher.encrypt(userMsg);
                out.println(encrypted);
                if (userMsg.equalsIgnoreCase("exit")) break;
            }

            System.out.println("👋 You left the chat.");
        } catch (IOException e) {
            System.err.println("❌ Unable to connect to server: " + e.getMessage());
        }
    }
}