// If you have to acquire a resource and want to guarantee that some cleanup action is run if the resource is acquired, use the bracket function:

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

// def bracket[F[_], R](acquire: F[R])(release: R => F[Unit]): Stream[F, R]

val err = Stream.raiseError[IO](new Exception("oh noes!"))

val count = new java.util.concurrent.atomic.AtomicLong(0)
// count: java.util.concurrent.atomic.AtomicLong = 0
val acquire = IO { println("incremented: " + count.incrementAndGet); () }
// acquire: IO[Unit] = IO(...)
val release = IO { println("decremented: " + count.decrementAndGet); () }

Stream.bracket(acquire)(_ => release).flatMap(_ => Stream(1,2,3) ++ err).compile.drain.unsafeRunSync()
// The inner stream fails, but notice the release action is still run