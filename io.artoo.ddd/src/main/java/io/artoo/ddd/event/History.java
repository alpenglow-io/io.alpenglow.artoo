package io.artoo.ddd.event;


import io.artoo.ddd.Domain;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface History extends Many<Domain.Event> {
  static History create(Domain.Event... events) {
    return new Past(events);
  }

  default Stream<Domain.Event> stream() {
    return StreamSupport.stream(spliterator(), false);
  }
}

final class Past implements History {
  private final Domain.Event[] events;

  Past(final Domain.Event... events) {this.events = events;}

  @Override
  public Cursor<Domain.Event> cursor() {
    return Cursor.open(events);
  }
}
