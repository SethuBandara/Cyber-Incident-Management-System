package com.example.cyber_incident_management_system;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddIncidentActivity extends AppCompatActivity {

    DBHelper databaseHelper;

    EditText etTitle, etDescription, etAffectedAsset;

    Spinner spinnerType, spinnerSeverity, spinnerStatus;

    TextView tvReportedDate;

    Button btnSaveIncident, btnCancel;

    int incidentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_incident);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DBHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etAffectedAsset = findViewById(R.id.etAffectedAsset);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerSeverity = findViewById(R.id.spinnerSeverity);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        tvReportedDate = findViewById(R.id.tvReportedDate);

        btnSaveIncident = findViewById(R.id.btnSaveIncident);
        btnCancel = findViewById(R.id.btnCancel);

        setupSpinners();

        incidentId = getIntent().getIntExtra("incident_id", -1);

        if (incidentId == -1) {

            setReportedDate();

            btnSaveIncident.setText("Save Incident");

        } else {

            loadIncident();

            btnSaveIncident.setText("Update Incident");
        }

        btnSaveIncident.setOnClickListener(v -> saveIncident());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupSpinners() {

        String[] types = {
                "Phishing", "Malware", "Unauthorized Access",
                "Brute Force Attack", "Data Breach",
                "Denial of Service", "Suspicious Activity", "Other"
        };

        String[] severities = {
                "Low", "Medium", "High", "Critical"
        };

        String[] statuses = {
                "Open", "Investigating", "Resolved"
        };

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, types);

        typeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<String> severityAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, severities);

        severityAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerSeverity.setAdapter(severityAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statuses);

        statusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(statusAdapter);
    }

    private void setReportedDate() {

        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        tvReportedDate.setText(format.format(new Date()));
    }

    private void loadIncident() {

        Cursor cursor = databaseHelper.getIncidentById(incidentId);

        if (cursor.moveToFirst()) {

            etTitle.setText(cursor.getString(1));
            etDescription.setText(cursor.getString(2));

            spinnerType.setSelection(
                    ((ArrayAdapter) spinnerType.getAdapter())
                            .getPosition(cursor.getString(3)));

            spinnerSeverity.setSelection(
                    ((ArrayAdapter) spinnerSeverity.getAdapter())
                            .getPosition(cursor.getString(4)));

            spinnerStatus.setSelection(
                    ((ArrayAdapter) spinnerStatus.getAdapter())
                            .getPosition(cursor.getString(5)));

            tvReportedDate.setText(cursor.getString(6));

            etAffectedAsset.setText(cursor.getString(7));
        }

        cursor.close();
    }

    private void saveIncident() {

        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Enter incident title");
            return;
        }

        String description = etDescription.getText().toString();
        String type = spinnerType.getSelectedItem().toString();
        String severity = spinnerSeverity.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        String date = tvReportedDate.getText().toString();
        String asset = etAffectedAsset.getText().toString();

        boolean result;

        if (incidentId == -1) {

            result = databaseHelper.insertIncident(
                    title, description, type, severity,
                    status, date, asset);

        } else {

            result = databaseHelper.updateIncident(
                    incidentId, title, description, type,
                    severity, status, date, asset);
        }

        if (result) {

            Toast.makeText(this, "Incident saved successfully",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this, "Operation failed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
