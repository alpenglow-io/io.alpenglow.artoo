package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.calisthenics.crud.Addable;
import dev.lug.oak.calisthenics.crud.FindableById;
import dev.lug.oak.calisthenics.crud.Id;
import dev.lug.oak.calisthenics.crud.RemovableById;
import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.quill.single.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.quill.query.Queryable.from;
import static java.util.Arrays.copyOf;
import static java.util.UUID.randomUUID;

public interface Currencies extends FindableById<Currency>, Addable<Currency>, RemovableById<Currency> {
  @NotNull
  @Contract("_ -> new")
  static Currencies of(final Currency... currencies) {
    return new ManyCurrencies(copyOf(currencies, currencies.length));
  }

  @Override
  default Nullable<Currency> findBy(Id id) {
    return () -> from(this).first(it -> it.has(id)).iterator();
  }

  @Override
  default Nullable<Currency> add() {
    return () -> from(this)
      .concat(
        from(
          Currency.of(
            () -> randomUUID().toString(),
            () -> "",
            () -> 0.0
          )
        )
      ).iterator();
  }

  @Override
  default Nullable<Currency> remove(Id id) {
    return Cursor::none;
  }
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
}
