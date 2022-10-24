package io.artoo.anacleto;

public interface Address<MESSAGE> {
  Address<MESSAGE> send(MESSAGE message);
}
