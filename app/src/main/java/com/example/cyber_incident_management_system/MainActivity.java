package com.example.cyber_incident_management_system;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper databaseHelper;

    ArrayList<Incident> incidentList = new ArrayList<>();
    ArrayList<Incident> filteredList = new ArrayList<>();

    IncidentAdapter adapter;

    RecyclerView rvIncidents;

    EditText etSearch;

    Spinner spinnerSeverityFilter, spinnerStatusFilter;

    TextView tvTotal, tvOpen, tvInvestigating, tvResolved, tvEmpty;

    Button btnAddIncident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int p16 = (int) (16 * getResources().getDisplayMetrics().density);
            v.setPadding(
                    systemBars.left + p16,
                    systemBars.top + p16,
                    systemBars.right + p16,
                    systemBars.bottom + p16
            );
            return insets;
        });

        databaseHelper = new DBHelper(this);

        rvIncidents = findViewById(R.id.rvIncidents);

        etSearch = findViewById(R.id.etSearch);

        spinnerSeverityFilter = findViewById(R.id.spinnerSeverityFilter);
        spinnerStatusFilter = findViewById(R.id.spinnerStatusFilter);

        tvTotal = findViewById(R.id.tvTotal);
        tvOpen = findViewById(R.id.tvOpen);
        tvInvestigating = findViewById(R.id.tvInvestigating);
        tvResolved = findViewById(R.id.tvResolved);
        tvEmpty = findViewById(R.id.tvEmpty);

        btnAddIncident = findViewById(R.id.btnAddIncident);

        rvIncidents.setLayoutManager(new LinearLayoutManager(this));

        adapter = new IncidentAdapter(filteredList, this);

        rvIncidents.setAdapter(adapter);

        btnAddIncident.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddIncidentActivity.class);

            startActivity(intent);
        });

        setupFilters();

        loadIncidents();
    }

    private void loadIncidents() {

        incidentList.clear();

        Cursor cursor = databaseHelper.getAllIncidents();

        while (cursor.moveToNext()) {

            Incident incident = new Incident(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );

            incidentList.add(incident);
        }

        cursor.close();

        updateStatistics();

        applyFilters();
    }

    public void editIncident(int id) {

        Intent intent = new Intent(
                MainActivity.this,
                AddIncidentActivity.class);

        intent.putExtra("incident_id", id);

        startActivity(intent);
    }

    public void deleteIncident(int id) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Incident")
                .setMessage("Are you sure you want to delete this incident?")

                .setPositiveButton("Yes", (dialog, which) -> {

                    boolean result = databaseHelper.deleteIncident(id);

                    if (result) {

                        Toast.makeText(this, "Incident deleted",
                                Toast.LENGTH_SHORT).show();

                        loadIncidents();
                    }
                })

                .setNegativeButton("No", null)

                .show();
    }

    private void setupFilters() {

        String[] severities = {
                "All Severities", "Low", "Medium", "High", "Critical"
        };

        String[] statuses = {
                "All Statuses", "Open", "Investigating", "Resolved"
        };

        ArrayAdapter<String> severityAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, severities);

        severityAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerSeverityFilter.setAdapter(severityAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statuses);

        statusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerStatusFilter.setAdapter(statusAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        AdapterView.OnItemSelectedListener listener =
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        applyFilters();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };

        spinnerSeverityFilter.setOnItemSelectedListener(listener);
        spinnerStatusFilter.setOnItemSelectedListener(listener);
    }

    private void applyFilters() {

        String search = etSearch.getText().toString().toLowerCase();

        String severity = spinnerSeverityFilter.getSelectedItem().toString();

        String status = spinnerStatusFilter.getSelectedItem().toString();

        filteredList.clear();

        for (Incident incident : incidentList) {

            boolean searchMatch =
                    incident.getTitle().toLowerCase().contains(search)
                            || incident.getDescription().toLowerCase().contains(search)
                            || incident.getIncidentType().toLowerCase().contains(search)
                            || incident.getAffectedAsset().toLowerCase().contains(search);

            boolean severityMatch =
                    severity.equals("All Severities")
                            || incident.getSeverity().equals(severity);

            boolean statusMatch =
                    status.equals("All Statuses")
                            || incident.getStatus().equals(status);

            if (searchMatch && severityMatch && statusMatch) {
                filteredList.add(incident);
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void updateStatistics() {

        int open = 0;
        int investigating = 0;
        int resolved = 0;

        for (Incident incident : incidentList) {

            if (incident.getStatus().equalsIgnoreCase("Open")) {
                open++;
            } else if (incident.getStatus().equalsIgnoreCase("Investigating")) {
                investigating++;
            } else if (incident.getStatus().equalsIgnoreCase("Resolved")) {
                resolved++;
            }
        }

        tvTotal.setText(String.valueOf(incidentList.size()));
        tvOpen.setText(String.valueOf(open));
        tvInvestigating.setText(String.valueOf(investigating));
        tvResolved.setText(String.valueOf(resolved));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && adapter != null) {
            loadIncidents();
        }
    }
}
