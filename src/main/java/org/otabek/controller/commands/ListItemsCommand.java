public class ListItemsCommand implements Command {
    private TablewareService tablewareService;
    private MainView mainView;

    public ListItemsCommand() {}

    public void setTablewareService(TablewareService tablewareService) {
        this.tablewareService = tablewareService;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public Response execute(Request request) {
        try {
            tablewareService.getAll().forEach(item -> mainView.displayItem(item.toString()));
            return new Response("Items listed successfully");
        } catch (DaoException e) {
            return new Response("Error while listing items: " + e.getMessage());
        }
    }
}
