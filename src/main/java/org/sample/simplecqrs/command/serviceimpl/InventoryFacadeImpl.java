package org.sample.simplecqrs.command.serviceimpl;

import java.util.Date;

import org.sample.simplecqrs.command.domain.CheckInItemsToInventory;
import org.sample.simplecqrs.command.domain.CreateInventoryItem;
import org.sample.simplecqrs.command.domain.DeactivateInventoryItem;
import org.sample.simplecqrs.command.domain.RemoveItemsFromInventory;
import org.sample.simplecqrs.command.domain.RenameInventoryItem;
import org.sample.simplecqrs.command.serviceapi.InventoryItemCommandEventSourcingProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryFacade.
 */
@Service("inventoryFacade")
public class InventoryFacadeImpl extends InventoryFacadeImplBase {
    @Autowired
    private InventoryItemCommandEventSourcingProcessor commandProcessor;

    public InventoryFacadeImpl() {
    }

    public void createInventoryItem(String itemId, String name) {
        commandProcessor.apply(new CreateInventoryItem(new Date(), itemId, name));
    }

    public void deactivateInventoryItem(String itemId) {
        commandProcessor.apply(new DeactivateInventoryItem(new Date(), itemId));
    }

    public void renameInventoryItem(String itemId, String newName) {
        commandProcessor.apply(new RenameInventoryItem(new Date(), itemId, newName));
    }

    public void checkInItemsToInventory(String itemId, int count) {
        commandProcessor.apply(new CheckInItemsToInventory(new Date(), itemId, count));
    }

    public void removeItemsFromInventory(String itemId, int count) {
        commandProcessor.apply(new RemoveItemsFromInventory(new Date(), itemId, count));
    }

}
