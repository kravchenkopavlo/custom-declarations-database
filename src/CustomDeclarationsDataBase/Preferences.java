package CustomDeclarationsDataBase;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Preferences implements Serializable {
    private FilterSet filterSet;
    private List<String> filterTextList;
    private LocalDate datefrom;
    private LocalDate dateto;
    private boolean rb_imp;
    private boolean rb_exp;
    private boolean rb_cntr;
    private boolean rb_section;
    private boolean rb_group;
    private boolean rb_gds;
    private boolean rb_declaration;
    private String openPage;

    public Preferences() {
        filterSet = new FilterSet();
    }

    public Preferences(FilterSet filterSet, List<String> filterTextList, LocalDate datefrom, LocalDate dateto, boolean rb_imp, boolean rb_exp, boolean rb_cntr, boolean rb_section, boolean rb_group, boolean rb_gds, boolean rb_declaration) {
        this.filterTextList = new ArrayList<>(filterTextList);
        this.filterSet = filterSet;
        this.datefrom = datefrom;
        this.dateto = dateto;
        this.rb_imp = rb_imp;
        this.rb_exp = rb_exp;
        this.rb_cntr = rb_cntr;
        this.rb_section = rb_section;
        this.rb_group = rb_group;
        this.rb_gds = rb_gds;
        this.rb_declaration = rb_declaration;
    }

    public void setOpenPage(String openPage) {
        this.openPage = openPage;
    }

    public FilterSet getFilterSet() {
        return filterSet;
    }

    public LocalDate getDatefrom() {
        return datefrom;
    }

    public LocalDate getDateto() {
        return dateto;
    }

    public boolean isRb_imp() {
        return rb_imp;
    }

    public boolean isRb_exp() {
        return rb_exp;
    }

    public boolean isRb_cntr() {
        return rb_cntr;
    }

    public boolean isRb_section() {
        return rb_section;
    }

    public boolean isRb_group() {
        return rb_group;
    }

    public boolean isRb_gds() {
        return rb_gds;
    }

    public boolean isRb_declaration() {
        return rb_declaration;
    }

    public String getOpenPage() {
        return openPage;
    }

    public List<String> getFilterTextList() {
        return filterTextList;
    }
}
