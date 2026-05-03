package service;

import java.io.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.google.gson.Gson;

public class ServicoIA {

    
    private static final String AZURE_URL = System.getenv().getOrDefault("AZURE_URL", "");
    private static final String AZURE_KEY = System.getenv().getOrDefault("AZURE_KEY", "");

    public static ResultadoIA analisar(byte[] imagemBytes) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            
            // Prepara o envio para o Azure
            HttpPost request = new HttpPost(AZURE_URL);
            request.setHeader("Prediction-Key", AZURE_KEY);
            request.setHeader("Content-Type", "application/octet-stream"); // Tipo para arquivo de imagem
            request.setEntity(new ByteArrayEntity(imagemBytes)); // Coloca a imagem no pacote

            // Envia e espera a resposta
            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();
                
                // Lê a resposta do Azure 
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                entity.writeTo(out);
                String jsonResposta = new String(out.toByteArray());
                
                // Transforma o texto JSON em objetos Java
                AzureResponse dadosAzure = new Gson().fromJson(jsonResposta, AzureResponse.class);
                
                // --- LÓGICA DE DIAGNÓSTICO ---
                double chanceFratura = 0.0;
                double chanceNormal = 0.0;

                // Procura nas predições os valores de "Fratura" e "Normal"
                for (Prediction p : dadosAzure.predictions) {
                    if (p.tagName.equalsIgnoreCase("Com fratura") || p.tagName.equalsIgnoreCase("Fracture")) {
                        chanceFratura = p.probability;
                    }
                    if (p.tagName.equalsIgnoreCase("Sem fratura") || p.tagName.equalsIgnoreCase("Sem Fratura")) {
                        chanceNormal = p.probability;
                    }
                }

                boolean incerto = Math.abs(chanceFratura - chanceNormal) < 0.10; // Margem de 5%
                
                String diagnostico;
                if (chanceFratura > chanceNormal) {
                    diagnostico = "FRATURA DETECTADA";
                } else {
                    diagnostico = "NORMAL";
                }
                
                return new ResultadoIA(diagnostico, Math.max(chanceFratura, chanceNormal), incerto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoIA("ERRO NA IA", 0.0, true);
        }
    }

    public static class AzureResponse {
        public Prediction[] predictions;
    }
    public static class Prediction {
        public double probability;
        public String tagName;
    }

    public static class ResultadoIA {
        public String diagnostico;
        public double confianca;
        public boolean precisaRevisao;

        public ResultadoIA(String d, double c, boolean p) {
            this.diagnostico = d;
            this.confianca = c;
            this.precisaRevisao = p;
        }
    }
}
