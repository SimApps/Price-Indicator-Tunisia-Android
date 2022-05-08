package com.amirami.simapp.priceindicatortunisia.fidcard
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirami.simapp.priceindicatortunisia.BR
import java.util.ArrayList
import com.amirami.simapp.priceindicatortunisia.databinding.FidCardDataBinding
import com.amirami.simapp.priceindicatortunisia.utils.Functions.setSafeOnClickListener

class FidCardAdapter(private val listener: FidCardItemListener)
    : ListAdapter<Barecode, FidCardAdapter.FidCardViewHolder>(DiffCallback()){


    interface FidCardItemListener {
        fun onClickedFidCardDelete(barecode: Barecode)
        fun onClickedFidCardEdit(barecode: Barecode)
        fun onClickedFidCardItem(barecode: Barecode)
    }
    private val items = ArrayList<Barecode>()
    private lateinit var barecode: Barecode

    fun setItems(items: MutableList<Barecode>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }



    inner  class FidCardViewHolder(private val dataBinding: FidCardDataBinding//,
        //  private val viewBinding: ViewDataBinding
    ): RecyclerView.ViewHolder(dataBinding.root){

        // val fidcardRvName: TextView = itemView.findViewById(R.id.fidcardRvName)
        // val deleteFidCardRvImvw: ImageView = itemView.findViewById(R.id.deleteFidCardRvImvw)
        init {
            dataBinding.apply {
                deleteFidCardRvImvw.setSafeOnClickListener {
                    listener.onClickedFidCardDelete( items[bindingAdapterPosition])
                }

               editFidcardimVw.setSafeOnClickListener {
                   listener.onClickedFidCardEdit( items[bindingAdapterPosition])
                }
                fidcardRvName.setSafeOnClickListener {
                    listener.onClickedFidCardItem( items[bindingAdapterPosition])
                }
            }


        }



        fun bindData(barecode: Barecode) = /*viewBinding*/dataBinding.setVariable(BR.fidcard, barecode)
    }

    /*{

        val fidcardRvName: TextView = itemView.findViewById(R.id.fidcardRvName)

        val deleteFidCardRvImvw: ImageView = itemView.findViewById(R.id.deleteFidCardRvImvw)



        init {
            itemLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClickedFidCard(textTitle.text)
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FidCardViewHolder {
        //       val v= LayoutInflater.from(parent.context).inflate(R.layout.fidcard_tiket, parent, false)

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FidCardDataBinding.inflate(
            layoutInflater,
            parent,
            false
        )

        /*  val binding = FidCardDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
          val layoutInflater = LayoutInflater.from(parent.context)
          val dataBinding = FidCardDataBinding.inflate(
              layoutInflater,
              parent,
              false
          )*/
        return FidCardViewHolder( binding/*,dataBinding*/)
    }

    override fun getItemCount(): Int {
        return items.size     //fidCardDBList.size
    }


    override fun onBindViewHolder(holderFidCard: FidCardViewHolder, position: Int) {


        barecode = items[position]
        //   val currentItem = getItem(position)
        //  val product = productList[position]
        holderFidCard.bindData(barecode)

        //    val cardFidDBList= dbHandlerFidCard.getFidCardDB()
        //  val cardFid:DbVariables=cardFidDBList[ holder.bindingAdapterPosition]

        //  holderFidCard.fidcardRvName.text=items[position].name//cardFid.FidCard_NameDB
        /*
           holder.deleteFidCardRvImvw.setSafeOnClickListener {
               Functions.showDialog(
                   mCtx,
                   "deleteFidCard",
                   //cardFid.FidCard_NameDB
                   items[position].name,
                   (mCtx as FragmentActivity).supportFragmentManager
               )

           }
           */
        /*  holderFidCard.fidcardRvName.setSafeOnClickListener {
               imageFidCarDialog(mCtx, items[position].name, items[position].value,items[position].barecodetype)
           }*/
    }


    class DiffCallback : DiffUtil.ItemCallback<Barecode>() {
        override fun areItemsTheSame(oldItem: Barecode, newItem: Barecode) =
            oldItem.value == newItem.value

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Barecode, newItem: Barecode) =
            oldItem == newItem
    }
}