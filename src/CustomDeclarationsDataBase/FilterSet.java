package CustomDeclarationsDataBase;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilterSet implements Serializable{
    private List<String> filterNames;
    private List<Object> filterValues;
    private LocalDate datefrom;
    private LocalDate dateto;

    public void setDatefrom(LocalDate datefrom) {
        this.datefrom = datefrom;
    }

    public void setDateto(LocalDate dateto) {
        this.dateto = dateto;
    }

    public FilterSet() {
        filterNames = new ArrayList<>();
        filterValues = new ArrayList<>();
    }

    public boolean addFilter(String name, Object value) {
        if (name.isEmpty()) return false;
        filterNames.add(name);
        filterValues.add(value);
        return true;
    }

    public void removeFilter(int index) {
        filterNames.remove(index);
        filterValues.remove(index);
    }

    public String getFilters() {
        String filters = "WHERE date >= "+datefrom.toEpochDay()+
                " AND date <= "+dateto.toEpochDay();

        for (String filterName:filterNames) {
            filters = filters + " AND " + filterName;
        }
        return filters;
    }

    public List<Object> getFilterValues() {
        return new ArrayList<>(this.filterValues);
    }

    public void clear() {
        this.filterNames.clear();
        this.filterValues.clear();
    }
}


