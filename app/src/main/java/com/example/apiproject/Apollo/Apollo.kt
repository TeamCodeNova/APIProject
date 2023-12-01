package com.example.apiproject.Apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-USER-ID", ApiConfig.USER_ID)
            .addHeader("X-API-KEY", ApiConfig.API_KEY)
            .build()

        return chain.proceed(request)
    }
}

val apolloClient: ApolloClient = ApolloClient.Builder()
    .serverUrl("https://api.taddy.org/graphql")
    .okHttpClient(
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    )
    .build()
