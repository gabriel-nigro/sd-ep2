// Libs para socket
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
// Lib para leitura de input do usuário
import java.util.Scanner;
// Lib para validar input do usuário
import java.util.regex.Pattern;
// Lib para escolher randômicamente
import java.util.Random;
// Lib para criar a tabela de Hash
import java.util.HashMap;
// Lib de exceção
import java.io.IOException;

public class Cliente {

    // Declara o Scanner para obter o input do usuário.
    private static Scanner entrada;

    /*
     * A classe ValorHash foi criada com o intuito de armazenar duas propriedades em
     * uma
     * chave do HashMap, sendo elas o "valor" e o "timestamp".
     */
    static public class ValorHash {
        private String valor;
        private long timestamp;

        /*
         * O construtor inicializa a instância recebendo as propriedades de valor e
         * timestamp.
         * 
         * @param valor
         * 
         * @param timestamp
         */
        public ValorHash(String valor, long timestamp) {
            this.valor = valor;
            this.timestamp = timestamp;
        }

        /*
         * Obtém o valor da propriedade valor.
         * 
         * @return String valor
         */
        public String getValor() {
            return this.valor;
        }

        /*
         * Obtém o valor do timestamp.
         * 
         * @return long timestamp
         */
        public long getTimestamp() {
            return this.timestamp;
        }
    }

    /*
     * A classe TabelaHash foi criada com o intuito de armazenar pares chave-valor.
     * Nela é instanciado um HashMap, tendo como chave uma String, e o valor uma
     * instância
     * da classe ValorHash, o qual por sua vez possui uma String valor e um
     * Timestamp
     * 
     * Para visualizarmos uma instância da classe, abaixo há sua representação na
     * notação JSON:
     * {
     * chave: ${propriedade},
     * valorHash: {
     * valor: ${valor},
     * timestamp: ${ts}
     * }
     * }
     */
    static public class TabelaHash {

        // Inicializa a tabela hash
        HashMap<String, ValorHash> tabelaHash = new HashMap<String, ValorHash>();

        // Construtor vazio
        public TabelaHash() {
        };

        /*
         * O método get() da classe realiza a chamada do método get() da instância
         * HashMap
         * 
         * @param propriedade Chave do HashMap
         * 
         * @return ValorHash a instância do ValorHash atrelada a chave informada no
         * parâmetro
         */
        public ValorHash get(String propriedade) {
            return this.tabelaHash.get(propriedade);
        }

        /*
         * O método put() da classe realiza a chamada do método put() da instância
         * HashMap
         * 
         * @param propriedade
         * 
         * @param valor
         * 
         * @param timestamp
         */
        public void put(String propriedade, String valor, long timestamp) {
            this.tabelaHash.put(propriedade, new ValorHash(valor, timestamp));
        }
    }

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
     * 
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
     * O método é responsável por receber a mensagem enviada ao seu ServerSocket
     * local.
     * 
     * @param socket A conexão socket com o remetente
     * 
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

    /*
     * O método é responsável por lidar com o fluxo de requisições PUT.
     * 
     * Dessa forma, cria-se o Socket com o destinatário e envia-lhe a mensagem com
     * os dados
     * necessários para o PUT. Após isso, é utilizada a função ".accept()" do
     * servidor local
     * para receber a mensagem de resposta da requisição.
     * 
     * Com a mensagem em mãos, verifica-se se o response é de PUT_OK, printando as
     * informações
     * indicadas na seção 6) do EP.
     * 
     * Funcionalidade 4.b)
     * 
     * @param servidor As informações de IP:PORTA do servidor destinatário.
     * 
     * @param mensagem A mensagem a ser enviada.
     * 
     * @param servidorLocal A instância do ServerSocket local
     * 
     */
    public static void put(String servidor, Mensagem mensagem, ServerSocket servidorLocal) {
        try {
            // Obtém informações de IP e PORTA do destinatário
            String ip = getIp(servidor);
            int porta = getPorta(servidor);

            // Abre um Socket para conexão com o ServerSocket remoto
            Socket socket = new Socket(ip, porta);

            // Envia mensagem
            enviaMensagem(socket, mensagem);

            // Abre o Socket remoto no ServerSocket local
            Socket servidorRemoto = servidorLocal.accept();

            // Recebe mensagem via Socket remoto
            Mensagem mensagemResponse = recebeMensagem(servidorRemoto);

            // Verifica se o response é igual a PUT_OK
            if (mensagemResponse.getResponse().equals("PUT_OK")) {
                // 6)
                System.out.println("\nPUT_OK key:" + mensagemResponse.getPropriedade() + " value:"
                        + mensagemResponse.getValor() + " timestamp " + mensagemResponse.getTimestamp()
                        + " realizada no servidor " + mensagemResponse.getServidorResposta());
            }

            // Fecha o socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * O método é responsável por lidar com o fluxo de requisições GET.
     * 
     * Dessa forma, cria-se o Socket com o destinatário e envia-lhe a mensagem com
     * os dados
     * necessários para o GET. Após isso, é utilizada a função ".accept()" do
     * servidor local
     * para receber a mensagem de resposta da requisição.
     * 
     * Com a mensagem em mãos, verifica-se se o response, o qual pode ser:
     * - null: Caso a chave não exista no servidor.
     * - TRY_OTHER_SERVER_OR_LATER: Caso o servidor ainda não tenha atualizado o
     * valor da chave.
     * - Valor da chave: Caso o valor da chave no servidor esteja mais atualizado
     * que o cliente.
     * Assim, são printadas as informações conforme indicadas na seção 6) do EP.
     * 
     * Funcionalidade 4.c)
     * 
     * @param servidor As informações de IP:PORTA do servidor destinatário.
     * 
     * @param mensagem A mensagem a ser enviada.
     * 
     * @param servidorLocal A instância do ServerSocket local
     * 
     */
    public static void get(String servidor, Mensagem mensagem, TabelaHash tabelaHash, ServerSocket servidorLocal) {
        String ip = getIp(servidor);
        int porta = getPorta(servidor);
        try {
            // Abre um Socket para conexão com o ServerSocket
            Socket socket = new Socket(ip, porta);

            // Envia mensagem
            enviaMensagem(socket, mensagem);

            // Abre o Socket remoto no ServerSocket local
            Socket servidorRemoto = servidorLocal.accept();

            // Recebe mensagem via Socket remoto
            Mensagem mensagemResponse = recebeMensagem(servidorRemoto);

            // Verifica o response
            if (mensagemResponse.getResponse() == null) {
                System.out.println("\nChave não existe no servidor " + servidor);
            } else if (mensagemResponse.getResponse().equals("TRY_OTHER_SERVER_OR_LATER")) {
                System.out.println("\nTRY_OTHER_SERVER_OR_LATER");
            } else {
                // Adiciona a tabela hash para log
                tabelaHash.put(mensagemResponse.getPropriedade(), mensagemResponse.getResponse(),
                        mensagemResponse.getTimestamp());

                // 6)
                System.out.println("\nGET key: " + mensagemResponse.getPropriedade() + " value: "
                        + mensagemResponse.getResponse() + " obtido do servidor " + servidor + ", meu timestamp "
                        + mensagemResponse.getTimestampCliente() + " e do servidor " + mensagemResponse.getTimestamp());
            }

            // Fecha o Socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // Inicializa a tabela hash
        TabelaHash tabelaHash = new TabelaHash();

        // Declar Servidor local para o Cliente
        String servidorLocal = null;
        ServerSocket serverSocketLocal = null;

        while (true) {
            System.out.println("\nMenu de acoes.");
            System.out.println("Digite uma opção:");

            // Se o peer já foi inicializado, não mostra a opção de inicialização
            if (!isInitialized)
                System.out.println("1 - INIT");
            System.out.println("2 - PUT");
            System.out.println("3 - GET");

            // Obtém código de ação informado pelo usuário
            acao = Integer.parseInt(entrada.nextLine());

            switch (acao) {
                case 1: {
                    // Funcionalidade 4.a)

                    // Verifica se o servidor já foi inicializado
                    if (isInitialized) {
                        System.out.println("\nO cliente encontra-se inicializado.");
                        break;
                    }

                    // Porta para criar o ServerSocket
                    System.out.println("\nInforme o IP:PORTA local.");
                    servidorLocal = getInputInfos();

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

                    // Inicializa servidor
                    serverSocketLocal = new ServerSocket(getPorta(servidorLocal));

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

                    // Cria mensagem
                    Mensagem mensagem = new Mensagem(propriedade, servidorLocal);
                    // Assume o valor da propriedade a ser inserida nos servidores
                    mensagem.setValor(valor);
                    // Identifica que a mensagem é de PUT
                    mensagem.setIsPut(isPut);
                    // Identificação que a mensagem vem do cliente
                    mensagem.setIsFromClient(true);

                    // Gera um número entre 0 e 2, para escolher o servidor de forma randômica
                    numeroServidor = rand.nextInt(2 + 1);

                    // Envia o put
                    put(servidores[numeroServidor], mensagem, serverSocketLocal);

                    break;
                }
                case 3: {
                    // Funcionalidade 4.c)
                    boolean isGet = true;

                    // Verifica se o servidor já foi inicializado
                    if (!isInitialized) {
                        System.out
                                .println("\nNecessario realizar inicializacao. Para isso, digite 1 no menu de acoes.");
                        break;
                    }

                    // Nome da propriedade
                    System.out.println("\nDigite o nome da propriedade:");
                    String propriedade = entrada.nextLine();

                    // Cria mensagem
                    Mensagem mensagem = new Mensagem(propriedade, servidorLocal);
                    // Identifica que a mensagem é de PUT
                    mensagem.setIsGet(isGet);
                    // Identificação que a mensagem vem do cliente
                    mensagem.setIsFromClient(true);

                    // Identifica o timestamp da chave presente na base do cliente
                    ValorHash chave = tabelaHash.get(propriedade);
                    // Verifica se a chave já existe para o cliente
                    if (chave == null) {
                        mensagem.setTimestampCliente(0);
                    } else {
                        mensagem.setTimestampCliente(chave.getTimestamp());
                    }

                    // Gera um número entre 0 e 2, para escolher o servidor de forma randômica
                    numeroServidor = rand.nextInt(2 + 1);

                    // Envia o GET
                    get(servidores[numeroServidor], mensagem, tabelaHash, serverSocketLocal);

                    break;
                }
                default:
                    // Caso o usuário informe o código incorreto no Menu de Ações.
                    System.out.println("Ação incorreta. Informe uma ação válida.");
                    break;
            }
        }
    }

}
