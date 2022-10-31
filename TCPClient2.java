import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
// Lib para leitura de input do usuário
import java.util.Scanner;

public class TCPClient2 {
    private static Scanner entrada;

    static Boolean verificaInicializacao(Boolean isInitialized) {
        if (!isInitialized) { 
          System.out.println("\nNecessario realizar inicializacao. Para isso, digite 1 no menu de acoes.");
          return true;
        }

        return false;
    }    
  
    public static void main(String[] args) throws Exception {
        // Variável que guarda o input do usuário
        entrada = new Scanner(System.in);

        // Ação escolhida pelo usuário
        int acao;

        // Variavel de estado para inicializacao
        boolean isInitialized = false;

        // Armazenamento das informações do server
        String serverInfos = null; 

        // Array para armazenamento dos vizinhos
        String[] vizinhos;
        vizinhos = new String[2];

        while(true) {
            System.out.println("\nMenu de acoes.");
            System.out.println("Digite uma opção:");

            // Se o peer já foi inicializado, não mostra a opção de inicialização
            if (!isInitialized)
                System.out.println("1 - INIT");
            System.out.println("2 - PUT");
            System.out.println("3 - GET");
            acao = Integer.parseInt(entrada.nextLine());

            switch (acao) {
                case 1: {
                    // Funcionalidade 4.b)
                    if (isInitialized) {
                        System.out.println("\nO peer encontra-se inicializado.");
                        break;
                    }
                    // Seta estado de inicializado como "true"
                    isInitialized = true;
                    break;
                }
                case 2: {
                    // Verifica se o servidor já foi inicializado
                    if (!isInitialized) { 
                        System.out.println("\nNecessario realizar inicializacao. Para isso, digite 1 no menu de acoes.");
                        break;
                    }

                    break;
                }
                case 3: {
                    // Verifica se o servidor já foi inicializado
                  if (!isInitialized) {
                        System.out.println("\nNecessario realizar inicializacao. Para isso, digite 1 no menu de acoes.");
                        break;
                    }

                    break;
                }
                default:
                    System.out.println("Ação incorreta. Informe uma ação válida.");
                    break;
            }
        } 
      /*
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
      */
    }

}
