package com.pabawaruni.bank_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int ANIMATION_DELAY = 200; // Delay between each letter animation (in milliseconds)
    private static final int ANIMATION_DURATION = 500; // Duration of each letter animation (in milliseconds)
    private static final int DELAY_AFTER_ANIMATION = 300; // Delay after the full word animation (in milliseconds)

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = findViewById(R.id.textView);
        animateText();
    }

    private void animateText() {
        final String word = "RATE  WATCH"; // The word to be animated
        final StringBuilder animatedWord = new StringBuilder(); // StringBuilder to store the animated word

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < word.length()) {
                    animatedWord.append(word.charAt(count)); // Append the next character of the word to the animatedWord
                    textView.setText(animatedWord); // Update the TextView with the animated word
                    count++;
                    handler.postDelayed(this, ANIMATION_DELAY); // Call the runnable again after the delay
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashScreen.this, Home.class)); // Start the Home activity
                            finish(); // Finish the current SplashScreen activity
                        }
                    }, DELAY_AFTER_ANIMATION);
                }
            }
        };

        AlphaAnimation animation = new AlphaAnimation(0, 1); // Create an AlphaAnimation to fade in the TextView
        animation.setDuration(ANIMATION_DURATION); // Set the duration of the animation
        textView.startAnimation(animation); // Start the animation on the TextView
        handler.postDelayed(runnable, ANIMATION_DELAY); // Call the runnable with initial delay
    }
}
