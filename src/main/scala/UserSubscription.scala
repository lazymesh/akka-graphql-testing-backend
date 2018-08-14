import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString

object UserSubscription extends App{
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import akka.http.scaladsl.unmarshalling.sse.EventStreamUnmarshalling._
  import system.dispatcher

  val userSubscription = """{"query":"subscription sub{userCreated{userId\nname}}","variables":"null","operationName":"sub"}""".stripMargin

  Http()
    .singleRequest(Get(s"http://localhost:8080/graphql").withEntity(HttpEntity.Strict(ContentType(MediaTypes.`application/json`), ByteString(userSubscription))))
    .flatMap(Unmarshal(_).to[Source[ServerSentEvent, NotUsed]])
    .foreach(source => source.runForeach(elem => println(elem.data) ))
}