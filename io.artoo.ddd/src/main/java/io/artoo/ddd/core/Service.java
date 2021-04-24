package io.artoo.ddd.core;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.value.Symbol;

import java.time.Instant;

public sealed interface Service {
  enum Type implements Service { Write, Read }

  record EventLog<F extends Record & Domain.Fact>(Symbol eventId, F eventData, Id modelId, Instant persistedAt, Instant emittedAt) {}

  interface Transaction extends Many<EventLog<?>> {}
  interface Operation<A extends Record & Domain.Act> extends Func.Bi<Id, A, Void> {}
  interface Reaction<F extends Record & Domain.Fact> extends Func.Uni<EventLog<F>, Void> {}
  interface Projection<F extends Record & Domain.Fact> extends Func.Uni<EventLog<F>, Void> {}
}
