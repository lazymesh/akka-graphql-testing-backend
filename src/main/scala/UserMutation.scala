import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import spray.json._

object UserMutation extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import system.dispatcher

  val createUserQuery =
    """{"query":"mutation setUser{createUser(userId:1, name:\"Ramesh Maharjan\"){userId\nname}}","variables":"null","operationName":"setUser"}""".stripMargin

  Http()
    .singleRequest(Get(s"http://localhost:8080/graphql").withEntity(HttpEntity.Strict(ContentType(MediaTypes.`application/json`), ByteString(createUserQuery))))
    .foreach(x => println(x._3.asInstanceOf[HttpEntity.Strict].data.utf8String.parseJson.prettyPrint))

  //TODO
  //read the configuration from conf file
  //stop akka connection as soon as mutation is done
}
