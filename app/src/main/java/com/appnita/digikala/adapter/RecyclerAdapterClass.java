package com.appnita.digikala.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnita.digikala.R;
import com.appnita.digikala.ui.WebPage;
import com.appnita.digikala.ui.RecyclerObjectClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterClass extends RecyclerView.Adapter<RecyclerAdapterClass.MyViewHolder> {

    Context context;
    List<RecyclerObjectClass> list;

    public RecyclerAdapterClass() {
    }

    public RecyclerAdapterClass(Context context, List<RecyclerObjectClass> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecyclerObjectClass recyclerObject = list.get(position);
//        holder.imageview.setImageResource(R.drawable.ic_launcher_background);
        Picasso.with(context)
                .load(recyclerObject.getImage())
                .placeholder(R.drawable.error)
                .into(holder.imageview);
        holder.title.setText(recyclerObject.getTitle());
        holder.content.setText(recyclerObject.getContent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.content.setText(Html.fromHtml(recyclerObject.getContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.content.setText(Html.fromHtml(recyclerObject.getContent()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebPage.class);
                intent.putExtra("url",recyclerObject.getUrl());
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageview;
        TextView title;
        TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.rv_image);
            title = itemView.findViewById(R.id.rv_title);
            content = itemView.findViewById(R.id.rv_content);
        }
    }
}
