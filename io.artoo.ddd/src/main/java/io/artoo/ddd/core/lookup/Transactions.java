package io.artoo.ddd.core.lookup;

import io.artoo.ddd.core.Domain;
import io.artoo.lance.value.Id;
import io.artoo.ddd.core.Service;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.value.Symbol;
import io.artoo.lance.value.Table;

import java.time.Instant;

public interface Transactions extends Many<Domain.Fact> {
  Transactions state(Id model);

  Transactions submit(Id model, Domain.Fact fact);
}

final class Ledger implements Transactions {
  private final Table<Transaction> transactions;
  private final Service.Bus bus;

  Ledger(final Service.Bus bus) {
    this(
      Table.local(),
      bus
    );
  }

  private Ledger(final Table<Transaction> transactions, final Service.Bus bus) {
    this.transactions = transactions;
    this.bus = bus;
  }

  @Override
  public State state(final Id model) {
    return new Dump(this, model);
  }

  @Override
  public Transactions submit(final Id model, final Domain.Fact fact, final String stateAlias) {
    return One
      .lone(new Transaction(model, fact, stateAlias))
      .peek(tx -> transactions.put(tx.transactionId, tx))
      .eventually(this);
  }

  @Override
  public Cursor<Domain.Fact> cursor() {
    return Many.from(transactions.values())
      .select(Transaction::fact)
      .cursor();
  }

  record Transaction(String factName, Domain.Fact fact, Id stateId, String stateAlias, Instant persistedAt) {
    public Transaction(Id stateId, Domain.Fact fact, String stateAlias) {
      this(Symbol.unique(), fact.$name(), fact, stateId, stateAlias, Instant.now());
    }
  }
}
