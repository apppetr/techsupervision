package ru.sviridov.techsupervision.pictures;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import ru.sviridov.techsupervision.free.R;

public class EditCommentsFragment extends Fragment {
   private CommentsAdapter adapter;

   public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {
      return var1.inflate(R.layout.fragment_comment_list, var2, false);
   }

   public void onViewCreated(View var1, @Nullable Bundle var2) {
      super.onViewCreated(var1, var2);
      this.adapter = new CommentsAdapter(this.getActivity(), (List)null);

      RecyclerView var3 = (RecyclerView)var1.findViewById(android.R.id.list);
      var3.setLayoutManager(new LinearLayoutManager(this.getActivity()));
      var3.setAdapter(this.adapter);
   }

   public void setComments(List var1) {
      this.adapter.setComments(var1);
   }
}
