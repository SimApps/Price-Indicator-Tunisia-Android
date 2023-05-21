package com.amirami.simapp.priceindicatortunisia.domain.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amirami.simapp.priceindicatortunisia.core.Constants.ID
import com.amirami.simapp.priceindicatortunisia.core.Constants.PAGE_SIZE
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_ARRAYS
import com.amirami.simapp.priceindicatortunisia.core.Constants.PRODUCTS_LIST_NAMES_COLLECTION
import com.amirami.simapp.priceindicatortunisia.products.model.ProductModel
import com.amirami.simapp.priceindicatortunisia.utils.Functions.removeLeadingZeroes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductModelFirestoreRepository @Inject constructor(

    private val productsRef: CollectionReference,
    private val db : FirebaseFirestore
) {


/*
    suspend fun getProductModelListFromFirestore(): DataOrException<MutableList<ProductModel>, Exception> {
        val dataOrException = DataOrException<MutableList<ProductModel>, Exception>()
        try {
            val productList = mutableListOf<ProductModel>()
            val products = productsRef.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING).get().await()
            for (document in products) {
                document.toObject(ProductModel::class.java).let {
                    productList.add(it)
                }
            }
            dataOrException.data = productList
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException
    }

    */

    suspend fun getProductModelListFromFirestore(searchtype:String,searchtext:String): DataOrException<MutableList<ProductModel>, String> {
        val dataOrException = DataOrException<MutableList<ProductModel>, String>()

        try {
            val productList = mutableListOf<ProductModel>()

            val products = productsRef.whereEqualTo(searchtype,
                if(searchtype ==ID) removeLeadingZeroes(searchtext) else searchtext
            )
                //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
                .get().await()
            if(!products.isEmpty){
                for (document in products) {
                    if (document.exists()) {
                        document.toObject(ProductModel::class.java).let {
                            productList.add(it)
                            //   Log.d(TAG,"ll"+productList[0].id )
                        }
                    }

                    else  dataOrException.e = "add"

                }
            }
            else dataOrException.e = "add"

            dataOrException.data = productList
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }




    suspend fun getResponseFromFirestoreUsingCoroutines(searchtype:String,searchtext:String): DataOrException<List<ProductModel>, String> {
    /*    val response = DataOrException<List<ProductModel>, String>()
        try {
            response.data = productsRef.whereEqualTo(searchtype,
                if(searchtype ==ID) removeLeadingZeroes(searchtext) else searchtext
            ).get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(ProductModel::class.java)
            }
        } catch (exception: Exception) {
            response.e = exception.toString()
        }
        return response*/
        val dataOrException = DataOrException<List<ProductModel>, String>()

        try {
            val productList = mutableListOf<ProductModel>()

            val products = productsRef.whereEqualTo(searchtype,
                if(searchtype ==ID) removeLeadingZeroes(searchtext) else searchtext
            )
                //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
                .get().await()
            if(!products.isEmpty){
                for (document in products) {
                    if (document.exists()) {
                        document.toObject(ProductModel::class.java).let {
                            productList.add(it)
                            //   Log.d(TAG,"ll"+productList[0].id )
                        }
                    }
                    else  dataOrException.e = "add"

                }
            }
            else dataOrException.e = "add"

            dataOrException.data = productList
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }


    suspend fun getAllProductModelListFromFirestore(): DataOrException<List<ProductModel>, String> {
        val response = DataOrException<List<ProductModel>, String>()
        try {
            response.data = productsRef.get().await().documents.mapNotNull { snapShot ->
                snapShot.toObject(ProductModel::class.java)
            }
        } catch (exception: Exception) {
            response.e = exception.toString()
        }
        return response

    }

    suspend fun getProductModelByIdFromFirestoreUsingCoroutines(text:String): DataOrException<List<ProductModel>, String> {
        val response = DataOrException<List<ProductModel>, String>()
        try {
            response.data = productsRef
                .whereEqualTo(ID, removeLeadingZeroes(text))
                .get()
                .await()
                .documents.mapNotNull { snapShot ->
                snapShot.toObject(ProductModel::class.java)
            }
        } catch (exception: Exception) {
            response.e = exception.toString()
        }
        return response
    }



    suspend    fun getAllProductModelNameListFromFirestore(): DataOrException<MutableList<ArrayList<String>>, String> {
        val DataOrExceptionProdNames = DataOrException<MutableList<ArrayList<String>>, String>()
        try {
            val productList = mutableListOf<ArrayList<String>>()

            val products = db.document(PRODUCTS_LIST_NAMES_COLLECTION)
                //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
                .get().await()

            if(products.exists())
                productList.add(products.get(PRODUCTS_LIST_NAMES_ARRAYS) as ArrayList<String>)
            else   DataOrExceptionProdNames.e = "error"

            DataOrExceptionProdNames.data = productList
        } catch (e: FirebaseFirestoreException) {
            DataOrExceptionProdNames.e = e.toString()
        }
        return DataOrExceptionProdNames
/*
        try {
            val productList = mutableListOf<ArrayList<String>>()


              db.get().await()


                //.whereEqualTo(searchtype, if(searchtype =="id") removeLeadingZeroes(searchtext) else searchtext )
                //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                       //remove  Functions.prod_name_array vriable after implement room
                        //Functions.prod_name_array = documentSnapshot.get("prod_name_list") as ArrayList<String>

                        productList.add(documentSnapshot.get("prod_name_list") as ArrayList<String>)


                    //    Log.d(TAG,"ll  : "+documentSnapshot.get("prod_name_list") as ArrayList<String> )
                    }
                    else{

                        DataOrExceptionProdNames.e= "prod_name_list empty"

                    }

                }
                .addOnFailureListener { e ->

                    DataOrExceptionProdNames.e =e.toString()
                }


            DataOrExceptionProdNames.names = productList
        } catch (e: FirebaseFirestoreException) {
            DataOrExceptionProdNames.e = e.toString()
        }
        return DataOrExceptionProdNames*/
    }



    suspend fun deleteProductModelInFirestore(id: String): DataOrException<Boolean, String> {
        val dataOrException = DataOrException<Boolean, String>()
        try {
            productsRef.document(id).delete().await()
            dataOrException.data = true
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }



    suspend fun addProductModelInFirestore(product: ProductModel, id: String): DataOrException<Boolean, String> {
        val dataOrException = DataOrException<Boolean, String>()
        try {
            productsRef.document(id).set(product , SetOptions.merge()).await()
            dataOrException.data = true
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }


    suspend    fun addProductModelNamesArrayInFirestore(prodName:String): DataOrException<Boolean, String> {
        val dataOrException = DataOrException<Boolean, String>()
        try {
            val products = db.document(PRODUCTS_LIST_NAMES_COLLECTION)
            //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
         products.update(PRODUCTS_LIST_NAMES_ARRAYS, FieldValue.arrayUnion(prodName)).await()

            dataOrException.data = true

        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }

    suspend    fun addAllProductModelNamesArrayInFirestore(prodName:ArrayList<String>): DataOrException<Boolean, String> {
        val dataOrException = DataOrException<Boolean, String>()
        try {
            val products = db.document(PRODUCTS_LIST_NAMES_COLLECTION)
            //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
            //    products.update(PRODUCTS_LIST_NAMES_ARRAYS, FieldValue.arrayUnion(prodName)).await()
            products.update(PRODUCTS_LIST_NAMES_ARRAYS, prodName).await()
            dataOrException.data = true

        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }


    suspend    fun deleteProductModelNamesArrayInFirestore(prodName:String): DataOrException<Boolean, String> {
        val dataOrException = DataOrException<Boolean, String>()
        try {
            val products = db.document(PRODUCTS_LIST_NAMES_COLLECTION)
            //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
            products.update(PRODUCTS_LIST_NAMES_ARRAYS, FieldValue.arrayRemove(prodName)).await()

            dataOrException.data = true

        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e.toString()
        }
        return dataOrException
    }






/*

4 methode to use caroutine flow ...

    data class Response(
        var products: List<ProductModel>? = null,
        var exception: Exception? = null
    )

    interface FirebaseCallback {
        fun onResponse(response: Response)
    }

    class ProductModelsRepository(
        private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
        private val productRef: CollectionReference = rootRef.collection("Tunisia")
    ) {



        fun getResponseFromFirestoreUsingCallback(callback: FirebaseCallback) {
            productRef.get().addOnCompleteListener { task ->
                val response = Response()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.products = result.documents.mapNotNull { snapShot ->
                            snapShot.toObject(ProductModel::class.java)
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                callback.onResponse(response)
            }
        }

        fun getResponseFromFirestoreUsingLiveData() : MutableLiveData<Response> {
            val mutableLiveData = MutableLiveData<Response>()
            productRef.get().addOnCompleteListener { task ->
                val response = Response()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.products = result.documents.mapNotNull { snapShot ->
                            snapShot.toObject(ProductModel::class.java)
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                mutableLiveData.value = response
            }
            return mutableLiveData
        }

        suspend fun getResponseFromFirestoreUsingCoroutines(): Response {
            val response = Response()
            try {
                response.products = productRef.get().await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(ProductModel::class.java)
                }
            } catch (exception: Exception) {
                response.exception = exception
            }
            return response
        }

        fun getResponseFromFirestoreUsingFlow() = flow {
            val response = Response()
            try {
                response.products = productRef.get().await().documents.mapNotNull { snapShot ->
                    snapShot.toObject(ProductModel::class.java)
                }
            } catch (exception: Exception) {
                response.exception = exception
            }
            emit(response)
        }
    }


    class ProductModelsViewModel (
        private val repository: ProductModelsRepository = ProductModelsRepository()
    ): ViewModel() {
        fun getResponseUsingCallback(callback: FirebaseCallback) = repository.getResponseFromFirestoreUsingCallback(callback)

        fun getResponseUsingLiveData() = repository.getResponseFromFirestoreUsingLiveData()

        val responseLiveData = liveData(Dispatchers.IO) {
            emit(repository.getResponseFromFirestoreUsingCoroutines())
        }

        fun getResponseUsingFlow() = liveData(Dispatchers.IO) {
            repository.getResponseFromFirestoreUsingFlow().collect { response ->
                emit(response)
            }
        }
    }

*/
    /* in ACITIVITY OR FRAGMENT

      private fun getResponseUsingCallback() {
        viewModel.getResponseUsingCallback(object : FirebaseCallback {
            override fun onResponse(response: Response) {
                print(response)
            }
        })
    }

    private fun getResponseUsingLiveData() {
        viewModel.getResponseUsingLiveData().observe(this, {
            print(it)
        })
    }

    private fun getResponseUsingCoroutines() {
        viewModel.responseLiveData.observe(this, {
            print(it)
        })
    }

    private fun getResponseUsingFlow() {
        viewModel.getResponseUsingFlow().observe(this, {
            print(it)
        })
    }
     */


    suspend  fun getListResponseFromFirestoreUsingCoroutines(searchtype:String,searchtext:String)
    : Flow<PagingData<ProductModel>>
    {
        val products = productsRef.whereEqualTo(searchtype,
            if(searchtype =="id") removeLeadingZeroes(searchtext) else searchtext
        )
            .limit(PAGE_SIZE.toLong())
            //.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
          //  .get()
            //.await()
        return  Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            FirestorePagingSource(products)
        }.flow
    }


   /* Pager(PagingConfig(pageSize = MifareUltralight.PAGE_SIZE)) {
        FirestorePagingSource(queryProductModelsByName)
    }.flow.cachedIn(viewModelScope)*/

}