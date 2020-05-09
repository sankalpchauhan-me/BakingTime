package me.sankalpchauhan.bakingtime.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientHolder> {
    private List<Ingredient> ingredientList = new ArrayList<>();

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        // StringBuilder mQuantityMeasureHolder= new StringBuilder();
        if (ingredientList.get(position).getQuantity() != null && ingredientList.get(position).getMeasure() != null) {
            holder.mQuantityMeasure.setText(String.format("%s %s", ingredientList.get(position).getQuantity(), ingredientList.get(position).getMeasure()));
        }

        if (ingredientList.get(position).getIngredient() != null) {
            holder.mIngredient.setText(ingredientList.get(position).getIngredient());
        }

    }

    @Override
    public int getItemCount() {
        if (ingredientList == null) {
            return 0;
        }
        return ingredientList.size();
    }

    public void setIngredientData(List<Ingredient> ingredientData) {
        ingredientList = ingredientData;
        notifyDataSetChanged();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quantity_measure)
        TextView mQuantityMeasure;
        @BindView(R.id.ingredient)
        TextView mIngredient;

        public IngredientHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
