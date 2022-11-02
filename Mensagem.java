// Lib para serializar os objetos enviados e recebidos via socket
import java.io.Serializable;
// Lib para utilização de Data
import java.util.Date;
// Lib para criar a tabela de Hash
import java.util.HashMap; 

public class Mensagem implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    
    private boolean isPut;
    private boolean isGet;
    private String propriedade;
    private String valor;
    private Date timestamp;
    private String response;


    public Mensagem(String propriedade, String valor, boolean isPut, boolean isGet) {
        this.propriedade = propriedade;
        this.valor = valor;
        this.isPut = isPut;
        this. isGet = isGet;
        timestamp = new Date();
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

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getResponse() {
        return this.response;
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
}
