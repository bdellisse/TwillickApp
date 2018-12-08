package ca.aircadets832.twillickapp.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ca.aircadets832.twillickapp.R;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private boolean authError = false;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                signIn();
            }
        });

        ImageButton bCalendar = findViewById(R.id.bCalendar);
        bCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this,
                        TwillickCalendarActivity.class);
                startActivity(myIntent);
            }
        });

        ImageButton bSignup = findViewById(R.id.bSignup);
        bSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String url = "http://www.signupgenius.com/index.cfm?go=c.SignUpSearch&eid=0ACBCCD6F5CFFD6D&cs=09B2BAAD8FBE8B637B0A64715BB29BBA&sortby=l.title";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        ImageButton bAbsence = findViewById(R.id.bAbsence);
        bAbsence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSfXgMbqnWUlxaiFWRnQYb1kFNzmQXNBCi5_3_zb8aSsVZFSxg/viewform?embedded=true";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        ImageButton bSupply = findViewById(R.id.bSupply);
        bSupply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSfYAOQwcehd2qg59UH06eW1Grz5tzLVKLwDyLPUftsdvqlA6Q/viewform?embedded=true";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        ImageButton signOut = findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                signOut(false);
            }});

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.getEmail().toLowerCase().contains("832aircadets.ca")) {
                                updateUI(user);
                            } else {
                                authError = true;
                                signOut(true);
                            }
                        } else {
                            authError = true;
                            updateUI(null);
                        }

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(boolean inError) {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.AuthFailureStatus).setVisibility(View.GONE);
            findViewById(R.id.profilePicture).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.userdetails).setVisibility(View.VISIBLE);

            ImageView profilePicture = findViewById(R.id.profilePicture);
            profilePicture.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(profilePicture);
            TextView displayName = findViewById(R.id.displayname);
            displayName.setText(user.getDisplayName());
            TextView email = findViewById(R.id.email);
            email.setText(user.getEmail());
            authError = false;
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.profilePicture).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            findViewById(R.id.userdetails).setVisibility(View.GONE);

            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            if (authError)
            {
                findViewById(R.id.AuthFailureStatus).setVisibility(View.VISIBLE);
            }
        }
    }


}
