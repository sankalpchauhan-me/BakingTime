package me.sankalpchauhan.bakingtime.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    private final RecipeAdapterOnClickHandler mClickHandler;
    private List<Recipe> recipeList = new ArrayList<>();

    public RecipeAdapter(RecipeAdapterOnClickHandler recipeAdapterOnClickHandler) {
        mClickHandler = recipeAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        if (recipeList.get(position).getImage() != null && !recipeList.get(position).getImage().isEmpty()) {
            Picasso.get().load(recipeList.get(position).getImage()).error(R.drawable.ic_broken_image_grey_24dp).into(holder.mRecipeImage, new Callback() {
                @Override
                public void onSuccess() {
                    Timber.d("Image load success from url %s", recipeList.get(position).getImage());
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }

            });
        }
        if (!recipeList.get(position).getName().isEmpty()) {
            holder.mRecipeName.setText(recipeList.get(position).getName());
        } else {
            holder.mRecipeName.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) {
            return 0;
        }
        return recipeList.size();
    }

    public void setRecipeData(List<Recipe> recipeData) {
        recipeList = recipeData;
        notifyDataSetChanged();
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_name)
        TextView mRecipeName;
        @BindView(R.id.recipe_image)
        ImageView mRecipeImage;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(recipeList.get(adapterPosition));
        }
    }
}
