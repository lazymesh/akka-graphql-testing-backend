import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import spray.json._

object UserQuery extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import system.dispatcher

  val userQuery =
    """{"query":"query getUser{users{userId\nname\nquestions{text\nanswer}}}","variables":"null","operationName":"getUser"}""".stripMargin

  Http()
    .singleRequest(Get(s"http://localhost:8080/graphql").withEntity(HttpEntity.Strict(ContentType(MediaTypes.`application/json`), ByteString(userQuery))))
    .foreach(x => println(x._3.asInstanceOf[HttpEntity.Strict].data.utf8String.parseJson.prettyPrint))

  //TODO
  //read the configuration from conf file
  //stop akka connection as soon as mutation is done
}