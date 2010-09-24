package org.sample.simplecqrs.command.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.sample.simplecqrs.command.domain.InventoryItemEventProperties.itemId;
import static org.sample.simplecqrs.command.domain.InventoryItemEventProperties.seq;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.sample.simplecqrs.command.domain.InventoryItemEvent;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for InventoryItemEvent
 */
@Repository("inventoryItemEventRepository")
public class InventoryItemEventRepositoryImpl extends InventoryItemEventRepositoryBase {
    public InventoryItemEventRepositoryImpl() {
    }

    @Override
    public List<InventoryItemEvent> findAllForItem(String itemId) {
        List<ConditionalCriteria> criteria = criteriaFor(InventoryItemEvent.class).withProperty(itemId()).eq(itemId)
                .orderBy(seq()).build();
        return findByCondition(criteria);
    }
}
