package ru.sviridov.techsupervision.pictures;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Comment;

public class CommentsAdapter extends RecyclerView.Adapter {
   private static final Map CLASS_TO_NAME;
   private List comments;
   private final LayoutInflater inflater;

   static {
      HashMap var0 = new HashMap();
      var0.put("ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotLineInstrument", "Линия");
      var0.put("ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotArrowInstrument", "Стрелка");
      var0.put("ru.sviridov.techsupervision.utils.vectors.impl.pivots.PivotOvalInstrument", "Овал");
      CLASS_TO_NAME = Collections.unmodifiableMap(Collections.synchronizedMap(var0));
   }

   public CommentsAdapter(@NonNull Context var1, @Nullable List var2) {
      this.inflater = LayoutInflater.from(var1);
      this.comments = new ArrayList();
      if (var2 != null) {
         this.comments.addAll(var2);
      }

   }

   public int getItemCount() {
      return this.comments.size();
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {

   }

   public void onBindViewHolder(CommentsAdapter.CommentViewHolder var1, int var2) {
      Comment var3 = (Comment)this.comments.get(var2);
      var1.tvTitle.setText((CharSequence)CLASS_TO_NAME.get(var3.getType()));
      var1.tvDate.setText(String.format("добавлено %1$te %1$tB %1$tY", var3.getDate()));
      var1.tvIndex.setText(String.valueOf(var2 + 1));
      var1.tvDesc.setText(var3.getDescription());
   }

   public CommentsAdapter.CommentViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new CommentsAdapter.CommentViewHolder(this.inflater.inflate(R.layout.item_comment, var1, false));
   }

   public void setComments(List var1) {
      this.comments = var1;
      this.notifyDataSetChanged();
   }

   class CommentViewHolder extends RecyclerView.ViewHolder {
      private final TextView tvDate;
      private final TextView tvDesc;
      private final TextView tvIndex;
      private final TextView tvTitle;

      public CommentViewHolder(View var2) {
         super(var2);
         this.tvTitle = (TextView)var2.findViewById(R.id.tvTitle);
         this.tvDate = (TextView)var2.findViewById(R.id.tvDate);
         this.tvIndex = (TextView)var2.findViewById(R.id.tvIndex);
         this.tvDesc = (TextView)var2.findViewById(R.id.tvDesc);
      }
   }
}
