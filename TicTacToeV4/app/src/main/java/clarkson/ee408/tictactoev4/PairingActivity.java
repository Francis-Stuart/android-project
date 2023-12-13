package clarkson.ee408.tictactoev4;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import clarkson.ee408.tictactoev4.model.Event;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.socket.PairingResponse;

public class PairingActivity extends AppCompatActivity {

    private final String TAG = "PAIRING";

    private Gson gson;

    private TextView noAvailableUsersText;
    private RecyclerView recyclerView;
    private AvailableUsersAdapter adapter;

    private Handler handler;
    private Runnable refresh;

    private boolean shouldUpdatePairing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        Log.e(TAG, "App is now created");
        // TODO: setup Gson with null serialization option
        gson = new GsonBuilder().serializeNulls().create();


        //Setting the username text
        TextView usernameText = findViewById(R.id.text_username);
        // TODO: set the usernameText to the username passed from LoginActivity (i.e from Intent)
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME_KEY");
        usernameText.setText(username);

        //Getting UI Elements
        noAvailableUsersText = findViewById(R.id.text_no_available_users);
        recyclerView = findViewById(R.id.recycler_view_available_users);

        //Setting up recycler view adapter
        adapter = new AvailableUsersAdapter(this, this::sendGameInvitation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateAvailableUsers(null);

        handler = new Handler();
        refresh = () -> {
            // TODO: call getPairingUpdate if shouldUpdatePairing is true
            if (shouldUpdatePairing) {
                getPairingUpdate();
            }
            handler.postDelayed(refresh, 1000);
        };
        handler.post(refresh);
    }

    /**
     * Send UPDATE_PAIRING request to the server
     */
    private void getPairingUpdate() {
        // TODO:  Send an UPDATE_PAIRING request to the server. If SUCCESS call handlePairingUpdate(). Else, Toast the error

        ServerApi.sendUpdatePairingRequest(new Callback<PairingResponse>() {
            @Override
            public void onResponse(Call<PairingResponse> call, Response<PairingResponse> response) {
                if (response.isSuccessful()) {
                    handlePairingUpdate(response.body());
                } else {
                    // TODO: Toast the error
                    Toast.makeText(PairingActivity.this, "Error updating pairing", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PairingResponse> call, Throwable t) {
                // TODO: Toast the error
                Toast.makeText(PairingActivity.this, "Failed to update pairing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handle the PairingResponse received form the server
     * @param response PairingResponse from the server
     */
    private void handlePairingUpdate(PairingResponse response) {
        // TODO: handle availableUsers by calling updateAvailableUsers()
        updateAvailableUsers(response.getAvailableUsers());
        // TODO: handle invitationResponse. First by sending acknowledgement calling sendAcknowledgement()

        // --TODO: If the invitationResponse is ACCEPTED, Toast an accept message and call beginGame
        // --TODO: If the invitationResponse is DECLINED, Toast a decline message
        Event invitationResponse = response.getInvitationResponse();
        if (invitationResponse != null) {
            sendAcknowledgement(invitationResponse);

            if (invitationResponse.getType() == Event.Type.ACCEPTED) {
                Toast.makeText(this, "Invitation accepted!", Toast.LENGTH_SHORT).show();
                beginGame(invitationResponse, 2);
            } else if (invitationResponse.getType() == Event.Type.DECLINED) {
                Toast.makeText(this, "Invitation declined.", Toast.LENGTH_SHORT).show();
            }
        }

        // TODO: handle invitation by calling createRespondAlertDialog()
    }

    /**
     * Updates the list of available users
     * @param availableUsers list of users that are available for pairing
     */
    public void updateAvailableUsers(List<User> availableUsers) {
        adapter.setUsers(availableUsers);
        if (adapter.getItemCount() <= 0) {
            // TODO show noAvailableUsersText and hide recyclerView
            noAvailableUsersText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // TODO hide noAvailableUsersText and show recyclerView
            noAvailableUsersText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sends game invitation to an
     * @param userOpponent the User to send invitation to
     */
    private void sendGameInvitation(User userOpponent) {
        // TODO:  Send an SEND_INVITATION request to the server. If SUCCESS Toast a success message. Else, Toast the error
        ServerApi.sendInvitation(userOpponent, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PairingActivity.this, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Toast the error
                    Toast.makeText(PairingActivity.this, "Failed to send invitation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // TODO: Toast the error
                Toast.makeText(PairingActivity.this, "Failed to send invitation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends an ACKNOWLEDGE_RESPONSE request to the server
     * Tell server i have received accept or declined response from my opponent
     */
    private void sendAcknowledgement(Event invitationResponse) {
        // TODO:  Send an ACKNOWLEDGE_RESPONSE request to the server.
        ServerApi.sendAcknowledgement(invitationResponse, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle the response if needed
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure if needed
            }
        });
    }

    /**
     * Create a dialog showing incoming invitation
     * @param invitation the Event of an invitation
     */
    private void createRespondAlertDialog(Event invitation) {
        // TODO: set shouldUpdatePairing to false
        shouldUpdatePairing = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Game Invitation");
        builder.setMessage(invitation.getSender() + " has Requested to Play with You");
        builder.setPositiveButton("Accept", (dialogInterface, i) -> acceptInvitation(invitation));
        builder.setNegativeButton("Decline", (dialogInterface, i) -> declineInvitation(invitation));
        builder.show();
    }

    /**
     * Sends an ACCEPT_INVITATION to the server
     * @param invitation the Event invitation to accept
     */
    private void acceptInvitation(Event invitation) {
        // TODO:  Send an ACCEPT_INVITATION request to the server. If SUCCESS beginGame() as player 2. Else, Toast the error
        ServerApi.sendAcceptInvitation(invitation, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // TODO: Call beginGame as player 2
                    beginGame(invitation, 2);
                } else {
                    // TODO: Toast the error
                    Toast.makeText(PairingActivity.this, "Failed to accept invitation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // TODO: Toast the error
                Toast.makeText(PairingActivity.this, "Failed to accept invitation", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

    /**
     * Sends an DECLINE_INVITATION to the server
     * @param invitation the Event invitation to decline
     */
    private void declineInvitation(Event invitation) {
        // TODO:  Send a DECLINE_INVITATION request to the server. If SUCCESS response, Toast a message, else, Toast the error
        private void declineInvitation(Event invitation) {
            // TODO:  Send a DECLINE_INVITATION request to the server. If SUCCESS response, Toast a message, else, Toast the error
            ServerApi.sendDeclineInvitation(invitation, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // TODO: Toast a message
                        Toast.makeText(PairingActivity.this, "Invitation declined", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO: Toast the error
                        Toast.makeText(PairingActivity.this, "Failed to decline invitation", Toast.LENGTH_SHORT).show();
                    }
                    // Set shouldUpdatePairing to true after DECLINE_INVITATION is sent.
                    shouldUpdatePairing = true;
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // TODO: Toast the error
                    Toast.makeText(PairingActivity.this, "Failed to decline invitation", Toast.LENGTH_SHORT).show();
                    // Set shouldUpdatePairing to true after DECLINE_INVITATION is sent.
                    shouldUpdatePairing = true;
                }
            });
        // TODO: set shouldUpdatePairing to true after DECLINE_INVITATION is sent.
    }

    /**
     *
     * @param pairing the Event of pairing
     * @param player either 1 or 2
     */
    private void beginGame(Event pairing, int player) {
        // TODO: set shouldUpdatePairing to false
            shouldUpdatePairing = false;
        // TODO: start MainActivity and pass player as data
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("PLAYER_KEY", player);
            startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: set shouldUpdatePairing to true
        shouldUpdatePairing = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // TODO: set shouldUpdatePairing to false
        shouldUpdatePairing = false;
        // TODO: logout by calling close() function of SocketClient
        SockeyClient.getInstance().close();
    }

}