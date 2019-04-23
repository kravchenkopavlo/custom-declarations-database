package CustomDeclarationsDataBase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class Report {
    private String title;
    private String filename;
    private String filters;
    private List<DataItem> rows;
    private static final String HTMLhead = "<!DOCTYPE html><html lang=\"ru\"><head><meta charset=\"UTF-8\"><title>%s</title></head>";
    private static final String HTMLtitle ="<body><font face=\"Arial\"><h3><center>%s</center></h3>";
    private static final String HTMLcaption = "<table border=\"1\" cellspacing=\"0\"><tr><td>№</td>%s<td align=\"center\">Вес,<br>тонн</td><td align=\"center\">Сумма,<br>тыс.USD</td><td>Доля<br>по весу</td></tr>";
    private static final String NOTHING_MESSAGE = "<h2>С указанными критериями в базе данных не найдено ни одной таможенной декларации.</h2>";
    private static final String HTMLresultsCaption = "<td align=\"right\">%,.0f</td><td align=\"right\">%,.0f</td><td align=\"right\">100%%</td></tr>";
    private static final String HTMLcounter = "<td>%d</td>";
    private static final String HTMLrow = "<td align=\"right\">%,.0f</td><td align=\"right\">%,.0f</td><td align=\"right\">%,.2f%%</td></tr>";
    private static final String HTMLtail = "</table></font></body></html>";

    Report() {
        this.filename = LocalDate.now().toString()+"-"+String.format("%d", LocalTime.now().getHour())+"-"+String.format("%d",LocalTime.now().getMinute())+".html";
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getFilename() {
        return this.filename;
    }

    void setRows(List<DataItem> list) {
        this.rows = new ArrayList<>(list);
    }

    void setFiltersText(String filters) {
        this.filters = filters;
    }

    void make(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename))) {
            bw.write(String.format(HTMLhead,title));
            bw.write(String.format(HTMLtitle,title));
            bw.write(this.filters);


            // если выборка пуста
                if (rows.size()==0) {
                bw.write(NOTHING_MESSAGE);
            } else {
                bw.write(String.format(HTMLcaption, rows.get(0).getHTMLCaptions()));
                bw.write(String.format(rows.get(0).getHTMLResults(), rows.size()));

                // считаем строчку с итогами:

                double weightsum = 0;
                double costsum = 0;
                for (DataItem row : rows) {
                    weightsum += row.getWeight();
                    costsum += row.getCost();
                }

                bw.write(String.format(HTMLresultsCaption, weightsum / 1000.00, costsum / 1000.00));

                int rowCount = 0;

                for (DataItem row : rows) {
                    rowCount++;
                    bw.write(String.format(HTMLcounter, rowCount));
                    bw.write(row.getHTMLrow());
                    bw.write(String.format(HTMLrow,
                            row.getWeight() / 1000.00,
                            row.getCost() / 1000.00,
                            (row.getWeight() / weightsum) * 100));
                }
            }

            bw.write(HTMLtail);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}