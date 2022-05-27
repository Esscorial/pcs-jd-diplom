import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    final static int PORT = 8989;

    public static void main(String[] args) throws Exception {

        System.out.println("Запуск сервера " + PORT + "...");
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        System.out.println(engine.search("бизнес"));

        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Запуск сервера " + PORT + "...");
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.printf("New connection accepted. Port: %d%n", clientSocket.getPort());

                    String inpData = in.readLine();

                    GsonBuilder builder = new GsonBuilder();
                    Gson gs = builder.create();

                    out.println(gs.toJson(engine.search(inpData)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server closed!");
        }
    }
}


