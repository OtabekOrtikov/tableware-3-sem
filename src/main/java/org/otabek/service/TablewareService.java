package org.otabek.service;

import org.otabek.dao.jdbc.JDBCTablewareDAO;
import org.otabek.entity.Tableware;
import org.otabek.exceptions.DaoException;

import java.util.List;

public class TablewareService {
    private JDBCTablewareDAO tablewareDAO;

    public TablewareService(JDBCTablewareDAO tablewareDAO) {
        this.tablewareDAO = tablewareDAO;
    }

    public Tableware createTableware(Tableware tableware) throws DaoException {
        return tablewareDAO.createItem(tableware);
    }

    public Tableware getByID(int id) throws DaoException {
        return tablewareDAO.findByID(id);
    }

    public List<Tableware> getAll() throws DaoException {
        return tablewareDAO.findAll();
    }

    public Tableware updateTableware(String column, String value, int id) throws DaoException {
        return tablewareDAO.updateItem(column, value, id);
    }

    public boolean deleteTableware(int id) throws DaoException {
        return tablewareDAO.delete(id);
    }
}
