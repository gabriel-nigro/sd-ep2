// Lib para utilização de Data
import java.util.Date;
import java.util.HashMap;

public class Teste {
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
    
    public static void main(String[] args) {

        // Inicializa a tabela hash
        HashMap<String, ValorHash> tabelaHash = new HashMap<String, ValorHash>();
        String propriedade = "legume";
        String valor = "abobora";
        Date timestamp = new Date();
        ValorHash valorHash = new ValorHash(valor, timestamp);
        tabelaHash.put(propriedade, valorHash);
        
        tabelaHash.put(propriedade, new ValorHash("teste", timestamp));

        System.out.println(tabelaHash.get(propriedade).getValor());

        int nada = 0;
        nada += 1;
        nada += 1;
        System.out.println("Nada: " + nada);
        
        
    }
}
