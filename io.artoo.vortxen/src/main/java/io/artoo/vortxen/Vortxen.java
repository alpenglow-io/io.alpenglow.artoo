package io.artoo.vortxen;

import io.vertx.core.Vertx;

public class Vortxen {
  public static void main(String[] args) {
    final var vertx = Vertx.vertx();
    vertx.eventBus();
  }
}
