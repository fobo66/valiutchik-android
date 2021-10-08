import io.github.cdimascio.dotenv.dotenv
import io.github.cdimascio.dotenv.Dotenv

private val env: Dotenv by lazy {
  dotenv {
    ignoreIfMissing = true
  }
}

fun loadSecret(key: String): String = env[key].orEmpty()
