package com.mwgroup.shannonfanocode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Controller {

    @FXML
    private Button answerButton;

    @FXML
    private HBox textCodeButton;

    @FXML
    private Button autoButton;

    @FXML
    private Button fullAnswerButton;

    @FXML
    private Label codeLabel;

    @FXML
    private TextArea resultArea;

    @FXML
    private TextField symField;

    @FXML
    private TextField verField;

    @FXML
    void initialize() {

        // Автоматическое заполнение полей.
        autoButton.setOnAction(actionEvent -> {
            symField.setText("a,b,c,d,e,f,g,h");
            verField.setText("0.25,0.25,0.25,0.125,0.0625,0.03125,0.015625,0.015625");
        });

        // Кнопка выполнения кодирования:
        answerButton.setOnAction(actionEvent -> {

            System.out.println("...............................................................................");

            // Создание переменных:
            String[] symbols = symField.getText().toLowerCase(Locale.ROOT).trim().split(","); // символы
            BigDecimal counter = new BigDecimal(0); // счётчик
            String[] verAsText = verField.getText().trim().split(","); // вероятности в String
            Double[] ver = new Double[verAsText.length]; // вероятности в Double
            List<Items> items = new ArrayList<>() {
            }; // объекты [sym - ver - code]

            // Метод, заполняющий массивы и проверяющий на правильность заполнения:
            getElements(symbols, counter, verAsText, ver);

            List<PreItems> preItems = new ArrayList<>(); // предварительные объекты [sym - ver]
            for (int i = 0; i < symbols.length; i++) {
                preItems.add(new PreItems(symbols[i], ver[i]));
            } // инициализация предварительных объектов

            // Метод: если символы равны, то их вероятности складываются, а объект с повторяющимся символом удаляется:
            sumOfVer(preItems);

            // Сортировка предварительных объектов по убыванию вероятности:
            preItems.sort(Comparator.comparingDouble(PreItems::getVer).reversed());

            // Пересоздание массивов символов и вероятностей:
            symbols = new String[preItems.size()]; // символы
            ver = new Double[preItems.size()]; // вероятности

            // Заполнение массивов преобразованными предварительными объектами:
            for (int i = 0; i < preItems.size(); i++) {
                symbols[i] = preItems.get(i).getSym();
                ver[i] = preItems.get(i).getVer();
            }

            // Создание и "опустошение (null -> 0)" массива, который будет хранить код:
            String[] coding = new String[preItems.size()];
            Arrays.fill(coding, ""); // "опустошение"

            // Проверка: если нет никаких ошибок во вводе, после метода "getElements", то:
            if (codeLabel.getText().trim().isEmpty()) {

                // Метод, выполняющий кодирование:
                codingShenFano(symbols, ver, coding, items, resultArea, false);

                // Метод, создающий код для каждого символа:
                makeCode(items);

                // Вывод результата в textArea и консоль:
                resultArea.setText("Символ     Вероятность     Кодирование\n");
                for (Items item : items) {
//                    System.out.format("%1s %10s %10s %n", item.getSym(), item.getVer(), item.getCode());
                    resultArea.setText(resultArea.getText() + item.toString().trim() + "\n");
                }
                System.out.println("...............................................................................\n");
            }

        });

        // Кнопка вывода полного решения:
        fullAnswerButton.setOnAction(actionEvent -> {

            System.out.println("...............................................................................");

            // Создание переменных:
            String[] symbols = symField.getText().toLowerCase(Locale.ROOT).trim().split(","); // символы
            BigDecimal counter = new BigDecimal(0); // счётчик
            String[] verAsText = verField.getText().trim().split(","); // вероятности в String
            Double[] ver = new Double[verAsText.length]; // вероятности в Double
            List<Items> items = new ArrayList<>() {
            }; // объекты [sym - ver - code]

            // Метод, заполняющий массивы и проверяющий на правильность заполнения:
            getElements(symbols, counter, verAsText, ver);

            List<PreItems> preItems = new ArrayList<>(); // предварительные объекты [sym - ver]
            for (int i = 0; i < symbols.length; i++) {
                preItems.add(new PreItems(symbols[i], ver[i]));
            } // инициализация предварительных объектов

//            System.out.println("\n\nДо: " + Arrays.toString(symbols) + ", " + Arrays.toString(ver));

            // Метод: если символы равны, то их вероятности складываются, а объект с повторяющимся символом удаляется:
            sumOfVer(preItems);

            // Сортировка предварительных объектов по убыванию вероятности:
            preItems.sort(Comparator.comparingDouble(PreItems::getVer).reversed());

            // Пересоздание массивов символов и вероятностей:
            symbols = new String[preItems.size()]; // символы
            ver = new Double[preItems.size()]; // вероятности

            // Заполнение массивов преобразованными предварительными объектами:
            for (int i = 0; i < preItems.size(); i++) {
                symbols[i] = preItems.get(i).getSym();
                ver[i] = preItems.get(i).getVer();
            }

            // Создание и "опустошение (null -> 0)" массива, который будет хранить код:
            String[] coding = new String[preItems.size()];
            Arrays.fill(coding, ""); // "опустошение"

//            System.out.println("После: " + Arrays.toString(symbols) + ", " + Arrays.toString(ver) + "\n");

            // Проверка: если нет никаких варнингов после метода "getElements", то:
            if (codeLabel.getText().trim().isEmpty()) {
                resultArea.setText("Метод Шеннона-Фано заключается в кодировании символов 'алфавита' кодом,\nсостоящим " +
                        "из 0 и 1. Чем реже символ встречается в тексте, тем длиннее\nкомбинация его кодирования.\n" +
                        "\nЗаданные символы и их вероятности:\n");
                for (int i = 0; i < preItems.size(); i++) {
                    resultArea.setText(resultArea.getText() + symbols[i] + " - " + ver[i] + "\n");
                }
                resultArea.setText(resultArea.getText() + "\n");

                // Метод, выполняющий кодирование:
                codingShenFano(symbols, ver, coding, items, resultArea, true);

                // Метод, создающий код для каждого символа:
                makeCode(items);

                resultArea.setText(resultArea.getText() + "\nДалее совмещаем все полученные единицы и нули\n" +
                        "для каждого символа и составляем их кодовое значение:\n" +
                        "\nСимвол     Вероятность     Кодирование\n");
                for (Items item : items) {
                    System.out.format("%1s %10s %10s %n", item.getSym(), item.getVer(), item.getCode());
                    resultArea.setText(resultArea.getText() + item.toString().trim() + "\n");
                }
            }

            System.out.println("...............................................................................\n");

        });

        // Переход на новую сцену "Кодирование символов текса":
        textCodeButton.setOnMouseClicked(mouseEvent -> {
            try {
                Parent textControllerParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("textCode.fxml")));
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

    // Метод, заполняющий массивы и проверяющий на правильность заполнения:
    public void getElements
    (String[] symbols, BigDecimal counter, String[] verAsText, Double[] ver) {
        for (int i = 0; i < verAsText.length; i++) {
            ver[i] = Double.parseDouble(verAsText[i]); // заполнение элементов Double элементами String
            System.out.print(ver[i]);
            if (i + 1 != verAsText.length) System.out.print(" + ");
            counter = counter.add(BigDecimal.valueOf(ver[i]));
        }
        System.out.println(" = " + counter);

        // Проверка: если количество символов и вероятностей не совпадает, то:
        if (symbols.length != verAsText.length) {
            codeLabel.setText("Количество вероятностей и символов различается!");
            codeLabel.setStyle("-fx-background-color: RED");
            resultArea.setText("");

            // Проверка: если сумма вероятностей большь единице или меньше 0.999999, то
        } else if (counter.doubleValue() > 1.0 || counter.doubleValue() < 0.99999) {
            codeLabel.setText("Сумма вероятностей должна быть равна единице!");
            codeLabel.setStyle("-fx-background-color: RED");
            resultArea.setText("");
            // counter.doubleValue() < 0.99999 нужен из-за того, что иногда сумма вероятностей не м.б. равна единице.
            // Например: дан текст: 'aas'. Вероятность а - 0.(6), вероятность s - 0.(3) => сумма вероятностей равна 0.(9).

            // Если никаких ошибок не обнаружено, то:
        } else {
            codeLabel.setText("");
            codeLabel.setStyle("-fx-background-color: none");
        }

    }

    // Метод, собирающий код для каждого символа:
    public void makeCode(List<Items> items) {
        // Цикл, который проверяет список items на наличие одинаковых символов:
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                // Если символы одинаковы и находятся на разных позициях, то:
                if (items.get(i).getSym().equals(items.get(j).getSym()) && i < j) {
                    items.get(i).setCode(items.get(i).getCode() + items.get(j).getCode()); // их коды объединяются
                    items.remove(j); // элемент j, который идёт после i, удаляется.
                }
            }
        }

        // В условии после && можно было бы поставить '!=', но лучше использовать '<', чтобы на всякий случай
        // избежать объединения кода не с той стороны.
        // Такое, возможно, даже и не случится, и можно было бы поставить '!=', но лучше перестраховаться и
        // использовать '<'.

        // В связи с тем, что мы использовали '<', нам придётся совершить рекурсию метода, т.к. без неё
        // иногда объект с одинаковым символом разделяется на несколько объектов из-за того, что список обновляется.

        // Цикл, который проверяет, не осталось ли после предыдущей операции объектов с одинаковым символом:
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                // Если обнаружено два объекта с одинаковым символом, то:
                if (items.get(i).getSym().equals(items.get(j).getSym()) && i < j) makeCode(items);
            }
        }

    }

    // Метод, суммирующий вероятности у символов с одинаковыми объектами
    private void sumOfVer(List<PreItems> preItems) {
        for (int i = 0; i < preItems.size(); i++) {
            for (int j = 0; j < preItems.size(); j++) {
                if (preItems.get(i).getSym().equals(preItems.get(j).getSym()) && i < j) {
                    preItems.get(i).setVer(preItems.get(i).getVer() + preItems.get(j).getVer());
                    preItems.remove(j);
                }
            }
        }

        for (int i = 0; i < preItems.size(); i++) {
            for (int j = 0; j < preItems.size(); j++) {
                // Если обнаружено два объекта с одинаковым символом, то:
                if (preItems.get(i).getSym().equals(preItems.get(j).getSym()) && i < j) sumOfVer(preItems);
            }
        }
    }

    // Кодирующий метод:
    public void codingShenFano
    (String[] symbols, Double[] ver, String[] coding, List<Items> items, TextArea resultArea, boolean b) {

        // Создание переменных:
        // Списки, которые позже будут добавлять символы:
        ArrayList<String> zeroSym = new ArrayList<>(), oneSym = new ArrayList<>(); // с нулём
        ArrayList<Double> zeroVer = new ArrayList<>(), oneVer = new ArrayList<>(); // с единицей

        // Счётчики нулей и единиц:
        int zeroCount = 0, oneCount = 0;

        // Временные переменные:
        BigDecimal[] temp = new BigDecimal[ver.length];
        BigDecimal tempCounter = new BigDecimal(0);

        // Переменные, минимальное значение разности вероятностей и индекс позиции, где эта разность минимальна:
        BigDecimal min = new BigDecimal(2), minIndex = new BigDecimal(0);
        // min принимает 2, т.к. вероятность никогда не будет больше единицы.

        // Переменная и цикл, суммирующий все вероятности:
        BigDecimal sum = new BigDecimal(0);
        for (Double aDouble : ver) {
            sum = sum.add(BigDecimal.valueOf(aDouble));
        }

        // Цикл, который ищет, между какими символами разность вероятностей минимальна:
        for (int i = 0; i < ver.length; i++) {

            // Цикл, считающий вероятности от 0 до i:
            for (int j = 0; j <= i; j++) {
                tempCounter = tempCounter.add(BigDecimal.valueOf(ver[j]));
            }

            // Переменная: | (sum -  tempCounter) - tempCounter |
            temp[i] = (sum.subtract(tempCounter)).subtract(tempCounter).abs();

            // Если min > temp[i], то:
            if (min.compareTo(temp[i]) > 0) {
                min = temp[i];
                minIndex = new BigDecimal(i);
            }

//            System.out.println("tempCounter = " + tempCounter + ", sum = " + sum);
//            System.out.println("temp[" + i + "] = " + temp[i]);
//            System.out.println(min + ".compareTo(" + temp[i] + ") = " + min.compareTo(temp[i]));
//            System.out.println("minIndex = " + minIndex + ", min = " + min + "\n");

            // Счётчик сбрасывается
            tempCounter = new BigDecimal(0);

        }

        // Цикл, который идёт от 0 до minIndex:
        for (int i = 0; i <= minIndex.intValue(); i++) {
            zeroSym.add(symbols[i]); // список нулей добавляет i-й символ
            zeroVer.add(ver[i]); // список вероятностей нулей принимает вероятность i-го символа
            coding[i] += "0"; // код i-го символа принимает "0"
            zeroCount++; // счётчик нулей
        }

        // Цикл, который идёт от minIndex + 1 до конца массива coding:
        for (int i = minIndex.intValue() + 1; i < coding.length; i++) {
            oneSym.add(symbols[i]); // список единиц добавляет i-й символ
            oneVer.add(ver[i]); // список вероятностей единиц принимает вероятность i-го символа
            coding[i] += "1"; // код i-го символа принимает "1"
            oneCount++; // счётчик единиц
        }

        // Заполнение списка объектами:
        for (int i = 0; i < ver.length; i++) {
            items.add(new Items(symbols[i], ver[i], coding[i]));
        }

        // Если нужно полное решение, то выполняется метод fullText:
        if (b) fullText(symbols, zeroVer, oneVer, min, minIndex, coding, resultArea);

//        System.out.println(zeroSym);
//        System.out.println(zeroVer + "\n");
//        System.out.println(oneSym);
//        System.out.println(oneVer + "\n");

        // Если количество нулей больше единицы, то:
        if (zeroCount > 1) {

            // Если нужно полное решение, то:
            if (b)
                resultArea.setText(resultArea.getText() + "\nРаботаем с символами, которые получили 0:\n");

            // Новый объект для сохранения кода (не уверен, что он нужен, но менять не буду)) ):
            String[] codingForNew = new String[zeroVer.toArray().length];
            Arrays.fill(codingForNew, ""); // "опустошение"

            // Рекурсия метода с нулевыми символами:
            codingShenFano(zeroSym.toArray(String[]::new), zeroVer.toArray(Double[]::new), codingForNew, items, resultArea, b);

        }

        // Если количество единиц больше единицы, то:
        if (oneCount > 1) {

            // Если нужно полное решение, то:
            if (b)
                resultArea.setText(resultArea.getText() + "\nРаботаем с символами, которые получили 1:\n");

            // Новый объект для сохранения кода (не уверен, что он нужен, но менять не буду)) ):
            String[] codingForNew = new String[oneVer.toArray().length];
            Arrays.fill(codingForNew, "");

            // Рекурсия метода с единичными символами:
            codingShenFano
                    (oneSym.toArray(String[]::new), oneVer.toArray(Double[]::new), codingForNew, items, resultArea, b);

        }

    }

    // Метод, вызывающийся в том случае, если пользователю нужно подробное решение:
    public void fullText
    (String[] symbols, ArrayList<Double> zeroVer, ArrayList<Double> oneVer, BigDecimal tmpMin,
     BigDecimal tmpMinIndex, String[] coding, TextArea resultArea) {
        resultArea.setText(resultArea.getText() + "Работаем с символами: ");
        for (int i = 0; i < symbols.length; i++) {
            resultArea.setText(resultArea.getText() + symbols[i]);
            if (i + 1 != symbols.length) resultArea.setText(resultArea.getText() + ", ");
        }

        resultArea.setText(resultArea.getText() + "\nИщем место, где разность вероятностей этих символов" +
                " будет минимальна:\nНаходим, что |(");
        for (int i = 0; i < zeroVer.size(); i++) {
            resultArea.setText(resultArea.getText() + zeroVer.get(i));
            if (i + 1 != zeroVer.size()) resultArea.setText(resultArea.getText() + " + ");
        }
        resultArea.setText(resultArea.getText() + ") - (");

        for (int i = 0; i < oneVer.size(); i++) {
            resultArea.setText(resultArea.getText() + oneVer.get(i));
            if (i + 1 != oneVer.size()) resultArea.setText(resultArea.getText() + " + ");
        }
        resultArea.setText(resultArea.getText() + ")| = " + tmpMin + "\n" +
                "Разделим символы в полученном месте (" + (tmpMinIndex.add(new BigDecimal(1))) + ")\n" +
                "Всё, что выше этого символа (включительно) принимают 0, что ниже - 1.\n" +
                "Таким образом, получаем, что\n");
        for (int i = 0; i < symbols.length; i++) {
            resultArea.setText(resultArea.getText() + symbols[i] + " - " + coding[i] + "\n");
        }
    }

}