package CustomDeclarationsDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public final String CONNECTION_STRING = "jdbc:sqlite:c:\\work\\td2017.db";

    private boolean doDynamicQuery;

    public void setDoDynamicQuery(boolean doDynamicQuery) {
        this.doDynamicQuery = doDynamicQuery;
    }

    private static final String SECTIONS_QUERY = "SELECT sections.code, sections.description, sum(weight), sum(cost) FROM td " +
            "JOIN groups ON substr(td.code,1,2) = groups.code " +
            "JOIN sections ON groups.section = sections.code " +
            "%s GROUP BY sections.code " +
            "ORDER BY sum(weight) DESC;";

    private static final String COUNTRIES_QUERY = "SELECT country, countries.country_name, sum(weight), sum(cost) FROM td " +
            "JOIN countries ON td.country = countries.country_code " +
            "%s GROUP BY country " +
            "ORDER BY sum(weight) DESC;";

    private static final String IMPORTERS_QUERY = "SELECT importer_name, importer_code, importer_adress, sum(weight), sum(cost) FROM td " +
            "%s GROUP BY importer_code " +
            "ORDER BY sum(weight) DESC;";

    private static final String PRODUCTS_QUERY = "SELECT td.code, groups.description, sum(weight), sum(cost) FROM td " +
            "JOIN groups ON substr(td.code,1,2) = groups.code " +
            "%s GROUP BY td.code " +
            "ORDER BY sum(weight) DESC;";

    private static final String EXPORTERS_QUERY = "SELECT exporter_name, countries.country_name, sum(weight), sum(cost) FROM td " +
            "JOIN countries ON td.country = countries.country_code " +
            "%s GROUP BY exporter_name " +
            "ORDER BY sum(weight) DESC;";

    private static final String DECLARATIONS_QUERY = "SELECT date, exporter_name, importer_code, importer_adress, importer_name, " +
            "td.code, groups.description, countries.country_name, td.description, weight, cost FROM td " +
            "JOIN countries ON td.country = countries.country_code " +
            "JOIN groups ON substr(td.code,1,2) = groups.code %s;";

    private static final String GROUPS_QUERY = "SELECT substr(td.code,1,2) AS group_code, groups.description, sum(weight), sum(cost) FROM td " +
            "JOIN groups ON substr(td.code,1,2) = groups.code " +
            "%s GROUP BY substr(td.code,1,2) " +
            "ORDER BY sum(weight) DESC;";

    private Connection connection;

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public PreparedStatement getPrepStatement(String query, FilterSet filterSet) throws SQLException {
        System.out.println(String.format(query,filterSet.getFilters()));
        PreparedStatement preparedStatement = connection.prepareStatement(String.format(query,filterSet.getFilters()));
        List<Object> values = new ArrayList<>(filterSet.getFilterValues());
        for(Object object: values) {
            switch (object.getClass().getName()) {
                case "java.lang.Integer":
                    preparedStatement.setInt(values.indexOf(object)+1,(int) object);
                    break;
                case "java.lang.Double":
                    preparedStatement.setDouble(values.indexOf(object)+1,(double) object);
                    break;
                case "java.lang.String":
                    preparedStatement.setString(values.indexOf(object)+1,(String) object);
                    break;
            }
        }
        System.out.println(preparedStatement.toString());
        return preparedStatement;
    }

    public List<DataItem> getSections(FilterSet filterSet) {
        List<DataItem> sections = new ArrayList<>();
        try (ResultSet results = getPrepStatement(SECTIONS_QUERY,filterSet).executeQuery()) {
                while (results.next()) {
                    Section section = new Section();
                    section.setCode(results.getString("code"));
                    section.setDescription(results.getString("description"));
                    section.setWeight(results.getDouble("sum(weight)"));
                    section.setCost(results.getDouble("sum(cost)"));
                    sections.add(section);
                }
            } catch (SQLException e) {
            e.printStackTrace();
        }
        return sections;
    }

    public List<DataItem> getGroups(FilterSet filterSet) {
        List<DataItem> groups = new ArrayList<>();
        try (ResultSet results = getPrepStatement(GROUPS_QUERY,filterSet).executeQuery()) {
            while (results.next()) {
                Group group = new Group();
                group.setCode(results.getString("group_code"));
                group.setDescription(results.getString("description"));
                group.setWeight(results.getDouble("sum(weight)"));
                group.setCost(results.getDouble("sum(cost)"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public List<DataItem> getImporters(FilterSet filterSet) {
        List<DataItem> importers = new ArrayList<>();
        try (ResultSet results = getPrepStatement(IMPORTERS_QUERY,filterSet).executeQuery()) {
            while (results.next()) {
                Importer importer = new Importer();
                importer.setName(results.getString("importer_name"));
                importer.setCode(results.getString("importer_code"));
                importer.setAdress(results.getString("importer_adress"));
                importer.setWeight(results.getDouble("sum(weight)"));
                importer.setCost(results.getDouble("sum(cost)"));
                importers.add(importer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importers;
    }

    public List<DataItem> getProducts(FilterSet filterSet) {
        List<DataItem> products = new ArrayList<>();
        try (ResultSet results = getPrepStatement(PRODUCTS_QUERY,filterSet).executeQuery()) {
            while (results.next()) {
                Product product = new Product();
                product.setCode(results.getString("code"));
                product.setDescription(results.getString("description"));
                product.setWeight(results.getDouble("sum(weight)"));
                product.setCost(results.getDouble("sum(cost)"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<DataItem> getDeclarations(FilterSet filterSet) {
        List<DataItem> declarations = new ArrayList<>();
        try (ResultSet results = getPrepStatement(DECLARATIONS_QUERY,filterSet).executeQuery()) {
            while (results.next()) {
                Declaration declaration = new Declaration();
                declaration.setDate(results.getInt("date"));
                declaration.setExporter_name(results.getString("exporter_name"));
                declaration.setImporter_code(results.getString("importer_code"));
                declaration.setImporter_adress(results.getString("importer_adress"));
                declaration.setImporter_name(results.getString("importer_name"));
                declaration.setCode(results.getString("code"));
                declaration.setDescription(results.getString(9));
                declaration.setCountry_name(results.getString("country_name"));
                declaration.setGroup_description(results.getString(7));
                declaration.setWeight(results.getDouble("weight"));
                declaration.setCost(results.getDouble("cost"));
                declarations.add(declaration);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return declarations;
    }

    public List<DataItem> getExporters(FilterSet filterSet) {
        List<DataItem> exporters = new ArrayList<>();
        try (ResultSet results = getPrepStatement(EXPORTERS_QUERY,filterSet).executeQuery()) {
            while (results.next()) {
                Exporter exporter = new Exporter();
                exporter.setName(results.getString("exporter_name"));
                exporter.setCountry(results.getString("country_name"));
                exporter.setWeight(results.getDouble("sum(weight)"));
                exporter.setCost(results.getDouble("sum(cost)"));
                exporters.add(exporter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exporters;
    }

    public List<DataItem> getCountries(FilterSet filterSet) {
        List<DataItem> countries = new ArrayList<>();
        try (ResultSet results = getPrepStatement(COUNTRIES_QUERY,filterSet).executeQuery()) {
            while (results.next()) {
                Country country = new Country();
                country.setCode(results.getString("country"));
                country.setName(results.getString("country_name"));
                country.setWeight(results.getDouble("sum(weight)"));
                country.setCost(results.getDouble("sum(cost)"));
                countries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }
}
