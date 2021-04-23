/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sk.stu.fiit.parsers.Requests.dto.EditTourOfferRequest;

/**
 *
 * @author adamf
 */
public class Main extends Application {
    
    /*
    IRequestVisitor parser = new XMLRequestParser();

    HttpPost httpPost = new HttpPost(
            "http://localhost:8080/api/v1/login/");
    httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");

    HttpEntity loginEntity = parser.constructLoginRequest(new LoginRequest("email@gmail.com", "password"));

    httpPost.setEntity(loginEntity);

    IResponseParser responseParser = new XMLResponseParser();

    try ( CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
        System.out.println(responseParser.parseLoginData(response).getUser().getType());
    }
    */
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Views/Signin.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        
        //EditTourOfferRequest request = new EditTourOfferRequest.Builder("id123").updateStartPlace("asd").build();
        
        
        
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        root.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
