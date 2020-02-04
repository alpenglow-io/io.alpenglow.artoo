package dev.lug.oak.sample.currency;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.pre.Predicate1;
import dev.lug.oak.query.Many;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.of(entries);
  }

  default Currency one(final Currency.Id id) {
    return this
      .single(entry -> entry.id().is(id))
      .as(Currency::from);
  }
}

interface IdPredicate extends Predicate1<Currency.Id> {}
