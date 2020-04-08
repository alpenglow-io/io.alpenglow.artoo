package trydent.sample.currency;

import trydent.cursor.Cursor;
import trydent.query.One;
import trydent.type.AsDouble;
import trydent.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public interface Currency extends One<Currency.Entry> {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return () -> Cursor.of(new Entry(id, name, amount));
  }

  @NotNull
  @Contract(pure = true)
  static Currency from(final Currency.Entry entry) {
    return () -> Cursor.of(entry);
  }

  default Currency change(Name name) {
    return () -> this
      .select(as(currency -> new Currency.Entry(
          currency.id,
          name,
          currency.amount
        )
      )).iterator();
  }

  default Currency increase(Amount amount) {
    return () -> this
      .select(as(currency -> new Currency.Entry(
          currency.id,
          currency.name,
          () -> amount.eval() + currency.amount.eval()
        )
      )).iterator();
  }

  final class Id implements AsString {
    private final String value;

    private Id(String value) {this.value = value;}

    @Override
    public final String eval() {
      return value;
    }

    public static One<Id> from(final String value) {
      return One.of(value)
        .where(it -> it.length() > 3)
        .select(as(Id::new));
    }
  }

  interface Name extends AsString {
  }

  interface Amount extends AsDouble {
  }

  final class Entry {
    public final Id id;
    public final Name name;
    public final Amount amount;

    public Entry(Id id, Name name, Amount amount) {
      this.id = id;
      this.name = name;
      this.amount = amount;
    }

    @Override
    public final String toString() {
      return String.format("Currency.Entry{ id=%s, name=%s, amount=%s }", id, name, amount);
    }

    @Override
    public final boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      var entry = (Entry) o;

      if (!Objects.equals(id, entry.id))
        return false;
      if (!Objects.equals(name, entry.name))
        return false;
      return Objects.equals(amount, entry.amount);
    }

    @Override
    public final int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (name != null ? name.hashCode() : 0);
      result = 31 * result + (amount != null ? amount.hashCode() : 0);
      return result;
    }
  }
}
