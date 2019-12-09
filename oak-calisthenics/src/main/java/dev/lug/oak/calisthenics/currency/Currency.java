package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.calisthenics.crud.Id;
import dev.lug.oak.quill.single.Nullable;
import dev.lug.oak.quill.tuple.Tuple;
import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.quill.tuple.Tuple.areNonNull;

@SuppressWarnings("UnusedReturnValue")
public interface Currency {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new SingleCurrency(id.eval(), name.eval(), amount.eval());
  }

  boolean has(Id id);
  Nullable<Currency> set(Name name, Amount amount);

  interface Name extends AsString {}
  interface Amount extends AsDouble {}
}

final class SingleCurrency implements Currency {
  private final String id;
  private final String name;
  private final double amount;

  SingleCurrency(final String id, final String name, final double amount) {
    this.id = id;
    this.name = name;
    this.amount = amount;
  }

  @Override
  public boolean has(@NotNull Id id) {
    return this.id.equals(id.eval());
  }

  @Override
  public Nullable<Currency> set(Name name, Amount amount) {
    return Tuple.of(name, amount)
      .where(areNonNull())
      .select((n, a) -> new SingleCurrency(id, n.eval(), a.eval()));
  }

  @Override
  public String toString() {
    return String.format("Currency {id='%s', name='%s', amount=%s}", id, name, amount);
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    var currency = (SingleCurrency) o;

    if (Double.compare(currency.amount, amount) != 0) return false;
    if (!id.equals(currency.id)) return false;
    return name.equals(currency.name);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id.hashCode();
    result = 31 * result + name.hashCode();
    temp = Double.doubleToLongBits(amount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
