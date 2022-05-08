package com.amirami.simapp.priceindicatortunisia.shoping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.ProductTiketBinding
import com.amirami.simapp.priceindicatortunisia.utils.Constants.PRODUITS_FRAIS
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.minusordeleteimage
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import java.math.BigDecimal

class ProductShopingRV(private val listener: OnItemClickListener) :
    ListAdapter<MutableList<ProductShopingRoom>, ProductShopingRV.TasksViewHolder>(DiffCallback()) {

    private val items = ArrayList<ProductShopingRoom>()
    private lateinit var ProductShopingRoom: ProductShopingRoom

    fun setItems(items: MutableList<ProductShopingRoom>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun numberformating(number:Double):String{
        return if (Functions.factor100(BigDecimal.valueOf(number)))
            number.toString()
        else
            String.format("%.3f", BigDecimal.valueOf(number))
    }

    fun setTextviewProdType(edtittext: EditText, productS: ProductShopingRoom, plus:Boolean){
        if(productS.typesub == PRODUITS_FRAIS){
            if(plus) edtittext.setText(String.format("%.3f", BigDecimal.valueOf(productS.quantity!!)+ BigDecimal.valueOf(0.1)))
            else if(BigDecimal.valueOf(productS.quantity!!)- BigDecimal.valueOf(0.1) <= BigDecimal.valueOf(0.001))
                edtittext.setText("0.001")
              else   edtittext.setText(String.format("%.3f", BigDecimal.valueOf(productS.quantity!!)- BigDecimal.valueOf(0.1)))
        }
        else{
            if(plus) edtittext.setText((productS.quantity!! + 1.0).toInt().toString())
            else if(productS.quantity!! - 1.0 <=0.0) edtittext.setText("1")
                else edtittext.setText((productS.quantity!! - 1.0).toInt().toString())
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ProductTiketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {

        ProductShopingRoom = items[position]


        holder.bind(ProductShopingRoom)
    }

    override fun getItemCount(): Int {
        return items.size     //fidCardDBList.size
    }

    inner class TasksViewHolder(private val binding: ProductTiketBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {

                bestpriceAndQuantityTxVw.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            listener.onTextEditConfirmClick(items[bindingAdapterPosition],if(bestpriceAndQuantityTxVw.text.toString()!="") bestpriceAndQuantityTxVw.text.toString().replace(",", ".").toDouble() else 0.0)

                        }

                        true
                    } else false
                }


/*
                bestpriceAndQuantityTxVw.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {

                    }
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            if (bestpriceAndQuantityTxVw.text.toString().isNotEmpty()) {
                                if(bestpriceAndQuantityTxVw.text.toString().replace(",", ".").toDouble()!=items[bindingAdapterPosition].quantity){
                                    minusordeleteimage(
                                        if(bestpriceAndQuantityTxVw.text.toString()!="")
                                            bestpriceAndQuantityTxVw.text.toString().replace(",", ".").toDouble()
                                        else  0.0,
                                        items[bindingAdapterPosition].typesub == PRODUITS_FRAIS,
                                        logoAndMinusImVw
                                    )

                                    listener.onTextEditChanged(items[bindingAdapterPosition],
                                        bestpriceAndQuantityTxVw.text.toString().replace(",", ".").toDouble()
                                    )
                                }

                            }
                        }

                    }
                })
*/
                addtoshoplistAndPlusTxVw.setSafeOnClickListener{
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        setTextviewProdType(bestpriceAndQuantityTxVw,items[bindingAdapterPosition],true)
                        listener.onAddQuantityBtnClick(items[bindingAdapterPosition])
                    }

                }

                logoAndMinusImVw.setSafeOnClickListener{

                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                       setTextviewProdType(bestpriceAndQuantityTxVw,items[bindingAdapterPosition],false)
                        listener.onMinusQuantityBtnClick(items[bindingAdapterPosition])
                    }

                }

                linearLayoutHolder.setSafeOnClickListener {

                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items[bindingAdapterPosition])

                    }
                }
            }
        }

        fun bind(task: ProductShopingRoom) {


            binding.apply {
                priceTxVw.text =  root.context.getString(R.string.Lunitéest, Functions.removeAllDigitExeptX(task.sieze))
                magasinImage.visibility = View.GONE

                nameTxVw.text = task.name

               if( ProductShopingRoom.typesub == PRODUITS_FRAIS) bestpriceAndQuantityTxVw.setText(numberformating(task.quantity!!))
               else bestpriceAndQuantityTxVw.setText(task.quantity!!.toInt().toString())


              //  addtoshoplistAndPlusTxVw.layoutParams.height = ViewGroup.MarginLayoutParams.WRAP_CONTENT
              //  addtoshoplistAndPlusTxVw.layoutParams.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT

           //     logoAndMinusImVw.layoutParams.height = ViewGroup.MarginLayoutParams.WRAP_CONTENT
            //    logoAndMinusImVw.layoutParams.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT

                /*  if(task.typesub == PRODUITS_FRAIS)
                                  bestpriceAndQuantityTxVw.addDecimalLimiter(3)
                              else
                                  bestpriceAndQuantityTxVw.addDecimalLimiter(0)
              */
                Functions.loadimageurl(root.context, task.imageurl, imageview)

                addtoshoplistAndPlusTxVw.setImageResource(R.drawable.ic_plus)

               minusordeleteimage(
                    task.quantity!!,
                    task.typesub == PRODUITS_FRAIS,
                    logoAndMinusImVw
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onTextEditConfirmClick(task: ProductShopingRoom, quantityEdited:Double)
       // fun onTextEditChanged(task: ProductShopingRoom,quantityEdited:Double)
        fun onAddQuantityBtnClick(task: ProductShopingRoom)
        fun onMinusQuantityBtnClick(task: ProductShopingRoom)
        fun onItemClick(task: ProductShopingRoom)
    }

    class DiffCallback : DiffUtil.ItemCallback<MutableList<ProductShopingRoom>>() {
        override fun areItemsTheSame(oldItem: MutableList<ProductShopingRoom>, newItem: MutableList<ProductShopingRoom>) =
            oldItem[0].productid == newItem[0].productid

        override fun areContentsTheSame(oldItem: MutableList<ProductShopingRoom>, newItem: MutableList<ProductShopingRoom>) =
            oldItem == newItem
    }
}