public class ExitCommand implements Command {
    @Override
    public Response execute(Request request) {
        return new Response("Exiting the system");
    }
}
