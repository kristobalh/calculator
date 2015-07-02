package calculator;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Irka on 22.06.2015.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        CalculatorGUI calculator = new CalculatorGUI();
        calculator.setVisible(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
