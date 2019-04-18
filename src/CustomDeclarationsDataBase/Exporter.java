package CustomDeclarationsDataBase;

public class Exporter implements DataItem{
    private String name;
    private String country;
    private double weight;
    private double cost;

    private static final String HTMLCAPTIONS = "<td>Наименование экспортёра (отправителя)</td><td>Страна отправления</td>";
    private static final String HTMLRESULTS = "<tr><td></td><td>Всего по %d экспортёрам:</td><td></td>";

    void setName(String name) {
        this.name = name;
    }

    void setCountry(String country) {
        this.country = country;
    }

    public double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

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
        return "<td>"+this.name+"</td><td>"+this.country+"</td>";
    }

    public String getHTMLResults(){
        return this.HTMLRESULTS;
    }
}
