package io.artoo.parry.util;

import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public enum Empty {
  Arrays;

  public final int[] EMPTY_INTS = {};
  public final byte[] EMPTY_BYTES = {};
  public final char[] EMPTY_CHARS = {};
  public final Object[] EMPTY_OBJECTS = {};
  public final Class<?>[] EMPTY_CLASSES = {};
  public final String[] EMPTY_STRINGS = {};
  public final AsciiString[] EMPTY_ASCII_STRINGS = {};
  public final StackTraceElement[] EMPTY_STACK_TRACE = {};
  public final ByteBuffer[] EMPTY_BYTE_BUFFERS = {};
  public final Certificate[] EMPTY_CERTIFICATES = {};
  public final X509Certificate[] EMPTY_X509_CERTIFICATES = {};
  public final javax.security.cert.X509Certificate[] EMPTY_JAVAX_X509_CERTIFICATES = {};
}
