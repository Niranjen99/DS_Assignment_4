public class TestDistributedSystem {

    public static void main(String[] args) {
        // Test parameters
        String serverName = "localhost";
        int portJohn = 6666;  // Port for John's Aggregation Server
        int portNur = 6667;   // Port for Nur's Aggregation Server

        // Sample data to upload
        String jsonData = "{sample json data}";

        // Testing John's Implementation
        System.out.println("Testing John's Implementation...");
        testUploadData(serverName, portJohn, jsonData);
        testGetData(serverName, portJohn, "testStationid");

        // Testing Nur's Implementation
        System.out.println("Testing Nur's Implementation...");
        testUploadData(serverName, portNur, jsonData);
        testGetData(serverName, portNur, "testStationid");
    }

    private static void testUploadData(String serverName, int port, String jsonData) {
        try (Socket socket = new Socket(serverName, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Prepare PUT request
            String putRequest = String.format("PUT /weather.json HTTP/1.1\r\nContent-Length: %d\r\nLamport-Clock: %d\r\n\r\n%s",
                    jsonData.length(), 1, jsonData);

            // Send request
            out.writeUTF(putRequest);
            out.flush();

            // Read response
            String response = in.readUTF();
            System.out.println("Response from server: " + response);

        } catch (IOException e) {
            System.err.println("Error during upload: " + e.getMessage());
        }
    }

    private static void testGetData(String serverName, int port, String stationId) {
        try (Socket socket = new Socket(serverName, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Prepare GET request
            String getRequest = String.format("GET /weather.json HTTP/1.1\r\nLamport-Clock: %d\r\n", 1);

            // Send request
            out.writeUTF(getRequest);
            out.flush();

            // Read response
            String response = in.readUTF();
            System.out.println("Response from server: " + response);

        } catch (IOException e) {
            System.err.println("Error during retrieval: " + e.getMessage());
        }
    }
}

