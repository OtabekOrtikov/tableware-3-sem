public class ChangeUserRoleCommand implements Command {
    private final UserService userService;
    private final MainView mainView;

    public ChangeUserRoleCommand(UserService userService, MainView mainView) {
        this.userService = userService;
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        int userId = mainView.requestUserIdForDeletion();
        Role newRole = mainView.requestRoleForNewUser();
        boolean updated = userService.changeUserRole(userId, newRole);
        if (updated) {
            return new Response("User role updated successfully");
        } else {
            return new Response("User not found");
        }
    }
}
