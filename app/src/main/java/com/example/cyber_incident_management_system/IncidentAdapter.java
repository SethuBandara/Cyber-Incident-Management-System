package com.example.cyber_incident_management_system;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    ArrayList<Incident> incidentList;
    MainActivity mainActivity;

    public IncidentAdapter(ArrayList<Incident> incidentList,
                           MainActivity mainActivity) {

        this.incidentList = incidentList;
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incident, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Incident incident = incidentList.get(position);

        holder.tvIncidentTitle.setText(incident.getTitle());
        holder.tvIncidentType.setText("Type: " + incident.getIncidentType());
        holder.tvSeverity.setText("Severity: " + incident.getSeverity());
        holder.tvStatus.setText("Status: " + incident.getStatus());
        holder.tvIncidentDate.setText("Date: " + incident.getReportedDate());
        holder.tvAffectedAsset.setText("Asset: " + incident.getAffectedAsset());

        holder.btnEdit.setOnClickListener(v -> {
            mainActivity.editIncident(incident.getId());
        });

        holder.btnDelete.setOnClickListener(v -> {
            mainActivity.deleteIncident(incident.getId());
        });
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvIncidentTitle, tvIncidentType;
        TextView tvSeverity, tvStatus;
        TextView tvIncidentDate, tvAffectedAsset;

        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvIncidentTitle = itemView.findViewById(R.id.tvIncidentTitle);
            tvIncidentType = itemView.findViewById(R.id.tvIncidentType);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvIncidentDate = itemView.findViewById(R.id.tvIncidentDate);
            tvAffectedAsset = itemView.findViewById(R.id.tvAffectedAsset);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}