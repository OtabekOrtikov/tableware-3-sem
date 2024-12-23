public class DeleteUserCommand implements Command {
    private UserService userService;
    private MainView mainView;

    public DeleteUserCommand() {}

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        int userId = mainView.requestUserIdForDeletion();
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return new Response("User deleted successfully");
        } else {
            return new Response("User not found");
        }
    }
}
