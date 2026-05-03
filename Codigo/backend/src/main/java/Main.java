import static spark.Spark.*;
import com.google.gson.Gson;
import dao.UsuarioDao;
import dao.PacienteDao;
import dao.ConsultaDao; // Importante
import dao.ExameDao;
import model.Usuario;
import model.Paciente;
import model.Consulta;
import service.ServicoIA; // Importante
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        // Define a porta
        port(8080);
        
        String portStr = System.getenv("PORT");
        int port = (portStr != null) ? Integer.parseInt(portStr) : 8080;
        port(port);
        
        staticFiles.externalLocation("public");
        
        // Pasta temporária para upload
        File uploadDir = new File("upload-temp");
        uploadDir.mkdir(); 
        staticFiles.externalLocation("upload-temp");
        staticFiles.externalLocation("public");

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "*");
            // Adiciona tipo JSON apenas se não for retorno de arquivo
            if(!request.pathInfo().equals("/upload-raiox")) { 
                response.type("application/json");
            }
        });

        post("/login", (req, res) -> {
            Usuario uSite = new Gson().fromJson(req.body(), Usuario.class);

            
            Usuario uBanco = UsuarioDao.buscarPorEmail(uSite.getEmail());
            
            if (uBanco == null) {
                res.status(401);
                return new Gson().toJson("Usuario nao existe");
            }

            boolean senhaValida = false;
            
            if(uBanco.senha.startsWith("$2a$")) {
                senhaValida = BCrypt.checkpw(uSite.getSenha(), uBanco.senha);
            } else {
                senhaValida = uBanco.senha.equals(uSite.getSenha());
            }

            if (senhaValida) {
                return new Gson().toJson(uBanco);
            } else {
                res.status(401);
                return new Gson().toJson("Senha incorreta");
            }
        });

        post("/pacientes", (req, res) -> {
            Paciente p = new Gson().fromJson(req.body(), Paciente.class);
            PacienteDao.inserir(p);
            res.status(201);
            return new Gson().toJson("Sucesso");
        });

        get("/pacientes", (req, res) -> new Gson().toJson(PacienteDao.listarTodos()));

        // --- 4. ROTAS DE CONSULTAS ---
        post("/consultas", (req, res) -> {
            Consulta c = new Gson().fromJson(req.body(), Consulta.class);
            ConsultaDao.agendar(c);
            res.status(201);
            return new Gson().toJson("Sucesso");
        });

        get("/consultas-lista", (req, res) -> {
            return new Gson().toJson(ConsultaDao.listarTodas());
        });
        
        post("/upload-raiox", (req, res) -> {
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
            
            String consultaIdStr = req.raw().getParameter("consultaId");
            Part filePart = req.raw().getPart("imagem");
            
            try (InputStream inputStream = filePart.getInputStream()) {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                
                // Chama a IA
                ServicoIA.ResultadoIA resultado = ServicoIA.analisar(bytes);
                
                // Salva
                ExameDao.salvarResultado(
                    Integer.parseInt(consultaIdStr), 
                    filePart.getSubmittedFileName(), 
                    resultado.diagnostico, 
                    resultado.confianca, 
                    resultado.precisaRevisao
                );
                
                return new Gson().toJson(resultado);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return new Gson().toJson("Erro: " + e.getMessage());
            }
        });
        
        post("/usuarios", (req, res) -> {
            Usuario u = new Gson().fromJson(req.body(), Usuario.class);
            UsuarioDao.inserir(u);
            res.status(201);
            return new Gson().toJson("Usuário criado!");
        });
        
        System.out.println("--- SERVIDOR RODANDO EM http://localhost:8080 ---");
    }
}