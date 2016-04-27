## Requirements

Java 8

## Building

./gradlew test 

## Running

./gradlew bootRun

## Assumptions

As the specification is very basic, I've made certain assumptions which are probably wrong but seem appropriate for
a coding exercise.

## Validation

I've added some validation rules on the basis that validation should be a strict as possible.

## Search

I wasn't at all sure what you want the exercise to illustrate e.g. to design a search from first principles or
use a 3rd party library. I've gone for the latter as that's what I would do in practice.

I've limited the number of matches to an arbitrary number viz 20.

## Error handling

Error handling is not specified. In the case of invalid input(s) I'm returning an array or error messages with status code 400.

## Other notes

Comments are only added if code is not self documenting.



