package com.ngoctientnt.template.core.auth.social

import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.config.SocialAuthConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSocialAuthClient @Inject constructor(
    private val socialAuthConfig: SocialAuthConfig,
) {
    suspend fun signIn(activity: ComponentActivity): Result<SocialIdentity> {
        if (!socialAuthConfig.isGoogleConfigured) {
            return Result.failure(SocialAuthException.NotConfigured)
        }

        return try {
            val credentialManager = CredentialManager.create(activity)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(socialAuthConfig.googleWebClientId)
                .setAutoSelectEnabled(false)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = activity,
                request = request,
            )

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleCredential.idToken
                if (idToken.isNullOrBlank()) {
                    Result.failure(SocialAuthException.Failed("Google ID token is empty"))
                } else {
                    Result.success(
                        SocialIdentity(
                            provider = SocialProvider.GOOGLE,
                            idToken = idToken,
                        ),
                    )
                }
            } else {
                Result.failure(SocialAuthException.Failed("Unsupported Google credential type"))
            }
        } catch (_: GetCredentialCancellationException) {
            Result.failure(SocialAuthException.Cancelled)
        } catch (_: NoCredentialException) {
            Result.failure(SocialAuthException.Cancelled)
        } catch (error: GetCredentialException) {
            Result.failure(
                SocialAuthException.Failed(
                    error.message ?: "Google sign-in failed",
                ),
            )
        } catch (error: Exception) {
            Result.failure(
                SocialAuthException.Failed(
                    error.message ?: "Google sign-in failed",
                ),
            )
        }
    }
}
