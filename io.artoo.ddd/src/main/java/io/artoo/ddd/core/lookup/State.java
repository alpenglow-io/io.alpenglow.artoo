package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.lance.value.Id;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface State extends Many<Domain.Fact> {
  State ensure(final Pred.Uni<? super Many<Domain.Fact>> validation);

  void commit(Domain.Fact... newFacts);
}

final class Dump implements State {
  private final Transactions transactions;
  private final Id id;
  private final Pred.Uni<? super Many<Domain.Fact>> validation;

  Dump(final Transactions transactions, final Id id) {
    this(
      transactions,
      id,
      ignored -> true
    );
  }

  private Dump(final Transactions transactions, final Id id, final Pred.Uni<? super Many<Domain.Fact>> validation) {
    this.transactions = transactions;
    this.id = id;
    this.validation = validation;
  }

  @Override
  public State ensure(final Pred.Uni<? super Many<Domain.Fact>> validation) {
    return new Dump(transactions, id, validation);
  }

  @Override
  public void commit(final Domain.Fact... newFacts) {
    for (final var newFact : newFacts)
      transactions.submit(id, newFact);
  }

  @Override
  public Cursor<Domain.Fact> cursor() {
    return transactions
      .where(past -> past.stateId().is(id) && validation.tryApply(Many.from(past.changes())))
      .selection(past -> Many.from(past.changes()))
      .cursor();
  }
}
