package org.otabek.controller.commands;

import org.otabek.controller.Request;
import org.otabek.controller.Response;
import org.otabek.entity.Tableware;
import org.otabek.service.TablewareService;
import org.otabek.view.MainView;

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
