package io.artoo.ddd.domain.changes;

import io.artoo.ddd.domain.Changes;
import io.artoo.ddd.domain.Domain;
import io.artoo.lance.literator.Cursor;

public final class Initial implements Changes {
  private final Domain.Event[] events;

  public Initial(final Domain.Event... events) {this.events = events;}

  @Override
  public Cursor<Domain.Event> cursor() {
    return Cursor.open(events);
  }
}
