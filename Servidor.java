// Lib para leitura de input do usuário
import java.util.Scanner;
// Libs para socket
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
// Lib para tabela de Hash
import java.util.HashMap;
// Lib para validar input do usuário
import java.util.regex.Pattern;
// Lib para exceptions
import java.io.IOException;
// Lib para utilização de Data
import java.util.Date;

public class Servidor {

    // Classe para utilizar dois valores no HashMap
    public class valorHash {
        private String valor;
        private Date timestamp;
        
        public valorHash(String valor, Date timestamp) {
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
    
    private static Scanner entrada;

    /*
     * O método é responsável por obter o IP a partir de uma string no formato
     * IPV4:PORTA
     * 
     * @param peerInfos Informações de IP:PORTA de um peer.
     * 
     * @return String O IP do peer.
     */
    static String getIp(String peerInfos) {
        String[] infosSplited = peerInfos.split(":");
        String ip = infosSplited[0];
        return ip;
    }

    /*
     * O método é responsável por obter a porta a partir de uma string no formato
     * IPV4:PORTA
     * 
     * @param peerInfos Informações de IP:PORTA de um peer.
     * 
     * @return int O IP do peer.
     */
    static int getPorta(String peerInfos) {
        String[] infosSplited = peerInfos.split(":");
        int porta = Integer.parseInt(infosSplited[1]);
        return porta;
    }

    /*
     * O método é responsável por validar a string a partir de um regex IPV4:PORTA.
     * O mesmo é utilizado para validar o input do usuário.
     * 
     * @param peerInfos Informações de IP:PORTA de um peer.
     * 
     * @return boolean O valor booleano para validar as informações.
     */
    static boolean infoValida(String peerInfos) {
        Pattern pattern = Pattern
                .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5]):[0-9]{1,5}$");
        boolean isValid = pattern.matcher(peerInfos).matches();
        if (!isValid)
            System.out.println("Informações inválidas. A formatação deve ser IP:PORTA");
        return isValid;
    }

    /*
     * 
     */
    static String getInputInfos() {
        String inputInfos = null;
        while(true) {
            // Pega IP e Porta
            inputInfos = entrada.nextLine();
            if (infoValida(inputInfos)) break;
        }
        return inputInfos;
    }

    public static Mensagem recebeMensagem(Socket socket) {
        try {
            // Transforma o pacote em uma instância da classe Mensagem.
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            Mensagem mensagem = (Mensagem) is.readObject();
            return mensagem;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void trataRequisicao(Socket socket, String servidor, String[] vizinhos, String lider) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    // Recebe mensagem
                    Mensagem mensagemRecebida = recebeMensagem(socket);

                    if(mensagemRecebida.isPut()) {
                        // Mensagem PUT
                        if (servidor.equals(lider)) {
                            // Replica o PUT para os outros
                        } else {
                            // Envia para o lider
                        }
                    } else if (mensagemRecebida.isGet()) {
                        // Mensagem GET
                        
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public static void main(String[] args) throws Exception {
        // Variável que guarda o input do usuário
        entrada = new Scanner(System.in);

        // Recebe informações do servidor via input do usuário
        System.out.println("\nInforme o IP:PORTA do servidor.");
        String serverInfos = getInputInfos(); 

        // Array para armazenamento dos vizinhos
        String[] vizinhos;
        vizinhos = new String[2];

        // Servidor vizinho[0]
        System.out.println("\nInforme o IP:PORTA do primeiro vizinho.");
        vizinhos[0] = getInputInfos();

        // Servidor vizinho[1]
        System.out.println("\nInforme o IP:PORTA do segundo vizinho.");
        vizinhos[1] = getInputInfos(); 

        // Servidor vizinho[1]
        System.out.println("\nInforme o IP:PORTA do líder.");
        String lider = getInputInfos(); 

        // Inicializa servidor
        ServerSocket serverSocket = new ServerSocket(getPorta(serverInfos));

        // Inicializa a tabela hash
        HashMap<String, valorHash> tabelaHash = new HashMap<String, valorHash>();
        
        while(true) {
            Socket node = serverSocket.accept();
        }
        


    }
}
