package com.AndersonHsieh.wmma_wheremymoneyat.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.AndersonHsieh.wmma_wheremymoneyat.R
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.TransactionRecyclerItemsBinding
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.ui.edit_activity.EditActivity
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.TransactionViewModel
import com.chauthai.swipereveallayout.SwipeRevealLayout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerAdapter(
    private val dataSet: MutableList<Transaction>,
    private val viewModel: TransactionViewModel
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: TransactionRecyclerItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //view holder is a container of the actual elements of each element in the list item
        //each item in the recyclerview gets a viewHolder


        //since we don't have that many views inside of each recyclerview item
        //storing a single view binding object can void lots of unnecessary code
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        // Create a new viewfinder, which defines the UI of the list item
        // create a container of the view elements

        var binding = TransactionRecyclerItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        //bind the data set and the views in the viewholder
        //in this case, a single view binding variable takes care of all the views

        with(holder.binding) {
            transactionRecyclerviewSwipeLayout.setSwipeListener(object :
                SwipeRevealLayout.SwipeListener {
                override fun onClosed(view: SwipeRevealLayout?) {
                    transactionRecyclerviewItemArrowImgview.rotation = 270f
                }

                override fun onOpened(view: SwipeRevealLayout?) {
                    transactionRecyclerviewItemArrowImgview.rotation = 90f
                }

                override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                }

            })
            transactionRecyclerviewItemNameTextview.text = dataSet[position].name
            transactionRecyclerviewItemAmountTextview.text = dataSet[position].amount.toString()
            transactionRecyclerviewItemTimeTextview.text = dataSet[position].time
            transactionRecyclerviewItemEdit.setOnClickListener {
                if (viewModel.isConnectedToInternet()) {
                    //TODO(waiting to debug)
                    packTransactionInfoAndStartEditActivity(
                        dataSet[position].id,
                        dataSet[position].name,
                        dataSet[position].amount,
                        it.context
                    )

                    //starting a new activity while closing swipelayout create possible screen glitch
                    //thus we sacrifice the animation for better visual experience
                    transactionRecyclerviewSwipeLayout.close(false)
                } else {
                    //user is only allowed to view the collection of transactions cached in SQLite when offline
                    Toast.makeText(
                        transactionRecyclerviewItemEdit.context,
                        R.string.offline,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            transactionRecyclerviewItemDelete.setOnClickListener {
                if (viewModel.isConnectedToInternet()) {
                    //TODO(waiting to debug)
                    Log.d(
                        Constants.LOGGING_TAG,
                        "onResponse: clicked ${dataSet[holder.adapterPosition].id}"
                    )
                    viewModel.deleteTransactions(dataSet[holder.adapterPosition].id)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                //only remove the item with API DELETE request is successful

                                dataSet.removeAt(holder.adapterPosition)
                                notifyItemRemoved(holder.adapterPosition)
                                Log.d(Constants.LOGGING_TAG, "onResponse: recycer ")

                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.d(Constants.LOGGING_TAG, "Failure: recycer")

                            }
                        })
                    transactionRecyclerviewSwipeLayout.close(true)
                } else {
                    //user is only allowed to view the collection of transactions cached in SQLite when offline
                    Toast.makeText(
                        transactionRecyclerviewItemEdit.context,
                        R.string.offline,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun packTransactionInfoAndStartEditActivity(
        id: Long,
        name: String,
        amount: Double,
        context: Context
    ) {
        val bundle = Bundle()
        bundle.putLong(Constants.TRANSACTION_ID, id)
        bundle.putString(Constants.TRANSACTION_NAME, name)
        bundle.putDouble(Constants.TRANSACTION_AMOUNT, amount)
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

}