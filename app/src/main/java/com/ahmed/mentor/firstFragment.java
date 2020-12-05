package com.ahmed.mentor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link secondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class firstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private static FirebaseAuth fAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView profile, pName, pEmail, pPhone, mentor, mName, mEmail, mPhone;

    //private String userID;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private ArrayList<String> keyList = new ArrayList<>();

    public firstFragment() {

        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static firstFragment newInstance(String param1, String param2) {
        firstFragment fragment = new firstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fragment_final, container, false);

        //Assigning
        profile = root.findViewById(R.id.profileTV); profile.setText("Your Profile");
        pName = root.findViewById(R.id.profileName);
        pEmail = root.findViewById(R.id.profileEmail);
        pPhone = root.findViewById(R.id.profilePhone);
        //profileImage = root.findViewById(R.id.profileImage);

        //Assigning Mentor
        //mentor = root.findViewById(R.id.MentorTV); mentor.setText("Mentor's Profile");
        mName = root.findViewById(R.id.mentorName);
        mEmail = root.findViewById(R.id.mentorEmail);
        mPhone = root.findViewById(R.id.mentorPhone);
        //mentorImage = root.findViewById(R.id.mentorImage);

        String userID = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("Users").document(userID); //Gets the current user
        final DocumentReference Interests = fStore.collection("Interests").document(userID);

       // final Double[] x = new Double[1];
       // final String[] name = new String[1];
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                pName.setText(documentSnapshot.getString("First Name")+ " " +documentSnapshot.getString("Last Name"));
                pEmail.setText(documentSnapshot.getString("Email"));
                pPhone.setText(documentSnapshot.getString("Phone"));
            }
        });

        DocumentReference dR = fStore.collection("Users").document(userID);
        dR.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String match = documentSnapshot.getString("Matched");
                if(!match.equals("null"))
                {
                    DocumentReference matchD = fStore.collection("Users").document(match);
                    matchD.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                            mName.setText((String)doc.get("First Name"));
                            mEmail.setText((String)doc.get("Email"));
                            mPhone.setText((String)doc.get("Phone"));
                        }
                    });
                }
                else
                {
                    mName.setText("You do not have a mentor/student yet");
                }
            }
        });



        //TextView welcome = (TextView)root.findViewById(R.id.welcomeTV);
        //((TextView)welcome).setText("Welcome!!");
        //((TextView)welcome).setTextSize(30);

        //Log.d("NAME OUTSIDE", (String) pName.getText());

        return root;
    }

    public static class finalFragment {

    }
}