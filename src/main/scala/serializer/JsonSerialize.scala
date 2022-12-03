package serializer

import java.util.Date

object Domain {
  case class User(id: Int, name: String, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])
}

object JsonSerialize extends App {

  trait JSONValue {
    def stringify: String
  }

  final case class JsonNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JsonString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }

  final case class JsonArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ", ", "]")
  }

  final case class JsonObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify: String = values.map {
      case (key, value) => "\"" + key + "\": " + value.stringify
    }.mkString("{", ", ", "}")
  }

  import Domain._

  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  object SerializerImplicits {
    implicit class SerializerOps[T](value: T) {
      def toJSON(implicit converter: JSONConverter[T]): JSONValue = converter.convert(value)
    }
  }

  import SerializerImplicits._

  implicit object NumberSerializer extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JsonNumber(value)
  }

  implicit object StringSerializer extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JsonString(value)
  }

  implicit object UserSerializer extends JSONConverter[User] {
    override def convert(user: User): JSONValue = JsonObject(Map(
      "id" -> JsonNumber(user.id),
      "name" -> JsonString(user.name),
      "email" -> JsonString(user.email)
    ))
  }

  implicit object PostSerializer extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JsonObject(Map(
      "posts" -> JsonString(post.content),
      "createdAt" -> JsonString(post.createdAt.toString)
    ))
  }

  implicit object FeedSerializer extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JsonObject(Map(
      "user" -> feed.user.toJSON,
      "posts" -> JsonArray(feed.posts.map(_.toJSON))
    ))
  }

  val alice = User(2, "Alice", "alice@gmail.com")
  val feed = Feed(alice, List(
    Post("I love programming", new Date),
    Post("Scala is awesome", new Date)
  ))
  println(feed.toJSON.stringify)

}
