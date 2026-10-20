package com.soupulsar.domain.model.partner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Partner {

    private final UUID id;
    private final String name;
    private boolean active;

    public static Partner create(String name) {
        return Partner.builder()
                .id(UUID.randomUUID())
                .name(name)
                .active(true)
                .build();
    }

    public static Partner restore(UUID id, String name, boolean active) {
        return Partner.builder()
                .id(id)
                .name(name)
                .active(active)
                .build();
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}