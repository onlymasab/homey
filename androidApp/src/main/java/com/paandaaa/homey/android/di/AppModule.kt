package com.paandaaa.homey.android.di

import android.content.Context
import androidx.room.Room
import com.paandaaa.homey.android.data.local.dao.GroceryDao
import com.paandaaa.homey.android.data.local.dao.MealPlanDao
import com.paandaaa.homey.android.data.local.dao.RecipeDao
import com.paandaaa.homey.android.data.local.database.AppDatabase
import com.paandaaa.homey.android.data.remote.api.GeminiApiService
import com.paandaaa.homey.android.data.remote.api.RecipeApiService
import com.paandaaa.homey.android.data.repository.AuthRepositoryImpl
import com.paandaaa.homey.android.domain.repository.AuthRepository
import com.paandaaa.homey.android.domain.usecase.auth.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Auth Repository
    @Provides
    @Singleton
    fun provideAuthRepository(client: SupabaseClient): AuthRepository {
        return AuthRepositoryImpl(client)
    }

    // Auth Use Cases
    @Provides
    @Singleton
    fun provideAuthUseCases(
        signIn: SignInWithGoogleUseCase,
        isAuthenticated: IsAuthenticatedUseCase,
        getUser: GetCurrentUserUseCase,
        signOut: SignOutUseCase,
        getIdToken: GetIdTokenUseCase
    ): AuthUseCases = AuthUseCases(
        signInWithGoogle = signIn,
        isAuthenticated = isAuthenticated,
        getCurrentUser = getUser,
        signOut = signOut,
        getIdToken = getIdToken
    )

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(authRepository: AuthRepository) =
        SignInWithGoogleUseCase(authRepository)

    @Provides
    @Singleton
    fun provideIsAuthenticatedUseCase(authRepository: AuthRepository) =
        IsAuthenticatedUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository) =
        GetCurrentUserUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignOutUseCase(authRepository: AuthRepository) =
        SignOutUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetIdTokenUseCase(authRepository: AuthRepository) =
        GetIdTokenUseCase(authRepository)

    // OkHttpClient with Logging
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Recipe Retrofit (Main API)
    @Provides
    @Singleton
    @Named("RecipeRetrofit")
    fun provideRecipeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Replace with your actual endpoint
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeApiService(@Named("RecipeRetrofit") retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }

    // Gemini Retrofit (Optional AI API)
    @Provides
    @Singleton
    @Named("GeminiRetrofit")
    fun provideGeminiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(@Named("GeminiRetrofit") retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }

    // Room Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "homey_ai_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(db: AppDatabase): RecipeDao = db.recipeDao()

    @Provides
    @Singleton
    fun provideGroceryDao(db: AppDatabase): GroceryDao = db.groceryDao()

    @Provides
    @Singleton
    fun provideMealPlanDao(db: AppDatabase): MealPlanDao = db.mealPlanDao()
}