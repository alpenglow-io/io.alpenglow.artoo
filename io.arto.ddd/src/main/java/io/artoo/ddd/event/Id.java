package io.artoo.ddd.event;

import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;

import java.util.UUID;

public interface Id extends One<UUID> {
  static Id random() {
    return Random.ID;
  }
}

enum Random implements Id {
  ID;

  @Override
  public Cursor<UUID> cursor() {
    return Cursor.open(UUID.randomUUID());
  }
}
