package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.query.many.Queryable.from;
import static java.util.Arrays.copyOf;

public interface Currencies extends Iterable<Currency> {
  @NotNull
  @Contract("_ -> new")
  static Currencies of(final Currency... entries) {
    return new ManyCurrencies(copyOf(entries, entries.length));
  }

  One<Currency> one(Currency.Id id);
}

final class ManyCurrencies implements Currencies {
  private final Currency[] currencies;

  @Contract(pure = true)
  ManyCurrencies(final Currency... currencies) {
    this.currencies = currencies;
  }

  @NotNull
  @Override
  public final Iterator<Currency> iterator() {
    return Cursor.of(currencies);
  }

  @Override
  public final One<Currency> one(Currency.Id id) {
    return from(currencies).first(currency -> currency.id().is(id));
  }
}
