package CustomDeclarationsDataBase;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class FilterSet implements Serializable{
    private List<String> filterNames;
    private List<Object> filterValues;
    private LocalDate datefrom;
    private LocalDate dateto;

    void setDatefrom(LocalDate datefrom) {
        this.datefrom = datefrom;
    }

    void setDateto(LocalDate dateto) {
        this.dateto = dateto;
    }

    FilterSet() {
        filterNames = new ArrayList<>();
        filterValues = new ArrayList<>();
    }

    void addFilter(String name, Object value) {
        if (!name.isEmpty()) {
            filterNames.add(name);
            filterValues.add(value);
        }
    }

    void removeFilter(int index) {
        filterNames.remove(index);
        filterValues.remove(index);
    }

    String getFilters() {
        String filters = "WHERE date >= "+datefrom.toEpochDay()+
                " AND date <= "+dateto.toEpochDay();

        for (String filterName:filterNames) {
            filters += " AND " + filterName;
        }
        return filters;
    }

    List<Object> getFilterValues() {
        return new ArrayList<>(this.filterValues);
    }

    void clear() {
        this.filterNames.clear();
        this.filterValues.clear();
    }
}


