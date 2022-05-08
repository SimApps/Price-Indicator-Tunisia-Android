package com.amirami.simapp.priceindicatortunisia.rvadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.databinding.ProducttypesTiketBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener
import java.text.Normalizer


class ProductTypesAdapter (private val item: Array<out String>, private val listener: OnItemClickListener): RecyclerView.Adapter<ProductTypesAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: ProducttypesTiketBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.apply {
                productTypeTiket.setSafeOnClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION)
                        listener.onItemTagsClick(item[bindingAdapterPosition]/*,images[bindingAdapterPosition]*/)
                }

            }
        }

        fun bind(position:Int) {
            binding.apply {




                productTypesTxVw.text = item[position]//.replaceFirstChar { it.uppercase() }
                //productTypesTxVw.text =item[position].unaccent()

/*
                  val uri = "@raw/" + "type_"+item[position].unaccent()
                  val imageResource = root.context.resources.getIdentifier(uri, null, root.context.packageName)
                */

                  productTypeImgVw.setAnimation(imageTypeLoader(item[position],root.context))
               // productTypeImgVw.setAnimationFromUrl("https://assets6.lottiefiles.com/packages/lf20_9ycwmgb9.json")





            }
        }
    }


    private fun imageTypeLoader(type:String,ctx:Context):Int{


        return if(type in ctx.resources.getStringArray(R.array.unit_of_mesur) &&type!="Oeufs" ) R.raw.unitmeasure
        else when (type){
            "Alimentation" ->R.raw.type_alimentation
            "Entretient de la Maison" ->R.raw.type_entretientdelamaison
            "Hygiène et Beauté" ->R.raw.type_hygieneetbeaute
            "Bébé" ->R.raw.type_bebe
            "Cuisine" ->R.raw.type_cuisine
            "Gros électroménager" ->R.raw.type_groselectromenager
            "Petit électroménager" ->R.raw.type_petitelectromenager
            "Animalerie" ->R.raw.type_animalerie
            "High-Tech" ->R.raw.type_hightech
            "Jardin" ->R.raw.type_jardin
            "Brico et Accessoires Auto" ->R.raw.type_bricoetaccessoiresauto
            "Fournitures Scolaires" ->R.raw.type_fournituresscolaires
            "Linge de Maison" ->R.raw.type_lingedemaison
            "Jeux et Jouets" ->R.raw.type_jeuxetjouets
            "Bagagerie" ->R.raw.type_bagagerie
            "Sport et Loisir" ->R.raw.type_sportetloisir
             "Oeufs"->R.raw.subtype_oeufs
          //  in ctx.resources.getStringArray(R.array.unit_of_mesur)->R.raw.unitmeasure
            "Tous les Catégories"->R.raw.allcategories
            else -> {
                R.raw.type_default
            }
        }
    }


    val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "").replace("\\s".toRegex(), "").lowercase().replace("-".toRegex(), "")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ProducttypesTiketBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = item.size

    interface OnItemClickListener {
        fun onItemTagsClick(item : String/*, image : Int*/)
    }

}
