
// Lib para utilização de Data
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class Teste {
    private static Scanner entrada;

    // Classe para utilizar dois valores no HashMap
    static public class ValorHash {
        private String valor;
        private Date timestamp;

        public ValorHash(String valor, Date timestamp) {
            this.valor = valor;
            this.timestamp = timestamp;
        }

        public String getValor() {
            return this.valor;
        }

        public Date getTimestamp() {
            return this.timestamp;
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {

        // Inicializa a tabela hash
        HashMap<String, ValorHash> tabelaHash = new HashMap<String, ValorHash>();
        String propriedade = "legume";
        String valor = "abobora";
        Date timestamp = new Date();
        ValorHash valorHash = new ValorHash(valor, timestamp);
        tabelaHash.put(propriedade, valorHash);

        tabelaHash.put(propriedade, new ValorHash("teste", timestamp));

        //System.out.println(tabelaHash.get(propriedade).getValor());

        // Inicializa a tabela hash
        HashMap<UUID, String> clientes = new HashMap<UUID, String>();
        UUID uuid = UUID.randomUUID();
        clientes.put(uuid, "nada");
        System.out.println(clientes.get(uuid));

       /*Socket socket = new Socket("127.0.0.1", 10097);
        System.out.println("Inet Address: " + socket.getInetAddress());
        System.out.println("Port: " + socket.getPort());
        System.out.println("Local socket address: " + socket.getLocalSocketAddress());

        // Leitura
        InputStreamReader is = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(is);
        String response = reader.readLine();
        System.out.println("DoServidor: " + response);

        /*Ação escolhida pelo usuário
        int acao;

        while (true) {
            System.out.println("\nMenu de acoes.");
            System.out.println("Digite uma opção:");

            // Se o peer já foi inicializado, não mostra a opção de inicialização
            System.out.println("1 - INIT");
            System.out.println("2 - PUT");
            System.out.println("3 - GET");
            acao = Integer.parseInt(entrada.nextLine());

            switch (acao) {
                case 1: {
                    // Funcionalidade 4.a)
                    System.out.println("Ação escolhida");
                    break;
                }
            }
        }
        */ 

    }
}
