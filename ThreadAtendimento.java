import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.CharBuffer;

public class ThreadAtendimento extends Thread {
    private Socket no;

    public ThreadAtendimento(Socket node) {
        no = node;
    }
    
    public void run() {
        try {
            // Transforma o pacote em uma instância da classe Mensagem.
            ObjectInputStream is = new ObjectInputStream(no.getInputStream());
            Mensagem msg = (Mensagem) is.readObject();
            System.out.println(msg.getNomeArquivo());

            // Transforma o pacote em uma instância da classe Mensagem.
            //ByteArrayInputStream in = new ByteArrayInputStream(IOUtils);
            //ObjectInputStream is2 = new ObjectInputStream(in);
            //Mensagem msg = (Mensagem) is2.readObject();
            
            
            OutputStream os = no.getOutputStream();
            DataOutputStream writer = new DataOutputStream(os);
            String texto = msg.getNomeArquivo();

            writer.writeBytes(texto.toUpperCase() + "\n");
        } catch (Exception e) {

        }
    }
}
