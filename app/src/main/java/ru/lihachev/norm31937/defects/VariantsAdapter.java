package ru.lihachev.norm31937.defects;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
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
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Note;
import ru.lihachev.norm31937.objects.Snip;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.utils.alerts.Dialogs;
import ru.lihachev.norm31937.widgets.ExpandableLayout;
import ru.lihachev.norm31937.widgets.RVCursorAdapter;

public class VariantsAdapter extends RVCursorAdapter<VariantsAdapter.VariantViewHolder> {
    private final LayoutInflater inflater;
    JSONMaker maker = new SimpleJSONMaker();
    public final SparseBooleanArray selectedIds = new SparseBooleanArray();
    public String URI = "ru.lihachev.norm31937.URI";
    public Variant variant;
    public Defect defect;
    public HashMap<Integer, Variant> mapVariants = new HashMap<>();
    public Variant[] userData;
    public Variant[] userChecked;

    public VariantsAdapter(@NonNull Context context, Cursor cursor) {
        super(context, cursor);
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelectedIds(@NonNull int[] selectedIds2) {
        this.selectedIds.clear();
        for (int selectedValue : selectedIds2) {
            this.selectedIds.put(selectedValue, true);
        }
        notifyDataSetChanged();
    }

    public void setUserData(Parcelable[] arrayData) {
        if (arrayData == null) {
            userData = null;
        } else {
            userData = new Variant[arrayData.length];
            for (int i = 0; i < arrayData.length; i++) {
                userData[i] = (Variant) arrayData[i];
            }
            notifyDataSetChanged();
        }
    }

    public VariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VariantViewHolder(this.inflater.inflate(R.layout.item_variant, parent, false));
    }

    public void onBindViewHolder(VariantViewHolder holder, Cursor cursor) throws JSONException {
        this.variant = (Variant) CupboardFactory.cupboard().withCursor(cursor).get(Variant.class);

        //берем данные пользователя для отображения
        if (userData != null)
            for (int i = 0; i < userData.length; i++) {
                if (userData[i].getId() == this.variant.getId())
                    this.variant = userData[i];
            }

        mapVariants.put(this.variant.getId(), this.variant);

        holder.textVariant.setText(variant.getName());

        if (variant.getSnipclas() == null) {
            holder.tvAddSnipToReport.setVisibility(View.INVISIBLE);
            holder.descriptiontextView.setVisibility(View.INVISIBLE);
        } else {
            if (!variant.getSnipclas().getDescription().equals("")) {
                holder.tvAddSnipToReport.setText("Добавить к отчету");//в базу устанавливать тег добаления комментария к отчету
                holder.descriptiontextView.setText(variant.getSnipclas().getDescription());
            } else {
                holder.tvAddSnipToReport.setVisibility(View.INVISIBLE);
                holder.descriptiontextView.setVisibility(View.INVISIBLE);
            }
        }

        String note = variant.getNote();
        if (!note.equals("")) {

            holder.tvNote.setText(variant.getNoteclas().getName());
            holder.notetextView.setText(variant.getNoteclas().getName());

            if ((!variant.getNoteclas().getArea().equals("")) || (!variant.getNoteclas().getCount().equals("")) || (!variant.getNoteclas().getDepth().equals("")) || (!variant.getNoteclas().getLength().equals("")) || (!variant.getNoteclas().getWidth().equals(""))) {
                String defectSize = variant.getNoteclas().getDepth() + variant.getNoteclas().getArea() + variant.getNoteclas().getLength() + variant.getNoteclas().getWidth() + variant.getNoteclas().getCount();
                holder.defectDetails.setText(defectSize);
            }

        } else {
            holder.notetextView.setText(R.string.note);
            holder.tvNote.setText("Добавить заметку");
        }

        holder.textCheckBox.setChecked(this.selectedIds.get(variant.getId()));
        holder.tvAddNoteToReport.setText("Добавить к отчету");//в базу устанавливать тег добаления заметки к отчету
        holder.textCheckBox.setChecked(this.selectedIds.get(variant.getId()));
    }

    public List<Variant> getSelected() {
        List<Variant> selectedVariants = new ArrayList<>();
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            if (this.selectedIds.get((int) getItemId(i))) {
                Variant selectedVar = (Variant) CupboardFactory.cupboard().withCursor(getItem(i)).get(Variant.class);
                Variant var = mapVariants.get(selectedVar.getId());
                selectedVariants.add(var);
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
        public final TextView notetextView;//кнопка добавить комментарий
        public final TextView tvNote;//поле добавить комментарий
        public final TextView tvAddSnipToReport;
        public final TextView tvAddNoteToReport;
        public final RelativeLayout tvnoteContainer;
        public final ImageView addDefectSize;

        // public final TextView descriptionTextLeftline;
        public final ExpandableLayout expandableLayout;

        //здесь заполняютя строки нужно сделать это выпадающим списком
        public VariantViewHolder(View itemView) {
            super(itemView);

            this.textCheckBox = itemView.findViewById(R.id.cbVariant);
            this.textVariant = itemView.findViewById(R.id.cbVariantText);
            this.descriptiontextView = itemView.findViewById(R.id.tvDesc);
            this.descriptionToggle = itemView.findViewById(R.id.descriptionToggle);
            this.tvnoteContainer = itemView.findViewById(R.id.tvNoteContainer);
            this.notetextView = itemView.findViewById(R.id.tvAddNote);//кнопка добавить комментарий
            this.tvNote = itemView.findViewById(R.id.tvNote);//поле добавить комментарий
            this.defectDetails = itemView.findViewById(R.id.tvAddSizes);//кнопка добавить размеры
            this.tvAddSnipToReport = itemView.findViewById(R.id.tvAddSnipToReport);
            this.tvAddNoteToReport = itemView.findViewById(R.id.tvAddNoteToReport);
            this.addDefectSize = itemView.findViewById(R.id.bDefectSize);
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
                    int itemVariantid = (int) getItemId();
                    Variant variant = mapVariants.get(itemVariantid);
                    View view2 = LayoutInflater.from(v.getContext()).inflate(R.layout.photo_comment, (ViewGroup) null);
                    final EditText etText = view2.findViewById(R.id.etText);
                    if (!variant.getNote().equals("")) {
                        try {
                            etText.setText(variant.getNoteclas().getName());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Dialogs.showCustomView(view2.getContext(), R.string.noteComment, view2, R.string.apply, R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int which) {
                            if (which == -1) {
                                Note noteclass = new Note();
                                noteclass.setDepth("");
                                noteclass.setLength("");
                                noteclass.setArea("");
                                noteclass.setWidth("");
                                noteclass.setCount("");
                                noteclass.setsDepth("");
                                noteclass.setQuality("");
                                noteclass.setsArea("");
                                noteclass.setsCount("");
                                noteclass.setsLength("");
                                noteclass.setsWidth("");

                                if (!variant.getNote().equals("")) {
                                    Note notes = null;
                                    try {
                                        notes = variant.getNoteclas();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String area = notes.getArea();
                                    try {
                                        if ((!variant.getNoteclas().getArea().equals("")) || (!variant.getNoteclas().getCount().equals("")) || (!variant.getNoteclas().getDepth().equals("")) || (!variant.getNoteclas().getLength().equals("")) || (!variant.getNoteclas().getWidth().equals(""))) {
                                            noteclass.setDepth(variant.getNoteclas().getDepth());
                                            noteclass.setLength(variant.getNoteclas().getLength());
                                            noteclass.setArea(variant.getNoteclas().getArea());
                                            noteclass.setWidth(variant.getNoteclas().getWidth());
                                            noteclass.setCount(variant.getNoteclas().getCount());
                                            noteclass.setsDepth(variant.getNoteclas().getsDepth());
                                            noteclass.setsWidth(variant.getNoteclas().getsDepth());
                                            noteclass.setsLength(variant.getNoteclas().getsLength());
                                            noteclass.setsArea(variant.getNoteclas().getsArea());
                                            noteclass.setsWidth(variant.getNoteclas().getsWidth());
                                            noteclass.setsCount(variant.getNoteclas().getsCount());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                noteclass.setName(etText.getText().toString());
                                variant.setNote(new Gson().toJson(noteclass));
                                mapVariants.remove(itemVariantid);
                                mapVariants.put(itemVariantid, variant);
                                notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case R.id.bDefectSize:
                    int itemId = (int) getItemId();
                    Variant variantForView = mapVariants.get(itemId);
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.fragment_defect_size, (ViewGroup) null);

                    final EditText etDepth = view.findViewById(R.id.etDepth);
                    final EditText etLength = view.findViewById(R.id.etLength);
                    final EditText etArea = view.findViewById(R.id.etArea);
                    final EditText etWidth = view.findViewById(R.id.etWidth);
                    final EditText etCount = view.findViewById(R.id.etCount);
                    final AppCompatSpinner sDepth = view.findViewById(R.id.sDepth);
                    final AppCompatSpinner sLength = view.findViewById(R.id.sLength);
                    final AppCompatSpinner sArea = view.findViewById(R.id.sArea);
                    final AppCompatSpinner sWidth = view.findViewById(R.id.sWidth);
                    final AppCompatSpinner sCount = view.findViewById(R.id.sCount);
                    try {
                        if (variantForView.getNoteclas() != null) {
                            etDepth.setText(variantForView.getNoteclas().getDepth());
                            etLength.setText(variantForView.getNoteclas().getLength());
                            etArea.setText(variantForView.getNoteclas().getArea());
                            etWidth.setText(variantForView.getNoteclas().getWidth());
                            etCount.setText(variantForView.getNoteclas().getCount());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Dialogs.showCustomView(view.getContext(), R.string.mark_comment, view, R.string.apply, R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int which) {
                            if (which == -1) {

                                Note noteclass = new Note();
                                noteclass.setName("");
                                noteclass.setsDepth("");
                                noteclass.setQuality("");
                                noteclass.setsArea("");
                                noteclass.setsCount("");
                                noteclass.setsLength("");
                                noteclass.setsWidth("");
                                if (!variantForView.getNote().equals("")) {
                                    try {
                                        noteclass.setName(variantForView.getNoteclas().getName());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                noteclass.setDepth(etDepth.getText().toString());
                                noteclass.setLength(etLength.getText().toString());
                                noteclass.setArea(etArea.getText().toString());
                                noteclass.setWidth(etWidth.getText().toString());
                                noteclass.setCount(etCount.getText().toString());


                                variantForView.setNote(new Gson().toJson(noteclass));
                                mapVariants.remove(itemId);
                                mapVariants.put(itemId, variantForView);
                                notifyDataSetChanged();

                            }
                        }
                    });
                    break;
                default:
                    break;
            }

        }
    }
}