package org.otabek.dao;

import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class JDBCTablewareDAO implements ITablewareDAO {
    private String url;
    private String user;
    private String password;

    public JDBCTablewareDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Tableware createItem(Tableware tableware) throws DaoException {
        String sql = "INSERT INTO tableware(name, width, color, price, type, category, radius, style) VALUES(?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tableware.getName());
            ps.setFloat(2, tableware.getWidth());
            ps.setString(3, tableware.getColor());
            ps.setFloat(4, tableware.getPrice());

            if (tableware instanceof Cup) {
                ps.setString(5, "Cup");
                ps.setString(6, ((Cup) tableware).getCategory());
                ps.setNull(7, Types.FLOAT);
                ps.setNull(8, Types.VARCHAR);
            } else if (tableware instanceof Plate) {
                ps.setString(5, "Plate");
                ps.setNull(6, Types.VARCHAR);
                ps.setFloat(7, ((Plate) tableware).getRadius());
                ps.setNull(8, Types.VARCHAR);
            } else if (tableware instanceof Teapot) {
                ps.setString(5, "Teapot");
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.FLOAT);
                ps.setString(8, ((Teapot) tableware).getStyle());
            } else {
                throw new DaoException("Unknown type of tableware provided for creation.");
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tableware.setId(rs.getInt(1));
                }
            }

            return tableware;
        } catch (SQLException e) {
            throw new DaoException("Error creating tableware", e);
        }
    }

    @Override
    public Tableware findByID(int id) throws DaoException {
        String sql = "SELECT * FROM tableware WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTableware(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding tableware with id " + id, e);
        }
    }

    @Override
    public List<Tableware> findAll() throws DaoException {
        String sql = "SELECT * FROM tableware";
        List<Tableware> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Tableware item = mapResultSetToTableware(rs);
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException("Error finding all tableware", e);
        }
    }

    @Override
    public Tableware updateItem(String column, String value, int id) throws DaoException {
        if (!isValidColumn(column)) {
            throw new DaoException("Unknown column: " + column);
        }

        String sql = "UPDATE tableware SET " + column + " = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (isStringColumn(column)) {
                ps.setString(1, value);
            } else {
                float floatValue;
                try {
                    floatValue = Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    throw new DaoException("Invalid numeric value for column " + column + ": " + value, e);
                }
                ps.setFloat(1, floatValue);
            }

            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return findByID(id);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Error updating tableware with id " + id, e);
        }
    }

    @Override
    public boolean delete(int id) throws DaoException {
        String sql = "DELETE FROM tableware WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error deleting tableware with id " + id, e);
        }
    }

    private Tableware mapResultSetToTableware(ResultSet rs) throws DaoException {
        try {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            float width = rs.getFloat("width");
            String color = rs.getString("color");
            float price = rs.getFloat("price");
            String type = rs.getString("type");
            String category = rs.getString("category");
            float radius = rs.getFloat("radius");
            String style = rs.getString("style");

            if (type == null) {
                throw new DaoException("Type column is null for tableware id " + id);
            }

            return switch (type.toUpperCase()) {
                case "CUP" -> new Cup(id, name, width, color, price, category);
                case "PLATE" -> new Plate(id, name, width, color, price, radius);
                case "TEAPOT" -> new Teapot(id, name, width, color, price, style);
                default ->
                        throw new DaoException("Unknown type of tableware retrieved from DB: " + type + " for id " + id);
            };
        } catch (SQLException e) {
            throw new DaoException("Error mapping result set to Tableware", e);
        }
    }

    private boolean isValidColumn(String column) {
        String[] stringColumns = {"name", "color", "type", "category", "style"};
        String[] floatColumns = {"width", "price", "radius"};

        for (String c : stringColumns) {
            if (c.equalsIgnoreCase(column)) return true;
        }
        for (String c : floatColumns) {
            if (c.equalsIgnoreCase(column)) return true;
        }
        return false;
    }

    private boolean isStringColumn(String column) {
        String[] stringColumns = {"name", "color", "type", "category", "style"};
        for (String c : stringColumns) {
            if (c.equalsIgnoreCase(column)) return true;
        }
        return false;
    }
}
