package re.artoo.lance.value;

import re.artoo.lance.func.InvokeException;
import re.artoo.lance.func.TryRunnable;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.One;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static re.artoo.lance.value.AutoUnlock.readLock;
import static re.artoo.lance.value.AutoUnlock.writeLock;


public sealed interface Lock {
  static Lock reentrant() { return Reentrant.ReadWrite; }

  <T> One<T> read(TrySupplier1<? extends T> operation);

  <T> One<T> write(TrySupplier1<? extends T> operation);

  boolean write(TryRunnable operation) throws Throwable;

  boolean read(TryRunnable operation) throws Throwable;

}

enum Reentrant implements Lock {
  ReadWrite(new ReentrantReadWriteLock());
  private final transient ReentrantReadWriteLock reentrant;

  Reentrant(ReentrantReadWriteLock lock) {
    this.reentrant = lock;
  }

  @Override
  public <T> One<T> read(TrySupplier1<? extends T> operation) {
    var read = reentrant.readLock();
    read.lock();
    try {
      return One.of(operation.invoke());
    } catch (Throwable e) {
      return One.gone("Can't read from operation", it -> new InvokeException(it, e));
    } finally {
      read.unlock();
    }
  }

  @Override
  public <T> One<T> write(TrySupplier1<? extends T> operation) {
    var written = reentrant.writeLock();
    written.lock();
    try {
      return One.of(operation.invoke());
    } catch (Throwable e) {
      return One.gone("Can't read from operation", it -> new InvokeException(it, e));
    } finally {
      written.unlock();
    }
  }

  @Override
  public boolean write(TryRunnable operation) throws Throwable {
    try (final var ignored = writeLock(reentrant)) {
      operation.invoke();
      return true;
    }
  }

  @Override
  public boolean read(TryRunnable operation) throws Throwable {
    try (final var ignored = readLock(reentrant)) {
      operation.invoke();
      return true;
    }
  }
}

