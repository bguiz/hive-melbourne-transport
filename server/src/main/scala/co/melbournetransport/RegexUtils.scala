package co.melbournetransport

import scala.util.matching.Regex

object RegexUtils {
  
  //Option Regexes
	val InstructionRegex = """(.*) Time:(.*)\z""".r
	val ArrivalRegex = """To (.*)\z""".r
	val TimeRegex = """Time:(.*)\z""".r
	val ZoneRegex  = """Zone\(s\):(.*)\z""".r
	val OperatorRegex  = """Operator:(.*)\z""".r
	val FrequencyRegex = """Frequency:(.*)\z""".r
	val DepartRegex = """Depart:(.*)\z""".r
	val ArriveRegex = """Arrive:(.*)\z""".r
	val DurationRegex = """Duration:(.*)\z""".r
	val OptionRegex = """(\w):(.*)\z""".r
	
  class RichRegex(underlying: Regex) {
    def matches(s: String) = underlying.pattern.matcher(s).matches
  }
  implicit def regexToRichRegex(r: Regex) = new RichRegex(r)
}