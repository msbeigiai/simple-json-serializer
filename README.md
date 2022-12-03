## A simple JSON serializer

input: 
```scala
  val alice = User(2, "Alice", "alice@gmail.com")
  val feed = Feed(alice, List(
      Post("I love programming", new Date),
      Post("Scala is awesome", new Date)
  ))
  println(feed.toJSON.stringify)
```

output:
```json
{
  "user": {
    "id": 2,
    "name": "Alice",
    "email": "alice@gmail.com"
  },
  "posts": [
    {
      "posts": "I love programming",
      "createdAt": "Sat Dec 03 15:53:45 IRST 2022"
    },
    {
      "posts": "Scala is awesome",
      "createdAt": "Sat Dec 03 15:53:45 IRST 2022"
    }
  ]
}
```

*enjoy*