package io.artoo.ddd.core.lookup.catalog;

import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.lookup.Lookup;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.value.Symbol;

import java.util.concurrent.atomic.AtomicReference;

public interface Catalog extends Lookup<Catalog.Snapshot, Catalog> {
  record Snapshot(
    Symbol snapshotId,
    String name,
    Record entry,
    Id entityId,
    String entityName
  ) {}

  static Catalog inMemory() { return new InMemory(); }
}

final class InMemory implements Catalog {
  private final Sync lock;
  private final AtomicReference<Many<Snapshot>> transactions;

  InMemory() {
    this(
      Sync.Lock,
      Many.empty()
    );
  }
  private InMemory(final Sync lock, final Many<Snapshot> transactions) {
    this.lock = lock;
    this.transactions = new AtomicReference<>(transactions);
  }

  @Override
  public Catalog attach(final Many<Snapshot> records) {
    synchronized (lock) {
      return transactions
        .accumulateAndGet(records, Concatenatable::concat)
        .eventually(this);
    }
  }

  @Override
  public boolean match(final Id id, final Pred.Uni<? super Many<Snapshot>> match) {
    synchronized (lock) {
      return match.apply(transactions.getAcquire().where(snapshot -> snapshot.entityId().is(id)));
    }
  }

  @Override
  public <T> T fetch(final Id id, final Func.Uni<? super Many<Snapshot>, ? extends T> fetch) {
    synchronized (lock) {
      return fetch.apply(transactions.getAcquire().where(snapshot -> snapshot.entityId().is(id)));
    }
  }

  @Override
  public Snapshot[] tryGet() {
    synchronized (lock) {
      return transactions.getAcquire().asArrayOf(Snapshot.class);
    }
  }

  private enum Sync {Lock}
}
