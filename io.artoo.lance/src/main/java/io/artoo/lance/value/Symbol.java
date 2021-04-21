package io.artoo.lance.value;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface Symbol extends Value<Long, Symbol> {
  static Symbol unique() {
    try {
      return new Unique(
        UUID.randomUUID(),
        MessageDigest.getInstance("SHA-512")
      );
    } catch (NoSuchAlgorithmException cause) {
      throw new IllegalStateException(cause);
    }
  }
  long value();
}

record Unique(long value) implements Symbol {
  Unique(UUID uuid, MessageDigest sha512) {
    this(new BigInteger(1, sha512.digest(uuid.toString().getBytes(UTF_8))).longValue());
  }

  @Override
  public Long tryGet() {
    return value;
  }
}
