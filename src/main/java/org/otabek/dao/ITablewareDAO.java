package org.otabek.dao;

import org.otabek.entity.Tableware;
import org.otabek.exceptions.DaoException;

import java.util.List;

public interface ITablewareDAO {
    Tableware createItem(Tableware tableware) throws DaoException;
    Tableware findByID(int id) throws DaoException;
    List<Tableware> findAll() throws DaoException;

    Tableware updateItem(String column, String value, int id) throws DaoException;

    boolean delete(int id) throws DaoException;
}
