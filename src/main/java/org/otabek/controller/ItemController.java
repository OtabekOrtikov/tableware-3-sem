package org.otabek.controller;

import org.otabek.controller.Response;
import org.otabek.controller.commands.Command;
import org.otabek.controller.commands.CommandFactory;

public class ItemController {
    private CommandFactory commandFactory;

    public ItemController(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public Response handleRequest(Request request) {
        Command itemCommand = commandFactory.createItemCommand(request);
        if (itemCommand != null) {
            return itemCommand.execute(request);
        } else {
            return new Response("Invalid item command");
        }
    }
}
