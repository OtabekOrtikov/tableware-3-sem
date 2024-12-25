package org.otabek.service;

import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.entity.item.Cup;
import org.otabek.entity.item.Plate;
import org.otabek.entity.item.Teapot;
import org.otabek.exceptions.DaoException;

import java.util.List;

public class TablewareService {
    private final JDBCTablewareDAO tablewareDAO;

    public TablewareService(JDBCTablewareDAO tablewareDAO) {
        this.tablewareDAO = tablewareDAO;
    }

    public Tableware createTableware(Tableware tableware) throws DaoException {
        validateTableware(tableware); // Validate input
        return tablewareDAO.createItem(tableware);
    }

    public Tableware getByID(int id) throws DaoException {
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than zero.");
        return tablewareDAO.findByID(id);
    }

    public List<Tableware> getAll() throws DaoException {
        return tablewareDAO.findAll();
    }

    public Tableware updateTableware(String column, String value, int id) throws DaoException {
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than zero.");
        if (column == null || column.trim().isEmpty()) throw new IllegalArgumentException("Column name cannot be null or empty.");
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Value cannot be null or empty.");
        return tablewareDAO.updateItem(column, value, id);
    }

    public boolean deleteTableware(int id) throws DaoException {
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than zero.");
        return tablewareDAO.delete(id);
    }

    // Validation method for Tableware
    private void validateTableware(Tableware tableware) {
        if (tableware == null) throw new IllegalArgumentException("Tableware cannot be null.");
        if (tableware.getName() == null || tableware.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (tableware.getWidth() <= 0) throw new IllegalArgumentException("Width must be greater than zero.");
        if (tableware.getPrice() <= 0) throw new IllegalArgumentException("Price must be greater than zero.");
        if (tableware.getColor() == null || tableware.getColor().trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty.");
        }

        switch (tableware) {
            case Cup cup -> {
                if (cup.getCategory() == null || cup.getCategory().trim().isEmpty()) {
                    throw new IllegalArgumentException("Category cannot be null or empty for a Cup.");
                }
            }
            case Plate plate -> {
                if (plate.getRadius() <= 0)
                    throw new IllegalArgumentException("Radius must be greater than zero for a Plate.");
            }
            case Teapot teapot -> {
                if (teapot.getStyle() == null || teapot.getStyle().trim().isEmpty()) {
                    throw new IllegalArgumentException("Style cannot be null or empty for a Teapot.");
                }
            }
            default -> throw new IllegalArgumentException("Unknown type of Tableware.");
        }
    }
}
