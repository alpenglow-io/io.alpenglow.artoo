package io.artoo.ddd.core.lookup.ledger;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service;
import io.artoo.ddd.core.lookup.Lookup;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.value.Symbol;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public interface Ledger extends Lookup<Ledger.Transaction, Ledger> {
  record Transaction(Symbol transactionId, String factName, Domain.Fact fact, Id entityId, String entityName, Instant persistedAt) {
    public Transaction(Id entityId, Domain.Fact fact, String entityName) {
      this(Symbol.unique(), fact.$name(), fact, entityId, entityName, Instant.now());
    }
  }
}

final class InMemory implements Ledger {
  private final InMemory.Sync lock;
  private final AtomicReference<Many<Transaction>> transactions;
  private final Service.Bus bus;

  private InMemory(final Service.Bus bus) {
    this(
      InMemory.Sync.Lock,
      Many.empty(),
      bus
    );
  }

  private InMemory(final InMemory.Sync lock, final Many<Transaction> transactions, final Service.Bus bus) {
    this.lock = lock;
    this.transactions = new AtomicReference<>(transactions);
    this.bus = bus;
  }

  @Override
  public Ledger attach(final Many<Transaction> records) {
    synchronized (lock) {
      return transactions
        .accumulateAndGet(records, Concatenatable::concat)
        .eventually(this);
    }
  }

  @Override
  public boolean match(final Id id, final Pred.Uni<? super Many<Transaction>> match) {
    synchronized (lock) {
      return match.apply(transactions.getAcquire().where(transaction -> transaction.entityId().is(id)));
    }
  }

  @Override
  public <T> T fetch(final Id id, final Func.Uni<? super Many<Transaction>, ? extends T> fetch) {
    synchronized (lock) {
      return fetch.apply(transactions.getAcquire().where(transaction -> transaction.entityId().is(id)));
    }
  }

  @Override
  public Transaction[] tryGet() {
    synchronized (lock) {
      return transactions.getAcquire().asArrayOf(Transaction.class);
    }
  }

  private enum Sync {Lock}
}
