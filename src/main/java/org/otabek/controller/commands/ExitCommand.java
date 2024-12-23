package org.otabek.controller.commands;

import org.otabek.controller.Request;
import org.otabek.controller.Response;

public class ExitCommand implements Command {
    @Override
    public Response execute(Request request) {
        return new Response("Exiting the system");
    }
}
