package CustomDeclarationsDataBase;

public class Country implements DataItem{
    private String code;
    private String name;
    private double weight;
    private double cost;

    private static final String HTMLCAPTIONS = "<td></td><td>Страна отправления</td>";
    private static final String HTMLRESULTS = "<tr><td></td><td></td><td>Всего по %d странам отправления:</td>";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "<td>"+this.code+"</td><td>"+this.name+"</td>";
    }

    public String getHTMLResults(){
        return this.HTMLRESULTS;
    }

}
