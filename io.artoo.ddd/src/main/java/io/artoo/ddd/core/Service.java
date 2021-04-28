package io.artoo.ddd.core;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.value.Symbol;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public sealed interface Service {
  enum Namespace implements Service {}

  record EventLog<F extends Record & Domain.Fact>(Symbol eventId, F eventData, Id modelId, Instant persistedAt, Instant emittedAt) {}

  interface Bus extends Many<Domain.Message> {
    Bus whenever(Class<Domain.Message> type, Cons.Uni<Domain.Message> consumer);
  }

  interface Ledger {
    Many<Domain.Fact> load(Id id);

    Many<Domain.Fact> save(Id id, Many<Domain.Fact> facts);
  }

  interface Model {
    Transaction<Many<Domain.Fact>> open(final Id id);

    <M extends Many<Domain.Fact>> Transaction<M> open(final Id id, Func.Uni<? super Many<Domain.Fact>, ? extends M> reconstructor);
  }

  interface Transaction<M extends Many<Domain.Fact>> {
    void commit(Func.Uni<? super M, ? extends Many<Domain.Fact>> submit);
  }

  interface Operation<A extends Record & Domain.Act> extends Func.Bi<Id, A, Void> {
  }

  interface Reaction<F extends Record & Domain.Fact> extends Func.Uni<EventLog<F>, Void> {
  }

  interface Projection<F extends Record & Domain.Fact> extends Func.Uni<EventLog<F>, Void> {
  }
}

final class Volatile implements Service.Model {
  private final Service.Ledger ledger;

  Volatile(Service.Ledger ledger) {
    this.ledger = ledger;
  }

  @Override
  public Service.Transaction<Many<Domain.Fact>> open(final Id id) {
    return new Uncommitted<>(ledger, id);
  }

  @Override
  public <M extends Many<Domain.Fact>> Service.Transaction<M> open(final Id id, final Func.Uni<? super Many<Domain.Fact>, ? extends M> reconstructor) {
    return new Uncommitted<>(ledger, id, reconstructor);
  }
}

final class Uncommitted<M extends Many<Domain.Fact>> implements Service.Transaction<M> {
  private final Service.Ledger ledger;
  private final Id id;
  private final Func.Uni<? super Many<Domain.Fact>, ? extends M> reconstructor;

  @SuppressWarnings("unchecked")
  Uncommitted(final Service.Ledger ledger, final Id id) {
    this(ledger, id, facts -> (M) facts);
  }
  Uncommitted(final Service.Ledger ledger, final Id id, final Func.Uni<? super Many<Domain.Fact>, ? extends M> reconstructor) {
    this.ledger = ledger;
    this.id = id;
    this.reconstructor = reconstructor;
  }

  @Override
  public void commit(final Func.Uni<? super M, ? extends Many<Domain.Fact>> submit) {
    ledger.save(id, submit.apply(reconstructor.apply(ledger.load(id))));
  }
}

final class Stateful implements Service.Ledger {
  private final Sync lock;
  private final AtomicReference<Many<Mutation>> mutations;
  private final Service.Bus bus;

  private Stateful(final Service.Bus bus) {
    this(
      Sync.Lock,
      Many.empty(),
      bus
    );
  }

  private Stateful(final Sync lock, final Many<Mutation> mutations, final Service.Bus bus) {
    this.lock = lock;
    this.mutations = new AtomicReference<>(mutations);
    this.bus = bus;
  }

  @Override
  public Many<Domain.Fact> load(final Id id) {
    synchronized (lock) {
      return Many.from(mutations.get()
        .where(mutation -> mutation.modelId.is(id))
        .select(mutation -> mutation.fact)
        .asArrayOf(Domain.Fact.class)
      );
    }
  }

  @Override
  public Many<Domain.Fact> save(Id id, final Many<Domain.Fact> facts) {
    synchronized (lock) {
      return
        mutations
          .accumulateAndGet(
            Symbol.unique().asQueryable()
              .selection(workId -> facts.select(fact -> new Mutation(Symbol.unique(), fact.$name(), fact, id, "random-name", workId, Instant.now())))
              .peek(System.out::println),
            Concatenatable::concat
          )
          .select(mutation -> mutation.fact);
    }
  }

  private enum Sync {Lock}

  private record Mutation(
    Symbol factId,
    String factName,
    Domain.Fact fact,
    Id modelId,
    String modelName,
    Long workId,
    Instant persistedAt
  ) {
  }
}
