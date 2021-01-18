package ru.lihachev.norm31937.defects;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.impl.SimpleJSONMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.pictures.PictureEditActivity;
import ru.lihachev.norm31937.utils.SelectListener;
import ru.lihachev.norm31937.utils.alerts.Dialogs;
import ru.lihachev.norm31937.utils.alerts.SelectDialogs;
import ru.lihachev.norm31937.values.ValuesProvider;
import ru.lihachev.norm31937.widgets.ExpandableLayout;
import ru.lihachev.norm31937.widgets.RVCursorAdapter;

public class VariantsAdapter extends RVCursorAdapter<VariantsAdapter.VariantViewHolder> {
    private final LayoutInflater inflater;
    JSONMaker maker = new SimpleJSONMaker();
    public final SparseBooleanArray selectedIds = new SparseBooleanArray();
    public String URI = "ru.lihachev.norm31937.URI";
    public Variant variant;
    public Defect defect;
    public VariantsAdapter(@NonNull Context context, Cursor cursor, String uri) {
        super(context, cursor);
        this.inflater = LayoutInflater.from(context);
        this.URI = SelectVariantsActivity.URI;
    }

    public void setSelectedIds(@NonNull int[] selectedIds2) {
        this.selectedIds.clear();
        for (int selectedValue : selectedIds2) {
            this.selectedIds.put(selectedValue, true);
        }
        notifyDataSetChanged();
    }

    public VariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VariantViewHolder(this.inflater.inflate(R.layout.item_variant, parent, false));
    }

    public void onBindViewHolder(VariantViewHolder holder, Cursor cursor) {
        this.variant = (Variant) CupboardFactory.cupboard().withCursor(cursor).get(Variant.class);
        holder.textVariant.setText(variant.getName());
        //holder.descriptiontextView.setText(variant.getS_description());

        if (!variant.getS_description().equals(""))
        {
            holder.tvAddSnipToReport.setText("Добавить к отчету");//в базу устанавливать тег добаления комментария к отчету
            holder.descriptiontextView.setText(variant.getS_description());
        }
        else{
            holder.tvAddSnipToReport.setVisibility(View.INVISIBLE);
            holder.descriptiontextView.setVisibility(View.INVISIBLE);
         //   holder.descriptionTextLeftline.setVisibility(View.INVISIBLE);
        }

        //if (variant.get_DefectSizes() != null)
            //holder.defectDetails.setText(variant.get_DefectSizes());
        //else
            holder.defectDetails.setText("Добавить размеры");

        if (variant.get_Note() != null)
            holder.notetextView.setText(variant.get_Note());
        else holder.notetextView.setText(R.string.note);


        holder.textCheckBox.setChecked(this.selectedIds.get(variant.getId()));

        holder.tvAddNoteToReport.setText("Добавить к отчету");//в базу устанавливать тег добаления заметки к отчету
        holder.textCheckBox.setChecked(this.selectedIds.get(variant.getId()));
    }

    public List<Variant> getSelected() {
        List<Variant> selectedVariants = new ArrayList<>();
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            if (this.selectedIds.get((int) getItemId(i))) {
                selectedVariants.add((Variant) CupboardFactory.cupboard().withCursor(getItem(i)).get(Variant.class));
            }
        }
        return selectedVariants;
    }

    class VariantViewHolder extends RVCursorAdapter.SelectableViewHolder {
        public final CheckBox textCheckBox;
        public final TextView textVariant;
        public final TextView descriptiontextView;
        public final TextView descriptionToggle;
        public final TextView defectDetails;
        public final TextView notetextView;
        public final TextView tvAddSnipToReport;
        public final TextView tvAddNoteToReport;
        public final RelativeLayout tvnoteContainer;
        public final ImageView addDefectSize;

       // public final TextView descriptionTextLeftline;
        public final ExpandableLayout expandableLayout;

        //здесь заполняютя строки нужно сделать это выпадающим списком
        public VariantViewHolder(View itemView) {
            super(itemView);

            this.textCheckBox = (CheckBox) itemView.findViewById(R.id.cbVariant);
            this.textVariant = (TextView) itemView.findViewById(R.id.cbVariantText);
            this.descriptiontextView = (TextView) itemView.findViewById(R.id.tvDesc);
            this.descriptionToggle = (TextView) itemView.findViewById(R.id.descriptionToggle);
            this.tvnoteContainer = (RelativeLayout) itemView.findViewById(R.id.tvNoteContainer);
            this.notetextView = (TextView) itemView.findViewById(R.id.tvAddNote);
            this.defectDetails = (TextView) itemView.findViewById(R.id.tvAddSizes);
            this.tvAddSnipToReport = (TextView) itemView.findViewById(R.id.tvAddSnipToReport);
            this.tvAddNoteToReport = (TextView) itemView.findViewById(R.id.tvAddNoteToReport);
            this.addDefectSize = (ImageView) itemView.findViewById(R.id.bDefectSize);

         //   this.descriptionTextLeftline = (TextView)  itemView.findViewById(R.id.leftline);
            this.textCheckBox.setOnClickListener(this);
            this.descriptionToggle.setOnClickListener(this);
            this.addDefectSize.setOnClickListener(this);//кнопка редактировать размеры
            this.tvnoteContainer.setOnClickListener(this);//редактировать комментарий
            this.expandableLayout = new ExpandableLayout((LinearLayout) itemView.findViewById(R.id.llDescription));

        }

        public void onClick(@NonNull View v) {
            int id = v.getId();
            switch (id) {
                case R.id.descriptionToggle:
                    if (this.expandableLayout.isVisible()) {
                        this.expandableLayout.expand();
                    } else {
                        this.expandableLayout.collapse();
                    }
                    break;
                case R.id.cbVariant:
                    int itemid = (int) getItemId();
                    boolean checked = this.textCheckBox.isChecked();
                    VariantsAdapter.this.selectedIds.delete(itemid);
                    VariantsAdapter.this.selectedIds.put(itemid, checked);
                    VariantsAdapter.this.notifyItemChanged(getLayoutPosition());
                    break;
                case R.id.tvNoteContainer:
                    View view2 = LayoutInflater.from(v.getContext()).inflate(R.layout.photo_comment, (ViewGroup) null);
                    final EditText etText = (EditText) view2.findViewById(R.id.etText);
                    Dialogs.showCustomView(view2.getContext(), R.string.noteComment, view2, R.string.apply, R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int which) {
                            if (which == -1) {
                                JSONMaker jSONMaker = VariantsAdapter.this.maker;
                                variant.setNote(etText.getText().toString());
                                Object[] objArr = {variant.get_Note(), Long.valueOf(System.currentTimeMillis())};
                             //   List<Variant> elements = CupboardFactory.cupboard().withContext(this.context).query(ValuesProvider.uri("elements"), Variant.class).list();
                               CupboardFactory.cupboard().withContext(view2.getContext()).update(ValuesProvider.uri("elements"), CupboardFactory.cupboard().withEntity(Variant.class).toContentValues(variant));

                            }
                        }  });
                    break;
                case R.id.bDefectSize:
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.fragment_defect_size, (ViewGroup) null);
                //   final EditText etText = (EditText) view.findViewById(R.id.etText);
                  //   SelectDialogs.showMeasumentDialog(view.getContext(), new SelectListener() {
                    //  @Override
                    //  public void onSelected(final Object selection) {
                      //  if (selection != null) {
                      //  }
                     // }
                    // }).show();
                   Dialogs.showCustomView(view.getContext(), R.string.mark_comment, view, R.string.apply, R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int which) {

                        }  });
                    break;
                default:
                    break;
            }

        }
    }
}