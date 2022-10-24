package io.artoo.anacleto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public sealed interface Actors {
  static Actors system() {
    return new System(Executors.newVirtualThreadPerTaskExecutor());
  }

  <MESSAGE> Address<MESSAGE> create(Function<Address<MESSAGE>, Behaviour<MESSAGE>> initial);

  final class System implements Actors {
    private final ExecutorService service;

    private System(ExecutorService service) {
      this.service = service;
    }

    @Override
    public <MESSAGE> Address<MESSAGE> create(Function<Address<MESSAGE>, Behaviour<MESSAGE>> initial) {
      return null;
    }
  }
}
