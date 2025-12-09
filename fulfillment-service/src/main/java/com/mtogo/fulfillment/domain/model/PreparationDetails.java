package com.mtogo.fulfillment.domain.model;

import java.util.List;

public record PreparationDetails(List<String> itemList, int estimatedMinutes) {
    public PreparationDetails {
        if (itemList == null || itemList.isEmpty()) {
            throw new IllegalArgumentException("itemList cannot be empty");
        }
        if (estimatedMinutes <= 0) {
            throw new IllegalArgumentException("estimatedMinutes must be positive");
        }
    }
}
