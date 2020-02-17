package dev.lug.oak.sample.currency;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.Pre;
import dev.lug.oak.query.Many;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.all(entries);
  }

  default Currency one(final Currency.Id id) {
    return this
      .single(entry -> entry.id().is(id))
      .as(Currency::from);
  }
}

interface IdPredicate extends Pre<Currency.Id> {}
