package io.artoo.parry;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class InternalThreadLocalMap {
  private static final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<>();
  private static final AtomicInteger nextIndex = new AtomicInteger();
  private static final int DEFAULT_ARRAY_LIST_INITIAL_CAPACITY = 8;
  private static final int STRING_BUILDER_INITIAL_SIZE = 1024;
  private static final int STRING_BUILDER_MAX_SIZE;
  private static final int HANDLER_SHARABLE_CACHE_INITIAL_CAPACITY = 4;
  private static final int INDEXED_VARIABLE_TABLE_INITIAL_SIZE = 32;
  public static final Object UNSET = new Object();
  private Object[] indexedVariables = newIndexedVariableTable();
  private int futureListenerStackDepth;
  private int localChannelReaderStackDepth;
  private Map<Class<?>, Boolean> handlerSharableCache;
  private Integer counterHashCode;
  private ThreadLocalRandom random;
  private Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
  private Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;
  private StringBuilder stringBuilder;
  private Map<Charset, CharsetEncoder> charsetEncoderCache;
  private Map<Charset, CharsetDecoder> charsetDecoderCache;
  private ArrayList<Object> arrayList;
  private BitSet cleanerFlags;
  /**
   * @deprecated
   */
  public long rp1;
  /**
   * @deprecated
   */
  public long rp2;
  /**
   * @deprecated
   */
  public long rp3;
  /**
   * @deprecated
   */
  public long rp4;
  /**
   * @deprecated
   */
  public long rp5;
  /**
   * @deprecated
   */
  public long rp6;
  /**
   * @deprecated
   */
  public long rp7;
  /**
   * @deprecated
   */
  public long rp8;
  /**
   * @deprecated
   */
  public long rp9;

  public static InternalThreadLocalMap getIfSet() {
    var thread = Thread.currentThread();
    return thread instanceof FastThreadLocalThread ? ((FastThreadLocalThread) thread).threadLocalMap() : slowThreadLocalMap.get();
  }

  public static InternalThreadLocalMap get() {
    var thread = Thread.currentThread();
    return thread instanceof FastThreadLocalThread ? fastGet((FastThreadLocalThread) thread) : slowGet();
  }

  private static InternalThreadLocalMap fastGet(FastThreadLocalThread thread) {
    var threadLocalMap = thread.threadLocalMap();
    if (threadLocalMap == null) {
      thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());
    }

    return threadLocalMap;
  }

  private static InternalThreadLocalMap slowGet() {
    var ret = (InternalThreadLocalMap) slowThreadLocalMap.get();
    if (ret == null) {
      ret = new InternalThreadLocalMap();
      slowThreadLocalMap.set(ret);
    }

    return ret;
  }

  public static void remove() {
    var thread = Thread.currentThread();
    if (thread instanceof FastThreadLocalThread) {
      ((FastThreadLocalThread) thread).setThreadLocalMap((InternalThreadLocalMap) null);
    } else {
      slowThreadLocalMap.remove();
    }

  }

  public static void destroy() {
    slowThreadLocalMap.remove();
  }

  public static int nextVariableIndex() {
    var index = nextIndex.getAndIncrement();
    if (index < 0) {
      nextIndex.decrementAndGet();
      throw new IllegalStateException("too many thread-local indexed variables");
    } else {
      return index;
    }
  }

  public static int lastVariableIndex() {
    return nextIndex.get() - 1;
  }

  private InternalThreadLocalMap() {
  }

  private static Object[] newIndexedVariableTable() {
    var array = new Object[32];
    Arrays.fill(array, UNSET);
    return array;
  }

  public int size() {
    var count = 0;
    if (this.futureListenerStackDepth != 0) {
      ++count;
    }

    if (this.localChannelReaderStackDepth != 0) {
      ++count;
    }

    if (this.handlerSharableCache != null) {
      ++count;
    }

    if (this.counterHashCode != null) {
      ++count;
    }

    if (this.random != null) {
      ++count;
    }

    if (this.typeParameterMatcherGetCache != null) {
      ++count;
    }

    if (this.typeParameterMatcherFindCache != null) {
      ++count;
    }

    if (this.stringBuilder != null) {
      ++count;
    }

    if (this.charsetEncoderCache != null) {
      ++count;
    }

    if (this.charsetDecoderCache != null) {
      ++count;
    }

    if (this.arrayList != null) {
      ++count;
    }

    var var2 = this.indexedVariables;
    var var3 = var2.length;

    for (var var4 = 0; var4 < var3; ++var4) {
      var o = var2[var4];
      if (o != UNSET) {
        ++count;
      }
    }

    return count - 1;
  }

  public StringBuilder stringBuilder() {
    var sb = this.stringBuilder;
    if (sb == null) {
      return this.stringBuilder = new StringBuilder(STRING_BUILDER_INITIAL_SIZE);
    } else {
      if (sb.capacity() > STRING_BUILDER_MAX_SIZE) {
        sb.setLength(STRING_BUILDER_INITIAL_SIZE);
        sb.trimToSize();
      }

      sb.setLength(0);
      return sb;
    }
  }

  public Map<Charset, CharsetEncoder> charsetEncoderCache() {
    var cache = this.charsetEncoderCache;
    if (cache == null) {
      this.charsetEncoderCache = (cache = new IdentityHashMap<>());
    }

    return cache;
  }

  public Map<Charset, CharsetDecoder> charsetDecoderCache() {
    var cache = this.charsetDecoderCache;
    if (cache == null) {
      this.charsetDecoderCache = cache = new IdentityHashMap<>();
    }

    return cache;
  }

  public <E> ArrayList<E> arrayList() {
    return this.arrayList(8);
  }

  @SuppressWarnings("unchecked")
  public <E> ArrayList<E> arrayList(int minCapacity) {
    var list = (ArrayList<E>) this.arrayList;
    if (list == null) {
      this.arrayList = new ArrayList<>(minCapacity);
      return (ArrayList<E>) this.arrayList;
    } else {
      list.clear();
      list.ensureCapacity(minCapacity);
      return list;
    }
  }

  public int futureListenerStackDepth() {
    return this.futureListenerStackDepth;
  }

  public void setFutureListenerStackDepth(int futureListenerStackDepth) {
    this.futureListenerStackDepth = futureListenerStackDepth;
  }

  public ThreadLocalRandom random() {
    var r = this.random;
    if (r == null) {
      this.random = r = ThreadLocalRandom.current();
    }

    return r;
  }

  public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
    var cache = this.typeParameterMatcherGetCache;
    if (cache == null) {
      this.typeParameterMatcherGetCache = (Map) (cache = new IdentityHashMap());
    }

    return (Map) cache;
  }

  public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
    var cache = this.typeParameterMatcherFindCache;
    if (cache == null) {
      this.typeParameterMatcherFindCache = cache = new IdentityHashMap<>();
    }
    return cache;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public Integer counterHashCode() {
    return this.counterHashCode;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public void setCounterHashCode(Integer counterHashCode) {
    this.counterHashCode = counterHashCode;
  }

  public Map<Class<?>, Boolean> handlerSharableCache() {
    var cache = this.handlerSharableCache;
    if (cache == null) {
      this.handlerSharableCache = cache = new WeakHashMap<>(4);
    }
    return cache;
  }

  public int localChannelReaderStackDepth() {
    return this.localChannelReaderStackDepth;
  }

  public void setLocalChannelReaderStackDepth(int localChannelReaderStackDepth) {
    this.localChannelReaderStackDepth = localChannelReaderStackDepth;
  }

  public Object indexedVariable(int index) {
    var lookup = this.indexedVariables;
    return index < lookup.length ? lookup[index] : UNSET;
  }

  public boolean setIndexedVariable(int index, Object value) {
    var lookup = this.indexedVariables;
    if (index < lookup.length) {
      var oldValue = lookup[index];
      lookup[index] = value;
      return oldValue == UNSET;
    } else {
      this.expandIndexedVariableTableAndSet(index, value);
      return true;
    }
  }

  private void expandIndexedVariableTableAndSet(int index, Object value) {
    var oldArray = this.indexedVariables;
    var oldCapacity = oldArray.length;
    var newCapacity = index | index >>> 1;
    newCapacity |= newCapacity >>> 2;
    newCapacity |= newCapacity >>> 4;
    newCapacity |= newCapacity >>> 8;
    newCapacity |= newCapacity >>> 16;
    ++newCapacity;
    var newArray = Arrays.copyOf(oldArray, newCapacity);
    Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
    newArray[index] = value;
    this.indexedVariables = newArray;
  }

  public Object removeIndexedVariable(int index) {
    var lookup = this.indexedVariables;
    if (index < lookup.length) {
      var v = lookup[index];
      lookup[index] = UNSET;
      return v;
    } else {
      return UNSET;
    }
  }

  public boolean isIndexedVariableSet(int index) {
    var lookup = this.indexedVariables;
    return index < lookup.length && lookup[index] != UNSET;
  }

  public boolean isCleanerFlagSet(int index) {
    return this.cleanerFlags != null && this.cleanerFlags.get(index);
  }

  public void setCleanerFlag(int index) {
    if (this.cleanerFlags == null) {
      this.cleanerFlags = new BitSet();
    }

    this.cleanerFlags.set(index);
  }

  static {
    //logger.debug("-Dio.netty.threadLocalMap.stringBuilder.initialSize: {}", STRING_BUILDER_INITIAL_SIZE);
    STRING_BUILDER_MAX_SIZE = 4096;
    //logger.debug("-Dio.netty.threadLocalMap.stringBuilder.maxSize: {}", STRING_BUILDER_MAX_SIZE);
  }
}
