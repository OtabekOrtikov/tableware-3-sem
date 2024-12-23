public class Request {
    private String command;
    private String[] params;

    public Request(String command, String... params) {
        this.command = command;
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }
}
