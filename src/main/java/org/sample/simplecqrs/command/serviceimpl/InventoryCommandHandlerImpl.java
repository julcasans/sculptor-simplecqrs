package org.sample.simplecqrs.command.serviceimpl;

import static org.fornax.cartridges.sculptor.framework.event.DynamicMethodDispatcher.dispatch;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.event.Event;
import org.sample.simplecqrs.command.domain.CheckInItemsToInventory;
import org.sample.simplecqrs.command.domain.CreateInventoryItem;
import org.sample.simplecqrs.command.domain.DeactivateInventoryItem;
import org.sample.simplecqrs.command.domain.InventoryItem;
import org.sample.simplecqrs.command.domain.RemoveItemsFromInventory;
import org.sample.simplecqrs.command.domain.RenameInventoryItem;
import org.sample.simplecqrs.command.eventapi.InventoryItemCreated;
import org.sample.simplecqrs.command.eventapi.InventoryItemDeactivated;
import org.sample.simplecqrs.command.eventapi.InventoryItemRenamed;
import org.sample.simplecqrs.command.eventapi.ItemsCheckedInToInventory;
import org.sample.simplecqrs.command.eventapi.ItemsRemovedFromInventory;
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
        getInventoryItemRepository().save(new InventoryItem(cmd.getItemId()));
        getInventoryItemEventPublisher().publishEvent(
                new InventoryItemCreated(cmd.getOccurred(), cmd.getItemId(), cmd.getName()));
    }

    public void handle(DeactivateInventoryItem cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.deactivate();
        getInventoryItemRepository().save(item);
        getInventoryItemEventPublisher().publishEvent(new InventoryItemDeactivated(cmd.getOccurred(), cmd.getItemId()));
    }

    public void handle(RenameInventoryItem cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.setName(cmd.getNewName());
        getInventoryItemRepository().save(item);
        getInventoryItemEventPublisher().publishEvent(
                new InventoryItemRenamed(cmd.getOccurred(), cmd.getItemId(), cmd.getNewName()));
    }

    public void handle(CheckInItemsToInventory cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.checkIn(cmd.getCountChange());
        getInventoryItemRepository().save(item);
        int currentCount = item.getCount();
        getInventoryItemEventPublisher().publishEvent(
                new ItemsCheckedInToInventory(cmd.getOccurred(), cmd.getItemId(), currentCount));
    }

    public void handle(RemoveItemsFromInventory cmd) {
        InventoryItem item = tryGetItem(cmd.getItemId());
        item.remove(cmd.getCountChange());
        getInventoryItemRepository().save(item);
        int currentCount = item.getCount();
        getInventoryItemEventPublisher().publishEvent(
                new ItemsRemovedFromInventory(new Date(), cmd.getItemId(), currentCount));
    }

    private InventoryItem tryGetItem(String itemId) {
        try {
            return getInventoryItemRepository().findByKey(itemId);
        } catch (InventoryItemNotFoundException e) {
            throw new IllegalStateException("Unknown item: " + itemId);
        }
    }
}
