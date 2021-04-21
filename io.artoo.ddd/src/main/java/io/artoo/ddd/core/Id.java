package io.artoo.ddd.core;

import io.artoo.lance.value.Value;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public sealed interface Id extends Value<UUID, Id> {
  static Id random() {
    return new Random();
  }

  default boolean is(Id id) { return equals(id); }

  record Random(UUID uuid) implements Id {
    public Random() { this(randomUUID()); };

    @Override
    public UUID tryGet() throws Throwable {
      return uuid;
    }
  }
}
