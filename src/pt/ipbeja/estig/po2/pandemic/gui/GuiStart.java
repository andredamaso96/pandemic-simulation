package pt.ipbeja.estig.po2.pandemic.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class GuiStart extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        //getParameters().getRaw().forEach(System.out::println);
        //String file = String.valueOf(getParameters().getRaw());
        ContagiousBoard board = new ContagiousBoard();
        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulador de Pandemia");

        if(System.console() != null) {
            List<String> arguments = getParameters().getRaw();
            String fileName = arguments.get(0);
            System.out.println(fileName);
            File file = new File(fileName);
            board.openFile(file);
        }


        primaryStage.setOnCloseRequest((e) -> {
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String[] args) {

        Application.launch(args);

    }

}
