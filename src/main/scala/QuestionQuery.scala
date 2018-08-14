import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import spray.json._

object QuestionQuery extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import system.dispatcher

  val questionQuery =
    """{"query":"query getQuestion{questions{text\nanswer\npostedBy\ncreatedAt}}","variables":"null","operationName":"getQuestion"}""".stripMargin

  Http()
    .singleRequest(Get(s"http://localhost:8080/graphql").withEntity(HttpEntity.Strict(ContentType(MediaTypes.`application/json`), ByteString(questionQuery))))
    .foreach(x => println(x._3.asInstanceOf[HttpEntity.Strict].data.utf8String.parseJson.prettyPrint))

  //TODO
  //read the configuration from conf file
  //stop akka connection as soon as mutation is done
}