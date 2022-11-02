import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
// Lib para leitura de input do usuário
import java.util.Scanner;
// Lib para validar input do usuário
import java.util.regex.Pattern;
// Lib para escolher randômicamente
import java.util.Random;
import java.util.Date;
// Lib para criar a tabela de Hash
import java.util.HashMap;

public class Cliente {

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

    public static void put(String servidor, Mensagem mensagem) {
        (new Thread() {
            @Override
            public void run() {
                String ip = getIp(servidor);
                int porta = getPorta(servidor);
                try {
                    // Abre um Socket para conexão com o ServerSocket
                    Socket socket = new Socket(ip, porta);

                    // Envia mensagem
                    enviaMensagem(socket, mensagem);

                    // Recebe response
                    Mensagem mensagemResponse = recebeMensagem(socket);

                    if (mensagemResponse.getResponse().equals("PUT_OK")) {
                        System.out.println("PUT_OK value");
                        System.out.println("Mensagem recebida, valor: " + mensagemResponse.getValor());
                    }

                    // Fecha o Socket
                    socket.close();
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

        // Ação escolhida pelo usuário
        int acao;

        // Variavel de estado para inicializacao
        boolean isInitialized = false;

        // Array para armazenamento dos vizinhos
        String[] servidores;
        servidores = new String[3];

        // Variável para escolher o servidor randomicamente
        Random rand = new Random();
        int numeroServidor;

        while (true) {
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
                    // Funcionalidade 4.a)

                    // Verifica se o servidor já foi inicializado
                    if (isInitialized) {
                        System.out.println("\nO cliente encontra-se inicializado.");
                        break;
                    }

                    // Servidor vizinho[0]
                    System.out.println("\nInforme o IP:PORTA do primeiro servidor.");
                    servidores[0] = getInputInfos();

                    // Servidor vizinho[1]
                    System.out.println("\nInforme o IP:PORTA do segundo servidor.");
                    servidores[1] = getInputInfos();

                    // Servidor vizinho[1]
                    System.out.println("\nInforme o IP:PORTA do terceiro servidor.");
                    servidores[2] = getInputInfos();

                    // Seta estado de inicializado como "true"
                    isInitialized = true;
                    break;
                }
                case 2: {
                    // Funcionalidade 4.b)
                    boolean isPut = true;

                    // Verifica se o servidor já foi inicializado
                    if (!isInitialized) {
                        System.out
                                .println("\nNecessario realizar inicializacao. Para isso, digite 1 no menu de acoes.");
                        break;
                    }

                    // Nome da propriedade
                    System.out.println("\nDigite o nome da propriedade:");
                    String propriedade = entrada.nextLine();

                    // Nome do valor
                    System.out.println("\nDigite o valor da propriedade:");
                    String valor = entrada.nextLine();

                    // Seta valor de timestamp
                    Date timestamp = new Date();
                    // Cria mensagem
                    Mensagem mensagem = new Mensagem(propriedade, valor, isPut, false, timestamp);

                    // Gera um número entre 0 e 2, para escolher o servidor de forma randômica
                    numeroServidor = rand.nextInt(2 + 1);

                    // Envia o put
                    put(servidores[numeroServidor], mensagem);

                    break;
                }
                case 3: {
                    // Funcionalidade 4.f)

                    // Verifica se o servidor já foi inicializado
                    if (!isInitialized) {
                        System.out
                                .println("\nNecessario realizar inicializacao. Para isso, digite 1 no menu de acoes.");
                        break;
                    }

                    // Gera um número entre 0 e 2, para escolher o servidor de forma randômica
                    numeroServidor = rand.nextInt(2 + 1);

                    break;
                }
                default:
                    System.out.println("Ação incorreta. Informe uma ação válida.");
                    break;
            }
        }
        /*
         * try {
         * Socket s = new Socket("127.0.0.1", 9000);
         * 
         * OutputStream os = s.getOutputStream();
         * DataOutputStream writer = new DataOutputStream(os);
         * 
         * InputStreamReader is = new InputStreamReader(s.getInputStream());
         * BufferedReader reader = new BufferedReader(is);
         * 
         * //BufferedReader inFromUser = new BufferedReader(new
         * InputStreamReader(System.in));
         * 
         * //String texto = inFromUser.readLine();
         * 
         * //writer.writeBytes(texto + "\n");
         * 
         * // Cria mensagem
         * Mensagem mensagem = new Mensagem("abobora", "abobora", false, false);
         * 
         * // declaração e preenchimento do buffer de envio
         * ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
         * ObjectOutputStream oos = new ObjectOutputStream(baos);
         * oos.writeObject(mensagem);
         * final byte[] sendMessage = baos.toByteArray();
         * 
         * writer.write(sendMessage);
         * 
         * String response = reader.readLine();
         * System.out.println("DoServidor: " + response);
         * 
         * s.close();
         * } catch (IOException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         */
    }

}
