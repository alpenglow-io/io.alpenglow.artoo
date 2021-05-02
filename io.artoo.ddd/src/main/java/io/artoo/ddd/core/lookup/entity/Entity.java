package io.artoo.ddd.core.lookup.entity;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.lookup.ledger.Ledger;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;

interface Entity<F extends Many<Domain.Fact>> extends One<Id> {
  default Entity<Facts> open(final Id id) {
    return open(id, facts -> () -> Cursor.open(facts.asArrayOf(Domain.Fact.class)));
  }

  <S extends Many<Domain.Fact>> Entity<S> open(final Id id, Func.Uni<? super F, ? extends S> reconstructor);

  private F throwAway(F facts) {
    throw new IllegalStateException("Can't validate the entity with actual state");
  }

  default Entity<F> assure(final Pred.Uni<? super F> validation) {
    return this
      .select(id -> open(id, facts -> this instanceof Opened && validation.tryApply(facts) ? facts : throwAway(facts)))
      .otherwise("Can't assure the selected entity", (m, c) -> new IllegalStateException(c, m));
  }

  default void commit(Domain.Fact... newFacts) {
    throw new IllegalStateException("Can't commit the selected entity is not opened");
  }

  interface Facts extends Many<Domain.Fact> {}
}

final class Unopened<F extends Many<Domain.Fact>> implements Entity<F> {
  private final Ledger ledger;

  Unopened(final Ledger ledger) {this.ledger = ledger;}

  @Override
  public Cursor<Id> cursor() {
    return Cursor.nothing();
  }

  @Override
  public <S extends Many<Domain.Fact>> Entity<S> open(final Id id, final Func.Uni<? super F, ? extends S> reconstructor) {
    return new Opened<>(ledger, id, reconstructor);
  }
}

final class Opened<F extends Many<Domain.Fact>, S extends Many<Domain.Fact>> implements Entity<S> {
  private final Ledger ledger;
  private final Id id;
  private final Func.Uni<? super F, ? extends S> reconstructor;

  Opened(final Ledger ledger, final Id id, final Func.Uni<? super F, ? extends S> reconstructor) {
    this.ledger = ledger;
    this.id = id;
    this.reconstructor = reconstructor;
  }

  @Override
  public Cursor<Id> cursor() {
    return Cursor.open(id);
  }

  @Override
  public <P extends Many<Domain.Fact>> Entity<P> open(final Id id, final Func.Uni<? super S, ? extends P> reconstructorAgain) {
    return new Opened<>(ledger, id, this.reconstructor.then(reconstructorAgain));
  }

  @Override
  public void commit(final Domain.Fact... newFacts) {
    ledger.attach(
      Many.from(newFacts)
        .select(fact -> new Ledger.Transaction(id, fact, "random-name"))
        .asArrayOf(Ledger.Transaction.class)
    );
  }
}

