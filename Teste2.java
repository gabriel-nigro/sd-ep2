import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Teste2 {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("127.0.0.1", 60730);

            OutputStream os = s.getOutputStream();
            DataOutputStream writer = new DataOutputStream(os);

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            String texto = inFromUser.readLine();
            
            writer.writeBytes(texto + "\n");

            s.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
