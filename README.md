# Coding Challenges

Ho deciso di dedicarmi a delle coding challenges per migliorare le mie capacità di problem solving e di ingegneria del software.

In queste pagine troverete le coding challenges che ho svolto, prese dal sito [Coding Challenges](https://codingchallenges.fyi).

## Challenge #1: Build your own wc tool

Prima di tutto bisogna compilare il progetto con il comando:

```bash
./mvnw clean verify
```

> NOTA: Richiede Maven 3.9.0+ e Java 23.

Poi, per eseguire il programma, basta eseguire il comando:

```bash
java -jar target/ccwc.jar -c src/test/resources/test.txt
```

L'output sarà:

```
  342190 src/test/resources/test.txt
```
