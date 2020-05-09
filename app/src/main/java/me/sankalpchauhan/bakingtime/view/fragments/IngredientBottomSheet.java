package me.sankalpchauhan.bakingtime.view.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.IngredientAppWidget;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.config.DefaultPrefSettings;
import me.sankalpchauhan.bakingtime.service.model.Ingredient;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.view.adapters.IngredientsAdapter;

public class IngredientBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ingredient_rv)
    RecyclerView mIngredientsRv;
    @BindView(R.id.widget_subscription)
    Switch mWidgetSwitch;
    private Recipe mRecipe;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private IngredientsAdapter mIngredientAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ingredient_bottom_sheet, container, false);
        ButterKnife.bind(this, v);
//        if(getActivity() instanceof AppCompatActivity){
//            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        }
        setHasOptionsMenu(true);
        toolbar.setTitle("Ingredient List");
        if (getArguments() != null) {
            mRecipe = (Recipe) getArguments().getSerializable(Constants.RECIPE_DATA);
            ingredientList = mRecipe.getIngredients();
            toolbar.setTitle(mRecipe != null ? mRecipe.getName() + " Ingredient List" : "Ingredient List");
            if (DefaultPrefSettings.getInstance().getRecipeId().equals(mRecipe.getId())) {
                mWidgetSwitch.setChecked(true);
            }
        }
        setUpRecyclerView();
        mWidgetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    DefaultPrefSettings.getInstance().setIngredients(mRecipe);
                } else {
                    DefaultPrefSettings.getInstance().clearSharedPrefs();
                }
                ComponentName provider = new ComponentName(getContext(), IngredientAppWidget.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
                int[] ids = appWidgetManager.getAppWidgetIds(provider);
                IngredientAppWidget ingredientAppWidget = new IngredientAppWidget();
                ingredientAppWidget.onUpdate(getContext(), appWidgetManager, ids);
            }
        });
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setUpRecyclerView() {
        mIngredientsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientsRv.setHasFixedSize(true);
        mIngredientAdapter = new IngredientsAdapter();
        mIngredientsRv.setAdapter(mIngredientAdapter);
        mIngredientAdapter.setIngredientData(ingredientList);
    }


}
