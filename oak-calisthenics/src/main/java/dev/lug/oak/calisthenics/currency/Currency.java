package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.quill.single.One;
import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface Currency {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new SingleCurrency(id.eval(), name.eval(), amount.eval());
  }

  Currency edit(Name name, Amount amount);
  Id id();
  One<Amount> amount();

  interface Id extends AsString {}
  interface Name extends AsString {}
  interface Amount extends AsDouble {}
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
  public Currency edit(Name name, Amount amount) {
    return new SingleCurrency(id, name.eval(), amount.eval());
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public Id id() {
    return () -> id;
  }

  @Override
  public final One<Amount> amount() {
    return One.of(amount).select(it -> () -> it);
  }
}
