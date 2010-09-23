package org.sample.simplecqrs.command.repositoryimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;
import static org.sample.simplecqrs.command.domain.InventoryItemSnapshotProperties.itemId;
import static org.sample.simplecqrs.command.domain.InventoryItemSnapshotProperties.version;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.sample.simplecqrs.command.domain.InventoryItemSnapshot;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for InventoryItemSnapshot
 */
@Repository("inventoryItemSnapshotRepository")
public class InventoryItemSnapshotRepositoryImpl extends InventoryItemSnapshotRepositoryBase {
    public InventoryItemSnapshotRepositoryImpl() {
    }

    @Override
    public InventoryItemSnapshot getLatestSnapshot(String itemId) {
        List<ConditionalCriteria> criteria = criteriaFor(InventoryItemSnapshot.class).withProperty(itemId()).eq(itemId)
                .orderBy(version()).descending().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, 1);
        PagedResult<InventoryItemSnapshot> result = findByCondition(criteria, pagingParameter);
        if (result.getValues().isEmpty()) {
            return null;
        } else {
            return result.getValues().get(0);
        }
    }
}
