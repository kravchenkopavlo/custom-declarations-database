package CustomDeclarationsDataBase;

public class Product implements DataItem{
    private String code;
    private String description;
    private double weight;
    private double cost;

    private static final String HTMLCAPTIONS = "<td>Код по УКТВЕД</td><td>Раздел справочника УКТВЕД</td>";
    private static final String HTMLRESULTS = "<tr><td></td><td></td><td>Всего по %d кодам УКТВЕД</td>";


    void setCode(String code) {
        this.code = code;
    }

    void setDescription(String description) {
        this.description = description;
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
        return "<td>"+this.code+"</td><td>"+this.description+"</td>";
    }

    public String getHTMLResults(){
        return this.HTMLRESULTS;
    }
}
