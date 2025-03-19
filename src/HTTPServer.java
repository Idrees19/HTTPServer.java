import java.io.*;
import java.net.*;

public class HTTPServer {
    private static final int PORT = 18080;
    private static final String ROOT_DIRECTORY = "./www"; // Change this to your HTML files directory

    public static void main(String[] args) throws IOException {
        System.out.println("Opening the server socket on port " + PORT);
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            System.out.println("Server waiting for client...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            handleRequest(clientSocket);
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {

            String requestLine = reader.readLine();
            if (requestLine == null || !requestLine.startsWith("GET")) {
                return;
            }

            System.out.println("Request: " + requestLine);
            String[] tokens = requestLine.split(" ");
            String requestedFile = tokens[1];

            if (requestedFile.equals("/")) {
                requestedFile = "/index.html"; // Default to index.html
            }

            File file = new File(ROOT_DIRECTORY + requestedFile);
            if (file.exists() && !file.isDirectory()) {
                sendResponse(outputStream, 200, "OK", file);
            } else {
                sendNotFound(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendResponse(OutputStream outputStream, int statusCode, String statusMessage, File file) throws IOException {
        String responseHeader = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                "Content-Type: text/html\r\n\r\n";
        outputStream.write(responseHeader.getBytes());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void sendNotFound(OutputStream outputStream) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<html><body><h1>404 Not Found</h1></body></html>";
        outputStream.write(response.getBytes());
    }
}