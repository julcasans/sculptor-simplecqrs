package org.sample.simplecqrs.command.serviceimpl;

import java.util.Date;

import org.sample.simplecqrs.command.domain.InventoryItem;
import org.sample.simplecqrs.command.eventapi.InventoryItemCreated;
import org.sample.simplecqrs.command.eventapi.InventoryItemDeactivated;
import org.sample.simplecqrs.command.eventapi.InventoryItemRenamed;
import org.sample.simplecqrs.command.eventapi.ItemsCheckedInToInventory;
import org.sample.simplecqrs.command.eventapi.ItemsRemovedFromInventory;
import org.sample.simplecqrs.command.exception.InventoryItemNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryFacade.
 */
@Service("inventoryFacade")
public class InventoryFacadeImpl extends InventoryFacadeImplBase {
    public InventoryFacadeImpl() {
    }

    public void createInventoryItem(String itemId, String name) {
        getInventoryItemRepository().save(new InventoryItem(itemId));
        getInventoryItemEventPublisher().publishEvent(new InventoryItemCreated(new Date(), itemId, name));
    }

    public void deactivateInventoryItem(String itemId) {
        InventoryItem item = tryGetItem(itemId);
        item.deactivate();
        getInventoryItemRepository().save(item);
        getInventoryItemEventPublisher().publishEvent(new InventoryItemDeactivated(new Date(), itemId));
    }

    public void renameInventoryItem(String itemId, String newName) {
        InventoryItem item = tryGetItem(itemId);
        item.setName(newName);
        getInventoryItemRepository().save(item);
        getInventoryItemEventPublisher().publishEvent(new InventoryItemRenamed(new Date(), itemId, newName));
    }

    public void checkInItemsToInventory(String itemId, int count) {
        InventoryItem item = tryGetItem(itemId);
        item.checkIn(count);
        getInventoryItemRepository().save(item);
        getInventoryItemEventPublisher().publishEvent(new ItemsCheckedInToInventory(new Date(), itemId, count));
    }

    public void removeItemsFromInventory(String itemId, int count) {
        InventoryItem item = tryGetItem(itemId);
        item.remove(count);
        getInventoryItemRepository().save(item);
        getInventoryItemEventPublisher().publishEvent(new ItemsRemovedFromInventory(new Date(), itemId, count));
    }

    private InventoryItem tryGetItem(String itemId) {
        try {
            return getInventoryItemRepository().findByKey(itemId);
        } catch (InventoryItemNotFoundException e) {
            throw new IllegalStateException("Unknown item: " + itemId);
        }
    }

}
