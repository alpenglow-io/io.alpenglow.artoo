package re.artoo.lance.experimental.value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

interface AutoUnlock extends AutoCloseable {
  static AutoUnlock writeLock(ReentrantReadWriteLock lock) { return new WriteLock(lock); }
  static AutoUnlock readLock(ReentrantReadWriteLock lock) { return new ReadLock(lock); }

  Lock lock();

  @Override
  default void close() {
    lock().unlock();
  }
}

record WriteLock(Lock lock) implements AutoUnlock {
  WriteLock(ReadWriteLock readWrite) {
    this(readWrite.writeLock());
  }
}

record ReadLock(Lock lock) implements AutoUnlock {
  ReadLock(ReadWriteLock readWrite) {
    this(readWrite.readLock());
  }
}
