package tw.joecwu.sumologic.scala.client

import grizzled.slf4j.Logger

trait Logger {
//  lazy val log = Logging.getLogger(ActorPool.loggingActorSystem,this)
  lazy val log = Logger(this.getClass.getName)

}

