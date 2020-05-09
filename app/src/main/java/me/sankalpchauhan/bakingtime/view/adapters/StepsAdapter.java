package me.sankalpchauhan.bakingtime.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {
    private final StepAdapterOnClickHandler mClickHandler;
    public int selected_position = -1;
    private List<Step> stepList = new ArrayList<>();

    public StepsAdapter(StepAdapterOnClickHandler stepAdapterOnClickHandler) {
        mClickHandler = stepAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.step_item, parent, false);
        return new StepHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        holder.itemView.setBackgroundColor(selected_position == position ? Color.LTGRAY : Color.TRANSPARENT);
        if (stepList.get(position).getShortDescription() != null) {
            holder.mStep.setText(String.format("Step %d: %s", position, stepList.get(position).getShortDescription()));
        } else {
            holder.mStep.setText("N/A");
        }
    }

    public void setStepsData(List<Step> stepData) {
        stepList = stepData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (stepList == null) {
            return 0;
        }
        return stepList.size();
    }

    public interface StepAdapterOnClickHandler {
        void onClick(Step step, int position);
    }

    public class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step)
        TextView mStep;

        public StepHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            mClickHandler.onClick(stepList.get(adapterPosition), adapterPosition);
        }
    }
}
