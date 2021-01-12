package ru.lihachev.norm31937.pictures;

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

import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Comment;

/* renamed from: ru.lihachev.norm31937.pictures.CommentsAdapter */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
   private static final Map<String, String> CLASS_TO_NAME;
   private List<Comment> comments = new ArrayList();
   private final LayoutInflater inflater;

   static {
      HashMap<String, String> data = new HashMap<>();
      data.put("ru.lihachev.norm31937.utils.vectors.impl.pivots.PivotLineInstrument", "Линия");
      data.put("ru.lihachev.norm31937.utils.vectors.impl.pivots.PivotArrowInstrument", "Стрелка");
      data.put("ru.lihachev.norm31937.utils.vectors.impl.pivots.PivotOvalInstrument", "Овал");
      CLASS_TO_NAME = Collections.unmodifiableMap(Collections.synchronizedMap(data));
   }

   public CommentsAdapter(@NonNull Context context, @Nullable List<Comment> comments2) {
      this.inflater = LayoutInflater.from(context);
      if (comments2 != null) {
         this.comments.addAll(comments2);
      }
   }

   public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new CommentViewHolder(this.inflater.inflate(R.layout.item_comment, parent, false));
   }

   public int getItemCount() {
      return this.comments.size();
   }

   public void onBindViewHolder(CommentViewHolder holder, int position) {
      Comment comment = this.comments.get(position);
      holder.tvTitle.setText(CLASS_TO_NAME.get(comment.getType()));
      holder.tvDate.setText(String.format("добавлено %1$te %1$tB %1$tY", new Object[]{Long.valueOf(comment.getDate())}));
      holder.tvIndex.setText(String.valueOf(position + 1));
      holder.tvDesc.setText(comment.getDescription());
   }

   public void setComments(List<Comment> comments2) {
      this.comments = comments2;
      notifyDataSetChanged();
   }

   /* renamed from: ru.lihachev.norm31937.pictures.CommentsAdapter$CommentViewHolder */
   class CommentViewHolder extends RecyclerView.ViewHolder {
      /* access modifiers changed from: private */
      public final TextView tvDate;
      /* access modifiers changed from: private */
      public final TextView tvDesc;
      /* access modifiers changed from: private */
      public final TextView tvIndex;
      /* access modifiers changed from: private */
      public final TextView tvTitle;

      public CommentViewHolder(View itemView) {
         super(itemView);
         this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
         this.tvDate = (TextView) itemView.findViewById(R.id.tvDate);
         this.tvIndex = (TextView) itemView.findViewById(R.id.tvIndex);
         this.tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
      }
   }
}
