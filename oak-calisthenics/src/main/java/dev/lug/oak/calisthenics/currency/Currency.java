package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currency {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new SingleCurrency(id.eval(), name.eval(), amount.eval());
  }

  Currency replace(Name name);

  interface Id extends AsString {}
  interface Name extends AsString {}
  interface Amount extends AsDouble {}

  final class Entry {
    private final Id id;
    private final Name name;
    private final Amount amount;

    public Entry(final Id id, final Name name, final Amount amount) {
      this.id = id;
      this.name = name;
      this.amount = amount;
    }

    public Id id() {
      return id;
    }

    public Name name() {
      return name;
    }

    public Amount amount() {
      return amount;
    }
  }
}

final class SingleCurrency implements Currency {
  private final String id;
  private final String name;
  private final double amount;

  SingleCurrency(String id, String name, double amount) {
    this.id = id;
    this.name = name;
    this.amount = amount;
  }

  @Override
  public Currency replace(Name name) {
    return new SingleCurrency(id, name.eval(), amount);
  }
}
