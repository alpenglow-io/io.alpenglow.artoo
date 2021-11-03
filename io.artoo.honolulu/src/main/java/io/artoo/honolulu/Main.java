package io.artoo.honolulu;

import static javafx.application.Application.launch;

public sealed interface Main {
  static void main(String[] args) {
    launch(Honolulu.class);
  }

  enum Namespace implements Main {}
}
