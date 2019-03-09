
lazy val redis_ping = (project in file("."))
  .settings(
    name := "redis_ping",
    organization := "com.sopranoworks",
    version := "0.1-SNAPSHOT",

    libraryDependencies ++= Seq(
      "org.jgroups" % "jgroups" % "4.0.3.Final",
      "redis.clients" % "jedis" % "2.9.0"
    )
  )
