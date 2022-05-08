package com.amirami.simapp.priceindicatortunisia.dialogues

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.BottomSheetModalBinding
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.shoping.ProductShopingRoom
import com.amirami.simapp.priceindicatortunisia.utils.Converters.fromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.PriceFormating
import com.amirami.simapp.priceindicatortunisia.utils.Functions.bottomsheetStateInfo
import com.amirami.simapp.priceindicatortunisia.utils.Functions.customUnitmesure
import com.amirami.simapp.priceindicatortunisia.utils.Functions.errorToast
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumberFromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.getNumbersFromString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.loadimageurl
import com.amirami.simapp.priceindicatortunisia.utils.Functions.logopalcer
import com.amirami.simapp.priceindicatortunisia.utils.Functions.pricenotdefined
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import com.amirami.simapp.priceindicatortunisia.utils.Functions.shortformateDate
import com.amirami.simapp.priceindicatortunisia.utils.Functions.showRestOfString
import com.amirami.simapp.priceindicatortunisia.utils.Functions.showStringifNotEmpty
import com.amirami.simapp.priceindicatortunisia.viewmodel.ProductInfoViewModel
import com.amirami.simapp.priceindicatortunisia.shoping.ShopListRoomViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.narify.netdetect.NetDetect
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal


@AndroidEntryPoint
class MyBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val productInfoViewModel: ProductInfoViewModel by activityViewModels()
    private val shopListRoomViewModel: ShopListRoomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetModalBinding.inflate(inflater, container, false)
        return binding.root
    }
    //  private var _binding: FragmentAddProductBinding? = null
    private var _binding: BottomSheetModalBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    var monoprixpriceBill =""
    var mgBill=""
    var azizaBill=""
    var carrefourBill=""
    var geantBill=""

    var nbrMissing_monoprixprice: Int =  0
    var nbrMissing_mgprice: Int =  0
    var nbrMissing_carrefourprice: Int =  0
    var nbrMissing_azzizaprice: Int =  0
    var nbrMissing_geantprice: Int =  0

    var monoprixBonusfid: Double =  0.0
    var mgBonusfid: Double =  0.0
    var carrefourBonusfid: Double =  0.0
    var azzizaBonusfid: Double =  0.0
    var geantBonusfid: Double =  0.0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        if(bottomsheetStateInfo == "showbill"){
            shopListRoomViewModel.billShopingRoom.observe(viewLifecycleOwner) {
                val dataState=  it

                BillViews()

                binding.toggleButtonGroup.check(R.id.prix_btn)
                calculatePrices(false,dataState)

                showOrdredBillPrices()


                binding.prixBtn.text= getString(R.string.Prix_sans_promotion)
                binding.promotionsBtn.text= getString(R.string.Prix_avec_promotion)


                binding.toggleButtonGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->

                    if (isChecked) {
                        when (checkedId) {
                            R.id.prix_btn ->{
                                calculatePrices(false,dataState)
                                showOrdredBillPrices()


                            }
                            R.id.promotions_btn ->{
                                calculatePrices(true,dataState)
                                showOrdredBillPrices()

                            }
                            R.id.bonus_btn ->{

                            }


                        }
                    }
                    else {

                        if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                            calculatePrices(false,dataState)
                            showOrdredBillPrices()

                            binding.toggleButtonGroup.check(R.id.prix_btn)
                        }

                    }

                }

            }
        }


        else {
            shopListRoomViewModel.nobillShopingRoom.observe(viewLifecycleOwner) { dataState->
                val product = Product(
                    dataState.productid!!.toString(),
                    dataState.date,
                    dataState.name,
                    dataState.namearabe,
                    dataState.marques,
                    dataState.marquesarabe,
                    dataState.description,
                    dataState.descriptionarabe,
                    dataState.imageurl,
                    dataState.type,
                    dataState.typesub,
                    dataState.typesubsub,
                    dataState.sieze,
                    PriceFormating(dataState.monoprixprice),
                    dataState.monoprixremarq,
                    PriceFormating(dataState.mgprice),
                    dataState.mgremarq,
                    PriceFormating(dataState.carrefourprice),
                    dataState.carrefourremarq,
                    PriceFormating(dataState.azzizaprice),
                    dataState.azzizaremarq,
                    PriceFormating(dataState.geantprice),
                    dataState.geantremarq,

                    dataState.monoprixremarqmodifdate,
                    dataState.mgremarqmodifdate,
                    dataState.carrefourremarqmodifdate,
                    dataState.azzizaremarqmodifdate,
                    dataState.geantremarqmodifdate,

                    Functions.getuserid(),
                    dataState.monoprixmodifdate,
                    dataState.mgmodifdate,
                    dataState.carrefourmodifdate,
                    dataState.azzizamodifdate,
                    dataState.geantmodifdate,

                    dataState.monoprixbonusfid,
                    dataState.mgbonusfid,
                    dataState.carrefourbonusfid,
                    dataState.azzizabonusfid,
                    dataState.geantbonusfid,

                    dataState.monoprixbonusfidmodifdate,
                    dataState.mgbonusfidmodifdate,
                    dataState.carrefourbonusfidmodifdate,
                    dataState.azzizabonusfidmodifdate,
                    dataState.geantbonusfidmodifdate,

                    fromString(dataState.monoprixPriceHistory),
                    fromString(dataState.mgpriceHistory),
                    fromString(dataState.azizaPriceHistory),
                    fromString(dataState.carrefourPriceHistory),
                    fromString(dataState.geantPriceHistory)
                )
                NoBillViews(bottomsheetStateInfo)

                loadimageurl(requireContext(),dataState.imageurl,_binding?.imageviewSpanel!!)

                _binding?.apply {
                    prodNameSpanel.text = dataState.name
                    prodDescriptionSpanel.text = dataState.description
                    prodTypeSpanel.text =  typeToTxt(dataState)
                    prodSiezeSpanel.text =  dataState.sieze
                }



                val sortedValues = mutableListOf(
                    BigDecimal(PriceFormating(dataState.monoprixprice))    to getString(R.string.monoprix),
                    BigDecimal(PriceFormating(dataState.mgprice))   to getString(R.string.mg),
                    BigDecimal(PriceFormating(dataState.carrefourprice))   to getString(R.string.carrefour),
                    BigDecimal(PriceFormating(dataState.azzizaprice)) to getString(R.string.azziza),
                    BigDecimal(PriceFormating(dataState.geantprice))   to getString(R.string.Géant))
                sortedValues.sortBy { it.first }



                binding.toggleButtonGroup.check(R.id.prix_btn)
                showProductWithoutBill(dataState, sortedValues)
                pricesTxtVisibilite(dataState , "prix")

                binding.toggleButtonGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->

                    if (isChecked) {
                        when (checkedId) {
                            R.id.prix_btn ->{
                                showProductWithoutBill(dataState, sortedValues)
                                pricesTxtVisibilite(dataState , "prix")
                            }
                            R.id.promotions_btn ->{
                                ShowremarqNoBill(dataState)
                                pricesTxtVisibilite(dataState , "")
                            }
                            R.id.bonus_btn ->{
                                ShowBonusNoBill(dataState)
                                pricesTxtVisibilite(dataState , "")
                            }
                        }
                    }
                    else {

                        if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                            showProductWithoutBill(dataState, sortedValues)
                            pricesTxtVisibilite(dataState , "prix")
                            binding.toggleButtonGroup.check(R.id.prix_btn)
                        }

                    }

                }


                _binding?.btnPricehistory?.setSafeOnClickListener{
                    //   productInfoViewModel.putproductInfoTomodify(product)
                    productInfoViewModel.putprodInfoTomodify(product)
                    goToPriceHistory()

                }

                if(fromString(dataState.monoprixPriceHistory).size>=2 ||
                    fromString(dataState.mgpriceHistory).size>=2||
                    fromString(dataState.azizaPriceHistory).size>=2||
                    fromString(dataState.carrefourPriceHistory).size>=2||
                    fromString(dataState.geantPriceHistory).size>=2   )
                    _binding?.btnPricehistory?.visibility = View.VISIBLE

                else _binding?.btnPricehistory?.visibility = View.GONE





                _binding?.addtoshopinglistSpanel?.setSafeOnClickListener{
                    if(bottomsheetStateInfo == "nobill") deleteOneProdShopList(dataState.productid,dataState.name)
                    else if(bottomsheetStateInfo == "productInfo")
                        shopListRoomViewModel.insertItem(
                            dataState,
                            getString(R.string.product_saved_in_shoping_list)
                        )
                }

                _binding?.btnModify?.setSafeOnClickListener{
                    if(bottomsheetStateInfo == "productInfo") goToAddfragment(product)
                   else{
                        NetDetect.check { isConnected: Boolean ->
                            if(isConnected){
                                goToAddfragment(product)
                            }
                            else errorToast(requireContext(), getString(R.string.Il_faut_etre_connecter_modifier_produit))
                        }
                    }
                }
            }
        }
    }

    private fun deleteOneProdShopList(productid: Long?, name:String){
      //  dismiss()
        val action = MyBottomSheetDialogFragmentDirections.actionMyBottomSheetDialogFragmentToDialogFragment("deleteOneItem",productid.toString(),name)
        NavHostFragment.findNavController(this@MyBottomSheetDialogFragment).navigate(action)


    }
    private fun NoBillViews(info:String){

        if(info=="nobill") _binding?.addtoshopinglistSpanel?.setImageResource(R.drawable.ic_delete)
        else if(info == "productInfo")_binding?.addtoshopinglistSpanel?.setImageResource(R.drawable.ic_add_to_cart)
    }

    private fun BillViews(){
        _binding?.apply {
            imageviewSpanel.visibility = View.GONE
            addtoshopinglistSpanel.visibility = View.GONE
            btnModify.visibility = View.GONE
            prodDescriptionSpanel.visibility = View.GONE
            prodSiezeSpanel.visibility = View.GONE
            prodTypeSpanel.visibility = View.GONE
            secondpriceImagevwPanel.visibility = View.VISIBLE
            thirdpriceImagevwPanel.visibility = View.VISIBLE
            fourthpriceImagevwPanel.visibility = View.VISIBLE
            fifthpriceImagevwPanel.visibility = View.VISIBLE

            prodSecondpriceSpanel.visibility = View.VISIBLE
            prodThirdpriceSpanel.visibility = View.VISIBLE
            prodForthpriceSpanel.visibility = View.VISIBLE
            prodFifthpriceSpanel.visibility = View.VISIBLE

            prodNameSpanel.visibility = View.GONE
            btnPricehistory.visibility = View.GONE
            bonusBtn.visibility = View.GONE
        }

    }

    private fun showOrdredBillPrices(){

        val sortedValues = mutableListOf(
            BigDecimal(monoprixpriceBill.replace(",", "."))    to getString(R.string.monoprix) ,
            BigDecimal(mgBill.replace(",", "."))   to getString(R.string.mg)  ,
            BigDecimal(carrefourBill.replace(",", "."))   to getString(R.string.carrefour) ,
            BigDecimal(azizaBill.replace(",", ".")) to getString(R.string.azziza)  ,
            BigDecimal(geantBill.replace(",", "."))   to getString(R.string.Géant) )
        sortedValues.sortBy { it.first }

        binding.prodFirstpriceSpanel.text = pricenotdefined(requireContext(),sortedValues[0].first.toString() ,true) + remarqTowhomBill(requireContext(),sortedValues[0].second)

        _binding?.prodSecondpriceSpanel?.text= pricenotdefined(requireContext(),sortedValues[1].first.toString(),true) +remarqTowhomBill(requireContext(),sortedValues[1].second)

        _binding?.prodThirdpriceSpanel?.text= pricenotdefined(requireContext(),sortedValues[2].first.toString(),true) +remarqTowhomBill(requireContext(),sortedValues[2].second)

        _binding?.prodForthpriceSpanel?.text= pricenotdefined(requireContext(),sortedValues[3].first.toString(),true) +remarqTowhomBill(requireContext(),sortedValues[3].second)

        _binding?.prodFifthpriceSpanel?.text= pricenotdefined(requireContext(),sortedValues[4].first.toString(),true) +remarqTowhomBill(requireContext(),sortedValues[4].second)

        _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[0].second))
        _binding?.secondpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[1].second))
        _binding?.thirdpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[2].second))
        _binding?.fourthpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[3].second))
        _binding?.fifthpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[4].second))
      /*  loadimageInt(logopalcer(requireContext(), sortedValues[0].second), _binding?.fiestpriceImagevwPanel!!)
        loadimageInt(logopalcer(requireContext(), sortedValues[1].second), _binding?.secondpriceImagevwPanel!!)
        loadimageInt(logopalcer(requireContext(), sortedValues[2].second), _binding?.thirdpriceImagevwPanel!!)
        loadimageInt(logopalcer(requireContext(), sortedValues[3].second), _binding?.fourthpriceImagevwPanel!!)
        loadimageInt(logopalcer(requireContext(), sortedValues[4].second), _binding?.fifthpriceImagevwPanel!!)*/


    }

    /* override fun onStop() {
         super.onStop()

        if(pricewithDicount && !showBill) pricewithDicount=false
     }*/

    //calculate price begin
    private  fun calculatePrices(calculateWithDiscount:Boolean, product: List<ProductShopingRoom>){
        monoprixBonusfid =  0.0
        mgBonusfid =  0.0
        carrefourBonusfid =  0.0
        azzizaBonusfid =  0.0
        geantBonusfid =  0.0

        if(product.isNotEmpty()){

            nbrMissingPrices(product)
            bonusFid(product)


            monoprixpriceBill = String.format("%.3f",monoprixPrice(product,calculateWithDiscount).toBigDecimal())
            mgBill= String.format("%.3f",mgPrice(product,calculateWithDiscount).toBigDecimal())
            azizaBill= String.format("%.3f",azzizaPrice(product,calculateWithDiscount).toBigDecimal())
            carrefourBill= String.format("%.3f",carrefourPrice(product,calculateWithDiscount).toBigDecimal())
            geantBill= String.format("%.3f",geantPrice(product,calculateWithDiscount).toBigDecimal())
        }
    }

    private  fun monoprixPrice(products: List<ProductShopingRoom>, calculateWithDiscount:Boolean) :String{

        var finalPrice=0.0
        products.indices.forEach { i ->
            val product = products[i]
            if(calculateWithDiscount){
                when {
                    product.monoprixremarq== getString(R.string.remisesurlaseuxiemme) ->              {
                        finalPrice +=  halfdiscountOnthesecond(product.quantity.toString(),product.monoprixprice,product.typesub)
                    }

                    product.monoprixremarq.contains(getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(product.quantity.toString(), getNumberFromString(product.monoprixremarq).toString())
                    }

                    product.monoprixremarq.contains(getString(R.string.emegratuit)) -> {

                        finalPrice += nemeGratuit (getNumberFromString(product.monoprixremarq).toInt()  , product.quantity.toString(),product.monoprixprice,product.typesub)
                    }
                    product.monoprixremarq.contains(getString(R.string.pourcentage)) ->              {
                        finalPrice +=   dicsountPourcentage(getNumberFromString(product.monoprixremarq).toInt() , product.quantity.toString(),product.monoprixprice)
                    }
                    product.monoprixremarq.contains(getString(R.string.aveccartefid)) -> {
                        finalPrice +=  priceWithFidCard(getNumberFromString(product.monoprixremarq).toInt(), product.quantity.toString())
                    }
                    product.monoprixremarq.contains(getString(R.string.Leprix)) -> {
                        finalPrice +=  packprice(getNumbersFromString(product.monoprixremarq, getString(R.string.Leprix), getString(R.string.est)), getNumbersFromString(product.monoprixremarq, getString(R.string.est), getString(R.string.TND)),product.quantity.toString(),product.monoprixprice,product.typesub)

                    }

                    else  -> {
                        finalPrice += pricenotdefinedBill(product.monoprixprice).toDouble() * product.quantity!!
                    }
                }
            }
            else {
                finalPrice += pricenotdefinedBill(product.monoprixprice).toDouble() * product.quantity!!
            }
        }

        return finalPrice.toString()
    }

    private  fun mgPrice(products: List<ProductShopingRoom>, calculateWithDiscount:Boolean) :String{
        var finalPrice=0.0
        products.indices.forEach { i ->
            val product = products[i]
            if(calculateWithDiscount){
                when {
                    product.mgremarq== getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice +=  halfdiscountOnthesecond(product.quantity.toString(),product.mgprice,product.typesub)
                    }
                    product.mgremarq.contains(getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(product.quantity.toString(),
                            getNumberFromString(product.mgremarq).toString())
                    }

                    product.mgremarq.contains(getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit (getNumberFromString(product.mgremarq).toInt() , product.quantity.toString(),product.mgprice,product.typesub)
                    }
                    product.mgremarq.contains(getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            getNumberFromString(product.mgremarq).toInt() , product.quantity.toString(),product.mgprice)
                    }
                    product.mgremarq.contains(getString(R.string.aveccartefid)) -> {
                        finalPrice +=  priceWithFidCard(
                            getNumberFromString(product.mgremarq).toInt(), product.quantity.toString())
                    }
                    product.mgremarq.contains(getString(R.string.Leprix)) -> {

                        finalPrice +=  packprice(getNumbersFromString(product.mgremarq, getString(R.string.Leprix), getString(R.string.est)), getNumbersFromString(product.mgremarq, getString(R.string.est), getString(R.string.TND)),product.quantity.toString(),product.mgprice,product.typesub)

                    }

                    else /*product.ProductMGRemarqueDB==""*/ -> {
                        finalPrice += pricenotdefinedBill(product.mgprice).toDouble() * product.quantity!!
                    }
                }
            }
            else {
                finalPrice += pricenotdefinedBill(product.mgprice).toDouble() * product.quantity!!
            }
        }

        return  finalPrice.toString()
    }

    private  fun carrefourPrice(products: List<ProductShopingRoom>, calculateWithDiscount:Boolean) :String{

        var finalPrice= 0.0

        products.indices.forEach { i ->

            val product= products[i]
            if(calculateWithDiscount){
                when {
                    product.carrefourremarq== getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice += halfdiscountOnthesecond(product.quantity.toString(),product.carrefourprice,product.typesub)
                    }

                    product.carrefourremarq.contains(getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(product.quantity.toString(),
                           getNumberFromString(product.carrefourremarq).toString())
                    }

                    product.carrefourremarq.contains(getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit (getNumberFromString(product.carrefourremarq).toInt() , product.quantity.toString(),product.carrefourprice,product.typesub)
                    }

                    product.carrefourremarq.contains(getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            getNumberFromString(product.carrefourremarq).toInt() , product.quantity.toString(),product.carrefourprice)
                    }

                    product.carrefourremarq.contains(getString(R.string.aveccartefid)) -> {
                        finalPrice +=  priceWithFidCard(getNumberFromString(product.carrefourremarq).toInt(), product.quantity.toString())
                    }
                    product.carrefourremarq.contains(getString(R.string.Leprix)) -> {
                        finalPrice += packprice(getNumbersFromString(product.carrefourremarq, getString(R.string.Leprix), getString(R.string.est)), getNumbersFromString(product.carrefourremarq, getString(R.string.est), getString(R.string.TND)),product.quantity.toString(),product.carrefourprice,product.typesub)
                    }

                    else /* product.ProductCarrefourRemarqueDB=="" */-> {
                        finalPrice += pricenotdefinedBill(product.carrefourprice).toDouble() * product.quantity!!
                    }
                }
            }
            else {
                finalPrice += pricenotdefinedBill(product.carrefourprice).toDouble() * product.quantity!!
            }
        }

        return finalPrice.toString()
    }

    private  fun azzizaPrice(products: List<ProductShopingRoom>, calculateWithDiscount:Boolean ) :String{

        var finalPrice=0.0

        products.indices.forEach { i ->
            val product = products[i]
            if(calculateWithDiscount){
                when {
                    product.azzizaremarq== getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice +=   halfdiscountOnthesecond(product.quantity.toString(),product.azzizaprice,product.typesub)
                    }
                    product.azzizaremarq.contains(getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(product.quantity.toString(),
                            getNumberFromString(product.azzizaremarq).toString())
                    }
                    product.azzizaremarq.contains(getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit (getNumberFromString(product.azzizaremarq).toInt(), product.quantity.toString(),product.azzizaprice,product.typesub)
                    }
                    product.azzizaremarq.contains(getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(
                            getNumberFromString(product.azzizaremarq).toInt() , product.quantity.toString(),product.azzizaprice)
                    }
                    product.azzizaremarq.contains(getString(R.string.aveccartefid)) -> {
                        finalPrice +=  priceWithFidCard(
                            getNumberFromString(product.azzizaremarq).toInt(), product.quantity.toString())
                    }
                    product.azzizaremarq.contains(getString(R.string.Leprix)) -> {
                        finalPrice +=    packprice(getNumbersFromString(product.azzizaremarq, getString(R.string.Leprix), getString(R.string.est)),
                           getNumbersFromString(product.azzizaremarq, getString(R.string.est), getString(R.string.TND)),
                            product.quantity.toString(),product.azzizaprice,product.typesub)
                    }
                    else /* product.ProductAzzizaRemarqueDB=="" */-> {
                        finalPrice += pricenotdefinedBill(product.azzizaprice).toDouble() * product.quantity!!
                    }
                }
            }
            else {
                finalPrice += pricenotdefinedBill(product.azzizaprice).toDouble() * product.quantity!!
            }

        }



        return finalPrice.toString()
    }

    private  fun geantPrice(products: List<ProductShopingRoom>, calculateWithDiscount:Boolean ) :String{

        var finalPrice=0.0

        products.indices.forEach { i ->
            val product = products[i]

            if(calculateWithDiscount){
                when {
                    product.geantremarq== getString(R.string.remisesurlaseuxiemme) -> {
                        finalPrice +=   halfdiscountOnthesecond(product.quantity.toString(),product.geantprice,product.typesub)
                    }
                    product.geantremarq.contains(getString(R.string.prixEnPromotion)) -> {
                        finalPrice += discountprice(product.quantity.toString(), getNumberFromString(product.geantremarq).toString())
                    }
                    product.geantremarq.contains(getString(R.string.emegratuit)) -> {
                        finalPrice += nemeGratuit (getNumberFromString(product.geantremarq).toInt(), product.quantity.toString(),product.geantprice,product.typesub)
                    }
                    product.geantremarq.contains(getString(R.string.pourcentage)) -> {
                        finalPrice += dicsountPourcentage(getNumberFromString(product.geantremarq).toInt() , product.quantity.toString(),product.geantprice)
                    }
                    product.geantremarq.contains(getString(R.string.aveccartefid)) -> {
                        finalPrice +=  priceWithFidCard(getNumberFromString(product.geantremarq).toInt(), product.quantity.toString())
                    }
                    product.geantremarq.contains(getString(R.string.Leprix)) -> {
                        finalPrice +=    packprice(getNumbersFromString(product.geantremarq, getString(R.string.Leprix), getString(R.string.est)), getNumbersFromString(product.geantremarq, getString(R.string.est), getString(R.string.TND)),product.quantity.toString(),product.geantprice,product.typesub)
                    }

                    else /*product.ProductGeantRemarqueDB==""*/ -> {
                        finalPrice += pricenotdefinedBill(product.geantprice).toDouble() * product.quantity!!
                    }
                }
            }
            else {
                finalPrice += pricenotdefinedBill(product.geantprice).toDouble() * product.quantity!!
            }
        }

        return  finalPrice.toString()
    }

    private fun nemeGratuit(indiceGratuit : Int, quantity:String, price:String, subtype: String):Double{
        return if(!customUnitmesure(subtype)) pricenotdefinedBill(price).toDouble() * quantity.toDouble()
         else quantity.toDouble()%indiceGratuit*pricenotdefinedBill(price).toDouble()

    }

    private fun packprice(nbrPack:Double, pricePack:Double, quantity: String, price: String, subtype:String):Double {

        return if(!customUnitmesure(subtype)) pricenotdefinedBill(price).toDouble() * quantity.toDouble()
        else quantity.toDouble()/nbrPack * pricePack + quantity.toDouble()%nbrPack * pricenotdefinedBill(price).toDouble()


    }

    private fun discountprice(quantity: String, price: String):Double{
        return quantity.toDouble()* pricenotdefinedBill(price).toDouble()
    }

    private  fun halfdiscountOnthesecond(qantity:String, price:String, subtype:String):Double{
        return if(!customUnitmesure(subtype)){
            pricenotdefinedBill(price).toDouble() * qantity.toDouble()
        } else{
            if(qantity.toDouble() % 2 ==0.0){
                //get -50% off
                (qantity.toDouble()/2)* pricenotdefinedBill(price).toDouble() + ((qantity.toDouble()/2)* pricenotdefinedBill(price).toDouble())/2

            } else{
                ((qantity.toDouble()/2)+1)* pricenotdefinedBill(price).toDouble() + ((qantity.toDouble()/2)* pricenotdefinedBill(price).toDouble())/2
            }
        }
    }

    private  fun dicsountPourcentage(discountPourcentage : Int, quantity:String, price:String):Double{
        return(pricenotdefinedBill(price).toDouble()-(pricenotdefinedBill(price).toDouble()*discountPourcentage/100))*quantity.toDouble()
    }

    private  fun priceWithFidCard(priceWithFidCard : Int, quantity:String):Double{
        return pricenotdefinedBill(priceWithFidCard.toString()).toDouble()*quantity.toDouble()
    }

    private fun bonusFid(products: List<ProductShopingRoom>){
        products.indices.forEach { i ->
            val product = products[i]

            monoprixBonusfid += pricenotdefinedBill(product.monoprixbonusfid).toDouble()* product.quantity!!
            mgBonusfid += pricenotdefinedBill(product.mgbonusfid).toDouble()* product.quantity!!
            carrefourBonusfid += pricenotdefinedBill(product.carrefourbonusfid).toDouble()* product.quantity!!
            azzizaBonusfid += pricenotdefinedBill(product.azzizabonusfid).toDouble()* product.quantity!!
            geantBonusfid += pricenotdefinedBill(product.geantbonusfid).toDouble()* product.quantity!!

        }

    }

    private fun nbrMissingPrices(products: List<ProductShopingRoom>){
        nbrMissing_monoprixprice =  0
        nbrMissing_mgprice = 0
        nbrMissing_carrefourprice =  0
        nbrMissing_azzizaprice = 0
        nbrMissing_geantprice = 0

        products.indices.forEach { i ->
            val product = products[i]

            if(pricenotdefinedBill(product.monoprixprice)=="0" ) nbrMissing_monoprixprice += 1


            if(pricenotdefinedBill(product.mgprice)=="0" ) nbrMissing_mgprice += 1


            if(pricenotdefinedBill(product.carrefourprice)=="0" ) nbrMissing_carrefourprice += 1


            if(pricenotdefinedBill(product.azzizaprice)=="0" ) nbrMissing_azzizaprice += 1


            if(pricenotdefinedBill(product.geantprice)=="0" ) nbrMissing_geantprice += 1

        }

    }

    private fun pricenotdefinedBill(price:String):String{
        return if(price!="99999.0" && price!=""  && price.isNotEmpty() ){
            val firstNummonoprixprice:BigDecimal? = try {
                BigDecimal(price)
            } catch (e: NumberFormatException) {
                null
            }

            if (!Functions.factor100(firstNummonoprixprice))
                firstNummonoprixprice.toString()
            else
                String.format("%.3f", firstNummonoprixprice)
        }
        else {
            "0"
        }
    }


    private fun remarqTowhomBill(context: Context, second:String):String{
        var remarque=""

        when (second) {
            context.getString(R.string.monoprix) -> {
                remarque=  showNbrMissingPrice(nbrMissing_monoprixprice) +  showStringifNotEmpty(pricenotdefined(context, monoprixBonusfid.toString(), false), "fidbonustring")
            }
            context.getString(R.string.mg) -> {
                remarque= showNbrMissingPrice(nbrMissing_mgprice) + showStringifNotEmpty(pricenotdefined(context, mgBonusfid.toString(), false), "fidbonustring")
            }
            context.getString(R.string.carrefour) -> {
                remarque= showNbrMissingPrice(nbrMissing_carrefourprice) + showStringifNotEmpty(pricenotdefined(context, carrefourBonusfid.toString(), false), "fidbonustring")
            }
            context.getString(R.string.azziza) -> {
                remarque= showNbrMissingPrice(nbrMissing_azzizaprice) + showStringifNotEmpty(pricenotdefined(context, azzizaBonusfid.toString(), false), "fidbonustring")
            }
            context.getString(R.string.Géant) -> {
                remarque=  showNbrMissingPrice(nbrMissing_geantprice) + showStringifNotEmpty(pricenotdefined(context, geantBonusfid.toString(), false), "fidbonustring")
            }
        }


        return remarque
    }



    private fun showNbrMissingPrice(nbr:Int):String{
        return if(nbr==0){
            ""
        } else {
            " (- $nbr ${getString(R.string.Prix)})"
        }
    }
    //calculate prices end

    private fun remarqTowhomNoBill(magasin:String, product: ProductShopingRoom):String{
        var remarque=""


        when (magasin) {
           getString(R.string.monoprix) -> {
                remarque=   showStringifNotEmpty(product.monoprixremarq, "") + showStringifNotEmpty(pricenotdefined(requireContext(), product.monoprixbonusfid, false), "fidbonustring") + showStringifNotEmpty(shortformateDate(product.monoprixmodifdate), "")
            }
            getString(R.string.mg) -> {
                remarque= showStringifNotEmpty(product.mgremarq, "") + showStringifNotEmpty(pricenotdefined(requireContext(), product.mgbonusfid, false), "fidbonustring") + showStringifNotEmpty(shortformateDate(product.mgmodifdate), "")
            }
            getString(R.string.carrefour) -> {
                remarque= showStringifNotEmpty(product.carrefourremarq, "") + showStringifNotEmpty(pricenotdefined(requireContext(), product.carrefourbonusfid, false), "fidbonustring") + showStringifNotEmpty(shortformateDate(product.carrefourmodifdate), "")
            }
            getString(R.string.azziza) -> {
                remarque= showStringifNotEmpty(product.azzizaremarq, "") + showStringifNotEmpty(pricenotdefined(requireContext(), product.azzizabonusfid, false), "fidbonustring") + showStringifNotEmpty(shortformateDate(product.azzizamodifdate), "")
            }
            getString(R.string.Géant) -> {
                remarque=  showStringifNotEmpty(product.geantremarq, "") + showStringifNotEmpty(pricenotdefined(requireContext(), product.geantbonusfid, false), "fidbonustring") + showStringifNotEmpty(shortformateDate(product.geantmodifdate), "")
            }
        }


        return remarque
    }

    private fun priceDateTowhomNoBill(magasin:String, product: ProductShopingRoom):String{

       return when (magasin) {
            getString(R.string.monoprix) -> showRestOfString(shortformateDate(product.monoprixmodifdate))
            getString(R.string.mg) -> showRestOfString(shortformateDate(product.mgmodifdate))
            getString(R.string.carrefour) -> showRestOfString(shortformateDate(product.carrefourmodifdate))
            getString(R.string.azziza) -> showRestOfString(shortformateDate(product.azzizamodifdate))
            getString(R.string.Géant) -> showRestOfString(shortformateDate(product.geantmodifdate))
           else -> {
               ""
           }
       }


    }



    private fun pricesTxtVisibilite(product: ProductShopingRoom, from:String){
        if(from=="prix") {
            if(product.name.contains(getString(R.string.carrefour)) ||
                product.name.contains(getString(R.string.monoprix))||
                product.name.contains(getString(R.string.azziza))||
                product.name.contains(getString(R.string.mg))||
                product.name.contains(getString(R.string.Géant))){

                _binding?.apply {

                    //    fiestpriceImagevwPanel.visibility = View.VISIBLE
                    secondpriceImagevwPanel.visibility = View.GONE
                    thirdpriceImagevwPanel.visibility = View.GONE
                    fourthpriceImagevwPanel.visibility = View.GONE
                    fifthpriceImagevwPanel.visibility = View.GONE

                    //   prodFirstpriceSpanel.visibility = View.VISIBLE
                    prodSecondpriceSpanel.visibility = View.GONE
                    prodThirdpriceSpanel.visibility = View.GONE
                    prodForthpriceSpanel.visibility = View.GONE
                    prodFifthpriceSpanel.visibility = View.GONE
                }

            }
        }
        else {
            _binding?.apply {
                fiestpriceImagevwPanel.setImageResource(R.drawable.azizalogo)
                secondpriceImagevwPanel.setImageResource(R.drawable.logo_carrefour)
                thirdpriceImagevwPanel.setImageResource(R.drawable.ic_monoprix_logo)
                fourthpriceImagevwPanel.setImageResource(R.drawable.mglogo)
                fifthpriceImagevwPanel.setImageResource(R.drawable.geantlogo)
            }
            when {
                product.name.contains(getString(R.string.carrefour)) -> {
                    _binding?.apply {
                        fiestpriceImagevwPanel.visibility = View.GONE
                        prodFirstpriceSpanel.visibility = View.GONE

                        thirdpriceImagevwPanel.visibility = View.GONE
                        prodThirdpriceSpanel.visibility = View.GONE

                        fourthpriceImagevwPanel.visibility = View.GONE
                        prodForthpriceSpanel.visibility = View.GONE

                        fifthpriceImagevwPanel.visibility = View.GONE
                        prodFifthpriceSpanel.visibility = View.GONE

                    }
                }
                product.name.contains(getString(R.string.monoprix)) -> {
                    _binding?.apply {

                        fiestpriceImagevwPanel.visibility = View.GONE
                        prodFirstpriceSpanel.visibility = View.GONE

                        secondpriceImagevwPanel.visibility = View.GONE
                        prodSecondpriceSpanel.visibility = View.GONE

                        fourthpriceImagevwPanel.visibility = View.GONE
                        prodForthpriceSpanel.visibility = View.GONE

                        fifthpriceImagevwPanel.visibility = View.GONE
                        prodFifthpriceSpanel.visibility = View.GONE

                    }
                }
                product.name.contains(getString(R.string.Géant)) -> {
                    _binding?.apply {
                        fiestpriceImagevwPanel.visibility = View.GONE
                        prodFirstpriceSpanel.visibility = View.GONE

                        secondpriceImagevwPanel.visibility = View.GONE
                        prodSecondpriceSpanel.visibility = View.GONE

                        fourthpriceImagevwPanel.visibility = View.GONE
                        prodForthpriceSpanel.visibility = View.GONE

                        thirdpriceImagevwPanel.visibility = View.GONE
                        prodThirdpriceSpanel.visibility = View.GONE


                    }
                }
                product.name.contains(getString(R.string.mg)) -> {
                    _binding?.apply {
                        thirdpriceImagevwPanel.visibility = View.GONE
                        prodThirdpriceSpanel.visibility = View.GONE

                        secondpriceImagevwPanel.visibility = View.GONE
                        prodSecondpriceSpanel.visibility = View.GONE

                        fourthpriceImagevwPanel.visibility = View.GONE
                        prodForthpriceSpanel.visibility = View.GONE

                        fifthpriceImagevwPanel.visibility = View.GONE
                        prodFifthpriceSpanel.visibility = View.GONE

                    }
                }
                product.name.contains(getString(R.string.azziza)) -> {
                    _binding?.apply {
                        thirdpriceImagevwPanel.visibility = View.GONE
                        prodThirdpriceSpanel.visibility = View.GONE

                        secondpriceImagevwPanel.visibility = View.GONE
                        prodSecondpriceSpanel.visibility = View.GONE

                        fourthpriceImagevwPanel.visibility = View.GONE
                        prodForthpriceSpanel.visibility = View.GONE

                        fifthpriceImagevwPanel.visibility = View.GONE
                        prodFifthpriceSpanel.visibility = View.GONE

                    }
                }
                else -> {
                    _binding?.apply {
                        fiestpriceImagevwPanel.visibility = View.VISIBLE
                        prodFirstpriceSpanel.visibility = View.VISIBLE

                        thirdpriceImagevwPanel.visibility = View.VISIBLE
                        prodThirdpriceSpanel.visibility = View.VISIBLE

                        secondpriceImagevwPanel.visibility = View.VISIBLE
                        prodSecondpriceSpanel.visibility = View.VISIBLE

                        fourthpriceImagevwPanel.visibility = View.VISIBLE
                        prodForthpriceSpanel.visibility = View.VISIBLE

                        fifthpriceImagevwPanel.visibility = View.VISIBLE
                        prodFifthpriceSpanel.visibility = View.VISIBLE

                    }
                }


            }

        }

    }

    private fun typeToTxt(product: ProductShopingRoom):String{
        return  if(product.typesub!="" && product.typesubsub!="") product.type + " -> "+product.typesub + " -> " + product.typesubsub
        else if(product.typesub!="") product.type + " -> "+product.typesub
        else  product.type
    }

    private fun ShowremarqNoBill(product: ProductShopingRoom){

        _binding?.apply {
            prodThirdpriceSpanel.text =if(checkifPromtionIsNotEmpty(product.monoprixremarq)) product.monoprixremarq +" : " + shortformateDate(product.monoprixremarqmodifdate) else getString(R.string.NA) //product.monoprixremarq
            prodForthpriceSpanel.text= if(checkifPromtionIsNotEmpty(product.mgremarq)) showStringifNotEmpty(product.mgremarq, "") +" : " + shortformateDate(product.mgremarqmodifdate) else getString(R.string.NA) //product.mgremarq
            prodSecondpriceSpanel.text= if(checkifPromtionIsNotEmpty(product.carrefourremarq)) showStringifNotEmpty(product.carrefourremarq, "")+" : " + shortformateDate(product.carrefourmodifdate) else getString(R.string.NA) //product.carrefourmodifdate
            prodFirstpriceSpanel.text=  if(checkifPromtionIsNotEmpty(product.azzizaremarq)) showStringifNotEmpty(product.azzizaremarq, "") +" : " + shortformateDate(product.azzizaremarqmodifdate) else getString(R.string.NA) //product.azzizaremarqmodifdate
            prodFifthpriceSpanel.text= if(checkifPromtionIsNotEmpty(product.geantremarq)) showStringifNotEmpty(product.geantremarq, "") +" : " + shortformateDate(product.geantremarqmodifdate) else getString(R.string.NA) //product.geantremarqmodifdate

        }

    }

    private fun ShowBonusNoBill(product: ProductShopingRoom){

        _binding?.apply {
            prodThirdpriceSpanel.text = if(checkifBonusIsNotEmpty(product.monoprixbonusfid)) showStringifNotEmpty(pricenotdefined(requireContext(), product.monoprixbonusfid, false), "fidbonustring") +" : " + shortformateDate(product.monoprixbonusfidmodifdate) else getString(R.string.NA)
            prodForthpriceSpanel.text=  if(checkifBonusIsNotEmpty(product.mgbonusfid))  showStringifNotEmpty(pricenotdefined(requireContext(), product.mgbonusfid, false), "fidbonustring") +" : " + shortformateDate(product.monoprixbonusfidmodifdate)  else getString(R.string.NA)
            prodSecondpriceSpanel.text=  if(checkifBonusIsNotEmpty(product.carrefourbonusfid)) showStringifNotEmpty(pricenotdefined(requireContext(), product.carrefourbonusfid, false), "fidbonustring")+" : " + shortformateDate(product.monoprixbonusfidmodifdate)  else getString(R.string.NA)
            prodFirstpriceSpanel.text=  if(checkifBonusIsNotEmpty(product.azzizabonusfid)) showStringifNotEmpty(pricenotdefined(requireContext(), product.azzizabonusfid, false), "fidbonustring")+" : " + shortformateDate(product.monoprixbonusfidmodifdate)  else getString(R.string.NA)
            prodFifthpriceSpanel.text=  if(checkifBonusIsNotEmpty(product.geantbonusfid))   showStringifNotEmpty(pricenotdefined(requireContext(), product.geantbonusfid, false), "fidbonustring")+" : " + shortformateDate(product.geantbonusfidmodifdate)  else getString(R.string.NA)

        }

    }


    private  fun checkifPromtionIsNotEmpty(remarque:String):Boolean{

        return !(remarque==getString(R.string.ajouter_promotions) || remarque=="")
    }




    private  fun checkifBonusIsNotEmpty(remarque:String):Boolean{

        return remarque != ""
    }
    private fun showProductWithoutBill(product: ProductShopingRoom, sortedValues: MutableList<Pair<BigDecimal, String>>){

        when {
            product.name.contains(getString(R.string.carrefour)) -> {
                _binding?.prodFirstpriceSpanel!!.text = if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), product.carrefourprice, true))) pricenotdefined(requireContext(), product.carrefourprice, true) +showRestOfString( shortformateDate(product.carrefourmodifdate))  else   getString(R.string.NA)


                _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), getString(R.string.carrefour)))
              /*  loadimageInt(
                    logopalcer(requireContext(), getString(R.string.carrefour)),
                    _binding?.fiestpriceImagevwPanel!!
                )*/

            }
            product.name.contains(getString(R.string.monoprix)) -> {
                _binding?.prodFirstpriceSpanel!!.text = if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), product.monoprixprice, true))) pricenotdefined(requireContext(), product.monoprixprice, true) +showRestOfString(shortformateDate(product.monoprixmodifdate))  else   getString(R.string.NA) //+ remarqTowhomNoBill(getString(R.string.monoprix), product)

                _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), getString(R.string.monoprix)))

             /*   loadimageInt(
                    logopalcer(requireContext(), getString(R.string.monoprix)),
                    _binding?.fiestpriceImagevwPanel!!
                )
*/
            }
            product.name.contains(getString(R.string.Géant)) -> {
                _binding?.prodFirstpriceSpanel!!.text =  if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), product.geantprice, true)))  pricenotdefined(requireContext(), product.geantprice, true)+showRestOfString(shortformateDate(product.geantmodifdate)) else   getString(R.string.NA)

                _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), getString(R.string.Géant)))

            /*    loadimageInt(logopalcer(requireContext(), getString(R.string.Géant)),
                    _binding?.fiestpriceImagevwPanel!!)*/



            }
            product.name.contains(getString(R.string.mg)) -> {
                _binding?.prodFirstpriceSpanel!!.text = if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), product.mgprice, true))) pricenotdefined(requireContext(), product.mgprice, true) +showRestOfString(shortformateDate(product.mgmodifdate)) else   getString(R.string.NA)

                _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), getString(R.string.mg)))

              /*  loadimageInt(
                    logopalcer(requireContext(), getString(R.string.mg)),
                    _binding?.fiestpriceImagevwPanel!!
                )*/

            }
            product.name.contains(getString(R.string.azziza)) -> {
                _binding?.prodFirstpriceSpanel!!.text =  if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), product.azzizaprice, true)))  pricenotdefined(requireContext(), product.azzizaprice, true) +showRestOfString(shortformateDate(product.azzizamodifdate)) else   getString(R.string.NA)

                _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), getString(R.string.azziza)))


             /*   loadimageInt(
                    logopalcer(requireContext(), getString(R.string.azziza)),
                    _binding?.fiestpriceImagevwPanel!!
                )*/

            }
            else -> {
                _binding?.apply {

                    //    fiestpriceImagevwPanel.visibility = View.VISIBLE
                    secondpriceImagevwPanel.visibility = View.VISIBLE
                    thirdpriceImagevwPanel.visibility = View.VISIBLE
                    fourthpriceImagevwPanel.visibility = View.VISIBLE
                    fifthpriceImagevwPanel.visibility = View.VISIBLE

                    //   prodFirstpriceSpanel.visibility = View.VISIBLE
                    prodSecondpriceSpanel.visibility = View.VISIBLE
                    prodThirdpriceSpanel.visibility = View.VISIBLE
                    prodForthpriceSpanel.visibility = View.VISIBLE
                    prodFifthpriceSpanel.visibility = View.VISIBLE
                }

                _binding?.prodFirstpriceSpanel!!.text = if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), sortedValues[0].first.toString(), true))) pricenotdefined(requireContext(), sortedValues[0].first.toString(), true) + priceDateTowhomNoBill(sortedValues[0].second, product) else   getString(R.string.NA)

                _binding?.prodSecondpriceSpanel!!.text= if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), sortedValues[1].first.toString(), true))) pricenotdefined(requireContext(), sortedValues[1].first.toString(), true) + priceDateTowhomNoBill(sortedValues[1].second, product) else   getString(R.string.NA)

                _binding?.prodThirdpriceSpanel!!.text= if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), sortedValues[2].first.toString(), true)))pricenotdefined(requireContext(), sortedValues[2].first.toString(), true) + priceDateTowhomNoBill(sortedValues[2].second, product) else   getString(R.string.NA)

                _binding?.prodForthpriceSpanel!!.text= if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), sortedValues[3].first.toString(), true))) pricenotdefined(requireContext(),sortedValues[3].first.toString(), true) + priceDateTowhomNoBill(sortedValues[3].second, product) else   getString(R.string.NA)

                _binding?.prodFifthpriceSpanel!!.text= if(checkifPrixIsNorixtEmpty(pricenotdefined(requireContext(), sortedValues[4].first.toString(), true))) pricenotdefined(requireContext(), sortedValues[4].first.toString(), true) + priceDateTowhomNoBill(sortedValues[4].second, product) else   getString(R.string.NA)

                _binding?.fiestpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[0].second))
                _binding?.secondpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[1].second))
                _binding?.thirdpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[2].second))
                _binding?.fourthpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[3].second))
                _binding?.fifthpriceImagevwPanel!!.setImageResource(logopalcer(requireContext(), sortedValues[4].second))

            /*    loadimageInt(logopalcer(requireContext(), sortedValues[0].second),_binding?.fiestpriceImagevwPanel!!)
                loadimageInt(logopalcer(requireContext(), sortedValues[1].second),_binding?.secondpriceImagevwPanel!!)
                loadimageInt(logopalcer(requireContext(), sortedValues[2].second),_binding?.thirdpriceImagevwPanel!!)
                loadimageInt(logopalcer(requireContext(), sortedValues[3].second),_binding?.fourthpriceImagevwPanel!!)
                loadimageInt(logopalcer(requireContext(), sortedValues[4].second),_binding?.fifthpriceImagevwPanel!!)*/
            }


        }
    }
    private  fun checkifPrixIsNorixtEmpty(remarque:String):Boolean{
        return remarque != getString(R.string.NA)
    }

    //MUST add this for history fragment

    private fun goToPriceHistory(){
        val action = MyBottomSheetDialogFragmentDirections.actionMyBottomSheetDialogFragmentToPricesHistoryGraphFragment()
        this@MyBottomSheetDialogFragment.findNavController().navigate(action) //   NavHostFragment.findNavController(this).navigate(action)
    }

    private  fun goToAddfragment(product:Product){
        productInfoViewModel.putprodInfoTomodify(product)

        findNavController().navigate(R.id.action_myBottomSheetDialogFragment_to_AddProductFragment)

       /* val action = MyBottomSheetDialogFragmentDirections.actionMyBottomSheetDialogFragmentToAddProductFragment()
        this@MyBottomSheetDialogFragment.findNavController().navigate(action)*/
    }


}



