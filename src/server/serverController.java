/*
 * @Isaac
 */
package server;

import com.jfoenix.controls.JFXSpinner;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Isaac
 */
public class serverController implements Initializable {
    
    @FXML
    private Label label;

    @FXML
    private JFXSpinner spinner;
    
    int nClient = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LaunchServer server = new LaunchServer(4321);
        
        label.textProperty().bind(server.messageProperty());
        
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(server);
        executorService.shutdown();
    }    
    
}
