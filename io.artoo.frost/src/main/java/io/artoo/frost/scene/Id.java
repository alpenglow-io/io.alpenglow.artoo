package io.artoo.frost.scene;

import io.artoo.lance.query.One;

import java.util.Objects;
import java.util.UUID;

public record Id(String value) {
  public Id {assert value != null;}

  public static One<Id> of(final String value) {
    return One.of(value)
      .where(Objects::nonNull)
      .select(Id::new);
  }

  public static Id from(final String value) {
    return new Id(value);
  }

  public static Id random() {
    return new Id(UUID.randomUUID().toString());
  }
}
