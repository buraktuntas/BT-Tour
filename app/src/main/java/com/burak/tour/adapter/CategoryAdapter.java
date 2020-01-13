package com.burak.tour.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.burak.tour.R;
import com.burak.tour.Interface.UpdateInterface;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>{

    private Context context;
    int temp=-1;
    private List<String> category;
    private UpdateInterface updateInterface;
    private int selected_position=-1;
    public CategoryAdapter(Context context, List<String> category, UpdateInterface updateInterface) {
        this.context = context;
        this.category = category;
        this.updateInterface = updateInterface;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_single_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final CustomViewHolder holder2=holder;

        holder.category_name.setText(category.get(position));

        if (holder2.category_name.getCurrentTextColor()==Color.parseColor("#ffffff") && temp==selected_position) {
            holder2.category_name.setBackgroundResource(R.drawable.buttonshape);
            holder2.category_name.setTextColor(Color.parseColor("#707070"));
            Log.e("if1:", String.valueOf(temp));
            temp=-1;

        }
        else if (selected_position == position) {
            holder2.category_name.setBackgroundResource(R.drawable.buttonshape_click);
            holder2.category_name.setTextColor(Color.parseColor("#ffffff"));
            Log.e("if2:",String.valueOf(temp));

        }
        else {
            holder2.category_name.setBackgroundResource(R.drawable.buttonshape);
            holder2.category_name.setTextColor(Color.parseColor("#707070"));
            Log.e("if3:",String.valueOf(temp));

        }


        holder.category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position != RecyclerView.NO_POSITION){
                    selected_position = position;
                    if(temp==selected_position){
                        Log.e("click:", String.valueOf(temp));
                            temp=-1;
                            updateInterface.kaydetClick("all");
                    }
                    else{
                            updateInterface.kaydetClick(category.get(position));
                    }
                    temp=selected_position;
                    notifyDataSetChanged();


                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return (null != category ? category.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        Button category_name;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        CustomViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            category_name = itemView.findViewById(R.id.name);


        }

    }
}
