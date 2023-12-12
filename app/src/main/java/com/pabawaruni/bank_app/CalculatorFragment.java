package com.pabawaruni.bank_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class CalculatorFragment extends Fragment {

    private EditText DepositAmount, InterestRate, Months;
    private Button calculate;
    private TextView interestEarned, TotalAmount;
    Double num1, num2, num3;


    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DepositAmount = view.findViewById(R.id.etDepositAmount);
        InterestRate = view.findViewById(R.id.etInterestRate);
        Months = view.findViewById(R.id.etMonths);
        calculate = view.findViewById(R.id.btnCalculate);
        interestEarned = view.findViewById(R.id.txtInterest);
        TotalAmount = view.findViewById(R.id.txtTotal);

        // Add the close button implementation here
        ImageView closeButton = view.findViewById(R.id.backButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish(); // Close the entire program
            }
        });

        Clicklistner();
    }

    // create fixed deposit calculation
    public void Clicklistner() {
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num1 = Double.parseDouble(DepositAmount.getText().toString());
                num2 = Double.parseDouble(InterestRate.getText().toString());
                num3 = Double.parseDouble(Months .getText().toString());
                Double result1 = num2 / 100;
                Double result2 = num1 * result1;
                Double result3 = result2 / 12;
                Double result4 = result3 * num3;
                Double result5 = result4 + num1;
                interestEarned.setText(String.valueOf(result4));
                TotalAmount.setText(String.valueOf(result5));
            }
        });
    }
}