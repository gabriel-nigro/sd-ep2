
// Lib para leitura de input do usuário
import java.util.Scanner;
// Libs para socket
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
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

    /*
     * A classe ValorHash foi criada com o intuito de armazenar duas propriedades em uma
     * chave do HashMap, sendo elas o "valor" e o "timestamp".
     */
    static public class ValorHash {
        private String valor;
        private long timestamp;

        /*
         * O construtor inicializa a instância recebendo as propriedades de valor e timestamp.
         * 
         * @param valor
         * @param timestamp
         */
        public ValorHash(String valor, long timestamp) {
            this.valor = valor;
            this.timestamp = timestamp;
        }

        /*
         * Obtém o valor da propriedade valor.
         * @return String valor
         */
        public String getValor() {
            return this.valor;
        }

        /*
         * Obtém o valor do timestamp.
         * @return long timestamp
         */
        public long getTimestamp() {
            return this.timestamp;
        }
    }

    /* 
     * A classe TabelaHash foi criada com o intuito de armazenar pares chave-valor.
     * Nela é instanciado um HashMap, tendo como chave uma String, e o valor uma instância
     * da classe ValorHash, o qual por sua vez possui uma String valor e um Timestamp
     * 
     * Para visualizarmos uma instância da classe, abaixo há sua representação na notação JSON:
     * {
     *   chave: ${propriedade},
     *   valorHash: {
     *      valor: ${valor},
     *      timestamp: ${ts}
     *   }
     * }
     */
    static public class TabelaHash {

        // Inicializa a tabela hash
        HashMap<String, ValorHash> tabelaHash = new HashMap<String, ValorHash>();

        // Construtor vazio
        public TabelaHash() {
        };

        /*
         * O método get() da classe realiza a chamada do método get() da instância HashMap
         * 
         * @param propriedade Chave do HashMap
         * 
         * @return ValorHash a instância do ValorHash atrelada a chave informada no parâmetro
         */
        public ValorHash get(String propriedade) {
            return this.tabelaHash.get(propriedade);
        }

        /*
         * O método put() da classe realiza a chamada do método put() da instância HashMap
         * 
         * @param propriedade
         * @param valor
         * @param timestamp
         */
        public void put(String propriedade, String valor, long timestamp) {
            this.tabelaHash.put(propriedade, new ValorHash(valor, timestamp));
        }
    }

    // Declara o Scanner para obter o input do usuário.
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
     * O método é responsável por receber o valor de entrada do usuário e validá-lo.
     * Como há um laço, a função só retornará após receber um valor válido.
     * 
     * @return String O valor do input validado.
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

    /*
     * O método é responsável por relizar o envio de mensagens via Socket.
     * 
     * Para tal, é utilizado o uso de Thread.
     * 
     * @param socket A conexão socket com o destinatário
     * @param mensagem A mensagem a ser enviada
     */
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

                    // Fecha o socket
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void enviaMensagemSincrona(Socket socket, Mensagem mensagem) {
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

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * O método é responsável por receber a mensagem enviada ao seu ServerSocket local.
     * 
     * @param socket A conexão socket com o remetente
     * @return Mensagem A mensagem enviada pelo remetente
     */
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
            mensagem.addReplicationCount();
            // EXCLUIR
            // System.out.println("Enviou mensagem pro 1");
            enviaMensagemSincrona(socket1, mensagem);

            // Obtém ip e porta
            String ip2 = getIp(servidor[1]);
            int porta2 = getPorta(servidor[1]);
            // Abre um Socket para conexão com o ServerSocket
            Socket socket2 = new Socket(ip2, porta2);
            // Envia para o primeiro vizinho
            mensagem.addReplicationCount();
            // EXCLUIR
            // System.out.println("Enviou mensagem pro 2");
            enviaMensagemSincrona(socket2, mensagem);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void trataRequisicao(Socket cliente, Mensagem mensagemRecebida, String servidor, String[] vizinhos,
            String lider, TabelaHash tabelaHash) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    // EXCLUIR
                    // System.out.println("\nRecebeu mensagem");
                    String propriedade = mensagemRecebida.getPropriedade();
                    String valor = mensagemRecebida.getValor();

                    // Funcionalidade 5.c)
                    if (mensagemRecebida.isPut()) {
                        // Mensagem PUT
                        // EXCLUIR
                        // System.out.println("\nMensagem de PUT");
                        if (servidor.equals(lider)) {
                            // EXCLUIR
                            // System.out.println("\nÉ líder");

                            // 6)
                            System.out.println("\nCliente " + mensagemRecebida.getRemetenteInfos() + " PUT key:" + mensagemRecebida.getPropriedade()
                                    + " value:" + mensagemRecebida.getValor());
                            // Associa um unix timestamp ao hash
                            long timestamp = new Date().getTime() / 1000;
                            mensagemRecebida.setTimestamp(timestamp);

                            // Caso não haja a propriedade, a mesma será adicionada.
                            // Caso esteja presente, será atualizada.
                            tabelaHash.put(propriedade, valor, timestamp);

                            // Como a mensagem será replicada, isReplication é setado para "true" e isPut
                            // para "false"
                            mensagemRecebida.setIsReplication(true);
                            mensagemRecebida.setIsPut(false);
                            // Como a mensagem será repassada aos servidores, isFromClient é setado para
                            // "false"
                            mensagemRecebida.setIsFromClient(false);

                            // Replica o PUT para os outros servidores
                            // EXCLUIR
                            // System.out.println("\nEnviou o replication PUT para todos os servidores");
                            replicaPut(vizinhos, mensagemRecebida);
                        } else {
                            // Cria Socket com o Líder
                            String ipLider = getIp(lider);
                            int portaLider = getPorta(lider);
                            Socket socketLider = new Socket(ipLider, portaLider);

                            // 6)
                            System.out.println("\nEncaminhando PUT key:" + mensagemRecebida.getPropriedade() + " value:"
                                    + mensagemRecebida.getValor());

                            // Encaminha para o lider
                            enviaMensagem(socketLider, mensagemRecebida);
                        }
                    } else if (mensagemRecebida.isGet()) {
                        // Mensagem GET

                        // Obtém as informações de IP e PORTA do remetente através da mensagem
                        String remetenteInfos = mensagemRecebida.getRemetenteInfos();
                        // Abre um Socket para conexão com o ServerSocket remoto (Remetente)
                        Socket remetente = new Socket(getIp(remetenteInfos), getPorta(remetenteInfos));

                        // Funcionalidade 5.f)
                        ValorHash chave = tabelaHash.get(mensagemRecebida.getPropriedade());
                        if (chave == null) {
                            // EXCLUIR
                            //System.out.println("Chave não existe, retorna null");
                            // Seta o response
                            mensagemRecebida.setResponse(null);
                            // Encaminha para o cliente
                            enviaMensagem(remetente, mensagemRecebida);

                        } else {
                            // Chave existe, logo verifica se o cliente possui a chave mais atualizada do
                            // que o servidor

                            long timestampHash = chave.getTimestamp();
                            long timestampCliente = mensagemRecebida.getTimestampCliente();

                            // Seta o timestamp da chave
                            mensagemRecebida.setTimestamp(timestampHash);
                            // Seta o Servidor que respondeu
                            mensagemRecebida.setServidorResposta(servidor);

                            if (timestampHash >= timestampCliente) {
                                // Seta o response
                                String valorHash = tabelaHash.get(mensagemRecebida.getPropriedade()).getValor();
                                mensagemRecebida.setResponse(valorHash);
                                // Encaminha para o cliente
                                enviaMensagem(remetente, mensagemRecebida);
                            } else {
                                // Seta o response
                                mensagemRecebida.setResponse("TRY_OTHER_SERVER_OR_LATER");
                                // Encaminha para o cliente
                                enviaMensagem(remetente, mensagemRecebida);
                            }
                            System.out.println("\nCliente " + mensagemRecebida.getRemetenteInfos() + " GET key:" + mensagemRecebida.getPropriedade()
                                    + " ts:" + mensagemRecebida.getTimestampCliente() + ". Meu ts é "
                                    + mensagemRecebida.getTimestamp() + ", portanto devolvendo "
                                    + mensagemRecebida.getResponse());
                        }
                    } else if (mensagemRecebida.isReplication()) {
                        // EXCLUIR
                        // System.out.println("\nRecebeu REPLICATION");

                        // 6)
                        System.out.println("\nREPLICATION key:" + mensagemRecebida.getPropriedade() + " value:"
                                + mensagemRecebida.getValor());
                        // Replicação da informação
                        long timestamp = mensagemRecebida.getTimestamp();
                        tabelaHash.put(propriedade, valor, timestamp);

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
                        // System.out.println("\nEnviou REPLICATION_OK");
                    } else if (mensagemRecebida.isReplicationOk() && mensagemRecebida.getReplicationCount() == 2) {
                        // EXCLUIR
                        // System.out.println("\nReplication count: " +
                        // mensagemRecebida.getReplicationCount());
                        // EXCLUIR
                        // System.out.println("\nREPLICATION_OK");
                        // EXCLUIR
                        //System.out.println("Mensagem UUID: " + mensagemRecebida.getUuid());
                        // Obtém as informações de IP e PORTA do remetente através da mensagem
                        String remetenteInfos = mensagemRecebida.getRemetenteInfos();
                        // Abre um Socket para conexão com o ServerSocket remoto (Remetente)
                        Socket remetente = new Socket(getIp(remetenteInfos), getPorta(remetenteInfos));
                        // 6)
                        ValorHash chave = tabelaHash.get(mensagemRecebida.getPropriedade());
                        long timestampHash = chave.getTimestamp();
                        System.out.println("\nEnviando PUT_OK ao Cliente " + mensagemRecebida.getRemetenteInfos()
                                + " da key:" + mensagemRecebida.getPropriedade() + " ts:" + timestampHash);
                        // Seta o response "PUT_OK" para o remetente
                        mensagemRecebida.setResponse("PUT_OK");
                        // Seta o Servidor que respondeu
                        mensagemRecebida.setServidorResposta(servidor);

                        // Envia mensagem ao remetente
                        enviaMensagem(remetente, mensagemRecebida);
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
        TabelaHash tabelaHash = new TabelaHash();

        // Tabela de hash de clientes
        //HashMap<UUID, Socket> clientes = new HashMap<UUID, Socket>();

        while (true) {
            Socket cliente = serverSocket.accept();

            // Recebe mensagem
            Mensagem mensagemRecebida = recebeMensagem(cliente);
             /*if (mensagemRecebida.isFromClient()) {
                // Excluir
                // System.out.println("\nA mensagem recebida é do cliente");
                clientes.put(mensagemRecebida.getUuid(), cliente);
            }*/

            //trataRequisicao(cliente, mensagemRecebida, serverInfos, vizinhos, lider, tabelaHash, clientes);
            trataRequisicao(cliente, mensagemRecebida, serverInfos, vizinhos, lider, tabelaHash);
        }

    }
}
