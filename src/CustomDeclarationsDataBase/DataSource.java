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

    public List<DataItem> getGroups(String filters) {
        System.out.println(String.format(GROUPS_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(GROUPS_QUERY,filters))) {

            List<DataItem> groups = new ArrayList<>();
            while (results.next()) {
                Group group = new Group();
                group.setCode(results.getString("group_code"));
                group.setDescription(results.getString("description"));
                group.setWeight(results.getDouble("sum(weight)"));
                group.setCost(results.getDouble("sum(cost)"));
                groups.add(group);
            }

            return groups;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<DataItem> getImporters(String filters) {
        System.out.println(String.format(IMPORTERS_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(IMPORTERS_QUERY,filters))) {

            List<DataItem> importers = new ArrayList<>();
            while (results.next()) {
                Importer importer = new Importer();
                importer.setName(results.getString("importer_name"));
                importer.setCode(results.getString("importer_code"));
                importer.setAdress(results.getString("importer_adress"));
                importer.setWeight(results.getDouble("sum(weight)"));
                importer.setCost(results.getDouble("sum(cost)"));
                importers.add(importer);
            }

            return importers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<DataItem> getProducts(String filters) {
        System.out.println(String.format(PRODUCTS_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(PRODUCTS_QUERY,filters))) {

            List<DataItem> products = new ArrayList<>();
            while (results.next()) {
                Product product = new Product();
                product.setCode(results.getString("code"));
                product.setDescription(results.getString("description"));
                product.setWeight(results.getDouble("sum(weight)"));
                product.setCost(results.getDouble("sum(cost)"));
                products.add(product);
            }

            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<DataItem> getDeclarations(String filters) {
        System.out.println(String.format(DECLARATIONS_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(DECLARATIONS_QUERY,filters))) {

            List<DataItem> declarations = new ArrayList<>();
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

            return declarations;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<DataItem> getExporters(String filters) {
        System.out.println(String.format(EXPORTERS_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(EXPORTERS_QUERY,filters))) {

            List<DataItem> exporters = new ArrayList<>();
            while (results.next()) {
                Exporter exporter = new Exporter();
                exporter.setName(results.getString("exporter_name"));
                exporter.setCountry(results.getString("country_name"));
                exporter.setWeight(results.getDouble("sum(weight)"));
                exporter.setCost(results.getDouble("sum(cost)"));
                exporters.add(exporter);
            }

            return exporters;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public List<DataItem> getSections(String filters) {
        System.out.println(String.format(SECTIONS_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(SECTIONS_QUERY,filters))) {

            List<DataItem> sections = new ArrayList<>();
            while (results.next()) {
                Section section = new Section();
                section.setCode(results.getString("code"));
                section.setDescription(results.getString("description"));
                section.setWeight(results.getDouble("sum(weight)"));
                section.setCost(results.getDouble("sum(cost)"));
                sections.add(section);
            }

            return sections;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public List<DataItem> getCountries(String filters) {
        System.out.println(String.format(COUNTRIES_QUERY,filters));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(String.format(COUNTRIES_QUERY,filters))) {

            List<DataItem> countries = new ArrayList<>();
            while (results.next()) {
                Country country = new Country();
                country.setCode(results.getString("country"));
                country.setName(results.getString("country_name"));
                country.setWeight(results.getDouble("sum(weight)"));
                country.setCost(results.getDouble("sum(cost)"));
                countries.add(country);
            }

            return countries;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}
