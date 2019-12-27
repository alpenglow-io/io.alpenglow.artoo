package dev.lug.oak.calisthenics.currency;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public final class RawCurrency {
  private final String id;
  private final String name;
  private final double amount;

  @Contract(pure = true)
  public RawCurrency(String id, String name, double amount) {
    this.id = id;
    this.name = name;
    this.amount = amount;
  }

  @Contract(pure = true)
  public String id() {
    return id;
  }

  @Contract(pure = true)
  public String name() {
    return name;
  }

  @Contract(pure = true)
  public double amount() {
    return amount;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final var that = (RawCurrency) o;

    if (Double.compare(that.amount, amount) != 0) return false;
    if (!Objects.equals(id, that.id)) return false;
    return Objects.equals(name, that.name);
  }

  @Override
  public final int hashCode() {
    int result;
    long temp;
    result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    temp = Double.doubleToLongBits(amount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public final String toString() {
    return String.format("Currency {id='%s', name='%s', amount=%s}", id, name, amount);
  }
}
