package pt.ipbeja.estig.po2.pandemic.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import pt.ipbeja.estig.po2.pandemic.model.*;
import pt.ipbeja.estig.po2.pandemic.model.Persons.Person;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * The main user interface for the Pandemic game.
 *
 * @author André Dâmaso - 15307
 * @version 2020-05-18
 */

public class ContagiousBoard extends VBox implements View {

    public static World world;
    public static WorldBoard pane;

    public MenuBar menuBar;
    VBox left;

    TextField initField = new TextField();
    TextField initLine = new TextField();
    TextField initCol= new TextField();
    TextField initHealhy = new TextField();
    TextField initSick = new TextField();
    TextField initImmune = new TextField();
    Text textArea = new Text("Número de pessoas:");
    Text textLine = new Text("Número de linhas:");
    Text textCol = new Text("Número de colunas:");
    Text textHealhy = new Text("Número de pessoas saudaveis:");
    Text textSick = new Text("Número de pessoas doentes:");
    Text textImmune = new Text("Número de pessoas imunes:");


    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series<Number, Number> series;
    LineChart<Number, Number> chart;
    private boolean isActive = true;

    private int x = 0;
    private int y = 0;

    Button startBtn;
    Button restartBtn;
    Button stopBtn;
    MenuItem submenu1;

    private Desktop desktop = Desktop.getDesktop();

    private List<String> data;


    public ContagiousBoard() {

        // Menu bar -------------------------------------------------------------------------------------------------------------------------------
        this.drarMenuBar();

        //Construção do cabeçalho --------------------------------------------------------------------------------------------------------------------
        this.drawButtons();
        this.disabledButtons(false, true, true);

        initField.setPrefWidth(60);
        HBox topBar = new HBox(
                stopBtn,
                restartBtn
                );

        topBar.setAlignment(Pos.CENTER); // Alinhar os nodes ao centro
        topBar.setSpacing(20);
        topBar.setPadding(new Insets(10)); // Margens interiores da HBox

        // Box do lado esquerdo --------------------------------------------------------------------------------
        initLine.setPrefWidth(30);
        initCol.setPrefWidth(30);
        initHealhy.setPrefWidth(30);
        initSick.setPrefWidth(30);
        VBox leftBox = new VBox(
                this.textLine,
                this.initLine,
                this.textCol,
                this.initCol,
                new Separator(Orientation.HORIZONTAL), // Separador/linha horizontal
                this.textHealhy,
                this.initHealhy,
                this.textSick,
                this.initSick,
                this.textImmune,
                this.initImmune,
                new Separator(Orientation.HORIZONTAL),
                startBtn
        );

        leftBox.setAlignment(Pos.CENTER); // Alinhar os nodes ao centro
        leftBox.setSpacing(18);
        leftBox.setPadding(new Insets(10));

        // Grafico --------------------------------------------------------------------------------------------------------------
        this.drawChart();

        // Adicionar cabeçalho ao tabuleiro numa vbox -------------------------------------------------------------------------------------
        left = new VBox(
                topBar

        );
        left.setPrefSize(600, 700);

        // Adicionar o gráfico ao lado direito da vbox
        HBox center = new HBox(
                leftBox,
                left,
                chart
        );

        center.setAlignment(Pos.CENTER); // Alinhar os nodes ao centro

        this.getChildren().addAll(menuBar, center);

    }

    /**
    * Construction of the file menu with the events of each item
    */
    private void drarMenuBar() {

        menuBar = new MenuBar();
        Menu menu1 = new Menu("File");
        submenu1 = new MenuItem("Open");
        MenuItem submenu2 = new MenuItem("Save As...");
        MenuItem submenu3 = new MenuItem("Exit");
        this.menuBar.getMenus().add(menu1);
        menu1.getItems().addAll(submenu1, submenu2, submenu3);

        submenu1.setOnAction(e -> {

            System.out.println("Menu Item 1 Selected");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Users\\usuario\\Documents\\EI\\PO2 2020\\15307_AndréDâmaso_TP02_PO2_2019-2020"));
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                openFile(file);
            }
        });

        submenu2.setOnAction(e -> {

            world.stop();

            System.out.println("Menu Item 2 Selected");
            FileChooser saveFile = new FileChooser();
            saveFile.setInitialDirectory(new File("C:\\Users\\usuario\\Documents\\EI\\PO2 2020\\15307_AndréDâmaso_TP02_PO2_2019-2020"));
            saveFile.setTitle("Open Resource File");
            saveFile.setInitialFileName("mysave");
            saveFile.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = saveFile.showSaveDialog(null);

            if(file != null)
            {
                saveFile(file);
            }

        });

        submenu3.setOnAction(event -> {
            Platform.exit();
        });

    }
    /**
    * This function saves the positions in the file
     */
    private void saveFile(File file) {

        data = world.setData();

        BufferedWriter wr = null;
        try {
            wr = new BufferedWriter(new FileWriter(file));
            for (String var : data) {
                wr.write(var);
                wr.newLine();
            }
            wr.close();

        } catch (Exception e) {
            //TODO: handle exception
        }


    }

    /**
     * This function open the positions present in the file
     */
    public void openFile(File file) {

        List<String> strings = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String availalbe;
            while((availalbe = bufferedReader.readLine()) != null) {
                strings.add(availalbe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int line = Integer.parseInt(strings.get(0));
        int col = Integer.parseInt(strings.get(1));

        List<String> listHealhy = new ArrayList<>();
        List<String> listImune = new ArrayList<>();
        List<String> listSick = new ArrayList<>();

        for (int i = 0; i < strings.size() - 1; i++) {
            switch (strings.get(i)) {
                case "healthy":
                    for (int j = i + 1; j < strings.size(); j++) {
                        if ((!strings.get(j).equals("immune")) && (!strings.get(j).equals("sick"))) {
                            listHealhy.add(strings.get(j));
                        } else {
                            break;
                        }
                    }
                    break;
                case "immune":
                    for (int j = i + 1; j < strings.size(); j++) {
                        //System.out.println(strings.get(j));
                        if ((!strings.get(j).equals("sick")) && (!strings.get(j).equals("healthy"))) {
                            listImune.add(strings.get(j));
                        } else {
                            break;
                        }
                    }
                    break;
                case "sick":
                    for (int j = i + 1; j < strings.size(); j++) {
                        if ((!strings.get(j).equals("immune")) && (!strings.get(j).equals("healthy"))) {
                            listSick.add(strings.get(j));
                        } else {
                            break;
                        }
                    }
                    break;
            }

        }

        if(line <= 3 || col <= 3){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Número de linhas ou colunas curto");
            alert.showAndWait();
        }else {
            disabledButtons(true, false, false);

            world = new World(this, line, col);
            this.pane = new WorldBoard(this.world, 10);
            chart.getData().add(series);
            left.getChildren().add(pane);
            world.setPersons(true, listHealhy, listImune, listSick);
            world.start();
        }

    }

    /**
     * The buttons of program
     */
    private void drawButtons() {

        startBtn = new Button("Começar");
        StartButtonHandler startButtonHandler = new StartButtonHandler();
        startBtn.setOnAction(startButtonHandler);

        stopBtn = new Button("Parar");
        StopButtonHandler stopButtonHandler = new StopButtonHandler();
        stopBtn.setOnAction(stopButtonHandler);

        restartBtn = new Button("Recomeçar");
        RestartButtonHandler restartButtonHandler = new RestartButtonHandler();
        restartBtn.setOnAction(restartButtonHandler);


    }

    /**
     * Draw chart
     */
    private void drawChart() {

        yAxis.setAnimated(false);
        yAxis.setLabel("Número de infetados");

        series = new XYChart.Series<>();
        series.setName("Curva de crescimento de infetados");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setMaxHeight(600);
        chart.setMaxWidth(600);

    }

    /**
     * Disable buttons
     */
   private void disabledButtons(boolean start,boolean stop, boolean restart){

        startBtn.setDisable(start);
        stopBtn.setDisable(stop);
        restartBtn.setDisable(restart);
        submenu1.setDisable(start);

    }


    @Override
    public void populateWorld() {
        pane.populateWorld();
    }

    @Override
    public void updatePosition(int index, int dx, int dy) {
        Platform.runLater( () -> {

            pane.updatePosition(dx, dy, index);

        });
    }

    @Override
    public void updateColor(Person person, int index) {

        Platform.runLater( () -> {
            pane.updateColor(person, index);
        });

    }

    @Override
    public void updateGraph(int numInfects) {
        Platform.runLater( () -> {

                y = numInfects;
                series.getData().add(new XYChart.Data<>(x, numInfects));
                x++;


        });
    }

    @Override
    public void updateData(List<String> dataList) {

        data = dataList;

    }

    /**
     * Restart game model
     */
    private void restart(int line, int col){

        if(line <= 3 || col <= 3){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Número de linhas ou colunas curto. \nLinha e Coluna têm de ser maiores que 3");
            alert.showAndWait();
            disabledButtons(false, false, false);
        }else if(line > 60 || col > 60){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Número de linhas ou colunas muito longo. \nLinha e Coluna têm de ser menores que 60");
            alert.showAndWait();
            disabledButtons(false, false, false);
        }else {
            world = new World(this, line, col);
            this.pane = new WorldBoard(this.world, 10);
            chart.getData().add(series);
        }
        //pane.autosize();
    }


    /**
     * Simulation start button handler
     */
    private class StartButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

                if(initCol.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Número de linhas inválido");
                    alert.showAndWait();

                }else if(initLine.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Número de colunas inválido");
                    alert.showAndWait();

                }else if(initHealhy.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Tem de selecionar quantidade de pessoas saudaveis");
                    alert.showAndWait();

                }else if(initSick.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Tem de selecionar quantidade de pessoas doentes");
                    alert.showAndWait();

                }else if(initImmune.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Tem de selecionar quantidade de pessoas imunes");
                    alert.showAndWait();

                }else {

                    disabledButtons(true, false, false);
                    //world = new World(ContagiousBoard.this, 60, 60);
                    int line = Integer.parseInt(initLine.getText());
                    int col = Integer.parseInt(initCol.getText());
                    restart(line, col);
                    left.getChildren().add(pane);
                    world.setSize(Integer.parseInt(initHealhy.getText()), Integer.parseInt(initSick.getText()),
                            Integer.parseInt(initImmune.getText()));
                    world.start();

                }

                System.out.println("começar");

        }
    }



    /**
     * Simulation stop button handler
     */
    private class StopButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            //Platform.runLater( () -> {
            disabledButtons(true, false, false);
            //finish();
            world.stop();

            System.out.println("stop");
           // });


        }
    }

    /**
     * Simulation restart button handler
     */
    private class RestartButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            //Platform.runLater( () -> {
            disabledButtons(true, false, false);
            world.restart();
            System.out.println("recomeçar");
            // });


        }
    }


}
