package tarms.dev.whatsapp.fragments.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import tarms.dev.whatsapp.MainActivity;
import tarms.dev.whatsapp.R;
import tarms.dev.whatsapp.UserActivity;

public class Login extends Fragment {

    private static final String TAG = "Login";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ImageView add_button = view.findViewById(R.id.add_button);
        TextView registration = view.findViewById(R.id.registration);

        add_button.setOnClickListener(view1 -> Registration());

        registration.setOnClickListener(view12 -> Registration());

        MaterialButton loginButton = view.findViewById(R.id.cirLoginButton);

        loginButton.setOnClickListener(login -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            TextInputLayout mEmail, mPassword;
            mEmail = view.findViewById(R.id.textInputEmail);
            mPassword = view.findViewById(R.id.textInputPassword);

            String email = mEmail.getEditText().getText().toString().trim();
            String pass = mPassword.getEditText().getText().toString().trim();

            boolean valid = true;

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email can't be empty");
                valid = false;
            } else mEmail.setErrorEnabled(false);

            if (valid)
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Invalid email address");
                    valid = false;
                } else mEmail.setErrorEnabled(false);

            if (pass.length() < 6) {
                mPassword.setError("Password should be at least 6");
                valid = false;
            } else mPassword.setErrorEnabled(false);

            if (valid) {

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {


                    if (task.isSuccessful()) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        Log.e(TAG, "onCreateView: ", task.getException());

                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            mEmail.setError("Email is wrong");
                        } else mEmail.setErrorEnabled(false);

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            mPassword.setError("Password is wrong");
                        } else mPassword.setErrorEnabled(false);

                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    public void Registration() {
        ((UserActivity) getActivity()).addFirstFragment(new Registration());
    }
}
