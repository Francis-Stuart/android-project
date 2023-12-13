package clarkson.ee408.tictactoev4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import clarkson.ee408.tictactoev4.client.AppExecutors;
import clarkson.ee408.tictactoev4.client.SocketClient;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.socket.Request;
import clarkson.ee408.tictactoev4.socket.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText displayNameField;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Getting Inputs
        Button registerButton = findViewById(R.id.buttonRegister);
        Button loginButton = findViewById(R.id.buttonLogin);
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        confirmPasswordField = findViewById(R.id.editTextConfirmPassword);
        displayNameField = findViewById(R.id.editTextDisplayName);

        // TODO: Initialize Gson with null serialization option
        gson = new Gson();

        //Adding Handlers
        //TODO: set an onclick listener to registerButton to call handleRegister()
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                handleRegister();
            }
        });

        //TODO: set an onclick listener to loginButton to call goBackLogin()
        loginButton.setOnClickListener(new View.OnClickListener){
            @Override
            public void onClick(View view){
                goBackLogin();
            }
        }
    }

    /**
     * Process registration input and pass it to {@link #submitRegistration(User)}
     */
    public void handleRegister() {
        // TODO: declare local variables for username, password, confirmPassword and displayName. Initialize their values with their corresponding EditText
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        String displayName = displayNameField.getText().toString().trim();
        // TODO: verify that all fields are not empty before proceeding. Toast with the error message
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: verify that password is the same af confirm password. Toast with the error message
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: Create User object with username, display name and password and call submitRegistration()
        User user = new User(username, displayName, password);
        submitRegistration(user);
    }

    /**
     * Sends REGISTER request to the server
     * @param user the User to register
     */
    void submitRegistration(User user) {
        //TODO: Send a REGISTER request to the server, if SUCCESS reponse, call goBackLogin(). Else, Toast the error message
        SocketClient.getInstance().sendRequest(Request.register(user), new SocketClient.ResponseListener()) {
            @Override
            public void onResponse(Response response){
                if (response.isSuccess()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            goBackLogin();
                        }
                    });
                } else {
                    final String errorMessage = response.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }
    }

    /**
     * Change the activity to LoginActivity
     */
    private void goBackLogin() {
        //TODO: Close this activity by calling finish(), it will automatically go back to its parent (i.e,. LoginActivity)
        finish();
    }

}