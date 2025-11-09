import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;

public class LibraryServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/addBook", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }

                String[] params = data.toString().split("&");
                String bookId = params[0].split("=")[1];
                String title = params[1].split("=")[1];
                String author = params[2].split("=")[1];
                String publisher = params[3].split("=")[1];
                String quantity = params[4].split("=")[1];

                try {
                    Connection con = DBConnection.getConnection();
                    String sql = "INSERT INTO books (book_id, title, author, publisher, quantity) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(bookId));
                    ps.setString(2, title);
                    ps.setString(3, author);
                    ps.setString(4, publisher);
                    ps.setInt(5, Integer.parseInt(quantity));
                    ps.executeUpdate();

                    String response = "{\"status\":\"success\"}";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    String response = "{\"status\":\"error\"}";
                    exchange.sendResponseHeaders(500, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("✅ Server running on http://localhost:8081");
    }
}
