package com.amirami.simapp.priceindicatortunisia.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.ProductTiketBinding
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.utils.Constants.PRODUITS_FRAIS
import com.firebase.ui.auth.ui.email.EmailLinkFragment.TAG
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.DatabaseReference
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt
import com.google.firebase.firestore.FieldValue
import java.text.DateFormat


object Functions {
    private var mInterstitialAd: InterstitialAd? = null
    var nbrInterstitialAdShowed=0


    lateinit var database: DatabaseReference
    var prod_name_array: ArrayList<String> = ArrayList()

    var checkedradio:Int=-55

    var bottomsheetStateInfo = "false"
        //  var pricewithDicount = false
    var commentaire = ""
    var reopendialogue = true

    val userRecord = FirebaseAuth.getInstance()

    var searchtext=""
    var searchtype="name"


    fun getCurrentDate():String{
        val currentDate = System.currentTimeMillis()
        return currentDate.toString() //DateFormat.getDateTimeInstance().format(currentDate) // formated
    }

    fun getcurrentDate(): String {

        val cal = Calendar.getInstance()
        val dayOfMonth = cal[Calendar.DAY_OF_MONTH].toString()
        val monthOfYear = cal[Calendar.MONTH].toString()
        val year = cal[Calendar.YEAR].toString()


        return "$dayOfMonth/$monthOfYear/$year"
    }

    fun formatDate(Date:String) :String{
        // SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(Date())
        // SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(Date())
        return  DateFormat.getDateTimeInstance().format(Date.toLong())
    }

    fun shortformateDate(Date:String):String{
        // SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(Date())
        // SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(Date())

        return if(isNumber(Date)) SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(Date.toLong())
        else Date
    }



    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    fun isDouble(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) || it == '.'}
    }

    @SuppressLint("DefaultLocale")
    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.lowercase().replaceFirstChar(Char::titlecase) }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let {
            activity?.hideKeyboard(it)
        }
    }

    fun searchType(text:String):String{

        searchtype = when {
            isNumber(text) -> {
                "id"
            }
            //search type of product
          /*  text in searchType -> {
                "type"
            }*/
            else -> {
                "name"
            }
        }
        return searchtype
    }


    fun showStringifNotEmpty(string: String,extrainfo:String):String{

        var strings = string.filter { !it.isWhitespace() }
        strings=  removeWord(strings ,"TND")
        return if(strings=="") strings
        else if(string=="0.00" || string=="0.000" || string=="0.0" || string=="0 TND" || string=="0.000 TND" ||string=="0.00 TND" ) ""
        else if(extrainfo=="fidbonustring" &&strings.toDouble().roundToInt()!=0 )"\n${String.format("%.3f",BigDecimal.valueOf(strings.toDouble()) )} TND bonus dans la carte de fidélité"
        else if(extrainfo=="fidbonustring" &&strings.toDouble().roundToInt()==0)  ""
        else string// " , $string  dans  carte d"
    } //


    fun showNbrMissingPrice(context: Context,nbr:Int):String{
        return if(nbr==0){
            ""
        } else {
            " (- $nbr ${context.getString(R.string.Prix)})"
        }
    }

    fun showRestOfString(text:String):String{
        return if (text!="") " : $text" else ""
    }

    fun pricenotdefined(context: Context,price:String,showajouterprix:Boolean):String{
//if modifiy then modifie pricenotdefinedbuttomsheet

        return if(price!="99999.0" && price!=""  && price.isNotEmpty()){
            val firstNummonoprixprice:BigDecimal? = try { BigDecimal(price) }
            catch (e: NumberFormatException) { null }

            if (!factor100(firstNummonoprixprice)) firstNummonoprixprice.toString() +context.getString(R.string.TND)
            else String.format("%.3f", firstNummonoprixprice)+context.getString(R.string.TND)
        }
        else if(showajouterprix) context.getString(R.string.NA)/*"Ajoutez un prix"*/ else "0"
    }


    fun factor100(n: BigDecimal?) = n!!.toDouble() % 100.0 == 0.0


    fun loadimageurl(context: Context,url:String,imageView: ImageView){

        imageView.load(url) {
            // crossfade(true)
            // crossfade(500)
            transformations(RoundedCornersTransformation(16f))
            error(R.drawable.ic_photo)
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            placeholder(R.drawable.ic_photo) //image shown when loading image
            scale(Scale.FIT)
            //   transformations(CircleCropTransformation())
            // transformations(GrayscaleTransformation())
            //   transformations(BlurTransformation(applicationContext))
            //  transformations(BlurTransformation(applicationContext, 5f))
        }

    }


    fun loadimageInt(image:Int,imageView: ImageView){

        imageView.load(image) {
            // crossfade(true)
            // crossfade(500)
          //  transformations(RoundedCornersTransformation(16f))
            error(R.drawable.ic_photo)
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            placeholder(R.drawable.ic_photo) //image shown when loading image
            scale(Scale.FILL)
            //   transformations(CircleCropTransformation())
            // transformations(GrayscaleTransformation())
            //   transformations(BlurTransformation(applicationContext))
            //  transformations(BlurTransformation(applicationContext, 5f))
        }

    }

    fun minusordeleteimage(Quantity: Double, double:Boolean, imageView: ImageView){
        if(double){
            if(Quantity<=0.001) imageView.setImageResource(R.drawable.ic_delete)
            else imageView.setImageResource(R.drawable.ic_minus)
        }
        else {
            if(Quantity.toInt()==1 || Quantity.toInt()==0) imageView.setImageResource(R.drawable.ic_delete)
            else imageView.setImageResource(R.drawable.ic_minus)
        }
    }

    fun changemodificationpricedate(oldprice:String,currentprice:String,olddate:String):String{
        return when {
            currentprice=="" ->  ""

            oldprice!=currentprice -> getCurrentDate()//SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(Date())

            // olddate=="" -> getCurrentDate()//SimpleDateFormat("d/MM/yyyy", Locale.getDefault()).format(Date())

            else -> olddate
        }
    }

    fun replacesiez(productSieze:String):String{
        var string: String = removeWord(productSieze, "L")
        string = removeWord(string, "/L")
        string = removeWord(string, "Unité")
        string = removeWord(string, "ml")
        string = removeWord(string, "cl")
        string = removeWord(string, "Kg")
        string = removeWord(string, "/Kg")
        string = removeWord(string, "g")
        string = removeWord(string, "m")
        string = removeWord(string, "cm")
        string = removeWord(string, "Rouleaux")
        string = removeWord(string, "Pièces")
        string = removeWord(string, "/Pièces")
        string = removeWord(string, "Oeufs")
        string = removeWord(string, "oeufs")
        string = removeWord(string, "watt")
        string = removeWord(string, " ")
        return string
    }

    fun removeWord(value: String, wordtoremove: String): String {
        var result = ""
        var possibleMatch = ""
        var i = 0
        var j = 0
        while ( i in value.indices) {
            if ( value[i] == wordtoremove[j] ) {
                if ( j == wordtoremove.length - 1 ) { // match
                    possibleMatch = "" // discard word
                    j = 0
                }
                else {
                    possibleMatch += value[i]
                    j++
                }
            }
            else {

                result += possibleMatch
                possibleMatch = ""

                if ( j == 0 ) {

                    result += value[i]
                }
                else {

                    j = 0
                    i-- // re-test
                }
            }

            i++
        }

        return result
    }

    fun removeWordWithReplace(value: String, oldword: String,neword:String): String = value.replace(oldword, neword)



    fun logopalcer(context:Context,string: String ):Int{
        return when (string) {
            context.getString(R.string.monoprix)    -> R.drawable.ic_monoprix_logo
            context.getString(R.string.mg)   -> R.drawable.mglogo
            context.getString(R.string.carrefour)   -> R.drawable.logo_carrefour
            context.getString(R.string.azziza)  -> R.drawable.azizalogo
            context.getString(R.string.Géant)  -> R.drawable.geantlogo
            else -> R.drawable.ic_plus
        }
    }

    fun removeLeadingZeroes(s: String): String {
        val sb = StringBuilder(s)
        while (sb.length > 1 && sb[0] == '0') {
            sb.deleteCharAt(0)
        }
        return sb.toString()
    }

    fun removeTrailingZeroes(s: String): String {
        val sb = StringBuilder(s)
        while (sb.length > 1 && sb[sb.length - 1] == '0') {
            sb.setLength(sb.length - 1)
        }
        return sb.toString()
    }

    fun getuserid ():String{
        return if(userRecord.uid!=null) userRecord.uid!!
        else "no_user"
    }

    fun checkPermission(context: Context):Boolean {
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
        {
            // Permission is not granted
            return false
        }
        return true
    }






    fun getNumberFromString(s: String):Double{
        return s.filter { it.isDigit() || it == '.'}.toDouble()
    }

    fun getNumbersFromString(string: String,indexOne:String,indexTwo:String):Double{
        return removeWord(string.substring(string.lastIndexOf(indexOne)+indexOne.length,string.indexOf(indexTwo)), " ").toDouble()
    }

    fun copyToclipord(context: Context,string: String){
        val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("note_copy",string  )
        myClipboard.setPrimaryClip(myClip)

        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }




    fun removeAllDigitExeptX(str: String): String {
        // Converting the given string
        // into a character array
        val charArray = str.toCharArray()
        var result = ""
        // Traverse the character array
        for (i in charArray.indices) {
            // Check if the specified character is not digit
            // then add this character into result variable
            if (!Character.isDigit(charArray[i]) ) {
                result += charArray[i]
            }
        }

        result= removeWord(result,  ".")
        result= removeWord(result,  "*")
        result= removeWord(result,  "x")
        result= removeWord(result,  "X")

        return  result.replace("\\s".toRegex(), "").replace("Rouleau", "Rouleaux")
    }


    fun customUnitmesure(ProductSubTypedb:String):Boolean{
        return ProductSubTypedb == PRODUITS_FRAIS
    }


    fun EditText.addDecimalLimiter(maxLimit: Int = 2) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = this@addDecimalLimiter.text!!.toString()
                if (str.isEmpty()) return
                val str2 = decimalLimiter(str, maxLimit)

                if (str2 != str) {
                    this@addDecimalLimiter.setText(str2)
                    val pos = this@addDecimalLimiter.text!!.length
                    this@addDecimalLimiter.setSelection(pos)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    fun EditText.decimalLimiter(string: String, MAX_DECIMAL: Int): String {

        var str = string
        if (str[0] == '.') str = "0$str"
        val max = str.length

        var rFinal = ""
        var after = false
        var i = 0
        var up = 0
        var decimal = 0
        var t: Char

        val decimalCount = str.count{ ".".contains(it) }

        if (decimalCount > 1)
            return str.dropLast(1)

        while (i < max) {
            t = str[i]
            if (t != '.' && !after) {
                up++
            } else if (t == '.') {
                after = true
            } else {
                decimal++
                if (decimal > MAX_DECIMAL)
                    return rFinal
            }
            rFinal += t
            i++
        }
        return rFinal
    }



    fun DeleteFieldInDocument(context: Context, prodNameToDelete:String, docid:String){
        val db = FirebaseFirestore.getInstance()
     //   Toast.makeText(context, "gd", Toast.LENGTH_LONG).show()
      //  val priceHistoryArray = dB.collection("Tunisia").document(docid)
        // Atomically remove a region from the "regions" array field.
      //  priceHistoryArray.update(prodNameToDelete, arrayRemove())

        val docRef = db.collection("Tunisia").document(docid)

// Remove the 'capital' field from the document
        val updates = hashMapOf<String, Any>(
            "AzizaPriceHistory" to FieldValue.delete(),
            "CarrefourPriceHistory" to FieldValue.delete(),
            "GeantPriceHistory" to FieldValue.delete(),
            "MGPriceHistory" to FieldValue.delete(),
            "MonoprixPriceHistory" to FieldValue.delete()
        )



        docRef.update(updates).addOnCompleteListener {
            Toast.makeText(context, "succ!", Toast.LENGTH_LONG).show()
        }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
            }

    }





    fun PriceFormating(price: String) :String = if(price=="")   "99999.0" else price
    fun PriceReFormating(price: String) :String = if(price=="99999.0")   "" else price

    fun getdateDiffrence(lastdate:String): String {

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)  //SimpleDateFormat("dd/MM/yyyy" )

        val simpleDateFormat= SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.US)
        val currentDT: Date = simpleDateFormat.parse(Date().toString())!!

        val lastdates: Date = sdf.parse(shortformateDate(lastdate))!!

        val diff: Long = abs(currentDT.time - lastdates.time)


        return  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString()
    }


    fun getcurrentDta(): String {

        val cal = Calendar.getInstance()
        val dayOfMonth = cal[Calendar.DAY_OF_MONTH].toString()
        val monthOfYear = cal[Calendar.MONTH].toString()
        val year = cal[Calendar.YEAR].toString()


        return "$dayOfMonth/$monthOfYear/$year"
    }


    class SafeClickListener(

        private var defaultInterval: Int = 1000,
        private val onSafeCLick: (View) -> Unit
    ) : View.OnClickListener {
        private var lastTimeClicked: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                return
            }
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeCLick(v)
        }
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }




    fun unwrap(context: Context):Activity {
        var scontext=context
        while (scontext !is Activity && scontext is ContextWrapper)
        {
            scontext = scontext.baseContext
        }
        return scontext as Activity
    }



    fun hideBonusFidIfZero(context: Context,string: String):String{
        return if(removeWord(string,context.getString(R.string.bonus_dans_la_carte_de_fidélité)).toInt()==0
            || removeWord(string,context.getString(R.string.bonus_dans_la_carte_de_fidélité)).toInt().toDouble()==0.0
            || removeWord(string,context.getString(R.string.bonus_dans_la_carte_de_fidélité)).toInt().toDouble()==0.00
            || removeWord(string,context.getString(R.string.bonus_dans_la_carte_de_fidélité)).toInt().toDouble()==0.000) {
            ""

        } else string
    }


    fun searchInGoogle(context: Context, questionTosearsh:String){

        if (questionTosearsh!=""){
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.setClassName(
                "com.google.android.googlequicksearchbox",
                "com.google.android.googlequicksearchbox.SearchActivity"
            )
            intent.putExtra("query", questionTosearsh)
            context.startActivity(intent)
        }else{
            DynamicToast.makeWarning(context, context.getString(R.string.error_message), 9).show()
        }
    }


/*
    fun recyclerviewhorizantal(context: Context, rvhorizantal: RecyclerView,manager: FragmentManager){
        /*if(searchtype=="id" && !isNumber(searchtext) ){

                Toast.makeText(context, "Invalid ID!", Toast.LENGTH_SHORT).show()

            } else{*/
        val db = FirebaseFirestore.getInstance()
        val query =db.collection("Tunisia").whereNotEqualTo("promo","promo")
             //   .whereLessThanOrEqualTo(searchtype, "$searchtext\uF7FF")*
                .limit(3)
        val options = FirestoreRecyclerOptions.Builder<ProductRV>().setQuery(query, ProductRV::class.java)
                .setLifecycleOwner(context as LifecycleOwner).build()
        val adapter = object: FirestoreRecyclerAdapter<ProductRV, Productviewholder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Productviewholder {
                val view = LayoutInflater.from(context).inflate(R.layout.product_tiket_horizantal, parent, false)
                return Productviewholder(view)
            }

            override fun onBindViewHolder(holder: Productviewholder, position: Int, model: ProductRV) {
                val tvname_rvh: TextView = holder.itemView.findViewById(R.id.name_rvh)
               // val tvdescription_rvh: TextView = holder.itemView.findViewById(R.id.description_rvh)
                val tvsieze_rvh: TextView = holder.itemView.findViewById(R.id.sieze_rvh)
                val tvbestprice_rvh: TextView = holder.itemView.findViewById(R.id.bestprice_rvh)
                val tvimageview_rvh: ImageView = holder.itemView.findViewById(R.id.imageview_rvh)
                val linearLayoutHolderHori: LinearLayout = holder.itemView.findViewById(R.id.linearLayoutHolderHori)

               // val textView: TextView = holder.itemView.findViewById(R.id.date_rvh)

                tvname_rvh.text = model.name
              //  tvdescription_rv.text = model.description
                tvsieze_rvh.text= model.sieze
                //textView.text= model.date
                linearLayoutHolderHori.setOnClickListener {
                    globalpromo=model.promo
                    globalid = model.id
                    globaldate=model.date
                    globalname =   model.name
                    globaldescription =  model.description
                    globalimageurl = model.imageurl
                    globaltype = model.type
                    globalsieze =  model.sieze
                    globalmonoprixprice =  model.monoprixprice
                    globalmonoprixremarq = model.monoprixremarq
                    globalmgprice = model.mgprice
                    globalmgremarq =  model.mgremarq
                    globalcarrefourprice = model.carrefourprice
                    globalcarrefourremarq =  model.carrefourremarq
                    globalazzizaprice =  model.azzizaprice
                    globalazzizaremarq =  model.azzizaremarq
                    globalgeantprice =  model.geantprice
                    globalgeantremarq =  model.geantremarq
                    MyBottomSheetDialogFragment().apply { show(manager, tag) }
                }
                Glide.with(context)
                        .load(model.imageurl)
                        // .transition(withCrossFade())
                        .centerCrop()
                        //.centerInside()
                        // .circleCrop()
                        //.apply(RequestOptions.circleCropTransform())
                        .error(
                                Glide.with(context)

                                        .load(R.drawable.ic_launcher_background)
                                        // .transition(withCrossFade())
                                        //  .centerCrop()
                                        //.centerInside()
                                        .circleCrop()
                                //.apply(RequestOptions.circleCropTransform())
                        )
                        .into(tvimageview_rvh)




                when (model.promo) {
                    "monoprix" -> tvbestprice_rvh.text = pricenotdefined(model.monoprixprice)
                    "mg" -> tvbestprice_rvh.text =pricenotdefined(model.mgprice)
                    "carrefour" -> tvbestprice_rvh.text =pricenotdefined(model.carrefourprice )
                    "azziza" -> tvbestprice_rvh.text =pricenotdefined(model.azzizaprice)
                    "geant" -> tvbestprice_rvh.text = pricenotdefined(model.geantprice)
                    else -> { // Note the block
                        print("x is neither 1 nor 2")
                    }
                }


            }
        }
        rvhorizantal.adapter = adapter
        rvhorizantal.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        // }

    }
*/


    fun loadInterstitialAd(context: Context ){
         val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, context.getString(R.string.Interstitial_adUnitId), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun listnerInterstitialAd(context: Context){

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadInterstitialAd(context)

            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                loadInterstitialAd(context)
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
    }

    fun showInterstitialAd(a: Activity,nbrinterstitialAdShowed:Int){

        if (mInterstitialAd != null   ) {
            if(nbrinterstitialAdShowed % 5 ==0){
                mInterstitialAd?.show(a)
            }
        }
        else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }


     fun errorToast(context: Context,message: String) {
        DynamicToast.make(context, message, 9).show()
    }

    fun succesToast(context: Context,message: String) {
        DynamicToast.make(context, message, 9).show()
    }

    fun warningToast(context: Context,message: String) {
        DynamicToast.makeWarning(context, message, 9).show()
    }
    fun dynamicToast(context: Context,message: String) {
        DynamicToast.make(context, message, 9).show()
    }
}