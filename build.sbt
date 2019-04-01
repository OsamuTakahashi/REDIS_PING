lazy val redis_ping = (project in file("."))
  .settings(
    name := "redis-ping",
    organization := "com.sopranoworks",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.2",

    libraryDependencies ++= Seq(
      "org.jgroups" % "jgroups" % "4.0.3.Final",
      "redis.clients" % "jedis" % "2.9.0"
    ),
    crossPaths := false,
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    sonatypeProfileName := "com.sopranoworks",
    pomExtra :=
      <url>https://github.com/OsamuTakahashi/redis-ping</url>
        <licenses>
          <license>
            <name>MIT</name>
            <url>https://opensource.org/licenses/MIT</url>
          </license>
        </licenses>
        <scm>
          <url>https://github.com/OsamuTakahashi/redis-ping</url>
          <connection>https://github.com/OsamuTakahashi/redis-ping.git</connection>
        </scm>
        <developers>
          <developer>
            <id>OsamuTakahashi</id>
            <name>Osamu Takahashi</name>
            <url>https://github.com/OsamuTakahashi/</url>
          </developer>
        </developers>
    
  )
