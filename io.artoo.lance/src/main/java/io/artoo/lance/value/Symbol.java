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
        MessageDigest.getInstance("MD5")
      );
    } catch (NoSuchAlgorithmException cause) {
      throw new IllegalStateException(cause);
    }
  }
}

record Unique(Long value) implements Symbol {
  Unique(UUID uuid, MessageDigest md5) {
    this(new BigInteger(1, md5.digest(uuid.toString().getBytes(UTF_8))).longValue());
  }

  @Override
  public Long tryGet() {
    return value;
  }
}
