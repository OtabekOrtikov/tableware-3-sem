public class ChangeAdminCommand implements Command {
    private UserService userService;
    private MainView mainView;

    public ChangeAdminCommand() {}

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        try {
            int newAdminId = mainView.requestUserIdForRoleChange();
            userService.changeAdmin(newAdminId);
            mainView.displaySuccessMessage("Admin role successfully transferred");
            return new Response("Admin role changed successfully");
        } catch (DaoException e) {
            return new Response("Error while changing admin: " + e.getMessage());
        }
    }
}
