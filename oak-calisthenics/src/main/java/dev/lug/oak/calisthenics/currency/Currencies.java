package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.quill.query.Queryable;
import dev.lug.oak.quill.single.Nullable;
import dev.lug.oak.type.AsArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.quill.Q.P.just;
import static dev.lug.oak.quill.query.Queryable.from;
import static java.util.Arrays.copyOf;

public interface Currencies extends Queryable<Currency> {
  @NotNull
  @Contract("_ -> new")
  static Currencies of(final RawCurrency... entries) {
    return new ManyCurrencies(copyOf(entries, entries.length));
  }

  Nullable<Currency> findBy(Currency.Id id);
}

interface CurrencyEntries extends Currencies, AsArray<RawCurrency> {
  @Override
  default Nullable<Currency> findBy(Currency.Id id) {
    return new FoundById(this, id);
  }
}

final class ManyCurrencies implements Currencies {
  private final RawCurrency[] currencies;

  @Contract(pure = true)
  ManyCurrencies(final RawCurrency... currencies) {
    this.currencies = currencies;
  }

  @NotNull
  @Override
  public final Iterator<Currency> iterator() {
    return from(currencies)
      .select(
        just(currency ->
          Currency.of(
            currency::id,
            currency::name,
            currency::amount
          )
        )
      ).iterator();
  }

  @Override
  public final Nullable<Currency> findBy(Currency.Id id) {
    return from(currencies)
      .where(entry -> entry.id().equals(id.eval()))
      .first()
      .select(entry -> Currency.of(
        entry::id,
        entry::name,
        entry::amount
      ));
  }
}


final class FoundById implements Nullable<Currency> {
  private final CurrencyEntries entries;
  private final Currency.Id id;

  FoundById(final CurrencyEntries entries, final Currency.Id id) {
    this.entries = entries;
    this.id = id;
  }

  @NotNull
  @Override
  public final Iterator<Currency> iterator() {
    return from(entries.eval())
      .where(currency -> currency.id().equals(id.eval()))
      .first()
      .select(currency ->
        Currency.of(
          currency::id,
          currency::name,
          currency::amount
        )
      )
      .iterator();
  }
}
