import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
// Lib para escolher randômicamente
import java.util.Random;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);

        while (true) {
            try {
                // Variável para escolher o servidor randomicamente
                Date tempo = new Date();
                System.out.println("Tempo: " + tempo);

                System.out.println("Esperando Conexão");
                Socket no = serverSocket.accept();
                System.out.println("Conn aceita");

                ThreadAtendimento thread = new ThreadAtendimento(no);
                thread.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}