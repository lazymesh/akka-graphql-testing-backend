import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.sun.xml.internal.bind.v2.TODO
import spray.json._

object QuestionMutation extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import system.dispatcher

  val createQuestionQuery =
    """{"query":"mutation setQuestion{createQuestion(text:\"is this a question?\", answer:\"Yes it is\", postedBy:1){text\nanswer}}","variables":"null","operationName":"setQuestion"}""".stripMargin

  Http()
    .singleRequest(Get(s"http://localhost:8080/graphql").withEntity(HttpEntity.Strict(ContentType(MediaTypes.`application/json`), ByteString(createQuestionQuery))))
    .foreach(x => println(x._3.asInstanceOf[HttpEntity.Strict].data.utf8String.parseJson.prettyPrint))

  //TODO
  //read the configuration from conf file
  //stop akka connection as soon as mutation is done
}