import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream


val err = Stream.raiseError[IO](new Exception("oh noes!"))

val err2 = Stream(1,2,3) ++ (throw new Exception("!@#$"))

val err3 = Stream.eval(IO(throw new Exception("error in effect!!!")))

// these fail when running
try err.compile.toList.unsafeRunSync() catch { case e: Exception => println(e) }

// handleErrorWith lets us catch these errors

err.handleErrorWith { e => Stream.emit(e.getMessage) }.compile.toList.unsafeRunSync()

// when using handleErrorWith (or attempt) the stream will be terminated after the error and no more values will be pulled. In the following example, the integer 4 is never pulled from the stream

val err4 = Stream(1,2,3).covary[IO] ++
  Stream.raiseError[IO](new Exception("bad things!")) ++
  Stream.eval(IO(4))

err4.handleErrorWith { _ => Stream(0) }.compile.toList.unsafeRunSync()