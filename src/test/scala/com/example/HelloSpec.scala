package com.example

import org.scalatest.{FlatSpec, Matchers}

class HelloSpec extends FlatSpec with Matchers {
  "The Hello object" should "say hello" in {
    WordGenerator.greeting shouldEqual "hello"
  }
}
