package CustomDeclarationsDataBase;

import java.time.LocalDate;

public class Declaration implements DataItem {
    private int date;
    private String exporter_name;
    private String importer_code;
    private String importer_adress;
    private String importer_name;
    private String code;
    private String group_description;
    private String country_name;
    private String description;
    private double weight;
    private double cost;

    private static final String HTMLCAPTIONS = "<td>Дата</td><td>Страна отправления</td><td>Экспортёр (отправитель)</td>" +
            "<td>Код импортёра</td><td>Импортёр (получатель)</td><td>Адрес импортёра</td><td>Код УКТВЕД</td>" +
            "<td>Раздел УКТВЕД</td><td>Описание таможенной декларации</td>";
    private static final String HTMLRESULTS = "<tr><td></td><td></td><td></td><td>Всего по %s деклараций:</td><td></td><td></td><td></td><td></td><td></td><td></td>";

    void setDate(int date) {
        this.date = date;
    }

    void setExporter_name(String exporter_name) {
        this.exporter_name = exporter_name;
    }

    void setImporter_code(String importer_code) {
        this.importer_code = importer_code;
    }

    void setImporter_adress(String importer_adress) {
        this.importer_adress = importer_adress;
    }

    void setImporter_name(String importer_name) {
        this.importer_name = importer_name;
    }

    void setCode(String code) {
        this.code = code;
    }

    void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getCost() {
        return cost;
    }

    void setCost(double cost) {
        this.cost = cost;
    }

    public String getHTMLCaptions() {
        return this.HTMLCAPTIONS;
    }

    public String getHTMLrow(){
        return "<td>"+ LocalDate.ofEpochDay(this.date).toString()+"</td><td>"+this.country_name+"</td>" +
                "<td>"+this.exporter_name+"</td><td>"+this.importer_code+"</td>" +
                "<td>"+this.importer_name+"</td><td>"+this.importer_adress+"</td>" +
                "<td>"+this.code+"</td><td>"+this.group_description+"</td>" +
                "<td>"+this.description+"</td>";
    }

    public String getHTMLResults(){
        return this.HTMLRESULTS;
    }
}
