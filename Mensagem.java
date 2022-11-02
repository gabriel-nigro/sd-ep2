// Lib para serializar os objetos enviados e recebidos via socket
import java.io.Serializable;
import java.net.Socket;
// Lib para utilização de Data
import java.util.Date;
// Lib para criar a tabela de Hash
import java.util.HashMap; 

public class Mensagem implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    
    private boolean isPut;
    private boolean isGet;
    private boolean isReplication;
    private boolean isReplicationOk;
    private String propriedade;
    private String valor;
    private Date timestamp;
    private String response;
    private Date responseTimestamp;
    private int replicationCount = 0;
    private Socket socketSolicitante;


    public Mensagem(String propriedade, String valor, boolean isPut, boolean isGetl, Date timestamp) {
        this.propriedade = propriedade;
        this.valor = valor;
        this.isPut = isPut;
        this. isGet = isGet;
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getResponse() {
        return this.response;
    }

    public Date getResponseTimestamp() {
        return this.responseTimestamp;
    }

    public int getReplicationCount() {
        return this.replicationCount;
    }

    public Socket getSocketSolicitante() {
        return this.socketSolicitante;
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

    public void setIsReplication(boolean valor) {
        this.isReplication = valor;
    }

    public void setIsReplicationOk(boolean valor) {
        this.isReplicationOk = valor;
    }

    public void setResponseTimestamp(Date timestamp) {
        this.responseTimestamp = timestamp;
    }

    public void setSocketSolicitante(Socket socketSolicitante) {
        this.socketSolicitante = socketSolicitante;
    }

    public void addReplicationCount() {
        this.replicationCount += 1;
    }
}
