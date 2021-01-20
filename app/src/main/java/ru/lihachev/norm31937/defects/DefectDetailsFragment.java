package ru.lihachev.norm31937.defects;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.velocity.util.introspection.VelPropertySet;

import java.util.Arrays;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.ProviderCompartment;
import ru.lihachev.norm31937.GreatApplication;
import ru.lihachev.norm31937.Helper;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.defects.place.PlaceDialogFragment;
import ru.lihachev.norm31937.defects.place.object.Place;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.MaterialVariant;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.utils.Formats;
import ru.lihachev.norm31937.utils.SelectListener;
import ru.lihachev.norm31937.utils.alerts.Dialogs;
import ru.lihachev.norm31937.utils.alerts.SelectDialogs;
import ru.lihachev.norm31937.values.ValuesProvider;

public class DefectDetailsFragment extends Fragment implements ElementController.ElementListener, PlaceDialogFragment.PlaceSelectListener {
    private Spinner category;
    public Defect defect;
    public ElementController elementController;
    private TextView tvCompensation;
    private TextView tvCoord;
    private TextView tvDefect;
    private TextView tvElemMat;
    private TextView tvReason;
    public TextView tvVolume;

    public static DefectDetailsFragment getInstance(int defectId) {
        DefectDetailsFragment fragment = new DefectDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(AddDefectActivity.DEFECT_ID, defectId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        int defectId;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.defect = new Defect();
        Bundle args = getArguments();
        if (args != null && (defectId = args.getInt(AddDefectActivity.DEFECT_ID, -1)) != -1) {
            this.defect = (Defect) CupboardFactory.cupboard().withContext(getActivity()).get(ContentUris.withAppendedId(UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL), (long) defectId), Defect.class);
        }
    }

    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_defect_details, container, false);
    }

    private void initViews(View view) {
        this.tvElemMat = (TextView) view.findViewById(R.id.tvElemMat);
        this.tvCoord = (TextView) view.findViewById(R.id.tvCoord);
        this.category = (Spinner) view.findViewById(R.id.sCategory);
        this.tvDefect = (TextView) view.findViewById(R.id.tvDefect);
        this.tvReason = (TextView) view.findViewById(R.id.tvReasons);
        this.tvCompensation = (TextView) view.findViewById(R.id.tvCompensations);
        this.tvVolume = (TextView) view.findViewById(R.id.tvVolume);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews(view);
        this.elementController = new ElementController(getActivity());
        View llElemMat = view.findViewById(R.id.llElemMat);
        llElemMat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DefectDetailsFragment.this.elementController.chooseElement();
            }
        });
        this.elementController.addListener(this);
        view.findViewById(R.id.llCoords).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DefectDetailsFragment.this.showCoordinatesChooser();
            }
        });
        this.category.setAdapter(createAdapter(R.string.danger_category, getResources().getStringArray(R.array.danger_categories)));
        this.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    DefectDetailsFragment.this.defect.setCategory(((char) ((position + 1040) - 1)) + "");
                    DefectDetailsFragment.this.saveDefect();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                DefectDetailsFragment.this.defect.setCategory("");
            }
        });
        View llDefect = view.findViewById(R.id.llDefect);
        llDefect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DefectDetailsFragment.this.defect.getElement() == null) {
                    Toast.makeText(GreatApplication.getAppContext(), R.string.choose_element, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(v.getContext(), SelectVariantsActivity.class);
                intent.putExtra(SelectVariantsActivity.ROOTS, new int[]{DefectDetailsFragment.this.defect.getMaterial().getMatElemId().intValue()});
                intent.putExtra(SelectVariantsActivity.ROOTS_STRING, DefectDetailsFragment.this.defect.getElement() + " " + DefectDetailsFragment.this.defect.getMaterial());

                int[] properties = Formats.extractIds(DefectDetailsFragment.this.defect.problems);
               intent.putExtra(SelectVariantsActivity.SELECTED_VALUES, properties);
               if (properties.length > 0) {
                   List<Variant> selectedVariants = Arrays.asList(DefectDetailsFragment.this.defect.problems);
                   Parcelable[] values = selectedVariants.toArray(new Parcelable[selectedVariants.size()]);
                   intent.putExtra(SelectVariantsActivity.VALUES_INFO, values);
               }

                intent.putExtra(SelectVariantsActivity.URI, "defects");
                DefectDetailsFragment.this.startActivityForResult(intent, 0);
            }
        });
        View llReason = view.findViewById(R.id.llReason);
        llReason.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DefectDetailsFragment.this.defect.problems == null || DefectDetailsFragment.this.defect.problems.length == 0) {
                    Toast.makeText(GreatApplication.getAppContext(), R.string.choose_problem, Toast.LENGTH_SHORT).show();
                    return;
                }

                int[] problems = Formats.extractIds(DefectDetailsFragment.this.defect.problems);
                String niceProblems = DefectDetailsFragment.this.defect.getNiceProblems();
                int[] reasons = Formats.extractIds(DefectDetailsFragment.this.defect.reasons);
                Intent intent = new Intent(v.getContext(), SelectVariantsActivity.class);
                intent.putExtra(SelectVariantsActivity.ROOTS, problems);
                intent.putExtra(SelectVariantsActivity.ROOTS_STRING, niceProblems);

                intent.putExtra(SelectVariantsActivity.SELECTED_VALUES, reasons);
                if (reasons.length > 0) {
                    List<Variant> selectedVariants = Arrays.asList(DefectDetailsFragment.this.defect.reasons);
                    Parcelable[] values = selectedVariants.toArray(new Parcelable[selectedVariants.size()]);
                    intent.putExtra(SelectVariantsActivity.VALUES_INFO, values);
                }
                intent.putExtra(SelectVariantsActivity.URI, "reasons");
                DefectDetailsFragment.this.startActivityForResult(intent, 1);
            }
        });
        View llCompensations = view.findViewById(R.id.llCompensations);
        llCompensations.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DefectDetailsFragment.this.defect.reasons == null || DefectDetailsFragment.this.defect.reasons.length == 0) {
                    Toast.makeText(GreatApplication.getAppContext(), R.string.choose_reasons, Toast.LENGTH_SHORT).show();
                    return;
                }

                int[] problems = Formats.extractIds(DefectDetailsFragment.this.defect.problems);//запрос к базе ID выбранных дефектов, для передачи в select
                String niceProblems = DefectDetailsFragment.this.defect.getNiceProblems();
                int[] Compensations = Formats.extractIds(DefectDetailsFragment.this.defect.compensations);//запрос к базе ID выбранных компенсаций, для отметки галочками
                Intent intent = new Intent(v.getContext(), SelectVariantsActivity.class);
                intent.putExtra(SelectVariantsActivity.ROOTS, problems);
                intent.putExtra(SelectVariantsActivity.ROOTS_STRING, niceProblems);

                intent.putExtra(SelectVariantsActivity.SELECTED_VALUES, Compensations);
                if (Compensations.length > 0) {
                    List<Variant> selectedVariants = Arrays.asList(DefectDetailsFragment.this.defect.compensations);
                    Parcelable[] values = selectedVariants.toArray(new Parcelable[selectedVariants.size()]);
                    intent.putExtra(SelectVariantsActivity.VALUES_INFO, values);
                }
                intent.putExtra(SelectVariantsActivity.URI, "compensations");
                DefectDetailsFragment.this.startActivityForResult(intent, 2);
            }
        });
        if (!Helper.isFree()) {
            initLongClickListeners(llElemMat, llDefect, llReason, llCompensations);
        }
        view.findViewById(R.id.llVolume).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectDialogs.showMeasumentDialog(DefectDetailsFragment.this.getActivity(), new SelectListener() {
                    @Override
                    public void onSelected(Object selection) {
                        if (selection != null) {
                            DefectDetailsFragment.this.defect.setVolume((String) selection);
                            //закоментил из за несоответсвия типов
                            //  DefectDetailsFragment.this.tvVolume.setText((Integer) selection);
                            DefectDetailsFragment.this.saveDefect();
                        }
                    }
                }).show();

            }
        });
        fillViews(this.defect);
    }

    private void initLongClickListeners(View llElemMat, View llDefect, View llReason, View llCompensations) {
        llElemMat.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (DefectDetailsFragment.this.defect.getElement() == null || DefectDetailsFragment.this.defect.getMaterial() == null) {
                    return false;
                }
                return DefectDetailsFragment.this.showDeleteElementDialog();
            }
        });
        llDefect.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return DefectDetailsFragment.this.showDeleteProblemsDialog();
            }
        });
        llReason.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return DefectDetailsFragment.this.showDeleteReasonDialog();
            }
        });
        llCompensations.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return DefectDetailsFragment.this.showDeleteCompensationDialog();
            }
        });
    }

    public void fillViews(Defect defect2) {
        if (defect2.getElement() != null) {
            this.tvElemMat.setText(getString(R.string.format_document_list, defect2.getElement(), defect2.getMaterial()));
        } else {
            this.tvElemMat.setText("");
        }
        if (defect2.place != null) {
            this.tvCoord.setText(defect2.place.toString());
        }
        if (!TextUtils.isEmpty(defect2.getCategory())) {
            this.category.setSelection((defect2.getCategory().charAt(0) - 1040) + 1);
        }
        this.tvDefect.setText(defect2.getNiceProblems());
        this.tvReason.setText(defect2.getNiceReasons());
        this.tvCompensation.setText(defect2.getNiceCompensations());
        this.tvVolume.setText(defect2.getVolume());
    }

    public void elementChanged(Variant element, MaterialVariant material) {
        this.tvElemMat.setText(getString(R.string.format_document_list, element, material));
        this.tvDefect.setText("");
        this.tvReason.setText("");
        this.tvCompensation.setText("");
        this.defect.setElement(element);
        this.defect.setMaterial(material);
        this.defect.problems = null;
        this.defect.reasons = null;
        this.defect.compensations = null;
        saveDefect();
    }

    public SpinnerAdapter get(String label, List<Variant> data) {
        String[] adapter = new String[((label == null ? 0 : 1) + data.size())];
        for (int i = 0; i < data.size(); i++) {
            adapter[i + 1] = data.get(i).getName();
        }
        adapter[0] = label;
        return new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, adapter) {
            public boolean areAllItemsEnabled() {
                return false;
            }

            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            Parcelable[] values = intent.getParcelableArrayExtra(SelectVariantsActivity.SELECTED_VALUES);
            switch (requestCode) {
                case 0:
                    this.defect.problems = Formats.migrateArray(values);
                    this.defect.reasons = null;
                    this.defect.compensations = null;
                    this.tvDefect.setText(Formats.formatArray((Object[]) values));
                    this.tvReason.setText("");
                    this.tvCompensation.setText("");
                    break;
                case 1:
                    this.defect.reasons = Formats.migrateArray(values);
                    this.defect.compensations = null;
                    this.tvReason.setText(Formats.formatArray((Object[]) values));
                    this.tvCompensation.setText("");
                    break;
                case 2:
                    this.defect.compensations = Formats.migrateArray(values);
                    this.tvCompensation.setText(Formats.formatArray((Object[]) values));
                    break;
            }
            saveDefect();
        }
    }

    public SpinnerAdapter createAdapter(@StringRes int labelId, @NonNull String[] values) {
        String label = getString(labelId);
        String[] valuesWithLabel = new String[(values.length + 1)];
        valuesWithLabel[0] = label;
        for (int i = 0; i < values.length; i++) {
            valuesWithLabel[i + 1] = values[i];
        }
        return new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, valuesWithLabel) {
            public boolean areAllItemsEnabled() {
                return false;
            }

            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
    }

    public void showCoordinatesChooser() {
        PlaceDialogFragment dlgFrgmt = PlaceDialogFragment.newInstance(this.defect.place);
        dlgFrgmt.setListener(this);
        dlgFrgmt.show(getChildFragmentManager(), "Place");
    }

    public void onPlaceSelected(Place place) {
        this.defect.place = place;
        saveDefect();
        this.tvCoord.setText(place.toString());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export /*2131558603*/:
                saveDefect();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveDefect() {
        this.defect.updated = System.currentTimeMillis();
        CupboardFactory.cupboard().withContext(getActivity()).update(UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL), CupboardFactory.cupboard().withEntity(Defect.class).toContentValues(this.defect));
    }

    public boolean showDeleteElementDialog() {
        Dialogs.show(getActivity(), R.string.delete_element, R.string.delete_element_message, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == -1) {
                    DefectDetailsFragment.this.deleteElement(DefectDetailsFragment.this.defect.getElement(), DefectDetailsFragment.this.defect.getMaterial());
                    DefectDetailsFragment.this.defect.setElement((Variant) null);
                    DefectDetailsFragment.this.defect.setMaterial((MaterialVariant) null);
                    DefectDetailsFragment.this.defect.compensations = null;
                    DefectDetailsFragment.this.defect.problems = null;
                    DefectDetailsFragment.this.defect.reasons = null;
                    DefectDetailsFragment.this.saveDefect();
                    DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
                }
            }
        });
        return true;
    }

    public void deleteElement(Variant element, MaterialVariant material) {
        ProviderCompartment cp = CupboardFactory.cupboard().withContext(getActivity());
        cp.delete(ValuesProvider.uri("defects2elements"), "mat_elem_id=?", String.valueOf(material.getMatElemId()));
        cp.delete(ValuesProvider.uri("elements2materials"), "mat_elem_id=?", String.valueOf(material.getMatElemId()));
        cp.delete(ValuesProvider.uri("elements"), "_id=?", String.valueOf(element.getId()));
        cp.delete(ValuesProvider.uri("materials"), "_id=?", String.valueOf(material.getId()));
    }

    public boolean showDeleteCompensationDialog() {
        Dialogs.show(getActivity(), R.string.delete_compensation, R.string.delete_compensation_message, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == -1) {
                    DefectDetailsFragment.this.deleteCompensations(DefectDetailsFragment.this.defect.compensations);
                    DefectDetailsFragment.this.defect.compensations = null;
                    DefectDetailsFragment.this.saveDefect();
                    DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
                }
            }
        });
        return true;
    }

    public void deleteCompensations(Variant... compensations) {
        String ids = Formats.formatArray(Formats.extractIds(compensations));
        ProviderCompartment cp = CupboardFactory.cupboard().withContext(getActivity());
        cp.delete(ValuesProvider.uri("reasons2compensations"), "compensation_id in (" + ids + ")", (String[]) null);
        cp.delete(ValuesProvider.uri("compensations"), "_id in (" + ids + ")", (String[]) null);
    }

    public void deleteReasons(Variant[] reasons) {
        String ids = Formats.formatArray(Formats.extractIds(reasons));
        ProviderCompartment cp = CupboardFactory.cupboard().withContext(getActivity());
        cp.delete(ValuesProvider.uri("defects2reasons"), "reason_id in (" + ids + ")", (String[]) null);
        cp.delete(ValuesProvider.uri("reasons2compensations"), "reason_id in (" + ids + ")", (String[]) null);
        cp.delete(ValuesProvider.uri("reasons"), "_id in (" + ids + ")", (String[]) null);
    }

    public void deleteProblems(Variant[] problems) {
        String ids = Formats.formatArray(Formats.extractIds(problems));
        ProviderCompartment cp = CupboardFactory.cupboard().withContext(getActivity());
        cp.delete(ValuesProvider.uri("defects2reasons"), "defect_id in (" + ids + ")", (String[]) null);
        cp.delete(ValuesProvider.uri("defects2elements"), "defect_id in (" + ids + ")", (String[]) null);
        cp.delete(ValuesProvider.uri("defects"), "_id in (" + ids + ")", (String[]) null);
    }

    public boolean showDeleteReasonDialog() {
        Dialogs.show(getActivity(), R.string.delete_reason, R.string.delete_reason_message, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == -1) {
                    DefectDetailsFragment.this.deleteReasons(DefectDetailsFragment.this.defect.reasons);
                    DefectDetailsFragment.this.defect.reasons = null;
                    DefectDetailsFragment.this.defect.compensations = null;
                    DefectDetailsFragment.this.saveDefect();
                    DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
                }
            }
        });
        return true;
    }

    public boolean showDeleteProblemsDialog() {
        Dialogs.show(getActivity(), R.string.delete_problem, R.string.delete_problem_message, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == -1) {
                    DefectDetailsFragment.this.deleteProblems(DefectDetailsFragment.this.defect.problems);
                    DefectDetailsFragment.this.defect.problems = null;
                    DefectDetailsFragment.this.defect.reasons = null;
                    DefectDetailsFragment.this.defect.compensations = null;
                    DefectDetailsFragment.this.saveDefect();
                    DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
                }
            }
        });
        return true;
    }
}
