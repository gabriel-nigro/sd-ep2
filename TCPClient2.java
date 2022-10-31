import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient2 {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("127.0.0.1", 9000);

            OutputStream os = s.getOutputStream();
            DataOutputStream writer = new DataOutputStream(os);

            InputStreamReader is = new InputStreamReader(s.getInputStream());
            BufferedReader reader = new BufferedReader(is);

            //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            //String texto = inFromUser.readLine();
            
            //writer.writeBytes(texto + "\n");

            // Cria mensagem
            Mensagem mensagem = new Mensagem("abobora", "abobora", false, false);

            // declaração e preenchimento do buffer de envio
            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(mensagem);
            final byte[] sendMessage = baos.toByteArray();

            writer.write(sendMessage);

            String response = reader.readLine();
            System.out.println("DoServidor: " + response);

            s.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
