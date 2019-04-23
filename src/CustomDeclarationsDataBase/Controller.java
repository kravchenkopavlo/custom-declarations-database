package CustomDeclarationsDataBase;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final URL startLink = getClass().getResource("start.html"); // ссылка на текст, который открывается при запуске программы
    private WebEngine engine;
    private String filename;
    private final static String SETTINGS_FILE = "settings.dat";
    private FilterSet filterSet;

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

    filterSet = new FilterSet();

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

    //добавляем listener на выбиралки дат


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
        filterSet.clear();
        added_filters.getItems().clear();
    }

    @FXML
    private void removeFilter() {
        int index = added_filters.getSelectionModel().getSelectedIndex();
        if (index >=0 ) {
            filterSet.removeFilter(index);
            added_filters.getItems().remove(index);
        }
    }

    @FXML
    private void filterAdd() {
        String s = "";
        switch (select_field.getValue()) {
            case "Экспортёр (отправитель)":
                s = "td.exporter_name ";
                break;
            case "Код страны отправления":
                s = "td.country ";
                break;
            case "Импортёр (получатель)":
                s = "td.importer_name ";
                break;
            case "Код ЄДРПОУ импортёра":
                s = "td.importer_code ";
                break;
            case "Код УКТВЕД товара":
                s = "td.code ";
                break;
            case "Описание товара":
                s = "td.description ";
                break;
            case "Вес товара в кг":
                s = "td.weight ";
                break;
            case "Стоимость товара в USD":
                s = "td.cost ";
                break;
        }

        Object v = new Object();
        switch (filter_method.getValue()) {
            case "содержит":
                s = s+"LIKE ?";
                v = "%"+filter_value.getText()+"%";
                break;
            case "не содержит":
                s = s+"NOT LIKE ?";
                v = "%"+filter_value.getText()+"%";
                break;
            case "начинается с":
                s = s+"LIKE ?";
                v = filter_value.getText()+"%";
                break;
            case "равен":
                s = s+"= ?";
                v = filter_value.getText();
                break;
            case ">":
                s = s+"> ?";
                v = Double.parseDouble(filter_value.getText());
                break;
            case ">=":
                s = s+">= ?";
                v = Double.parseDouble(filter_value.getText());
                break;
            case "<":
                s = s+"< ?";
                v = Double.parseDouble(filter_value.getText());
                break;
            case "<=":
                s = s+"<= "+filter_value.getText()+" ";
                v = Double.parseDouble(filter_value.getText());
                break;
            case "=":
                s = s+"= ?";
                v = Double.parseDouble(filter_value.getText());
                break;
        }

        filterSet.addFilter(s,v);

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

        // устанавливаем
        filterSet.setDatefrom(datefrom.getValue());
        filterSet.setDateto(datefrom.getValue());

        // в зависимости от типа данных в строках отчёта - оформляем отчёт
        if (rb_section.isSelected()) {
            List<DataItem> sections = dataSource.getSections(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по разделам УКТВЕД за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(sections);
        }

        if (rb_cntr.isSelected()) {
            List<DataItem> countries = dataSource.getCountries(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по странам отправления за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(countries);
        }

        if (rb_imp.isSelected()) {
            List<DataItem> importers = dataSource.getImporters(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по импортёрам (отправителям) за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(importers);

        }

        if (rb_declaration.isSelected()) {
            List<DataItem> declarations = dataSource.getDeclarations(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по таможенным декларациям за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(declarations);
        }

        if (rb_gds.isSelected()) {
            List<DataItem> products = dataSource.getProducts(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по кодам УКТВЕД за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(products);
        }

        if (rb_exp.isSelected()) {
            List<DataItem> exporters = dataSource.getExporters(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по экспортёрам (отправителям) за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(exporters);
        }

        if (rb_group.isSelected()) {
            List<DataItem> groups = dataSource.getGroups(filterSet);
            report.setTitle("Анализ импорта в Украину товаров по группам УКТВЕД за период с: "+datefrom.getValue().toString()+" по: "+dateto.getValue().toString());
            report.setRows(groups);
        }

        // создаём HTML-файл
        report.make();
        filename = report.getFilename();

        showReport();
        dataSource.close();
    }

    private void showReport() {
        //отображаем получившуюся таблицу

        if ((filename == null) || filename.equals(startLink.toString())){
            engine.load(startLink.toString());
            saveMenu.setDisable(false);
            printMenu.setDisable(false);

        } else {
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
        String filters = "<small>";
        for (String s:added_filters.getItems()) {
            filters += s+"; ";
        }
        filters += "</small>";
        return filters;
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
        Preferences preferences = new Preferences(filterSet,added_filters.getItems(), datefrom.getValue(),dateto.getValue(),
                rb_imp.isSelected(),rb_exp.isSelected(),rb_cntr.isSelected(),rb_section.isSelected(),
                rb_group.isSelected(),rb_gds.isSelected(),rb_declaration.isSelected());
        if (filename == null) {
            preferences.setOpenPage(startLink.toString());
        } else {
            preferences.setOpenPage(filename);
        }
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(settingsFile))) {
            ous.writeObject(preferences);
        } catch (Exception e) {
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
            Preferences preferences = new Preferences();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile))) {
                preferences = (Preferences) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            datefrom.setValue(preferences.getDatefrom());
            dateto.setValue(preferences.getDateto());
            rb_imp.setSelected(preferences.isRb_imp());
            rb_exp.setSelected(preferences.isRb_exp());
            rb_cntr.setSelected(preferences.isRb_cntr());
            rb_section.setSelected(preferences.isRb_section());
            rb_group.setSelected(preferences.isRb_group());
            rb_gds.setSelected(preferences.isRb_gds());
            rb_declaration.setSelected(preferences.isRb_declaration());
            filename = preferences.getOpenPage();
            showReport();
            filterSet = preferences.getFilterSet();
            filterSet = preferences.getFilterSet();
            added_filters.getItems().clear();
            added_filters.getItems().addAll(preferences.getFilterTextList());
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
