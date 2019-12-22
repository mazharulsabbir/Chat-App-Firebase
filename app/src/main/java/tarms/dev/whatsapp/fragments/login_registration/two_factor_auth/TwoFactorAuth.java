package tarms.dev.whatsapp.fragments.login_registration.two_factor_auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import tarms.dev.whatsapp.MainActivity;
import tarms.dev.whatsapp.R;
import tarms.dev.whatsapp.model.User;
import tarms.dev.whatsapp.utils.Utils;

public class TwoFactorAuth extends Fragment {

    private static final String TAG = "TwoFactorAuth";

    private static final String PHONE_NO = "phone-no";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;

    private User user;

    private String verificationCode = "";

    private String s1 = "", s2 = "", s3 = "", s4 = "", s5 = "", s6 = "";

    private EditText et1, et2, et3, et4, et5, et6;

    private Toast toast;

    public static TwoFactorAuth newInstance(User user) {
        TwoFactorAuth fragment = new TwoFactorAuth();
        Bundle args = new Bundle();
        args.putParcelable(TwoFactorAuth.PHONE_NO, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(PHONE_NO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two_factor_auth, container, false);

        et1 = view.findViewById(R.id.two_factor_code1);
        et2 = view.findViewById(R.id.two_factor_code2);
        et3 = view.findViewById(R.id.two_factor_code3);
        et4 = view.findViewById(R.id.two_factor_code4);
        et5 = view.findViewById(R.id.two_factor_code5);
        et6 = view.findViewById(R.id.two_factor_code6);


        et1.requestFocus();

        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));
        et5.addTextChangedListener(new GenericTextWatcher(et5));
        et6.addTextChangedListener(new GenericTextWatcher(et6));

        //phone number verification started
        startPhoneNumberVerification(user.getPhone());

        view.findViewById(R.id.verify).setOnClickListener(verify -> {
            if (verificationCode.trim().length() == 6)
                verifyOTP();
        });

        return view;
    }

    private void verifyCode(String code) {
        this.verificationCode = code;
    }

    public void verifyOTP() {
        verifyNumber(mVerificationId, verificationCode.trim());
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Logger.getLogger("Phone Verification ->").warning("Started");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        signInWithPhoneAuthCredential(credential);
                    }


                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);

                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getActivity(), "Code Sent", Toast.LENGTH_SHORT);
                        toast.show();


                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        mResendToken = token;
                    }

                });


        // [END start_phone_auth]
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Log.d(TAG, "onVerificationCompleted:" + credential);
//                        signInWithPhoneAuthCredential(credential);

                        String code = credential.getSmsCode();
                        Logger.getLogger("Code").warning(String.valueOf(code));

                        if (code != null) {
                            et1.setText(String.valueOf(code.charAt(0)));
                            et2.setText(String.valueOf(code.charAt(1)));
                            et3.setText(String.valueOf(code.charAt(2)));
                            et4.setText(String.valueOf(code.charAt(3)));
                            et5.setText(String.valueOf(code.charAt(4)));
                            et6.setText(String.valueOf(code.charAt(5)));
                        }
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);

                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getActivity(), "Code Sent", Toast.LENGTH_SHORT);
                        toast.show();


                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        mResendToken = token;
                    }

                },         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        String code = credential.getSmsCode();
                        Logger.getLogger("Code").warning(String.valueOf(code));

                        if (code != null) {
                            et1.setText(String.valueOf(code.charAt(0)));
                            et2.setText(String.valueOf(code.charAt(1)));
                            et3.setText(String.valueOf(code.charAt(2)));
                            et4.setText(String.valueOf(code.charAt(3)));
                            et5.setText(String.valueOf(code.charAt(4)));
                            et6.setText(String.valueOf(code.charAt(5)));
                        }

                        AuthCredential linkAuthCredential =
                                EmailAuthProvider.getCredential(user.getEmail(),
                                        user.getPassword()
                                );

                        linkWithEmailAndPassword(linkAuthCredential);

                    } else {
                        // Sign in failed, display a message and update the UI
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT);
                        toast.show();

                        Log.w(TAG, "signInWithCredential:failure" + mVerificationId, task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            if (toast != null)
                                toast.cancel();
                            toast = Toast.makeText(getActivity(), "Wrong Code", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        if (task.getException() instanceof FirebaseTooManyRequestsException) {
                            if (toast != null)
                                toast.cancel();
                            toast = Toast.makeText(getActivity(), "Registration Failed! Too many requests", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    private void verifyNumber(String mVerificationId, String iCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, iCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void linkWithEmailAndPassword(AuthCredential credential) {
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "linkWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    if (user != null) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                        Map<String, Object> createNewUser = new HashMap<>();
                        createNewUser.put("name", this.user.getName());
                        createNewUser.put("email", this.user.getEmail());
                        createNewUser.put("phone", this.user.getPhone());
                        createNewUser.put("image", this.user.getImage());

                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(this.user.getName().trim())
                                .setPhotoUri(Uri.parse(this.user.getImage()))
                                .build();

                        reference.child(Utils.getUserInfoReference(user))
                                .updateChildren(createNewUser)
                                .addOnCompleteListener(complete -> {

                                    user.updateProfile(userProfileChangeRequest);

                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                });

                    }
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.getException());
                    Toast.makeText(getContext(), "Registration failed.\n" + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.two_factor_code1:
                    if (text.length() == 1) {
                        et2.requestFocus();
                        s1 = text;
                    }
                    break;
                case R.id.two_factor_code2:
                    if (text.length() == 1) {
                        et3.requestFocus();
                        s2 = text;
                    } else if (text.length() == 0)
                        et1.requestFocus();
                    break;
                case R.id.two_factor_code3:
                    if (text.length() == 1) {
                        et4.requestFocus();
                        s3 = text;
                    } else if (text.length() == 0)
                        et2.requestFocus();
                    break;
                case R.id.two_factor_code4:
                    if (text.length() == 0)
                        et3.requestFocus();
                    else if (text.length() == 1) {
                        et5.requestFocus();
                        s4 = text;
                    }
                    break;
                case R.id.two_factor_code5:
                    if (text.length() == 1) {
                        et6.requestFocus();
                        s5 = text;
                    } else if (text.length() == 0)
                        et4.requestFocus();

                    break;
                case R.id.two_factor_code6:
                    s6 = text;
                    if (text.length() == 0)
                        et5.requestFocus();

                    break;
            }

            String code = s1 + s2 + s3 + s4 + s5 + s6;
            verifyCode(code);
            Logger.getLogger("Verification Code: ").warning(code);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}
