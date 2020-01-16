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
  static Currencies from(final Currency.Entry... entries) {
    return new ManyCurrencies(copyOf(entries, entries.length));
  }

  One<Currency> one(Currency.Id id);
}

final class ManyCurrencies implements Currencies {
  private final Currency.Entry[] currencies;

  @Contract(pure = true)
  ManyCurrencies(final Currency.Entry... currencies) {
    this.currencies = currencies;
  }

  @NotNull
  @Override
  public final Iterator<Currency> iterator() {
    return Cursor.none();
  }

  @Override
  public final One<Currency> one(Currency.Id id) {
    return One.none();
  }
}
