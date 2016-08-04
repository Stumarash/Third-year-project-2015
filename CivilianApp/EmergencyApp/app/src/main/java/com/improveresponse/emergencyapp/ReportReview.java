package com.improveresponse.emergencyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Report Review class handling the review of the  emergency being reported
 * @author Dokes
 */
public class ReportReview extends BaseActivity{

    private TextView reportType;
    private TextView medic_name;
    private TextView time;
    private TextView location;
    private TextView reference;
    private String emergencyType, emergencyName, emergencytime, emergencyloc, refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_review);

        reportType = (TextView)findViewById(R.id.report_type_review_one_plus);
        medic_name = (TextView)findViewById(R.id.report_type_review_two_plus);
        time = (TextView)findViewById(R.id.report_type_review_three_plus);
        location = (TextView)findViewById(R.id.report_type_review_four_plus);
        reference = (TextView)findViewById(R.id.report_type_review_six_plus);

        Intent intent = getIntent();
        emergencyType = intent.getStringExtra("Type");
        emergencyName = intent.getStringExtra("Name");
        emergencytime = intent.getStringExtra("Time");
        emergencyloc = intent.getStringExtra("location");

        reportType.setText(emergencyType);
        medic_name.setText(emergencyName);
        time.setText(emergencytime);
        location.setText(emergencyloc);
        reference.setText("Notified via email");
    }



}
