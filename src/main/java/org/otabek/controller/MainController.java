public class MainController {
    private AdminController adminController;
    private ItemController itemController;

    public MainController(AdminController adminController, ItemController itemController) {
        this.adminController = adminController;
        this.itemController = itemController;
    }

    public Response handleRequest(Request request) {
        if (request.getCommand().startsWith("/admin")) {
            return adminController.handleRequest(request);
        } else if (request.getCommand().startsWith("/item")) {
            return itemController.handleRequest(request);
        } else {
            return new Response("Invalid command");
        }
    }
}
