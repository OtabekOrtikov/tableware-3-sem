public class CreateItemCommand implements Command {
    private TablewareService tablewareService;
    private MainView mainView;

    public CreateItemCommand() {}

    public void setTablewareService(TablewareService tablewareService) {
        this.tablewareService = tablewareService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        String itemType = mainView.requestItemType();
        Tableware itemDetails = mainView.requestItemDetails(itemType);
        itemService.createItem(itemDetails);
        return new Response("Item created successfully");
    }
}
