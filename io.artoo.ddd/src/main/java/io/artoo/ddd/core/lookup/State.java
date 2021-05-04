package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

public interface State extends Many<Store.Change> {
  State ensure(final Pred.Uni<? super Many<Store.Change>> validation);

  void commit(Domain.Fact... newFacts);
}

final class Dump implements State {
  private final Store store;
  private final Id id;
  private final Pred.Uni<? super Many<Store.Change>> validation;

  Dump(final Store store, final Id id) {
    this(
      store,
      id,
      ignored -> true
    );
  }

  private Dump(final Store store, final Id id, final Pred.Uni<? super Many<Store.Change>> validation) {
    this.store = store;
    this.id = id;
    this.validation = validation;
  }

  @Override
  public State ensure(final Pred.Uni<? super Many<Store.Change>> validation) {
    return new Dump(store, id, validation);
  }

  @Override
  public void commit(final Domain.Fact... newFacts) {
    for (final var newFact : newFacts)
      store.submit(id, newFact);
  }

  @Override
  public Cursor<Store.Change> cursor() {
    return store
      .where(past -> past.stateId().is(id) && validation.tryApply(Many.from(past.changes())))
      .selection(past -> Many.from(past.changes()))
      .cursor();
  }
}
