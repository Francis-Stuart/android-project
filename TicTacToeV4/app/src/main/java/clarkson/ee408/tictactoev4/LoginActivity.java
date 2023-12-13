package clarkson.ee408.tictactoev4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import clarkson.ee408.tictactoev4.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting UI elements
        Button loginButton = findViewById(R.id.buttonLogin);
        Button registerButton = findViewById(R.id.buttonRegister);
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);

        // TODO: Initialize Gson with null serialization option
        Gson gson = new GsonBuilder().serializeNulls().create();

        //Adding Handlers
        loginButton.setOnClickListener(view -> handleLogin());
        registerButton.setOnClickListener(view -> gotoRegister());
    }

    /**
     * Process login input and pass it to {@link #submitLogin(User)}
     */
    public void handleLogin() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // TODO: verify that all fields are not empty before proceeding. Toast with the error message
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: Create User object with username and password and call submitLogin()
        submitLogin()
        User user = new User(username, password);
        submitLogin(user);
    }
    }

    /**
     * Sends a LOGIN request to the server
     * @param user User object to login
     */
    public void submitLogin(User user) {
        // TODO: Send a LOGIN request, If SUCCESS response, call gotoPairing(), else, Toast the error message from sever
        gotoPairing(), else, Toast the error message from server

        boolean loginSuccessful = checkLogin(user);

        if (loginSuccessful) {
            gotoPairing();
        } else {
            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    }

    /**
     * Switch the page to {@link PairingActivity}
     * @param username the data to send
     */
    public void gotoPairing(String username) {
        // TODO: start PairingActivity and pass the username
        Intent intent = new Intent(this, PairingActivity.class);
        intent.putExtra("USERNAME", username); // Pass the username as an extra
        startActivity(intent);
        }
    }

    /**
     * Switch the page to {@link RegisterActivity}
     */
    public void gotoRegister() {
        // TODO: start RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        }
    }
}