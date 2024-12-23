    public class ListUsersCommand implements Command {
        private UserService userService;
        private MainView mainView;

        public ListUsersCommand() {}

        public void setUserService(UserService userService) {
            this.userService = userService;
        }

        public void setMainView(MainView mainView) {
            this.mainView = mainView;
        }

        @Override
        public Response execute(Request request) {
            try {
                userService.listAllUsers().forEach(user -> mainView.displayUser(user.toString()));
                return new Response("Users listed successfully");
            } catch (DaoException e) {
                return new Response("Error while listing users: " + e.getMessage());
            }
        }
    }
