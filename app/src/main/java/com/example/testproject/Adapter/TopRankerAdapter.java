package com.example.testproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testproject.Model.TopRankerSetGet;
import com.example.testproject.R;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TopRankerAdapter extends RecyclerView.Adapter<TopRankerAdapter.rankerViewHolder> {
    Context context;
    List<TopRankerSetGet> topRankerList;
    public TopRankerAdapter(Context context, List<TopRankerSetGet> topRankerList) {
        this.context=context;
        this.topRankerList=topRankerList;

    }

    @NonNull
    @Override
    public TopRankerAdapter.rankerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.top_ranker_view, parent, false);
        return new TopRankerAdapter.rankerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopRankerAdapter.rankerViewHolder holder, int position) {
            holder.tv_ranker_name.setText(topRankerList.get(position).getRanker_user_name());
            holder.tv_ranker_score.setText(topRankerList.get(position).getRanker_correct_mark()
            +" / "+topRankerList.get(position).getRanker_total_marks());

        Picasso.with(context).load(topRankerList.get(position).getRanker_user_img())
                .error(context.getResources().getDrawable(R.drawable.icon_profile))
                .into(holder.iv_top_ranker);
    }

    @Override
    public int getItemCount() {
        return topRankerList.size();
    }
    class rankerViewHolder extends RecyclerView.ViewHolder{
        CircleImageView iv_top_ranker;
        TextView tv_ranker_name,tv_ranker_score;
        public rankerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_top_ranker=itemView.findViewById(R.id.iv_top_ranker);
            tv_ranker_name=itemView.findViewById(R.id.tv_ranker_name);
            tv_ranker_score=itemView.findViewById(R.id.tv_ranker_score);
        }
    }
}
