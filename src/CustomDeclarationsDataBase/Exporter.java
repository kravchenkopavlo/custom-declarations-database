package CustomDeclarationsDataBase;

public class Exporter implements DataItem{
    private String name;
    private String country;
    private double weight;
    private double cost;

    private static final String HTMLCAPTIONS = "<td>Наименование экспортёра (отправителя)</td><td>Страна отправления</td>";
    private static final String HTMLRESULTS = "<tr><td></td><td>Всего по %d экспортёрам:</td><td></td>";

    public Exporter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
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
