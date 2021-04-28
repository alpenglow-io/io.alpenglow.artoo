package io.artoo.ddd.core;

import io.artoo.ddd.core.event.EventBus;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.value.Symbol;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public sealed interface Service {
  enum Namespace implements Service {}

  record EventLog<F extends Record & Domain.Fact>(Symbol eventId, F eventData, Id modelId, Instant persistedAt, Instant emittedAt) {}

  interface Omnibus extends Many<Domain.Message> {
    Omnibus whenever(Class<Domain.Message> type, Cons.Uni<Domain.Message> consumer);
  }

  interface Ledger {
    Many<Domain.Fact> load(Id id, String name);
    Many<Domain.Fact> save(Domain.Fact... facts);
  }

  interface Model<M extends Model<M>> {
    M open(final Id id);
    M submit();
  }

  interface Transaction {
    One<Integer> commit(Func.Unary<Many<Domain.Fact>> changes);
  }

  interface Operation<A extends Record & Domain.Act> extends Func.Bi<Id, A, Void> {
  }

  interface Reaction<F extends Record & Domain.Fact> extends Func.Uni<EventLog<F>, Void> {
  }

  interface Projection<F extends Record & Domain.Fact> extends Func.Uni<EventLog<F>, Void> {
  }
}

final class Aggregate implements Service.Model<Aggregate> {
  private final Service.Ledger ledger;
  private final Many<Domain.Fact> facts;

  Aggregate(Service.Ledger ledger, Many<Domain.Fact> facts) {
    this.ledger = ledger;
    this.facts = facts;
  }

  @Override
  public Aggregate open(Id id) {
    return new Aggregate(ledger, ledger.load(id, "aggregate"));
  }

  public Aggregate

  @Override
  public Aggregate submit() {
    return null;
  }
}

final class State implements Service.Model {
  private final Sync lock;
  private final AtomicReference<Many<Mutation>> mutations;
  private final EventBus eventBus;

  private State(final EventBus eventBus) {
    this(
      Sync.Lock,
      Many.empty(),
      eventBus
    );
  }

  private State(final Sync lock, final Many<Mutation> mutations, final EventBus eventBus) {
    this.lock = lock;
    this.mutations = new AtomicReference<>(mutations);
    this.eventBus = eventBus;
  }

  @Override
  public final Service.Transaction open(Id modelId) {
    synchronized (lock) {
      return changes -> {
        final Many<Domain.Fact> select = mutations
          .get()
          .where(mutation -> mutation.modelId.is(modelId))
          .select(mutation -> mutation.eventFact);
        return mutations.accumulateAndGet(
          changes.apply(select).select(fact -> new Mutation(Symbol.unique(), fact.$name(), fact, ))
          ,
          Concatenatable::concat
        )
          .count();
      };
    }
  }

  private enum Sync {Lock}

  private record Mutation(
    Symbol eventId,
    String eventName,
    Domain.Fact eventFact,
    Id modelId,
    String modelName,
    Symbol workId,
    Instant persistedAt,
    Instant emittedAt
  ) {
  }


  @Override
  public <A extends Domain.Aggregate> One<Integer> commit(final A aggregate) {
    synchronized (lock) {
      return aggregate
        .select(work ->
          mutations
            .get()
            .any(log -> log.workId.is(work.id()))
            .where(it -> !it)
            .selection(it ->
              work
                .changes()
                .select(event ->
                  new Commit(
                    Symbol.unique(),
                    event.$name(),
                    event,
                    work.aggregateId(),
                    aggregate.$name(),
                    work.id(),
                    Instant.now()
                  )
                )
            )
        )
        .peek(logs -> mutations
          .accumulateAndGet(logs, Concatenatable::concat)
          .eventually(log -> eventBus.emit(new Domain.EventMessage(log.eventId, log.eventFact, log.modelId, log.persistedAt, Instant.now())))
        )
        .select(logs -> logs.count())
        .otherwise("Can't commit aggregate", IllegalStateException::new);
    }
  }

  @Override
  public History findHistoryBy(final Id aggregateId) {
    synchronized (lock) {
      return
        History.past(
          mutations
            .get()
            .where(log -> log.modelId.is(aggregateId))
            .select(log -> log.eventFact)
            .asArrayOf(Domain.Event.class)
        );
    }
  }
}
