package example
object Anonfun {
  var firstname = "John"
  def hello = (s: String) => s + firstname

  def main(args: Array[String]) {
    println(firstname)
    println(hello("Hello "))
    firstname = "Hans" // [2]
    println(hello("Hallo "))

    val ciao = hello
    println(ciao("Ciao "))
  }
}
