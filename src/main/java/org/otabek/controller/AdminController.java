public class AdminController {
    private CommandFactory commandFactory;

    public AdminController(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public Response handleRequest(Request request) {
        Command adminCommand = commandFactory.createAdminCommand(request);
        if (adminCommand != null) {
            return adminCommand.execute(request);
        } else {
            return new Response("Invalid admin command");
        }
    }
}