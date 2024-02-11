ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

val flinkVersion = "1.17.1"
val postgresVersion = "42.5.4"
val logbackVersion = "1.3.14"

val flinkDependencies = Seq(
    "org.apache.flink" % "flink-core" % flinkVersion % "provided",
    "org.apache.flink" % "flink-clients" % flinkVersion,
    "org.apache.flink" % "flink-table-common" % flinkVersion % "provided",
    "org.apache.flink" %% "flink-table-api-scala" % flinkVersion,
    "org.apache.flink" %% "flink-table-api-scala-bridge" % flinkVersion % Test,
    "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
    "org.apache.flink" %% "flink-table-api-scala-bridge" % flinkVersion % Provided,
    "org.apache.flink" % "flink-table-planner-loader" % "1.17.1" % Provided,
    "org.apache.flink" % "flink-table-runtime" % "1.17.1" % "provided",
    "org.apache.flink" %% "flink-connector-filesystem" % "1.11.6",
    "org.apache.flink" % "flink-connector-files" % "1.17.1",
    "org.apache.flink" % "flink-csv" % "1.17.1" % "provided"


)
val flinkConnectors = Seq(
  "org.apache.flink" %% "flink-connector-kafka" % "1.14.6",
  "org.apache.flink" %% "flink-connector-cassandra" % "1.16.1",
  "org.apache.flink" %% "flink-connector-jdbc" % "1.14.6",
  "org.postgresql" % "postgresql" % postgresVersion
)

val logging = Seq(
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion
)
lazy val root = (project in file("."))
  .settings(
    name := "flink-demo"
  )
libraryDependencies ++= flinkDependencies ++ flinkConnectors