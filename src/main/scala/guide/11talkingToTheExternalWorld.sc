import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global

// functions which execute side effects synchronously

  // Just wrap these effects in a Stream.eval

def destroyUniverse(): Unit = { println("BOOM!!!")}

val s = Stream.exec(IO { destroyUniverse() }) ++ Stream("...moving on")

s.compile.toVector.unsafeRunSync()

  //  Sync.delay can be used for this generally can bring synchronous effect type

import cats.effect.Sync

val T = Sync[IO]

val s2 = Stream.exec(T.delay { destroyUniverse() }) ++ Stream("...moving on")

s2.compile.toVector.unsafeRunSync()

  // be sure the expression you pass to delay doesn't throw exceptions

// Functions which execute effects asynchronously, and invoke a callback once when completed

  // Async will come in handy

trait Connection {
  def readBytes(onSuccess: Array[Byte] => Unit, onFailure: Throwable => Unit): Unit

  def readBytesE(onComplete: Either[Throwable,Array[Byte]] => Unit): Unit =
    readBytes(bs => onComplete(Right(bs)), e => onComplete(Left(e)))

  override def toString = "<connection>"
}

val c = new Connection:
  override def readBytes(onSuccess: Array[Byte] => Unit, onFailure: Throwable => Unit): Unit = {
    Thread.sleep(200)
    onSuccess(Array(0,1,2))
  }

val bytes = IO.async_[Array[Byte]]( cb => c.readBytesE(cb))

Stream.eval(bytes).map(_.toList).compile.toVector.unsafeRunSync()

// Functions which execute effects asynchronously, and invoke a callback one or more times as results become available
