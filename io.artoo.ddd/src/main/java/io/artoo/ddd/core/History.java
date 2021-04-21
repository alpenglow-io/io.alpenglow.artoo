package io.artoo.ddd.core;

import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface History extends Many<Domain.Event> {
  static History past(Domain.Event... events) {
    return new Past(events);
  }

  final class Past implements History {
    private final Domain.Event[] events;

    public Past(final Domain.Event[] events) {this.events = events;}

    @Override
    public Cursor<Domain.Event> cursor() {
      return Cursor.open(events);
    }
  }
}
