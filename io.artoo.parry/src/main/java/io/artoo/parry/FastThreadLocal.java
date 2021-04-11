package io.artoo.parry;

public class FastThreadLocal<V> {
  private static final int variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
  private final int index = InternalThreadLocalMap.nextVariableIndex();

  public static void removeAll() {
    InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
    if (threadLocalMap != null) {
      try {
        Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
        if (v != null && v != InternalThreadLocalMap.UNSET) {
          Set<FastThreadLocal<?>> variablesToRemove = (Set)v;
          FastThreadLocal<?>[] variablesToRemoveArray = (FastThreadLocal[])variablesToRemove.toArray(new FastThreadLocal[0]);
          FastThreadLocal[] var4 = variablesToRemoveArray;
          int var5 = variablesToRemoveArray.length;

          for(int var6 = 0; var6 < var5; ++var6) {
            FastThreadLocal<?> tlv = var4[var6];
            tlv.remove(threadLocalMap);
          }
        }
      } finally {
        InternalThreadLocalMap.remove();
      }

    }
  }

  public static int size() {
    InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
    return threadLocalMap == null ? 0 : threadLocalMap.size();
  }

  public static void destroy() {
    InternalThreadLocalMap.destroy();
  }

  private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
    Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
    Set variablesToRemove;
    if (v != InternalThreadLocalMap.UNSET && v != null) {
      variablesToRemove = (Set)v;
    } else {
      variablesToRemove = Collections.newSetFromMap(new IdentityHashMap());
      threadLocalMap.setIndexedVariable(variablesToRemoveIndex, variablesToRemove);
    }

    variablesToRemove.add(variable);
  }

  private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
    Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
    if (v != InternalThreadLocalMap.UNSET && v != null) {
      Set<FastThreadLocal<?>> variablesToRemove = (Set)v;
      variablesToRemove.remove(variable);
    }
  }

  public FastThreadLocal() {
  }

  public final V get() {
    InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
    Object v = threadLocalMap.indexedVariable(this.index);
    return v != InternalThreadLocalMap.UNSET ? v : this.initialize(threadLocalMap);
  }

  public final V getIfExists() {
    InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
    if (threadLocalMap != null) {
      Object v = threadLocalMap.indexedVariable(this.index);
      if (v != InternalThreadLocalMap.UNSET) {
        return v;
      }
    }

    return null;
  }

  public final V get(InternalThreadLocalMap threadLocalMap) {
    Object v = threadLocalMap.indexedVariable(this.index);
    return v != InternalThreadLocalMap.UNSET ? v : this.initialize(threadLocalMap);
  }

  private V initialize(InternalThreadLocalMap threadLocalMap) {
    Object v = null;

    try {
      v = this.initialValue();
    } catch (Exception var4) {
      PlatformDependent.throwException(var4);
    }

    threadLocalMap.setIndexedVariable(this.index, v);
    addToVariablesToRemove(threadLocalMap, this);
    return v;
  }

  public final void set(V value) {
    if (value != InternalThreadLocalMap.UNSET) {
      InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
      this.setKnownNotUnset(threadLocalMap, value);
    } else {
      this.remove();
    }

  }

  public final void set(InternalThreadLocalMap threadLocalMap, V value) {
    if (value != InternalThreadLocalMap.UNSET) {
      this.setKnownNotUnset(threadLocalMap, value);
    } else {
      this.remove(threadLocalMap);
    }

  }

  private void setKnownNotUnset(InternalThreadLocalMap threadLocalMap, V value) {
    if (threadLocalMap.setIndexedVariable(this.index, value)) {
      addToVariablesToRemove(threadLocalMap, this);
    }

  }

  public final boolean isSet() {
    return this.isSet(InternalThreadLocalMap.getIfSet());
  }

  public final boolean isSet(InternalThreadLocalMap threadLocalMap) {
    return threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index);
  }

  public final void remove() {
    this.remove(InternalThreadLocalMap.getIfSet());
  }

  public final void remove(InternalThreadLocalMap threadLocalMap) {
    if (threadLocalMap != null) {
      Object v = threadLocalMap.removeIndexedVariable(this.index);
      removeFromVariablesToRemove(threadLocalMap, this);
      if (v != InternalThreadLocalMap.UNSET) {
        try {
          this.onRemoval(v);
        } catch (Exception var4) {
          PlatformDependent.throwException(var4);
        }
      }

    }
  }

  protected V initialValue() throws Exception {
    return null;
  }

  protected void onRemoval(V value) throws Exception {
  }
}
