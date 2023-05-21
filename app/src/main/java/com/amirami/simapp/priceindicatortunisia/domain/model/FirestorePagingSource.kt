package com.amirami.simapp.priceindicatortunisia.domain.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestorePagingSource (
    private val queryProductsByName: Query
) : PagingSource<QuerySnapshot, ProductModel>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, ProductModel>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ProductModel> {
        return try {
            val currentPage = params.key ?: queryProductsByName.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = queryProductsByName.startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(ProductModel::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}