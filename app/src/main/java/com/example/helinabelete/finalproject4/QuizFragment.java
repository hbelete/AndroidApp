package com.example.helinabelete.finalproject4;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.helinabelete.finalproject4.GuessButtonListener;
import com.example.helinabelete.finalproject4.QuizViewModel;

public class QuizFragment extends Fragment {
    private SecureRandom random;
    private Animation shakeAnimation;
    private RelativeLayout quizRelativeLayout;
    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private TableRow[] guessTableRows;
    private TextView answerTextView;
    private QuizViewModel quizViewModel;
    public static ArrayList<String> countryList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quizViewModel = ViewModelProviders.of(getActivity()).get(QuizViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        setRetainInstance(true); // Retain this fragment on configuration changes.
        OnClickListener guessButtonListener = new GuessButtonListener(this);
        TableLayout answersTableLayout = (TableLayout) view.findViewById(R.id.answersTableLayout);

        this.random = new SecureRandom();
        this.shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
        this.shakeAnimation.setRepeatCount(5);
        this.quizRelativeLayout = view.findViewById(R.id.quizRelativeLayout);
        this.questionNumberTextView = view.findViewById(R.id.questionNumberTextView);
        this.flagImageView = (ImageView) view.findViewById(R.id.flagImageView);

        this.guessTableRows = new TableRow[4];
        this.answerTextView = view.findViewById(R.id.answerTextView);
        this.countryList = new ArrayList<String>();




        for (int i = 0; i < answersTableLayout.getChildCount(); i++) {
            try {
                if (answersTableLayout.getChildAt(i) instanceof TableRow) {
                    this.guessTableRows[i] = (TableRow) answersTableLayout.getChildAt(i);
                }
            } catch (ArrayStoreException e) {
                Log.e(QuizViewModel.getTag(),
                        "Error getting button rows on loop #" + String.valueOf(i), e);
            }
        }

        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setOnClickListener(guessButtonListener);
            }
        }

        this.questionNumberTextView.setText(
                getString(R.string.question, 1, QuizViewModel.getFlagsInQuiz()));
        return view;
    }

    public void updateGuessRows() {

        int numberOfGuessRows = this.quizViewModel.getGuessRows();
        for (TableRow row : this.guessTableRows) {
            row.setVisibility(View.GONE);
        }
        for (int rowNumber = 0; rowNumber < numberOfGuessRows; rowNumber++) {
            guessTableRows[rowNumber].setVisibility(View.VISIBLE);
        }
    }


    public void resetQuiz() {
        this.quizViewModel.clearFileNameList();
        this.quizViewModel.setFileNameList(getActivity().getAssets());
        this.quizViewModel.resetTotalGuesses();
        this.quizViewModel.resetCorrectAnswers();
        this.quizViewModel.clearQuizCountriesList();

        int flagCounter = 1;
        int numberOfFlags = this.quizViewModel.getFileNameList().size();
        while (flagCounter <= QuizViewModel.getFlagsInQuiz()) {
            int randomIndex = this.random.nextInt(numberOfFlags);

            //String filename = this.quizViewModel.getFileNameList().get(randomIndex);
            String filename = this.quizViewModel.getFileNameList().get(randomIndex);

            if (!this.quizViewModel.getQuizCountriesList().contains(filename)) {
                this.quizViewModel.getQuizCountriesList().add(filename);
                countryList.add(filename);
                Log.e(filename, "testing filename");
                //countryList.add(filename);
                //Log.d("testing country array", countryList.toString());
                //Log.e(filename,"Filename List");
                ++flagCounter;
            }
        }

        this.updateGuessRows();
        this.loadNextFlag();
    }

    private void loadNextFlag() {
        AssetManager assets = getActivity().getAssets();
        String nextImage = this.quizViewModel.getNextCountryFlag();
        String region = nextImage.substring(0, nextImage.indexOf('-'));

        this.quizViewModel.setCorrectAnswer(nextImage);
        answerTextView.setText("");

        questionNumberTextView.setText(getString(R.string.question,
                (quizViewModel.getCorrectAnswers() + 1), QuizViewModel.getFlagsInQuiz()));

        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
            animate(false);
        } catch (IOException e) {
            Log.e(QuizViewModel.getTag(), "Error Loading " + nextImage, e);
        }

        this.quizViewModel.shuffleFilenameList();

        for (int rowNumber = 0; rowNumber < this.quizViewModel.getGuessRows(); rowNumber++) {
            for (int column = 0; column < guessTableRows[rowNumber].getChildCount(); column++) {
                Button guessButton = (Button) guessTableRows[rowNumber].getVirtualChildAt(column);
                guessButton.setEnabled(true);
                String answerChoices = this.quizViewModel.getFileNameList()
                        .get((rowNumber * 2) + column)
                        .substring(this.quizViewModel.getFileNameList()
                                .get((rowNumber * 2) + column).indexOf('-') + 1)
                        .replace('_', ' ');
                guessButton.setText(answerChoices);
                ///Log.e(filename,"Testing Filename Countries");
            }
        }

        int row = this.random.nextInt(this.quizViewModel.getGuessRows());
        int column = this.random.nextInt(2);
        TableRow randomRow = guessTableRows[row];
        ((Button) randomRow.getChildAt(column)).setText(this.quizViewModel.getCorrectCountryName());
    }

    public void animate(boolean animateOut) {
        if (this.quizViewModel.getCorrectAnswers() == 0) {
            return;
        }
        int centreX = (quizRelativeLayout.getLeft() + quizRelativeLayout.getRight()) / 2;
        int centreY = (quizRelativeLayout.getTop() + quizRelativeLayout.getBottom()) / 2;
        int radius = Math.max(quizRelativeLayout.getWidth(), quizRelativeLayout.getHeight());
        Animator animator;
        if (animateOut) {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizRelativeLayout, centreX, centreY, radius, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    loadNextFlag();
                }
            });
        } else {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizRelativeLayout, centreX, centreY, 0, radius);
        }

        animator.setDuration(500);
        animator.start();
    }

    public void incorrectAnswerAnimation(){
        flagImageView.startAnimation(shakeAnimation);

        answerTextView.setText(R.string.incorrect_answer);
        answerTextView.setTextColor(getResources().getColor(R.color.red));
    }

    public void disableButtons() {
        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setEnabled(false);
            }
        }
    }

    public TextView getAnswerTextView() {
        return answerTextView;
    }

    public QuizViewModel getQuizViewModel() {
        return quizViewModel;
    }
}



