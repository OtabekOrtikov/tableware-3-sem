public class CommandFactory {

    public Command createAdminCommand(Request request) {
        switch (request.getCommand()) {
            case "/list":
                return new ListUsersCommand();
            case "/create":
                return new CreateUserCommand();
            case "/delete":
                return new DeleteUserCommand();
            case "/changeAdmin":
                return new ChangeAdminCommand();
            case "/exit":
                return new ExitCommand();
            default:
                return null;
        }
    }

    public Command createItemCommand(Request request) {
        switch (request.getCommand()) {
            case "/list":
                return new ListItemsCommand();
            case "/create":
                return new CreateItemCommand();
            case "/update":
                return new UpdateItemCommand();
            case "/delete":
                return new DeleteItemCommand();
            case "/back":
                return new BackCommand();
            default:
                return null;
        }
    }
}
