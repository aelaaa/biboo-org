package com.example.biboo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;



public class Prompt_Dialogs extends Dialog {

    private ResetGameActivity resetGameActivity;
    private ExitGameCallback exitGameCallback;
    private GoToHomeScreenCallback goToHomeScreenCallback;
    private boolean isResetting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public interface ExitGameCallback {
        void onExitGame();
    }

    public interface ResetGameActivity {
        void onResetGameActivity();
    }

    public interface GoToHomeScreenCallback {
        void onGoToHomeScreen();
    }

    public interface AnimaDietUseHintCallback {
        void onUseHint();
    }

    public Prompt_Dialogs(@NonNull Context context) {
        super(context);
    }

    public void setResetGameActivity(ResetGameActivity reset){
        this.resetGameActivity = reset;
    }

    public void setExitGameCallback(ExitGameCallback callback) {
        this.exitGameCallback = callback;
    }

    public void setGoToHomeScreenCallback(GoToHomeScreenCallback callback) {
        this.goToHomeScreenCallback = callback;
    }


    public void confirmationDialogs(String title, String description, Context context) {
        setContentView(R.layout.prompt_confirm);
        TextView promptTitle = findViewById(R.id.txtLoginConfirm);
        TextView promptText = findViewById(R.id.txtLoginConfirmDes);
        Button yes = findViewById(R.id.btnYES);
        Button no = findViewById(R.id.btnNO);

        promptTitle.setText(title);
        promptText.setText(description);


        yes.setOnClickListener(v -> {
            if (exitGameCallback != null) {
                exitGameCallback.onExitGame();
            } else if (goToHomeScreenCallback != null) {
                goToHomeScreenCallback.onGoToHomeScreen();
            } else if (resetGameActivity != null && !isResetting) {
                // Only initiate the reset if it's not already in progress
                isResetting = true;
                resetGameActivity.onResetGameActivity();
            }
            dismiss();
        });
        no.setOnClickListener(v -> {
            dismiss();
        });
    }


    public void statusDialogs(String title, String description) {
        setContentView(R.layout.prompt_status);
        TextView promptTitle = findViewById(R.id.txtstatusTitle);
        TextView promptText = findViewById(R.id.txtstatusDescription);
        Button ok = findViewById(R.id.btnOKAY);

        promptTitle.setText(title);
        promptText.setText(description);

        ok.setOnClickListener(v-> {
            dismiss();
        });

    }

    public void CongratsDialogs(String title, String description) {
        setContentView(R.layout.prompt_status);
        TextView promptTitle = findViewById(R.id.txtstatusTitle);
        TextView promptText = findViewById(R.id.txtstatusDescription);
        Button ok = findViewById(R.id.btnOKAY);

        promptTitle.setText(title);
        promptText.setText(description);

        ok.setOnClickListener(v-> {
            Intent intent = new Intent(getContext(), Homepage_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            dismiss(); // Dismiss the dialog
        });

    }


    public void infoDialog() {
        setContentView(R.layout.prompt_tutorial);
        Button ok = findViewById(R.id.btn_tutorialOK);
        ok.setOnClickListener(v-> {
            dismiss(); // Dismiss the dialog
        });

    }


    public void HintDialogforAnimafication(String title, String description) {
        setContentView(R.layout.animafication_hint);
        TextView promptTitle = findViewById(R.id.txthintTitle);
        TextView promptText = findViewById(R.id.txtHintDescription);
        Button ok = findViewById(R.id.btn_hint_okay);

        promptTitle.setText(title);
        promptText.setText(description);

        ok.setOnClickListener(v-> {
            dismiss();
        });
    }

    public void HintDialogforAnimaDiet(String title, String description, AnimaDietUseHintCallback animaDietUseHintCallback) {
        setContentView(R.layout.prompt_confirm);
        TextView promptTitle = findViewById(R.id.txtLoginConfirm);
        TextView promptText = findViewById(R.id.txtLoginConfirmDes);
        Button yes = findViewById(R.id.btnYES);
        Button no = findViewById(R.id.btnNO);

        promptTitle.setText(title);
        promptText.setText(description);


        yes.setOnClickListener(v -> {
            if (animaDietUseHintCallback != null) {
                animaDietUseHintCallback.onUseHint();
            }
            dismiss();

        });
        no.setOnClickListener(v -> {
            dismiss();
        });
    }


    public void changePassword(String title, String input_Text, String description, String textHint) {
        setContentView(R.layout.prompt_change_password);
        Button cancel = findViewById(R.id.btnCancel);
        Button submit = findViewById(R.id.btnOkayPassChanged);
        TextView promptTitle = findViewById(R.id.txtChangepasswordTitle);
        TextView promptText = findViewById(R.id.txtchangepasswordDescription);
        TextView promptTxtInputDescrip = findViewById(R.id.txtEnterCode);
        EditText promptInput = findViewById(R.id.edtxtCode);

        promptTitle.setText(title);
        promptText.setText(description);
        promptTxtInputDescrip.setText(input_Text);
        promptInput.setHint(textHint);

        cancel.setOnClickListener(v -> {

        });

        submit.setOnClickListener(v-> {

        });
    }

    // Method to show the settings dialog
    public void showSettingsDialog(FragmentManager fragmentManager) {
        settings_Dialog dialogFragment = new settings_Dialog();
        dialogFragment.show(fragmentManager, "settings_dialog");
    }

    // Setting up the settings dialog
    public static class settings_Dialog extends DialogFragment implements Prompt_Dialogs.ExitGameCallback, Prompt_Dialogs.GoToHomeScreenCallback, Prompt_Dialogs.ResetGameActivity {

        private Prompt_Dialogs parentDialog;
        private ResetGameActivity resetGameActivity;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.prompt_settings, container, false);

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setCancelable(false);

            resetGameActivity = (ResetGameActivity) requireActivity();
            parentDialog = new Prompt_Dialogs(requireContext());
            parentDialog.setResetGameActivity(resetGameActivity);
            Button btn_continue = view.findViewById(R.id.btn_settings_continue);
            Button btn_reset = view.findViewById(R.id.btn_settings_reset);
            Button btn_exitgame = view.findViewById(R.id.btn_settings_exitgame);
            Button btn_homescreen = view.findViewById(R.id.btn_settings_homescreen);

            // Check if the activity is still attached before proceeding
            if (getActivity() == null || isRemoving() || isDetached()) {
                Log.e("settings_Dialog", "Activity is null or detached. Dismissing the dialog.");
                dismiss();
                return;
            }

            Log.d("settings_Dialog", "onViewCreated: Dialog created successfully.");

            btn_continue.setOnClickListener(v -> {
                dismiss();
            });

            btn_reset.setOnClickListener(v -> {
                if (parentDialog != null) {
                    parentDialog.setResetGameActivity(this);
                    parentDialog.confirmationDialogs("Confirmation!"
                            , "Would you like to reset the game? All progress will be lost."
                            , requireContext());
                    parentDialog.show();
                }
            });

            btn_exitgame.setOnClickListener(v -> {
                if (parentDialog != null) {
                    parentDialog.setExitGameCallback(this);
                    parentDialog.confirmationDialogs("Confirmation!"
                            , "Would you like to exit the game? All progress will be lost and scores will be summarized."
                            , requireContext());
                    parentDialog.show();
                }
            });

            btn_homescreen.setOnClickListener(v -> {
                if (parentDialog != null) {
                    parentDialog.setGoToHomeScreenCallback(this);
                    parentDialog.confirmationDialogs("Confirmation!"
                            , "Would you like to return to homescreen? All progress will be lost and scores will be summarized."
                            , requireContext());
                    parentDialog.show();
                }
            });
        }


        @Override
        public void onResetGameActivity() {

            if (getActivity() instanceof Animafication) {
                ((Animafication) getActivity()).onResetGameActivity();
            }else if (getActivity() instanceof Anima_Diet) {
                ((Anima_Diet) getActivity()).onResetGameActivity();
            }

        }


        @Override
        public void onExitGame() {
            if (getActivity() != null) {
                getActivity().finishAffinity();
            }
        }

        @Override
        public void onGoToHomeScreen() {
            Log.d("settings_Dialog", "onGoToHomeScreen callback triggered");
            if (isAdded() && getActivity() != null) {
                try {
                    Intent intent = new Intent(getActivity(), Homepage_activity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("settings_Dialog", "Fragment not attached to an activity");
            }
        }
}
}
