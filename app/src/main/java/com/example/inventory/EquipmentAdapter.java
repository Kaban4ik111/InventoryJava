package com.example.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {
    private List<Equipment> equipmentList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Equipment equipment);
    }

    public EquipmentAdapter(List<Equipment> equipmentList, OnItemClickListener listener) {
        this.equipmentList = equipmentList;
        this.onItemClickListener = listener;
    }

    class EquipmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvEquipmentName, tvEquipmentCondition, tvCabinetNumber;

        EquipmentViewHolder(View itemView) {
            super(itemView);
            tvEquipmentName = itemView.findViewById(R.id.tvEquipmentName);
            tvEquipmentCondition = itemView.findViewById(R.id.tvEquipmentCondition);
            tvCabinetNumber = itemView.findViewById(R.id.tvCabinetNumber);
        }
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder holder, int position) {
        Equipment equipment = equipmentList.get(position);

        holder.tvEquipmentName.setText(equipment.name);
        holder.tvEquipmentCondition.setText("Состояние: " + equipment.condition);
        holder.tvCabinetNumber.setText("Каб. " + equipment.cabinet);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(equipment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    public void updateList(List<Equipment> newList) {
        equipmentList = newList;
        notifyDataSetChanged();
    }
}