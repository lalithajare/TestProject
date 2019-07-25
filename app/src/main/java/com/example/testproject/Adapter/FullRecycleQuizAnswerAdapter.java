package com.example.testproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.testproject.Model.AnswerSetGet;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import java.util.List;

public class FullRecycleQuizAnswerAdapter extends RecyclerView.Adapter<FullRecycleQuizAnswerAdapter.ansViewHolder> {
    private Context context;
    private List<AnswerSetGet> ansList;

    private int selectedPosition = -1;
    private Button btn_save_next, btn_clear;
    private String question, questionId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public FullRecycleQuizAnswerAdapter(Context context, List<AnswerSetGet> ansList, String question, String questionId, Button btn_save_next/*,Button btn_clear*/, Integer selectedPosition) {

        this.context = context;
        this.ansList = ansList;
        this.btn_save_next = btn_save_next;
        //this.btn_clear = btn_clear;
        this.question = question;
        this.questionId = questionId;
        if (selectedPosition == -1){
        }else {
            this.selectedPosition = selectedPosition;
        }
        Const.testquestionAnswerStoreHash.put(question, ansList);
    }

    @NonNull
    @Override
    public FullRecycleQuizAnswerAdapter.ansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.custom_gk_answer_list_layout, parent, false);
        return new FullRecycleQuizAnswerAdapter.ansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FullRecycleQuizAnswerAdapter.ansViewHolder viewHolder, int position) {

        //AnswerSetGet answerSetGet = ansList.get(position);
        int no=position+1;
        Spanned htmlAsSpanned = Html.fromHtml(ansList.get(position).getGoal_answer());
        viewHolder.radioButton.setText("("+(char)(no+'A'-1)+ ")  "+htmlAsSpanned);
        pref = context.getSharedPreferences("HashValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
      /*  for (Map.Entry<String, ?> entry : pref.getAll().entrySet()) {
            Const.answerStoreHash.put(entry.getKey(), ansList.get(position).getGoal_answer());*/
            if (Const.answerStoreHash.containsKey(questionId)) {
                //check the radio button if both position and selectedPosition matches
                viewHolder.radioButton.setChecked(position == selectedPosition);
            }
       /* }*/

        //Set the position tag to both radio button and label
        viewHolder.radioButton.setTag(position);

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!viewHolder.radioButton.isSelected()) {
                    viewHolder.radioButton.setChecked(true);
                    viewHolder.radioButton.setSelected(true);
                    //clearButton.setEnabled(true);
                    //clearButton.setTextColor(Color.parseColor("#FFFFFF"));
                    itemCheckChanged(v);

                } else {

                    viewHolder.radioButton.setChecked(false);
                    viewHolder.radioButton.setSelected(false);
                    Const.answerStoreHash.remove(questionId);
                    // clearButton.setEnabled(false);
                    // clearButton.setTextColor(Color.parseColor("#B9BBBB"));
                     btn_save_next.setVisibility(View.GONE);
                }*/

                itemCheckChanged(v);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return ansList.size();
    }
    class ansViewHolder extends RecyclerView.ViewHolder{

        private RadioButton radioButton;
        public ansViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.ans_radio);
        }
    }
    //On selecting any view set the current position to selectedPosition and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
        Const.CHOOSE_QUESTION_ID = questionId;
        Const.ANSWER_ID = ansList.get(selectedPosition).getGoal_answers_id();
        btn_save_next.setVisibility(View.VISIBLE);
        // btn_clear.setVisibility(View.VISIBLE);
        // btn_clear.setText("Clear\n Selection");
       // btn_clear.setTextColor(Color.parseColor("#FFFFFF"));

        Const.answerStoreHash.put(questionId, ansList.get(selectedPosition).getGoal_answer());
        Const.answerCheckHash.put(questionId, String.valueOf(selectedPosition));
        Const.answerQuestionStoreHash.put(question, ansList.get(selectedPosition).getGoal_answer());

    }



    //Return the selectedPosition item
    public String getSelectedItem() {
        if (selectedPosition != -1) {
            return ansList.get(selectedPosition).getGoal_answers_id();
        }
        return "";
    }

    //Delete the selected position from the arrayList
    public void deleteSelectedPosition() {
        if (selectedPosition != -1) {
            ansList.remove(selectedPosition);
            selectedPosition = -1;//after removing selectedPosition set it back to -1
            notifyDataSetChanged();
        }
    }
}
