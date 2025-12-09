package com.mtogo.fulfillment.domain.model;

public record KitchenNote(String text) {
    public KitchenNote {
        if (text != null) {
            text = text.trim();
        }
    }
}
