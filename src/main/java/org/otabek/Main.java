    public class Main {
        public static void main(String[] args) {
            AppConfig config = new AppConfig();
            config.getMainController().run();
            CommandFactory commandFactory = new CommandFactory();
            AdminController adminController = new AdminController(commandFactory);
            ItemController itemController = new ItemController(commandFactory);
            MainController mainController = new MainController(adminController, itemController, commandFactory);
            MainView mainView = new MainView(mainController);

            mainView.start();
        }
    }
