package com.example.apiproject

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

/// <summary>
/// Read the Interception function for information
/// </summary>
private class AuthorizationInterceptor : Interceptor {

    /// <summary>
    /// Gets Apollo Request that will be built in "apolloClient" value after this class
    /// </summary>
    /// <return>Apollo request</return>
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-USER-ID", "599")
            .addHeader("X-API-KEY", "bfc65097269c742516dad0f0f2da4d4e1958eda6cadabc576d44999f512012649e41897fbb21449decf977c1d4f1b14663")
            .build()

        return chain.proceed(request)
    }
}

// Builds Apollo request and gives data
val apolloClient = ApolloClient.Builder()
    .serverUrl("https://api.taddy.org/graphql")
    .okHttpClient(
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    )
    .build()
