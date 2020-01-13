package com.burak.tour.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.burak.tour.model.Tour;
import com.burak.tour.R;
import com.burak.tour.DetailsActivity;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private Context context;
    private List<Tour> dataList;

    public ListAdapter(Context context, List<Tour> dataList){
        this.context = context;
        this.dataList = dataList;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

//        view.setOnClickListener(this);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ListAdapter.CustomViewHolder holder, int position) {

        Tour tour = dataList.get(position);



        Picasso.get()
                .load(tour.getAvatar())
                .fit()
                .placeholder(R.drawable.loading)
                .transform(new RoundedCornersTransformation(10,0))
                .into(holder.avatar);


        holder.name.setText(tour.getName().toUpperCase()+" - "+tour.getCity().toUpperCase());
        holder.details.setText("$"+tour.getPrice()+" / "+tour.getCategory());


    }

    @Override
    public int getItemCount() {
        return (dataList!=null ? dataList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView name;
        ImageView avatar;
        TextView category;
        TextView date;
        TextView details;
        CardView cardView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        CustomViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.full_name);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            date = (TextView) itemView.findViewById(R.id.date);
            category = (TextView) itemView.findViewById(R.id.category);
            details = (TextView) itemView.findViewById(R.id.details);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Intent i=new Intent(v.getContext(), DetailsActivity.class);
            i.putExtra("name",dataList.get(clickedPosition).getName());
            i.putExtra("createdAt",dataList.get(clickedPosition).getCreatedAt());
            i.putExtra("avatar",dataList.get(clickedPosition).getAvatar());
            i.putExtra("city",dataList.get(clickedPosition).getCity());
            i.putExtra("date",dataList.get(clickedPosition).getDate());
            i.putExtra("category",dataList.get(clickedPosition).getCategory());
            i.putExtra("price",dataList.get(clickedPosition).getPrice());
            i.putExtra("tourid",dataList.get(clickedPosition).getTourid());

            context.startActivity(i);


        }
    }
}
