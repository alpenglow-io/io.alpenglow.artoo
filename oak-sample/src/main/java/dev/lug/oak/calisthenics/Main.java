package dev.lug.oak.calisthenics;

import dev.lug.oak.calisthenics.currency.Currencies;
import dev.lug.oak.calisthenics.currency.Currency;

import static dev.lug.oak.calisthenics.Main.DefaultCurrency.Dollar;
import static dev.lug.oak.calisthenics.Main.DefaultCurrency.Lira;
import static dev.lug.oak.calisthenics.Main.DefaultCurrency.Pound;
import static java.lang.System.out;

public interface Main {

  enum DefaultCurrency implements Currency.Name {
    Lira("Lira"), Dollar("Dollar"), Pound("Pound");

    private final String name;

    DefaultCurrency(final String name) {
      this.name = name;
    }

    @Override
    public final String eval() {
      return name;
    }
  }

  static void main(String... args) {
    final var lira = new Currency.Entry(
      () -> "asd",
      Lira,
      () -> 1000.0
    );
    final var dollaro = new Currency.Entry(
      () -> "dsa",
      Dollar,
      () -> 10.0
    );

    Currencies.from(lira, dollaro)
      .one(() -> "dsa")
      .change(Pound)
      .increase(() -> 32.3)
      .eventually(out::println);
  }
}
