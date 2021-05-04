package io.artoo.ddd.core;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.Many;
import io.artoo.lance.value.Symbol;

import java.time.Instant;

public sealed interface Service {
  enum Namespace implements Service {}

  interface Bus extends Many<Domain.Message> {
    Bus whenever(Class<Domain.Message> type, Cons.Uni<Domain.Message> consumer);
  }


  interface Operation<A extends Record & Domain.Act> extends Cons.Bi<Id, A> {}

  interface Reaction extends Cons.Uni<EventLog> {}

  interface Projection extends Cons.Uni<EventLog> {}


}

