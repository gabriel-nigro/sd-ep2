
// Lib para leitura de input do usuário
import java.util.Scanner;
// Libs para socket
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        while (true) {
            // Pega IP e Porta
            inputInfos = entrada.nextLine();
            if (infoValida(inputInfos))
                break;
        }
        return inputInfos;
    }

    public static void enviaMensagem(Socket socket, Mensagem mensagem) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    // Cria a cadeia de saída (escrita) de informações do socket
                    OutputStream os = socket.getOutputStream();
                    DataOutputStream writer = new DataOutputStream(os);

                    // declaração e preenchimento do buffer de envio
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(mensagem);
                    final byte[] sendMessage = baos.toByteArray();

                    // Envia mensagem
                    writer.write(sendMessage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
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
        return null;
    }

    public static void replicaPut(String[] servidor, Mensagem mensagem) {
        try {
            // Encaminha para primeiro servidor
            String ip1 = getIp(servidor[0]);
            int porta1 = getPorta(servidor[0]);
            // Abre um Socket para conexão com o ServerSocket
            Socket socket1 = new Socket(ip1, porta1);
            // Envia para o primeiro vizinho
            mensagem.addReplicationCount();
            enviaMensagem(socket1, mensagem);

            // Obtém ip e porta
            String ip2 = getIp(servidor[1]);
            int porta2 = getPorta(servidor[1]);
            // Abre um Socket para conexão com o ServerSocket
            Socket socket2 = new Socket(ip2, porta2);
            // Envia para o primeiro vizinho
            mensagem.addReplicationCount();
            enviaMensagem(socket2, mensagem);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void trataRequisicao(Socket socket, String servidor, String[] vizinhos, String lider,
            HashMap<String, ValorHash> tabelaHash) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    // Recebe mensagem
                    Mensagem mensagemRecebida = recebeMensagem(socket);
                    String propriedade = mensagemRecebida.getPropriedade();
                    String valor = mensagemRecebida.getValor();
                    Date timestamp = mensagemRecebida.getTimestamp();

                    // EXCLUIR
                    System.out.println("\nRecebeu mensagem");

                    // Funcionalidade 5.c)
                    if (mensagemRecebida.isPut()) {
                        // Mensagem PUT
                        // EXCLUIR
                        System.out.println("\nMensagem de PUT");
                        if (servidor.equals(lider)) {
                            // EXCLUIR
                            System.out.println("\nÉ líder");

                            // Caso não haja a propriedade, a mesma será adicionada.
                            // Caso esteja presente, será atualizada.
                            tabelaHash.put(propriedade, new ValorHash(valor, timestamp));

                            mensagemRecebida.setIsPut(false);
                            mensagemRecebida.setIsReplication(true);

                            // Replica o PUT para os outros servidores

                            // EXCLUIR
                            System.out.println("\nEnviou o replication PUT para todos os servidores");

                            replicaPut(vizinhos, mensagemRecebida);
                        } else {
                            // Encaminha para o lider
                            // enviaMensagem(lider, mensagemRecebida);
                        }
                    } else if (mensagemRecebida.isGet()) {
                        // Mensagem GET

                    } else if (mensagemRecebida.isReplication()) {
                        // EXCLUIR
                        System.out.println("\nRecebeu REPLICATION");
                        // Replicação da informação
                        tabelaHash.put(propriedade, new ValorHash(valor, timestamp));

                        // Servidor 1
                        // Obtém ip e porta
                        String ipLider = getIp(lider);
                        int portaLider = getPorta(lider);
                        // Abre um Socket para conexão com o ServerSocket
                        Socket socketLider = new Socket(ipLider, portaLider);

                        // Seta REPLICATION_OK
                        mensagemRecebida.setIsReplication(false);
                        mensagemRecebida.setIsReplicationOk(true);

                        // Devolve com REPLICATION_OK para líder
                        enviaMensagem(socketLider, mensagemRecebida);
                        // EXCLUIR
                        System.out.println("\nEnviou REPLICATION_OK");
                    } else if (mensagemRecebida.isReplicationOk() && mensagemRecebida.getReplicationCount() == 2) {
                        // EXCLUIR
                        System.out.println("\nAmbos servidores responderam com REPLICATION_OK");
                        // Se ambos servidores realizaram o put e retornaram o "REPLICATION_OK"
                        mensagemRecebida.setResponse("PUT_OK");
                        // Envia PUT_OK para o solicitante
                        enviaMensagem(socket, mensagemRecebida);
                        // EXCLUIR
                        System.out.println("\nEncaminhou mensagem de volta ao cliente");
                    }

                } catch (Exception e) {
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
        HashMap<String, ValorHash> tabelaHash = new HashMap<String, ValorHash>();

        while (true) {
            Socket socket = serverSocket.accept();
            trataRequisicao(socket, serverInfos, vizinhos, lider, tabelaHash);
        }

    }
}
