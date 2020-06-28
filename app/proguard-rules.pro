  -keepattributes *Annotation*
  -keepattributes SourceFile,LineNumberTable
  -keep public class * extends java.lang.Exception
  -repackageclasses

  # Keep until > okhttp 4.7.2
  # https://github.com/square/okhttp/issues/6092
  -keepnames class okhttp3.OkHttpClient
