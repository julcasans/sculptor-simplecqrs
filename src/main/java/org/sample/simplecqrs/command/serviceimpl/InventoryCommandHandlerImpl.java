package org.sample.simplecqrs.command.serviceimpl;

import static org.fornax.cartridges.sculptor.framework.event.DynamicMethodDispatcher.dispatch;

import org.fornax.cartridges.sculptor.framework.event.Event;
import org.sample.simplecqrs.command.domain.CheckInItemsToInventory;
import org.sample.simplecqrs.command.domain.CreateInventoryItem;
import org.sample.simplecqrs.command.domain.DeactivateInventoryItem;
import org.sample.simplecqrs.command.domain.InventoryItem;
import org.sample.simplecqrs.command.domain.RemoveItemsFromInventory;
import org.sample.simplecqrs.command.domain.RenameInventoryItem;
import org.sample.simplecqrs.command.exception.InventoryItemNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryCommandHandler.
 */
@Service("inventoryCommandHandler")
public class InventoryCommandHandlerImpl extends InventoryCommandHandlerImplBase {

    public InventoryCommandHandlerImpl() {
    }

    public void receive(Event event) {
        dispatch(this, event, "handle");
    }

    public void handle(CreateInventoryItem cmd) {
        InventoryItem item = new InventoryItem(cmd.getItemId(), cmd.getName(), true);

        getInventoryItemRepository().save(item);
    }

    public void handle(DeactivateInventoryItem cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.deactivate();
        getInventoryItemRepository().save(item);
    }

    public void handle(RenameInventoryItem cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.rename(cmd.getNewName());
        getInventoryItemRepository().save(item);
    }

    public void handle(CheckInItemsToInventory cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.checkIn(cmd.getCountChange());
        getInventoryItemRepository().save(item);
    }

    public void handle(RemoveItemsFromInventory cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.remove(cmd.getCountChange());
        getInventoryItemRepository().save(item);
    }

    private InventoryItem tryGetItem(String itemId) {
        try {
            return getInventoryItemRepository().findByKey(itemId);
        } catch (InventoryItemNotFoundException e) {
            throw new IllegalStateException("Unknown item: " + itemId);
        }
    }
}
