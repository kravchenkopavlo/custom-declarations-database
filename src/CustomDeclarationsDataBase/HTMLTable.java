package CustomDeclarationsDataBase;

import java.util.List;

public class HTMLTable {
    private String name;
    private List<List<Object>> columns;

    public HTMLTable(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addColumn(List<Object> column) {
        columns.add(column);
    }

    public void makeTable() {

    }
}
