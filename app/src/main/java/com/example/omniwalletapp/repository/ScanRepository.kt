package com.example.omniwalletapp.repository

import com.example.omniwalletapp.BuildConfig
import com.example.omniwalletapp.entity.EtherPriceResponse
import com.example.omniwalletapp.entity.EtherScanResponse
import com.example.omniwalletapp.util.Constants
import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class ScanRepository @Inject constructor(
    okHttpClient: OkHttpClient, gson: Gson
) {

    @Inject
    lateinit var networkRepository: NetworkRepository

    private val etherScanApiClient: EtherScanApiClient

    private interface EtherScanApiClient {
        @GET("/api?module=account&action=txlist")
        fun fetchTransaction(
            @Query("address") address: String,
            @Query("page") page: Int,
            @Query("offset") offset: Int,
            @Query("sort") sort: String,
            @Query("apikey") apiKey: String
        ): Single<EtherScanResponse>

        @GET("/api?module=stats&action={type}")
        fun fetchEthPrice(@Query("apikey") apiKey: String, @Path("type") type:String): Single<EtherPriceResponse>
    }

    init {
        etherScanApiClient = Retrofit.Builder()
            .baseUrl(networkRepository.getDefaultNetwork().backendUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(EtherScanApiClient::class.java)
    }

    fun fetchTransaction(address: String, page: Int, offset: Int, type:String): Single<EtherScanResponse> {
        return etherScanApiClient.fetchTransaction(
            address,
            page,
            offset,
            "DESC",
            if(type==Constants.BSC_SYMBOL) BuildConfig.BSC_API else BuildConfig.Etherscan_API
        )
    }

    fun fetchEthPrice(type:String): Single<EtherPriceResponse> {
        return etherScanApiClient.fetchEthPrice(
            if(type==Constants.BSC_SYMBOL) BuildConfig.BSC_API else BuildConfig.Etherscan_API,
            if(type==Constants.BSC_SYMBOL) "bnbprice" else "ethprice"
        )
    }
}