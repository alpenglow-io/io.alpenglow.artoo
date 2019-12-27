package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.quill.single.Nullable;
import dev.lug.oak.quill.tuple.Tuple;
import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static dev.lug.oak.quill.tuple.Tuple.areNonNull;

@SuppressWarnings("UnusedReturnValue")
public interface Currency {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new SingleCurrency(record(id, name, amount));
  }

  @NotNull
  @Contract("_, _, _ -> new")
  static RawCurrency record(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new RawCurrency(
      id.eval(),
      name.eval(),
      amount.eval()
    );
  }

  Nullable<Currency> edit(Name name, Amount amount);

  interface Id extends AsString {}
  interface Name extends AsString {}
  interface Amount extends AsDouble {}
}

final class SingleCurrency implements Currency {
  private final RawCurrency currency;

  public SingleCurrency(final RawCurrency currency) {
    this.currency = currency;
  }

  @Override
  public Nullable<Currency> edit(Name name, Amount amount) {
    return Tuple.of(name, amount)
      .where(areNonNull())
      .select((n, a) -> new SingleCurrency(Currency.record(currency::id, n, a)));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var that = (SingleCurrency) o;
    return Objects.equals(currency, that.currency);
  }

  @Override
  public int hashCode() {
    return currency != null ? currency.hashCode() : 0;
  }

  @Override
  public String toString() {
    return currency.toString();
  }
}
