public class CreateUserCommand implements Command {
    private UserService userService;
    private MainView mainView;

    public CreateUserCommand() {}

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        String username = mainView.requestUsernameForNewUser();
        String password = mainView.requestPasswordForNewUser();
        Role role = mainView.requestRoleForNewUser();
        User newUser = userService.createUser(username, password, role);
        if (newUser != null) {
            return new Response("User created successfully");
        } else {
            return new Response("User already exists");
        }
    }
}
