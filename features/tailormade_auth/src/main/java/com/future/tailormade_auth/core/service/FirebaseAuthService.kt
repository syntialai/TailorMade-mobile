package com.future.tailormade_auth.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade_auth.core.api.AuthApiUrl
import com.future.tailormade_auth.core.model.response.FirebaseTokenResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FirebaseAuthService {

  @GET(AuthApiUrl.USERS_GET_FIREBASE_TOKEN_PATH)
  suspend fun getFirebaseToken(
      @Path("userId") userId: String): BaseSingleObjectResponse<FirebaseTokenResponse>
}