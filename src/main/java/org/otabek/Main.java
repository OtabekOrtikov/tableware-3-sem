package org.otabek;

import org.otabek.entity.item.Cup;
import org.otabek.exceptions.DaoException;

public class Main {
    public static void main(String[] args) throws DaoException {
        AppConfig config = new AppConfig();
        config.getMainController().run();
    }
}