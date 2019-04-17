package CustomDeclarationsDataBase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final URL startLink = getClass().getResource("start.html"); // ссылка на текст, который открывается при запуске программы
    private WebEngine engine;
    private String filename;
    private List<String> filters = new ArrayList<>();
    private final static String SETTINGS_FILE = "settings.dat";

    @FXML
    WebView mywebview; // окно вывода всех отчётов

    @FXML
    DatePicker datefrom; // дата начала отбора

    @FXML
    DatePicker dateto; // дата конца отбора

    @FXML
    RadioButton rb_imp; // в строках - импортёры

    @FXML
    RadioButton rb_exp; // в строках - экспортёры

    @FXML
    RadioButton rb_cntr; // в строках - страны

    @FXML
    RadioButton rb_gds; // в строках - товарные группы

    @FXML
    RadioButton rb_section; // в строках - разделы УКТЗЕД

    @FXML
    RadioButton rb_group; // в строках - группы УКТЗЕД

    @FXML
    RadioButton rb_declaration; // в строках - декларации

    @FXML
    Button btn_makeTable; //кнопка для формирования отчёта

    @FXML
    Label status; // текст в строке состояния

    @FXML
    ProgressBar progress; // прогресс-бар в строке состояния

    @FXML
    ChoiceBox<String> select_field; //выбор по какому полю добавлять фильтр

    @FXML
    ChoiceBox<String> filter_method; //выбор типа сравнения для фильтра

    @FXML
    TextField filter_value; // значение для фильтра

    @FXML
    Button add_filter; //кнопка добавления фильтра

    @FXML
    ListView<String> added_filters; //добавленные фильтры

    @FXML
    Button remove_filter; // удаление выбранного фильтра

    @FXML
    Button clear_filters; // кнопка очистки всех фильтров

    @FXML
    MenuItem saveMenu;

    @FXML
    MenuItem printMenu;

public void initialize() {
    engine = mywebview.getEngine();

    // отключаем пункты меню Сохранить и Печать - до первого сформированного отчёта
    saveMenu.setDisable(true);
    printMenu.setDisable(true);

    // объединяем радиобатоны в тоглгруп
    ToggleGroup toggleGroup = new ToggleGroup();
    rb_cntr.setToggleGroup(toggleGroup);
    rb_exp.setToggleGroup(toggleGroup);
    rb_imp.setToggleGroup(toggleGroup);
    rb_gds.setToggleGroup(toggleGroup);
    rb_section.setToggleGroup(toggleGroup);
    rb_group.setToggleGroup(toggleGroup);
    rb_declaration.setToggleGroup(toggleGroup);

// устанавливаем
    select_field.getItems().add("Экспортёр (отправитель)");
    select_field.getItems().add("Код страны отправления");
    select_field.getItems().add("Импортёр (получатель)");
    select_field.getItems().add("Код ЄДРПОУ импортёра");
    select_field.getItems().add("Код УКТВЕД товара");
    select_field.getItems().add("Описание товара");
    select_field.getItems().add("Вес товара в кг");
    select_field.getItems().add("Стоимость товара в USD");
    select_field.setValue("Код УКТВЕД товара");

//    setDefaults();
    loadSettings(SETTINGS_FILE);
}

    @FXML
    private void saveResults(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл отчёта как...");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Файлы HTML (*.htm)","*.html");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File fileTo = fileChooser.showSaveDialog(mywebview.getScene().getWindow());
        File fileFrom = new File(filename);
        if (fileTo != null) {
            try {
                Files.copy(fileFrom.toPath(), fileTo.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @FXML
    private void clearFilters() {
        filters.clear();
        added_filters.getItems().clear();
    }

    @FXML
    private void removeFilter() {
        int index = added_filters.getSelectionModel().getSelectedIndex();
        if (index >=0 ) {
            if (index==0 && added_filters.getItems().size()>1) {
                filters.set(1,filters.get(1).replace("AND","WHERE"));
            }

            filters.remove(index);
            added_filters.getItems().remove(index);

            for (String filter:filters) {
                System.out.println(filter);
            }
        }
    }

    @FXML
    private void filterAdd() {
        String s = "";

        if (added_filters.getItems().size() > 0) {
            s = "AND ";
        } else {
            s = "WHERE ";
        }

        if (select_field.getValue().equals("Экспортёр (отправитель)")) {
            s = s + "td.exporter_name ";
            filter_value.setText(filter_value.getText().replaceAll("[^a-zа-яёіїA-ZА-ЯЁІЇ\\d]",""));
        }
        if (select_field.getValue().equals("Код страны отправления")) {
            s = s + "td.country ";
            filter_value.setText(filter_value.getText().replaceAll("[^a-zа-яёіїA-ZА-ЯЁІЇ\\d]",""));
        }
        if (select_field.getValue().equals("Импортёр (получатель)")) {
            s = s + "td.importer_name ";
            filter_value.setText(filter_value.getText().replaceAll("[^a-zа-яёіїA-ZА-ЯЁІЇ\\d]",""));
        }
        if (select_field.getValue().equals("Код ЄДРПОУ импортёра")) {
            s = s + "td.importer_code ";
            filter_value.setText(filter_value.getText().replaceAll("\\D",""));
        }
        if (select_field.getValue().equals("Код УКТВЕД товара")) {
            s = s + "td.code ";
            filter_value.setText(filter_value.getText().replaceAll("\\D",""));
        }
        if (select_field.getValue().equals("Описание товара")) {
            s = s + "td.description ";
            filter_value.setText(filter_value.getText().replaceAll("[^a-zа-яёіїA-ZА-ЯЁІЇ\\d]",""));
        }
        if (select_field.getValue().equals("Вес товара в кг")) {
            s = s + "td.weight ";
            filter_value.setText(filter_value.getText().replaceAll("\\D",""));
        }
        if (select_field.getValue().equals("Стоимость товара в USD")) {
            s = s + "td.cost ";
            filter_value.setText(filter_value.getText().replaceAll("\\D",""));
        }

        if (filter_method.getValue().equals("содержит")) {s = s+"LIKE \"%"+filter_value.getText()+"%\" ";}
        if (filter_method.getValue().equals("не содержит")) {s = s+"NOT LIKE \"%"+filter_value.getText()+"%\" ";}
        if (filter_method.getValue().equals("начинается с")) {s = s+"LIKE \""+filter_value.getText()+"%\" ";}
        if (filter_method.getValue().equals("равен")) {s = s+"= \""+filter_value.getText()+"\"";}
        if (filter_method.getValue().equals(">")) {s = s+"> "+filter_value.getText()+" ";}
        if (filter_method.getValue().equals(">=")) {s = s+">= "+filter_value.getText()+" ";}
        if (filter_method.getValue().equals("<")) {s = s+"< "+filter_value.getText()+" ";}
        if (filter_method.getValue().equals("<=")) {s = s+"<= "+filter_value.getText()+" ";}
        if (filter_method.getValue().equals("=")) {s = s+"= "+filter_value.getText()+" ";}

        filters.add(s);
        added_filters.getItems().add(select_field.getValue()+" "+
                filter_method.getValue()+" \""+filter_value.getText()+"\"");
        filter_value.setText("");
    }

    @FXML
    private void filterFieldChange() {

        if (select_field.getValue().equals("Экспортёр (отправитель)") ||
                select_field.getValue().equals("Код страны отправления") ||
                select_field.getValue().equals("Импортёр (получатель)") ||
                select_field.getValue().equals("Код ЄДРПОУ импортёра") ||
                select_field.getValue().equals("Описание товара")) {

            filter_method.getItems().clear();
            filter_method.getItems().add("содержит");
            filter_method.getItems().add("не содержит");
            filter_method.setValue("содержит");
        }

        if (select_field.getValue().equals("Код УКТВЕД товара")) {
            filter_method.getItems().clear();
            filter_method.getItems().add("начинается с");
            filter_method.getItems().add("равен");
            filter_method.setValue("начинается с");
        }

        if (select_field.getValue().equals("Вес товара в кг") ||
                select_field.getValue().equals("Стоимость товара в USD")) {
            filter_method.getItems().clear();
            filter_method.getItems().add(">");
            filter_method.getItems().add(">=");
            filter_method.getItems().add("<");
            filter_method.getItems().add("<=");
            filter_method.getItems().add("=");
            filter_method.setValue(">=");
        }
    }

    @FXML
    private void getResults(){

        // подключаемся к базе данных
        DataSource dataSource = new DataSource();
        dataSource.open();

        // создаём новый объект отчёта
        Report report = new Report();

        // передаём созданному отчёту текст установленных фильтров
        report.setFiltersText(getHTMLFilters());

        // в зависимости от типа данных в строках отчёта - оформляем отчёт
        if (rb_section.isSelected()) {
            List<DataItem> sections = dataSource.getSections(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по разделам УКТВЕД за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(sections);
        }

        if (rb_cntr.isSelected()) {
            List<DataItem> countries = dataSource.getCountries(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по странам отправления за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(countries);
        }

        if (rb_imp.isSelected()) {
            List<DataItem> importers = dataSource.getImporters(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по импортёрам (отправителям) за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(importers);

        }

        if (rb_declaration.isSelected()) {
            List<DataItem> declarations = dataSource.getDeclarations(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по таможенным декларациям за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(declarations);
        }

        if (rb_gds.isSelected()) {
            List<DataItem> products = dataSource.getProducts(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по кодам УКТВЕД за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(products);
        }

        if (rb_exp.isSelected()) {
            List<DataItem> exporters = dataSource.getExporters(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по экспортёрам (отправителям) за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(exporters);
        }

        if (rb_group.isSelected()) {
            List<DataItem> groups = dataSource.getGroups(getSQLFilters());
            report.setTitle("Анализ импорта в Украину товаров по группам УКТВЕД за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(groups);
        }

        // создаём HTML-файл
        report.make();
        filename = report.getFilename();

        showReport();
    }

    private void showReport() {
        //отображаем получившуюся таблицу
        if (!filename.equals("")){
            saveMenu.setDisable(false);
            printMenu.setDisable(false);
            try {
                engine.load(new File(filename).toURI().toURL().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void setDefaults() { // процедура для установки всех частей интерфейса по-умолчанию

        // устанавливаем умолчание для группы радиобатонов
        rb_gds.setSelected(true);

        // устанавливаем умолчания для полей выбора даты
        datefrom.setValue(LocalDate.of(2017,1,1));
        dateto.setValue(LocalDate.of(2017,12,31));

        // убираем все фильтры
        clearFilters();

        // отображаем стартовую страницу
        showStartinfo();
    }

    private String getHTMLFilters(){
    // метод для получения строки текста с описанием текущих установленных фильтров в формате HTML
        StringBuilder sb = new StringBuilder();
        sb.append("<small>");
        for (String s:added_filters.getItems()) {
            sb.append(s+"; ");
        }
        sb.append("</small>");
        return sb.toString();
    }

    private String getSQLFilters() {

//        // Этот метод обрабатывает состояние элементов интерфейса, на основании которых формирует
//        // часть SQL-запроса, ответственного за фильтрацию получаемых данных

        StringBuilder stringBuilder = new StringBuilder();
        for (String s:filters) {
            stringBuilder.append(s);
        }

        stringBuilder.append("AND date >= ");
        stringBuilder.append(datefrom.getValue().toEpochDay());
        stringBuilder.append(" AND date <= ");
        stringBuilder.append(dateto.getValue().toEpochDay());

        return stringBuilder.toString();
    }

    @FXML
    private void handleExit() {
        saveSettings(SETTINGS_FILE);
        Platform.exit();
    }

    @FXML
    private void handlePrint() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(mywebview.getScene().getWindow())) {
            mywebview.getEngine().print(printerJob);
            printerJob.endJob();
        }
    }

    private void saveSettings(String settingsFile) {
    // очищаем файл настроек
        try {
            Files.deleteIfExists(new File(settingsFile).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // сохраняем состояние окна в файл
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile))) {
            bw.write((int) datefrom.getValue().toEpochDay());
            bw.write((int) dateto.getValue().toEpochDay());
            if (rb_imp.isSelected()) bw.write("imp");
            if (rb_exp.isSelected()) bw.write("exp");
            if (rb_cntr.isSelected()) bw.write("cntr");
            if (rb_section.isSelected()) bw.write("section");
            if (rb_group.isSelected()) bw.write("group");
            if (rb_gds.isSelected()) bw.write("gds");
            if (rb_declaration.isSelected()) bw.write("declaration");
            bw.newLine();
            if (filename.length()==0) {
                bw.write(startLink.toString());
            } else {
                bw.write(filename);
            }
            bw.newLine();
            for (String filter:filters) {
                bw.write(filter.toString()+"\t"+added_filters.getItems().get(filters.indexOf(filter)));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSettings(String settingsFile) {
        // загружаем настройки окна
        File sFile = new File(settingsFile);
        if (!sFile.exists()) {
            setDefaults();
        }
        else {
            try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))) {
                datefrom.setValue(LocalDate.ofEpochDay(br.read()));
                dateto.setValue(LocalDate.ofEpochDay(br.read()));
                String s = br.readLine();
                if (s.equals("imp")) rb_imp.setSelected(true);
                if (s.equals("exp")) rb_exp.setSelected(true);
                if (s.equals("cntr")) rb_cntr.setSelected(true);
                if (s.equals("section")) rb_section.setSelected(true);
                if (s.equals("group")) rb_group.setSelected(true);
                if (s.equals("gds")) rb_gds.setSelected(true);
                if (s.equals("declaration")) rb_declaration.setSelected(true);
                filename = br.readLine();
                showReport();
                filters.clear();
                added_filters.getItems().clear();
                while ((s = br.readLine()) != null) {
                    filters.add(s.split("\t")[0]);
                    added_filters.getItems().add(s.split("\t")[1]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLoadSettings(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить файл настроек");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Файлы настроек (*.dat)","*.dat");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File settingsFile = fileChooser.showOpenDialog(mywebview.getScene().getWindow());
        if (settingsFile != null) {
            loadSettings(settingsFile.toString());
        }
    }

    @FXML
    private void handleSaveSettings() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл настроек");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Файлы настроек (*.dat)","*.dat");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File settingsFile = fileChooser.showSaveDialog(mywebview.getScene().getWindow());
        if (settingsFile != null) {
            saveSettings(settingsFile.toString());
        }
    }

    @FXML
    private void showStartinfo() {
        engine.load(startLink.toString());
    }

    @FXML
    private void checkUpdates() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Проверка обновлений");
        alert.setHeaderText("Обновление невозможно.");
        alert.setContentText("В тестовой версии программы обновления отключены.");
        alert.showAndWait();
    }

    @FXML
    private void handleOpenHomepage() {
        engine.load("https://tamognja.com.ua/");
    }
}