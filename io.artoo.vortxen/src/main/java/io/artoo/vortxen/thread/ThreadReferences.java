package io.artoo.vortxen.thread;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.IntegerHolder;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.TypeParameterMatcher;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ThreadReferences {

  static final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<InternalThreadLocalMap>();
  static final AtomicInteger nextIndex = new AtomicInteger();

  /** Used by {@link FastThreadLocal} */
  Object[] indexedVariables;

  // Core thread-locals
  int futureListenerStackDepth;
  int localChannelReaderStackDepth;
  Map<Class<?>, Boolean> handlerSharableCache;
  IntegerHolder counterHashCode;
  ThreadLocalRandom random;
  //Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
  //Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;

  // String-related thread-locals
  StringBuilder stringBuilder;
  Map<Charset, CharsetEncoder> charsetEncoderCache;
  Map<Charset, CharsetDecoder> charsetDecoderCache;

  // ArrayList-related thread-locals
  ArrayList<Object> arrayList;

  ThreadReferences(Object[] indexedVariables) {
    this.indexedVariables = indexedVariables;
  }
}
