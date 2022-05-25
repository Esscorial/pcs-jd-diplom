import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try (
                Socket socket = new Socket("localhost", 8989);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("Введите слово, которое вы хотите найти: ");
            String word = scanner.nextLine();
            out.println(word);
            System.out.print(print(in.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String print(String json) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonParser p = new JsonParser();
        JsonElement e = p.parse(json);
        return gson.toJson(e);
    }
}