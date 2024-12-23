public class MainView {
    private Scanner scanner;
    private MainController mainController;

    public MainView(MainController mainController) {
        this.mainController = mainController;
        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        System.out.println("Welcome to the system! Please select a command:");
        System.out.println("/admin - Admin commands");
        System.out.println("/item - Item commands");
        System.out.println("/exit - Exit the system");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void displayResponse(Response response) {
        System.out.println(response.getMessage());
    }

    public void start() {
        while (true) {
            displayMainMenu();
            String command = readCommand();
            if (command.equals("/exit")) {
                break;
            }
            Request request = new Request(command);
            Response response = mainController.handleRequest(request);
            displayResponse(response);
        }
    }
}
