package com.mwgroup.shannonfanocode;

import java.io.IOException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TextController {

    @FXML
    private Label shenFanoButton;

    @FXML
    private Button answerButton;

    @FXML
    private TextArea textArea;

    @FXML
    private TextArea resArea;

    @FXML
    private Button dayButton;

    @FXML
    private Button exampleButton;

    @FXML
    private Button fullAnswerButton;

    @FXML
    private Button clearButton;

    @FXML
    void initialize() {

        // Очистка текста:
        clearButton.setOnAction(actionEvent -> {
            textArea.setText("");
            resArea.setText("");
        });

        // Вставка текста для примера:
        exampleButton.setOnAction(actionEvent -> textArea.setText("Текст"));

        // Вставка сегодняшнего дня:
        dayButton.setOnAction(actionEvent -> textArea.setText(new Date().toString()));

        // Подсчёт символов и вероятностей:
        answerButton.setOnAction(actionEvent -> {

            System.out.println("...............................................................................");

            int counter = 0; // счётчик, который будет суммировать вероятности

            // Если поле не пустое, то:
            if (!textArea.getText().isEmpty()) {

                String string = textArea.getText().trim(); // String принимает текст из поля
                string = string.toLowerCase(Locale.ROOT).replaceAll(" ", ""); // нижний регистр и удаление пробелов

                List<PreItems> preItems = new ArrayList<>(); // создание списка
                for (int i = 0; i < string.length(); i++) {
                    preItems.add(new PreItems(string.charAt(i) + "", 1));
                } // заполнение списка символами
                countingSym(preItems); // подсчёт символов

                for (PreItems textItem : preItems) {
                    counter += textItem.getVer();
                } // сумма вероятностей

                resArea.setText(textArea.getText() + "\nОбщее число символов: " + counter);

                // Создание и инициализация списка предварительных массивов
                List<Items> textItems = new ArrayList<>();
                for (int i = 0; i < preItems.size(); i++) {
                    textItems.add(new Items(preItems.get(i).getSym(), preItems.get(i).getVer(), ""));
                    textItems.get(i).setVer(textItems.get(i).getVer() / counter);
                }

                // Сортировка предварительных объектов по убыванию вероятности:
                textItems.sort(Comparator.comparingDouble(Items::getVer).reversed());

                // Создание и инициализация массива символов преобразованного списка:
                String[] symbols = new String[textItems.size()];
                for (int i = 0; i < textItems.size(); i++) {
                    symbols[i] = textItems.get(i).getSym();
                }

                // Создание и инициализация массива вероятностей символов преобразованного списка:
                Double[] ver = new Double[textItems.size()];
                for (int i = 0; i < textItems.size(); i++) {
                    ver[i] = textItems.get(i).getVer();
                }

                // Создание и "опустошение (null -> 0)" массива, который будет хранить код:
                String[] coding = new String[ver.length];
                Arrays.fill(coding, ""); // "опустошение"

                // Создание объекта типа Controller для работы с методами из класса Controller:
                Controller controller = new Controller();

                // Вызов методов (подробнее см. в классе Controller):
                controller.codingShenFano(symbols, ver, coding, textItems, resArea, false);
                controller.makeCode(textItems);

                // Вывод результата на экран и в консоль:
                resArea.setText(resArea.getText() + "\n\nСимвол          Вероятность          Кодирование\n");
                for (Items item : textItems) {
                    System.out.format("%1s %-15s %-15s %n", item.getSym(), item.getVer(), item.getCode());
                    resArea.setText(resArea.getText() + item.toString().trim() + "\n");
                }

                System.out.println("...............................................................................\n");

            } else textArea.setText("Введите данные в строку.");

        });

        // Подробное решение:
        fullAnswerButton.setOnAction(actionEvent -> {

            System.out.println("...............................................................................");

            int counter = 0; // счётчик, который будет суммировать вероятности

            // Если поле не пустое, то:
            if (!textArea.getText().isEmpty()) {

                String string = textArea.getText().trim(); // String принимает текст из поля
                string = string.toLowerCase(Locale.ROOT).replaceAll(" ", ""); // нижний регистр и удаление пробелов

                List<PreItems> preItems = new ArrayList<>(); // создание списка
                for (int i = 0; i < string.length(); i++) {
                    preItems.add(new PreItems(string.charAt(i) + "", 1));
                } // заполнение списка символами
                countingSym(preItems); // подсчёт символов

                for (PreItems textItem : preItems) {
                    counter += textItem.getVer();
                } // сумма вероятностей

                resArea.setText(textArea.getText() + "\nОбщее число символов: " + counter);

                // Создание и инициализация списка предварительных массивов
                List<Items> textItems = new ArrayList<>();
                for (int i = 0; i < preItems.size(); i++) {
                    textItems.add(new Items(preItems.get(i).getSym(), preItems.get(i).getVer(), ""));
                    textItems.get(i).setVer(textItems.get(i).getVer() / counter);
                }

                // Сортировка предварительных объектов по убыванию вероятности:
                textItems.sort(Comparator.comparingDouble(Items::getVer).reversed());

                // Создание и инициализация массива символов преобразованного списка:
                String[] symbols = new String[textItems.size()];
                for (int i = 0; i < textItems.size(); i++) {
                    symbols[i] = textItems.get(i).getSym();
                }

                // Создание и инициализация массива вероятностей символов преобразованного списка:
                Double[] ver = new Double[textItems.size()];
                for (int i = 0; i < textItems.size(); i++) {
                    ver[i] = textItems.get(i).getVer();
                }


                resArea.appendText("\nСимволы, их количество в тексте и вероятность:\n");
                for (Items textItem : textItems) {
                    resArea.appendText(textItem.getSym() + " - " +
                            ((int) (textItem.getVer() * counter)) + " - " + textItem.getVer() + "\n");
                }

                // Создание и "опустошение (null -> 0)" массива, который будет хранить код:
                String[] coding = new String[ver.length];
                Arrays.fill(coding, ""); // "опустошение"

                // Создание объекта типа Controller для работы с методами из класса Controller:
                Controller controller = new Controller();

                resArea.appendText("Решение:\n");

                // Вызов методов (подробнее см. в классе Controller):
                controller.codingShenFano(symbols, ver, coding, textItems, resArea, true);
                controller.makeCode(textItems);

                // Вывод результата на экран и в консоль:
                resArea.setText(resArea.getText() + "\n\nСимвол          Вероятность          Кодирование\n");
                for (Items item : textItems) {
                    System.out.format("%1s %-15s %-15s %n", item.getSym(), item.getVer(), item.getCode());
                    resArea.setText(resArea.getText() + item.toString().trim() + "\n");
                }

                System.out.println("...............................................................................\n");

            } else textArea.setText("Введите данные в строку.");

        });

        // Переход на новую сцену "Кодирование с помощью метода Шеннона-Фано":
        shenFanoButton.setOnMouseClicked(mouseEvent -> {
            try {
                Parent textControllerParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainWindow.fxml")));
                Scene textControllerScene = new Scene(textControllerParent);
                Stage appStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                appStage.hide();
                appStage.setScene(textControllerScene);
                appStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    // Метод подсчёта символов в списке:
    private void countingSym(List<PreItems> textItems) {
        for (int i = 0; i < textItems.size(); i++) {
            for (int j = 0; j < textItems.size(); j++) {
                if (textItems.get(i).getSym().equals(textItems.get(j).getSym()) && i < j) {
                    textItems.get(i).setVer(textItems.get(i).getVer() + 1);
                    textItems.remove(j);
                }
            }
        }

        // Проверка: если после выполнения есть символы с одинаковым названием, то выполняем метод ещё раз:
        for (int i = 0; i < textItems.size(); i++) {
            for (int j = 0; j < textItems.size(); j++) {
                if (textItems.get(i).getSym().equals(textItems.get(j).getSym()) && i < j) countingSym(textItems);
            }
        }
    }

}
