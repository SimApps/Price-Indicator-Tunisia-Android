package com.amirami.simapp.priceindicatortunisia.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.ProductTiketBinding
import com.amirami.simapp.priceindicatortunisia.model.Product
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import java.math.BigDecimal


class ProductsAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<Product, ProductsAdapter.ProductViewHolder>(Companion) {
   /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = ProductDataBinding.inflate(
                layoutInflater,
                parent,
                false
        )
        return ProductViewHolder(dataBinding)
    }*/


  /*  private val items = ArrayList<Product>()
    private lateinit var ProductShopingRoom: Product

    fun setItems(items: Product) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder{
        val binding = ProductTiketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position) ?: return




        holder.bind(product)
    }

    companion object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    inner class ProductViewHolder(
        private val binding: ProductTiketBinding
    ) :  RecyclerView.ViewHolder(binding.root) {
    /*    init {
            binding.apply {


                addtoshoplistAndPlusTxVw.setSafeOnClickListener{
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener.onAddQuantityBtnClick(items[bindingAdapterPosition])
                    }

                }


                linearLayoutHolder.setSafeOnClickListener {

                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items[bindingAdapterPosition])

                    }
                }
            }
        }*/

        fun bind(model: Product) {

                binding.apply {


                    addtoshoplistAndPlusTxVw.setSafeOnClickListener{
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            listener.onAddQuantityBtnClick(model)
                        }

                    }


                    linearLayoutHolder.setSafeOnClickListener {

                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            listener.onItemClick(model)

                        }
                    }
                }
            //////
            val sortedValues = mutableListOf(
                BigDecimal(Functions.PriceFormating(model.monoprixprice!!)) to binding.root.context.getString(R.string.monoprix),
                BigDecimal(Functions.PriceFormating(model.mgprice!!)) to binding.root.context.getString(R.string.mg),
                BigDecimal(Functions.PriceFormating(model.carrefourprice!!)) to binding.root.context.getString(
                    R.string.carrefour
                ),
                BigDecimal(Functions.PriceFormating(model.azzizaprice!!)) to binding.root.context.getString(
                    R.string.azziza
                ),
                BigDecimal(Functions.PriceFormating(model.geantprice!!)) to binding.root.context.getString(
                    R.string.Géant
                )
            )
            sortedValues.sortBy { it.first }


            binding.apply {
                nameTxVw.text = model.name
                addtoshoplistAndPlusTxVw.setImageResource(R.drawable.ic_add_to_cart)
                bestpriceAndQuantityTxVwLayout.visibility = View.GONE
                logoAndMinusImVw.visibility = View.GONE

                Functions.loadimageurl(binding.root.context, model.imageurl!!, imageview)


                fun priceDate(magasin: String, product: Product): String {
                    var remarque = ""


                    when (magasin) {
                        binding.root.context.getString(R.string.monoprix) -> {
                            remarque =
                                    /* showStringifNotEmpty(product.monoprixremarq!!, "") + showStringifNotEmpty(
                                         pricenotdefined(requireContext(), product.monoprixbonusfid!!, false), "fidbonustring") + */
                                Functions.showStringifNotEmpty(
                                    Functions.shortformateDate(product.monoprixmodifdate!!),
                                    ""
                                )
                        }
                        binding.root.context.getString(R.string.mg) -> {
                            remarque =/* showStringifNotEmpty(product.mgremarq!!, "") + showStringifNotEmpty(
                    pricenotdefined(requireContext(), product.mgbonusfid!!, false),
                    "fidbonustring"
                ) + */Functions.showStringifNotEmpty(
                                Functions.shortformateDate(product.mgmodifdate!!),
                                ""
                            )
                        }
                        binding.root.context. getString(R.string.carrefour) -> {
                            remarque =
                                    /* showStringifNotEmpty(product.carrefourremarq!!, "") + showStringifNotEmpty(
                                         pricenotdefined(requireContext(), product.carrefourbonusfid!!, false),
                                         "fidbonustring"
                                     ) +*/Functions.showStringifNotEmpty(
                                Functions.shortformateDate(product.carrefourmodifdate!!),
                                ""
                            )
                        }
                        binding.root.context.getString(R.string.azziza) -> {
                            remarque = /*showStringifNotEmpty(product.azzizaremarq!!, "") + showStringifNotEmpty(
                    pricenotdefined(requireContext(), product.azzizabonusfid!!, false),
                    "fidbonustring"
                ) + */Functions.showStringifNotEmpty(
                                Functions.shortformateDate(product.azzizamodifdate!!),
                                ""
                            )
                        }
                        binding.root.context.getString(R.string.Géant) -> {
                            remarque = /*showStringifNotEmpty(product.geantremarq!!, "") + showStringifNotEmpty(
                    pricenotdefined(requireContext(), product.geantbonusfid!!, false),
                    "fidbonustring"
                ) + */Functions.showStringifNotEmpty(
                                Functions.shortformateDate(product.geantmodifdate!!),
                                ""
                            )
                        }
                    }

                    return remarque
                }


                fun showProductWithoutBill(
                    product: Product,
                    sortedValues: MutableList<Pair<BigDecimal, String>>
                ) {

                    when {
                        product.name!!.contains(binding.root.context.getString(R.string.carrefour)) -> {
                            priceTxVw.text = Functions.pricenotdefined(
                                binding.root.context,
                                product.carrefourprice!!,
                                true
                            )
                            magasinImage.setImageResource(R.drawable.logo_carrefour)
                        }
                        product.name!!.contains(binding.root.context.getString(R.string.monoprix)) -> {
                            priceTxVw.text = Functions.pricenotdefined(
                                binding.root.context,
                                product.monoprixprice!!,
                                true
                            )
                            magasinImage.setImageResource(R.drawable.ic_monoprix_logo)
                        }
                        product.name!!.contains(binding.root.context.getString(R.string.Géant)) -> {
                            priceTxVw.text = Functions.pricenotdefined(
                                binding.root.context,
                                product.geantprice!!,
                                true
                            )
                            magasinImage.setImageResource(R.drawable.geantlogo)
                        }
                        product.name!!.contains(binding.root.context.getString(R.string.mg)) -> {
                            priceTxVw.text = Functions.pricenotdefined(
                                binding.root.context,
                                product.mgprice!!,
                                true
                            )
                            magasinImage.setImageResource(R.drawable.mglogo)
                        }
                        product.name!!.contains(binding.root.context.getString(R.string.azziza)) -> {
                            priceTxVw.text = Functions.pricenotdefined(
                                binding.root.context,
                                product.azzizaprice!!,
                                true
                            )
                            magasinImage.setImageResource(R.drawable.azizalogo)
                        }
                        else -> {
                            /*
                                 _bottomsheetbinding.apply {
                                     secondpriceLL.visibility = View.VISIBLE
                                     thirdpriceLL.visibility = View.VISIBLE
                                     fourthpriceLL.visibility = View.VISIBLE
                                     fifthpriceLL.visibility = View.VISIBLE
                                 }
                 */
                            priceTxVw.text = Functions.pricenotdefined(
                                binding.root.context,
                                sortedValues[0].first.toString(),
                                true
                            ) + " : " + priceDate(sortedValues[0].second, product)

                            /*     _bottomsheetbinding.prodSecondpriceSpanel.text = pricenotdefined(
                                     requireContext(),
                                     sortedValues[1].first.toString(),
                                     true
                                 ) + remarqTowhom(sortedValues[1].second, product)

                                 _bottomsheetbinding.prodThirdpriceSpanel.text = pricenotdefined(
                                     requireContext(),
                                     sortedValues[2].first.toString(),
                                     true
                                 ) + remarqTowhom(sortedValues[2].second, product)

                                 _bottomsheetbinding.prodForthpriceSpanel.text = pricenotdefined(
                                     requireContext(),
                                     sortedValues[3].first.toString(),
                                     true
                                 ) + remarqTowhom(sortedValues[3].second, product)

                                 _bottomsheetbinding.prodFifthpriceSpanel.text = pricenotdefined(
                                     requireContext(),
                                     sortedValues[4].first.toString(),
                                     true
                                 ) + remarqTowhom(sortedValues[4].second, product)
                 */

                            magasinImage.setImageResource(
                                Functions.logopalcer(
                                    binding.root.context,
                                    sortedValues[0].second
                                )
                            )
                            // loadimageInt(logopalcer(requireContext(), sortedValues[0].second), _bottomsheetbinding.magasinImage)
                            /*    loadimageInt(logopalcer(requireContext(), sortedValues[1].second), _bottomsheetbinding.secondpriceImagevwPanel)
                                loadimageInt(logopalcer(requireContext(), sortedValues[2].second), _bottomsheetbinding.thirdpriceImagevwPanel)
                                loadimageInt(logopalcer(requireContext(), sortedValues[3].second), _bottomsheetbinding.fourthpriceImagevwPanel)
                                loadimageInt(logopalcer(requireContext(), sortedValues[4].second), _bottomsheetbinding.fifthpriceImagevwPanel)*/

                            //   }
                            // }
                        }


                    }





                }

                showProductWithoutBill(model, sortedValues)
            }


        }
    }


    interface OnItemClickListener {
      //  fun onTextEditConfirmClick(task: Product, quantityEdited:Double)
        // fun onTextEditChanged(task: ProductShopingRoom,quantityEdited:Double)
        fun onAddQuantityBtnClick(task: Product)
      //  fun onMinusQuantityBtnClick(task: Product)
        fun onItemClick(task: Product)
    }




}