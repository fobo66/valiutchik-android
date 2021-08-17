import io.github.cdimascio.dotenv.dotenv
import org.gradle.api.Project

fun loadSecret(project: Project, key: String): String {
  val env = dotenv {
    directory = project.projectDir.path
  }

  return env[key].orEmpty()
}
