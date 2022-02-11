import fs2.Stream

val s0 = Stream.empty
val s1 = Stream.emit(1)
val s1a = Stream(1, 2, 3)
val s1b = Stream.emits(List(1, 2, 3))

s1.toList // List(1)

(Stream(1,2,3) ++ Stream(4,5)).toList
Stream(1,2,3).map(_ + 1).toList
Stream(1,2,3).filter(_ % 2 != 0).toList
Stream(1,2,3).fold(0)(_ + _).toList
Stream(None,Some(2),Some(3)).collect { case Some(i) => i }.toList
Stream.range(0,5).intersperse(42).toList
Stream(1,2,3).flatMap(i => Stream(i,i)).toList
Stream(1,2,3).repeat.take(9).toList
Stream(1,2,3).repeatN(2).toList

// can also include evaluation of effects

import cats.effect.IO

// FS2 does not care what effect type you use for your streams

// any stream using .eval is effectful => can't be run using .toList

val eff: Stream[IO, Int] = Stream.eval(IO { println("BEING RUN!!"); 1 + 1 })

import cats.effect.unsafe.implicits.global
eff.compile.toList.unsafeRunSync()

val ra = eff.compile.toVector // gather all output into a Vector
// ra: IO[Vector[Int]] = IO(...) // gather all output into a Vector
val rb = eff.compile.drain // purely for effects
// rb: IO[Unit] = IO(...) // purely for effects
val rc = eff.compile.fold(0)(_ + _) // run and accumulate some result
// rc: IO[Int] = IO(...)

// notice all these return IO but don't actually perform ^

ra.unsafeRunSync()
rb.unsafeRunSync()
rc.unsafeRunSync()
rc.unsafeRunSync()
