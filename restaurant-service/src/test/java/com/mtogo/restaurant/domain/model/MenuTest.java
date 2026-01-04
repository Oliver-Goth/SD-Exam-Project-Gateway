package com.mtogo.restaurant.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuTest {

    @Test
    void publishSetsStatusToPublished() {
        Menu menu = new Menu(1L);

        menu.publish();

        assertEquals(MenuStatus.PUBLISHED, menu.getStatus());
    }

    @Test
    void archiveSetsStatusToArchived() {
        Menu menu = new Menu(1L);

        menu.archive();

        assertEquals(MenuStatus.ARCHIVED, menu.getStatus());
    }

    @Test
    void newMenuHasDraftStatus() {
        Menu menu = new Menu(1L);

        assertEquals(MenuStatus.DRAFT, menu.getStatus());
    }
}
