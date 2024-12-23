package org.otabek.controller.commands;

import org.otabek.controller.Request;
import org.otabek.controller.Response;
import org.otabek.exceptions.DaoException;

public interface Command {
    Response execute(Request request) throws DaoException;
}
