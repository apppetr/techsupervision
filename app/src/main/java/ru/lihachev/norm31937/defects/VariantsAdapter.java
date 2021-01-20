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

        if (!variant.getNote().equals("")) {
            if (variant.getNoteclas().getQuality().equals("0")) {
                holder.tvAddSnipToReport.setText("Добавить в отчет");
            }

            if (variant.getNoteclas().getQuality().equals("")) {
                holder.tvAddSnipToReport.setText("Добавить в отчет");
            }

            if (variant.getNoteclas().getQuality().equals("1")) {
                holder.tvAddSnipToReport.setText("Убрать из отчета");
            }
        }

        String note = variant.getNote();
        if (!note.equals("")) {

            holder.tvNote.setText(variant.getNoteclas().getName());
            holder.notetextView.setText(variant.getNoteclas().getName());

            if ((!variant.getNoteclas().getArea().equals("")) || (!variant.getNoteclas().getCount().equals("")) || (!variant.getNoteclas().getDepth().equals("")) || (!variant.getNoteclas().getLength().equals("")) || (!variant.getNoteclas().getWidth().equals(""))) {
                String defectSize = "Гл. " + variant.getNoteclas().getDepth() + " "  + variant.getNoteclas().getsDepth() + " Пл. " + variant.getNoteclas().getArea() + " " + variant.getNoteclas().getsArea() + " Дл. " +  variant.getNoteclas().getLength() + " " + variant.getNoteclas().getsLength()  + " Ш. " + variant.getNoteclas().getWidth() + " " + variant.getNoteclas().getsWidth() + " Кол-во " + " " + variant.getNoteclas().getCount();
                holder.defectDetails.setText(defectSize);
            }

        } else {
            holder.notetextView.setText(R.string.note);
            holder.tvNote.setText("Добавить заметку");
        }

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
        public final RelativeLayout defectDetailsContainer;
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
            this.defectDetailsContainer = itemView.findViewById(R.id.defectDetailsContainer);
            this.notetextView = itemView.findViewById(R.id.tvAddNote);//кнопка добавить комментарий
            this.tvNote = itemView.findViewById(R.id.tvNote);//поле добавить комментарий
            this.defectDetails = itemView.findViewById(R.id.tvAddSizes);//кнопка добавить размеры
            this.tvAddSnipToReport = itemView.findViewById(R.id.tvAddSnipToReport);//Текст добавить в отчет
            this.tvAddNoteToReport = itemView.findViewById(R.id.tvAddNoteToReport);//Текст добавить в отчет
            this.addDefectSize = itemView.findViewById(R.id.bDefectSize);
            this.textCheckBox.setOnClickListener(this);
            this.descriptionToggle.setOnClickListener(this);
            this.tvAddSnipToReport.setOnClickListener(this);//Текст добавить в отчет
            this.tvAddNoteToReport.setOnClickListener(this);//Текст добавить в отчет
            this.addDefectSize.setOnClickListener(this);//кнопка редактировать размеры
            this.tvnoteContainer.setOnClickListener(this);//редактировать комментарий
            this.defectDetailsContainer.setOnClickListener(this);//редактировать размеры
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
                case R.id.tvAddSnipToReport:
                    int itemAddSnip = (int) getItemId();
                    Variant variantSnip = mapVariants.get(itemAddSnip);
                    Note noteclassSnip = new Note();

                    if (!variantSnip.getNote().equals("")) {
                        try {
                            noteclassSnip.setName(variantSnip.getNoteclas().getName());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        noteclassSnip.setName(variantSnip.getNoteclas().getName());
                        noteclassSnip.setLength(variantSnip.getNoteclas().getLength());
                        noteclassSnip.setArea(variantSnip.getNoteclas().getArea());
                        noteclassSnip.setWidth(variantSnip.getNoteclas().getWidth());
                        noteclassSnip.setCount(variantSnip.getNoteclas().getCount());
                        noteclassSnip.setDepth(variantSnip.getNoteclas().getDepth());
                        noteclassSnip.setsDepth(variantSnip.getNoteclas().getsDepth());
                        noteclassSnip.setsArea(variantSnip.getNoteclas().getsArea());
                        noteclassSnip.setsCount(variantSnip.getNoteclas().getsCount());
                        noteclassSnip.setsLength(variantSnip.getNoteclas().getsLength());
                        noteclassSnip.setsWidth(variantSnip.getNoteclas().getsWidth());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!variantSnip.getNote().equals("")) {
                        try {
                            if (variantSnip.getNoteclas().getQuality().equals("0")) {
                                noteclassSnip.setQuality("1");
                            }

                            if (variantSnip.getNoteclas().getQuality().equals("")) {
                                noteclassSnip.setQuality("1");
                            }

                            if (variantSnip.getNoteclas().getQuality().equals("1")) {
                                noteclassSnip.setQuality("0");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    variantSnip.setNote(new Gson().toJson(noteclassSnip));
                    mapVariants.remove(itemAddSnip);
                    mapVariants.put(itemAddSnip, variantSnip);
                    notifyDataSetChanged();

                    break;

                case R.id.tvAddNoteToReport:
                    if (!variant.getNote().equals("")) {
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
                    int itemNoteid = (int) getItemId();
                    Variant variant = mapVariants.get(itemNoteid);
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

                                if (!variant.getNote().equals("")) {
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
                                mapVariants.remove(itemNoteid);
                                mapVariants.put(itemNoteid, variant);
                                notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case R.id.defectDetailsContainer:
                case R.id.bDefectSize:
                    int itemDefectId = (int) getItemId();
                    Variant variantForView = mapVariants.get(itemDefectId);
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

                                noteclass.setsDepth((String) sDepth.getSelectedItem());
                                noteclass.setsArea((String) sArea.getSelectedItem());
                                noteclass.setsCount((String) sCount.getSelectedItem());
                                noteclass.setsLength((String) sLength.getSelectedItem());
                                noteclass.setsWidth((String) sWidth.getSelectedItem());

                                variantForView.setNote(new Gson().toJson(noteclass));
                                mapVariants.remove(itemDefectId);
                                mapVariants.put(itemDefectId, variantForView);
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