import java.io.*;
import java.net.*;

public class ServerRunner {
    private static final int PORT = 1234;

    public static void startServer() {
        new Thread(() -> {
            try {
                System.out.println("Server started on port " + PORT);

                final SubstitutionCipher cipher = new SubstitutionCipher();
                String keyString = cipher.getKeyString();

                try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                    
                    // Accept first client (the host)
                    Socket client1 = serverSocket.accept(); 
                    System.out.println("Client 1 (Host) connected.");
                    PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
                    BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
                    out1.println(keyString);
                    out1.println("Connected to chatroom. Waiting for another user...");

                    // Accept second client
                    Socket client2 = serverSocket.accept();
                    System.out.println("Client 2 connected.");
                    PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
                    out2.println(keyString);
                    out2.println("Connected to chatroom.");

                    out1.println(" Chat started! Type 'exit' to leave.");
                    out2.println(" Chat started! Type 'exit' to leave.");

                    // Handle messages
                    new Thread(() -> handleClient("Host", in1, out2, cipher)).start();
                    new Thread(() -> handleClient("Guest", in2, out1, cipher)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void startAndWaitForClients(Runnable onFirstClient, Runnable onBothClients) {
        try {
            System.out.println("Server started. Waiting for two clients...");

            final SubstitutionCipher cipher = new SubstitutionCipher();
            String keyString = cipher.getKeyString();

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                
                Socket client1 = serverSocket.accept(); 
                System.out.println("Client 1 connected.");
                if (onFirstClient != null) {
                    onFirstClient.run();
                }
                
                PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
                BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
                out1.println(keyString);
                out1.println("Connected to chatroom. Waiting for another user...");

                Socket client2 = serverSocket.accept();
                System.out.println("Client 2 connected.");
                if (onBothClients != null) {
                    onBothClients.run();
                }
                
                PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
                BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
                out2.println(keyString);
                out2.println("Connected to chatroom.");

                out1.println(" Chat started! Type 'exit' to leave.");
                out2.println(" Chat started! Type 'exit' to leave.");

                new Thread(() -> handleClient("Client 1", in1, out2, cipher)).start();
                new Thread(() -> handleClient("Client 2", in2, out1, cipher)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(String clientName, BufferedReader in, PrintWriter outOther, SubstitutionCipher cipher) {
        String encryptedMsg;
        try {
            while ((encryptedMsg = in.readLine()) != null) {
                System.out.println("Encrypted from " + clientName + ": " + encryptedMsg);
                String decrypted = cipher.decrypt(encryptedMsg);
                System.out.println("Decrypted from " + clientName + ": " + decrypted);
                if (decrypted.equalsIgnoreCase("exit")) {
                    outOther.println("❌ " + clientName + " has left the chat.");
                    break;
                }
                outOther.println(clientName + ": " + decrypted);
            }
        } catch (IOException e) {
            System.out.println("Connection lost with " + clientName);
        }
    }
}