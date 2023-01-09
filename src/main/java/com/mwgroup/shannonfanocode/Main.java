package com.mwgroup.shannonfanocode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

// Данная программа разработана с использованием графического интерфейса JavaFX.
// Работа программы заключается в кодировании символов методом Шеннона-Фано.
// Кодировать можно как символы по заданным вероятностям, так и символы текста.
// Интерфейс приложения удобен и понятен, поэтому трудностей с использованием программы возникнуть не должно.
// Для связи с автором можете написать на почту maksoomaksoo@gmail.com.

// Controller - класс для работы со сценой mainWindow.fxml
// TextController - класс для работы со сценой textCode.fxml
// PreItems - класс для создания предварительных объектов в контроллерах
// Items - класс для создания объектов в контроллерах


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}