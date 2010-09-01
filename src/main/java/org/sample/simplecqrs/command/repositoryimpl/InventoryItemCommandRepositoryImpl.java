package org.sample.simplecqrs.command.repositoryimpl;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for InventoryItemCommand
 */
@Repository("inventoryItemCommandRepository")
public class InventoryItemCommandRepositoryImpl
    extends InventoryItemCommandRepositoryBase {
    public InventoryItemCommandRepositoryImpl() {
    }
}
