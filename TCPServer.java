import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);

        while (true) {
            try {
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