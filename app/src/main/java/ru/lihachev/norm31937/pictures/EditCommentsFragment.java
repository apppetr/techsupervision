package ru.lihachev.norm31937.pictures;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Comment;

public class EditCommentsFragment extends Fragment {
   private CommentsAdapter adapter;

   public void setComments(List<Comment> comments) {

      this.adapter.setComments(comments);
   }

   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_comment_list, container, false);
   }

   public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      this.adapter = new CommentsAdapter(getActivity(), (List<Comment>) null);
      RecyclerView recView = (RecyclerView) view.findViewById(android.R.id.list);
      recView.setLayoutManager(new LinearLayoutManager(getActivity()));
      recView.setAdapter(this.adapter);
   }
}