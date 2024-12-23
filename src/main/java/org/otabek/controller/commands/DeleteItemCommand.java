public class DeleteItemCommand implements Command {
    private TablewareService tablewareService;
    private MainView mainView;

    public DeleteItemCommand() {}

    public void setTablewareService(TablewareService tablewareService) {
        this.tablewareService = tablewareService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        int itemId = mainView.requestItemIdForDeletion();
        boolean deleted = itemService.deleteItem(itemId);
        if (deleted) {
            return new Response("Item deleted successfully");
        } else {
            return new Response("Item not found");
        }
    }
}
