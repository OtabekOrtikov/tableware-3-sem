public class BackCommand implements Command {
    @Override
    public Response execute(Request request) {
        return new Response("Going back");
    }
}
