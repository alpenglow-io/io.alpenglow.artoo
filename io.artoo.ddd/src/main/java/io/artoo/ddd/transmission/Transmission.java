package io.artoo.ddd.transmission;

import io.artoo.ddd.domain.Domain;

import java.util.List;

public enum Transmission {;
  interface RequireProperty extends Domain.Command<RequireProperty.Params> { record Params() {} }
  interface RequireProperties extends Domain.Command<RequireProperties.Params> {
    record Params(String... properties) {}
  }
}
