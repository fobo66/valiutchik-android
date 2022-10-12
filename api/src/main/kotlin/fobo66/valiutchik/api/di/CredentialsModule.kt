package fobo66.valiutchik.api.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.api.R

@Module
@InstallIn(SingletonComponent::class)
object CredentialsModule {
  @ApiUsername
  @Provides
  fun provideUsername(@ApplicationContext context: Context): String =
    context.getString(R.string.apiUsername)

  @ApiPassword
  @Provides
  fun providePassword(@ApplicationContext context: Context): String =
    context.getString(R.string.apiPassword)
}
