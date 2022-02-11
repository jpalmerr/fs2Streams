import fs2.Stream

Stream.range(0,100).takeWhile(_ < 7).toList
// List[Int] = List(0, 1, 2, 3, 4, 5, 6)
Stream("Alice","Bob","Carol").intersperse("|").toList
// List[String] = List(Alice, |, Bob, |, Carol)
Stream.range(1,10).scan(0)(_ + _).toList // running sum
// List(0, 1, 3, 6, 10, 15, 21, 28, 36, 45)