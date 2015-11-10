import code.model.{Account, User, DBS}
import controllers.RootController
import org.squeryl.{Session, SessionFactory}
import play.api._
import play.api.mvc.{Handler, RequestHeader}
import org.squeryl.PrimitiveTypeMode._
import util.{DBSchema, Migrations}

object Global extends GlobalSettings {

  override def beforeStart(app: Application): Unit = {
    super.beforeStart(app)
  }

  override def onRouteRequest(req: RequestHeader): Option[Handler] = {
    Some(RootController.route(req))
  }

  override def onStart(app: Application): Unit = {
    SessionFactory.concreteFactory = Some(() => {
      Session.create(DBS.conn, new org.squeryl.adapters.PostgreSqlAdapter())
    })

    super.onStart(app)

    DBSchema.main(Array())
    Migrations.run()

    inTransaction {
      // Create first user if he doesn't exist:
      if (from(DBS.users)(user => select(user.id)).page(0, 1).toList.isEmpty) {
        new User(new Account().save().id, "Demo", "User", "demo@slynx.org").setPassword("demo").save()
      }
    }
  }

  override def onStop(app: Application): Unit = {
    super.onStop(app)
    println("SHUTTING DOWN HIKARI DATA SOURCE")
    DBS.ds.shutdown()
  }
}

