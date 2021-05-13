package io.artoo.lance.value;

import io.artoo.lance.query.One;

import java.util.UUID;

public sealed interface Id extends Value<UUID, Id> {
  static Id universal() { return new Universal(UUID.randomUUID()); }

  static One<Id> universal(String value) {
    return One.maybe(value)
      .select(UUID::fromString)
      .select(Universal::new);
  }

  record Universal(UUID value) implements Id {
    @Override
    public UUID tryGet() {
      return value;
    }
  }
}
