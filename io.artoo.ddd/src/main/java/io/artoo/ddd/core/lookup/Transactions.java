package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.core.Service;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.value.Symbol;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Transactions extends Many<Domain.Fact> {
  State state(Id stateId);

  Transactions submit(Id stateId, Domain.Fact fact, String stateAlias);
  default Transactions submit(Id id, Domain.Fact fact) {
    return submit(id, fact, null);
  }

  record EventLog(String factName, Domain.Fact fact, Id stateId, String stateAlias, Instant persistedAt, Instant emittedAt) {}
}

final class Ledger implements Transactions {
  private final Map<Symbol, Transaction> transactions;
  private final Service.Bus bus;

  Ledger(final Service.Bus bus) {
    this(
      new ConcurrentHashMap<>(),
      bus
    );
  }

  private Ledger(final Map<Symbol, Transaction> transactions, final Service.Bus bus) {
    this.transactions = transactions;
    this.bus = bus;
  }

  @Override
  public State state(final Id stateId) {
    return new Dump(this, stateId);
  }

  @Override
  public Transactions submit(final Id stateId, final Domain.Fact fact, final String stateAlias) {
    return One
      .lone(new Transaction(stateId, fact, stateAlias))
      .peek(tx -> transactions.put(tx.transactionId, tx))
      .eventually(this);
  }

  @Override
  public Cursor<Domain.Fact> cursor() {
    return Many.from(transactions.values())
      .select(Transaction::fact)
      .cursor();
  }

  record Transaction(Symbol transactionId, String factName, Domain.Fact fact, Id stateId, String stateAlias, Instant persistedAt) {
    public Transaction(Id stateId, Domain.Fact fact, String stateAlias) {
      this(Symbol.unique(), fact.$name(), fact, stateId, stateAlias, Instant.now());
    }
  }
}
