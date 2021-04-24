package io.artoo.ddd.core;

import io.artoo.lance.query.One;
import io.artoo.lance.value.Value;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public sealed interface Id extends Value<UUID, Id> {
  static Id uuid() { return new Uuid(); }
  static One<Id> uuid(String value) {
    return One.maybe(value).select(UUID::fromString).select(Uuid::new);
  }
}

record Uuid(UUID value) implements Id {
  public Uuid() { this(randomUUID()); }

  @Override
  public UUID tryGet() {
    return value;
  }
}
