package proceed.tree

import proceed.events.EventType

import scala.annotation.tailrec


trait Node {
  var path: String = _
  var id: String = _
  var key: Option[String] = None

  //FIXME: maybe better def
  lazy val childrensPath = s"$path.$id"

  //TODO: write macro for this (and maybe even generate hash)
  lazy val nodeType: String = this.getClass.getSimpleName()

  def as(name: String) = {
    this.key = Some(name)
    this
  }

  var children: ChildMap = NoChildsMap

  /*
  def @#(name: String) = {
    as(name)
  }
  */

  def element: Element

  override def toString = s"$nodeType($path . $id # ${key.getOrElse()})"

  @tailrec final def handleEvent[A <: EventType](id: Seq[String], eventType: A)(event: eventType.Event): Unit = {
    id match {
      case head :: Nil => children.getNode(head) match {
        case Some(e: Element) => e.handle(eventType)(event)
        case _ => //TODO: Error Handling
      }
      case head :: tail => children.getNode(head) match {
        case Some(n: Node) => n.handleEvent(tail, eventType)(event)
        case _ => // TODO: Error Handling
      }
    }
  }

}

object EmptyNode extends Node {
  //FIXME: better unique key for empty node
  key = Some("")

  override def element() = {
    throw new UnsupportedOperationException
  }
}

/*
case class TextNode(content: String) extends Node {

}*/
