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

import static java.util.stream.Collectors.groupingBy;

public interface Ledger extends Many<Ledger.Past> {
  State state(Id stateId);

  Ledger attach(Id stateId, Domain.Fact fact, String stateAlias);

  default Ledger attach(Id id, Domain.Fact fact) {
    return attach(id, fact, null);
  }

  record Past(Id stateId, Change... changes) {}
  record Change(String factName, Domain.Fact fact, Instant persistedAt) {}
}

final class InMemory implements Ledger {
  private final Map<Symbol, Transaction> transactions;
  private final Service.Bus bus;

  InMemory(final Service.Bus bus) {
    this(
      new ConcurrentHashMap<>(),
      bus
    );
  }

  private InMemory(final Map<Symbol, Transaction> transactions, final Service.Bus bus) {
    this.transactions = transactions;
    this.bus = bus;
  }

  @Override
  public State state(final Id stateId) {
    return new Dump(this, stateId);
  }

  @Override
  public Ledger attach(final Id stateId, final Domain.Fact fact, final String stateAlias) {
    return One
      .lone(new Transaction(stateId, fact, stateAlias))
      .peek(tx -> transactions.put(tx.transactionId, tx))
      .eventually(this);
  }

  @Override
  public Cursor<Past> cursor() {
    return Cursor.iteration(
      transactions.values().stream()
        .collect(groupingBy(tx -> tx.stateId))
        .entrySet().stream()
        .map(entry ->
          new Past(
            entry.getKey(),
            entry.getValue().stream()
              .map(tx -> new Change(tx.factName, tx.fact, tx.persistedAt))
              .toArray(Change[]::new)
          )
        )
        .iterator()
    );
  }

  record Transaction(Symbol transactionId, String factName, Domain.Fact fact, Id stateId, String stateAlias, Instant persistedAt) {
    public Transaction(Id stateId, Domain.Fact fact, String stateAlias) {
      this(Symbol.unique(), fact.$name(), fact, stateId, stateAlias, Instant.now());
    }
  }
}
