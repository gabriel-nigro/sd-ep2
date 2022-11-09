// Lib para serializar os objetos enviados e recebidos via socket
import java.io.Serializable;
import java.net.Socket;
// Lib para utilização de Data
import java.util.Date;
// Lib para criar a tabela de Hash
import java.util.HashMap;
import java.util.UUID; 

public class Mensagem implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    
    private boolean isPut;
    private boolean isGet;
    private boolean isReplication;
    private boolean isReplicationOk;
    private boolean isFromClient;
    private String propriedade;
    private String valor;
    private long timestamp;
    private long timestampCliente;
    private String response;
    private int replicationCount = 0;
    private UUID uuid = UUID.randomUUID();
    private String servidorReceptorPrimario;


    public Mensagem(String propriedade) {
        this.propriedade = propriedade;
    }

    public String getPropriedade() {
        return this.propriedade;
    }

    public String getValor() {
        return this.valor;
    }

    public boolean isPut() {
        return this.isPut;
    }

    public boolean isGet() {
        return this.isGet;
    }

    public boolean isReplication() {
        return this.isReplication;
    }

    public boolean isReplicationOk() {
        return this.isReplicationOk;
    }

    public boolean isFromClient() {
        return this.isFromClient;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public long getTimestampCliente() {
        return this.timestampCliente;
    }

    public String getResponse() {
        return this.response;
    }

    public String getServidorReceptorPrimario() {
        return this.servidorReceptorPrimario;
    }

    public int getReplicationCount() {
        return this.replicationCount;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setPropriedade(String propriedade) {
        this.propriedade = propriedade;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setIsPut(boolean valor) {
        this.isPut = valor;
    }

    public void setIsGet(boolean valor) {
        this.isGet = valor;
    }

    public void setIsReplication(boolean valor) {
        this.isReplication = valor;
    }

    public void setIsReplicationOk(boolean valor) {
        this.isReplicationOk = valor;
    }

    public void setIsFromClient(boolean valor) {
        this.isFromClient = valor;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestampCliente(long timestamp) {
        this.timestampCliente = timestamp;
    }

    public void setServidorReceptorPrimario(String servidor) {
        this.servidorReceptorPrimario = servidor;
    }

    public void addReplicationCount() {
        this.replicationCount += 1;
    }
}
