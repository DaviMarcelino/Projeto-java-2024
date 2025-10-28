package tca.com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import tca.com.dao.PassageiroDAO;
import tca.com.dao.ReservaDAO;
import tca.com.dao.VooDAO;
import tca.com.dao.FuncionarioDAO;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        PassageiroDAO passageiroDAO = new PassageiroDAO();
        passageiroDAO.criarTabelaSeNaoExistir();

        VooDAO vooDAO = new VooDAO();
        vooDAO.criarTabelaSeNaoExistir();

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        funcionarioDAO.criarTabelaSeNaoExistir(); 

        ReservaDAO reservaDAO = new ReservaDAO();
        reservaDAO.criarTabelaSeNaoExistir(); 

        FXMLLoader fxTela = loadFXML("./views/TelaLogin.fxml"); 
        Parent root = fxTela.load();
        scene = new Scene(root, 789, 441);
        stage.setScene(scene);
        stage.show();
    }

    public static FXMLLoader loadFXML(String caminho) {
        return new FXMLLoader(App.class.getResource(caminho));
    }

    public static void mudaTela(String caminho) {
        try {
            FXMLLoader fxTela = loadFXML(caminho);
            Parent root = fxTela.load();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}