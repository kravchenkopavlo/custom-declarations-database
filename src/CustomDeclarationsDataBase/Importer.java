package CustomDeclarationsDataBase;

public class Importer implements DataItem{
    private String name;
    private String code;
    private String adress;
    private double weight;
    private double cost;

    private static final String HTMLCAPTIONS = "<td>Код</td><td>Наименование импортёра (получателя)</td><td>Юридический адрес импортёра</td>";
    private static final String HTMLRESULTS = "<tr><td></td><td></td><td>Всего по %d импортёрам:</td><td></td>";

    void setName(String name) {
        this.name = name;
    }

    void setCode(String code) {
        this.code = code;
    }

    void setAdress(String adress) {
        this.adress = adress;
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
        return "<td>"+this.code+"</td><td>"+this.name+"</td><td>"+this.adress+"</td>";
    }

    public String getHTMLResults(){
        return this.HTMLRESULTS;
    }
}
