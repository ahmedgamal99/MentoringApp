package com.ahmed.mentor;

import android.content.Intent;
import android.media.MediaDrm;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Nullable;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link secondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class secondFragment extends Fragment {

    public TextView textView;
    public Button test;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fireBase = FirebaseAuth.getInstance();
    private String currentUser = fireBase.getCurrentUser().getUid();

    private Map<String, Integer> scores = new HashMap<String, Integer>();
    private Map<String, ArrayList<Double>> nonFreshmen = new HashMap<String, ArrayList<Double>>();
    private ArrayList<Double> temp = new ArrayList<Double>();
    private ArrayList<Double> currentList = new ArrayList<>();
    private ArrayList<String> keyList = new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public secondFragment() {
    } // Required empty public constructor

    // TODO: Rename and change types and number of parameters
    public static secondFragment newInstance(String param1, String param2) {
        secondFragment fragment = new secondFragment();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = (ViewGroup) inflater.inflate(R.layout.fragment_second, container, false);
        String userID = fireBase.getCurrentUser().getUid();

        final CollectionReference interests = fStore.collection("Users").document(userID).collection("Interests");
        interests.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Double tempx;
                    Map<String, Object> dataHolder = doc.getData();
                    tempx = (Double) dataHolder.get("Art");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("Board Games");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("Cooking");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("Music");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("Reading");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("Sports");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("TV Shows");
                    currentList.add(tempx);
                    tempx = (Double) dataHolder.get("Video Games");
                    currentList.add(tempx);
                }

                Log.d("currentList", String.valueOf(currentList.size()));
            }
        });

        final Boolean[] size = {false};
        final DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                CollectionReference stmt = fStore.collection("Users").document(currentUser).collection("Interests");
                stmt.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(queryDocumentSnapshots.size() == 1 ) size[0] = true;
                        else size[0] = false;
                        Log.d("SIZE", String.valueOf(size[0]));

                        String string = documentSnapshot.getString("Matched");
                        if (documentSnapshot.getBoolean("Freshman") == true && Objects.equals(string, "null") && size[0] == true) {

                            final CollectionReference users = fStore.collection("Users");
                            users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Boolean isFreshman = document.getBoolean("Freshman");
                                        String str = document.getString("Matched");
                                        if (!isFreshman & str.equals("null")) {
                                            final String userID = document.getId();
                                            Log.d("ID", userID);
                                            keyList.add(userID);
                                        }
                                    }
                                    Log.d("KeyList size", String.valueOf(keyList.size()));

                                    Log.d("Looping through KeyList", String.valueOf(keyList.size()));
                                    for (int i = 0; i < keyList.size(); i++) {
                                        final CollectionReference Mentors = fStore.collection("Users").document(keyList.get(i)).collection("Interests");
                                        final int finalI = i;
                                        Mentors.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                double score = 0;
                                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                                    Double tempx;
                                                    Map<String, Object> dataHolder = doc.getData();
                                                    tempx = (Double) dataHolder.get("Art");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("Board Games");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("Cooking");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("Music");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("Reading");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("Sports");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("TV Shows");
                                                    temp.add(tempx);
                                                    tempx = (Double) dataHolder.get("Video Games");
                                                    temp.add(tempx);


                                                    for (int k = 0; k < temp.size(); k++) {
                                                        score += Math.abs(temp.get(k) - currentList.get(k));
                                                    }
                                                    scores.put(keyList.get(finalI), (int) score);

                                                    Log.d("SCORE ONLY", String.valueOf(score));
                                                    Log.d("SCORES SIZE", String.valueOf(scores.size()));

                                                    nonFreshmen.put(keyList.get(finalI), new ArrayList<Double>(temp)); //NO LONGER NEEDED
                                                    Log.d("finalI", String.valueOf(finalI));
                                                    Log.d("TEMP INSIDE", String.valueOf(temp.size()));
                                                    Log.d("NF SIZE", String.valueOf(nonFreshmen.size()));
                                                    temp.clear();


                                                }
                                                Log.d("scoreS outside", String.valueOf(scores.size()));

                                                TextView mentorsTV = root.findViewById(R.id.mentorsTV);
                                                mentorsTV.setText("Mentors");

                                                int mentor = finalI;
                                                final double finalScore = score;
                                                switch (mentor) {
                                                    case 0:
                                                        DocumentReference documentReference = fStore.collection("Users").document(keyList.get(finalI));
                                                        final String mentorID1 = keyList.get(finalI);
                                                        final TextView mName1 = root.findViewById(R.id.mName1);
                                                        final TextView mScore1 = root.findViewById(R.id.mScore1);

                                                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                mName1.setText("Name: " + documentSnapshot.getString("First Name") + " " + documentSnapshot.getString("Last Name"));
                                                                mScore1.setText(String.valueOf(finalScore));
                                                            }
                                                        });
                                                        TextView mMatch1 = root.findViewById(R.id.mMatch1);
                                                        mMatch1.setText("Match!");
                                                        mMatch1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Task<Void> dr1 = fStore.collection("Users").document(mentorID1).update("Matched", currentUser);
                                                                Task<Void> dr2 = fStore.collection("Users").document(currentUser).update("Matched", mentorID1);
                                                                Toast.makeText(getActivity().getApplicationContext(), "Incomplete", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        break;

                                                    case 1:
                                                        documentReference = fStore.collection("Users").document(keyList.get(finalI));
                                                        final String mentorID2 = keyList.get(finalI);
                                                        final TextView mName2 = root.findViewById(R.id.mName2);
                                                        final TextView mScore2 = root.findViewById(R.id.mScore2);

                                                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                mName2.setText("Name: " + documentSnapshot.getString("First Name") + " " + documentSnapshot.getString("Last Name"));
                                                                mScore2.setText(String.valueOf(finalScore));
                                                            }
                                                        });
                                                        TextView mMatch2 = root.findViewById(R.id.mMatch2);
                                                        mMatch2.setText("Match!");
                                                        mMatch2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Task<Void> dr1 = fStore.collection("Users").document(mentorID2).update("Matched", currentUser);
                                                                Task<Void> dr2 = fStore.collection("Users").document(currentUser).update("Matched", mentorID2);
                                                                Toast.makeText(getActivity().getApplicationContext(), "Incomplete", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        break;

                                                    case 2:
                                                        documentReference = fStore.collection("Users").document(keyList.get(finalI));
                                                        final String mentorID3 = keyList.get(finalI);
                                                        final TextView mName3 = root.findViewById(R.id.mName3);
                                                        final TextView mScore3 = root.findViewById(R.id.mScore3);

                                                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                mName3.setText("Name: " + documentSnapshot.getString("First Name") + " " + documentSnapshot.getString("Last Name"));
                                                                mScore3.setText(String.valueOf(finalScore));
                                                            }
                                                        });
                                                        TextView mMatch3 = root.findViewById(R.id.mMatch3);
                                                        mMatch3.setText("Match!");
                                                        mMatch3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Task<Void> dr1 = fStore.collection("Users").document(mentorID3).update("Matched", currentUser);
                                                                Task<Void> dr2 = fStore.collection("Users").document(currentUser).update("Matched", mentorID3);
                                                                Toast.makeText(getActivity().getApplicationContext(), "Incomplete", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        break;
                                                }
                                                mentor = mentor + 1;

                                            }
                                        });
                                        Log.d("scoreS 2!", String.valueOf(scores.size()));
                                        Log.d("NF SIZE OUT", String.valueOf(nonFreshmen.size()));

                                    }

                                }
                            });

                        }
                        else
                        {
                            DocumentReference matched = fStore.collection("Users").document(currentUser);
                            matched.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    String str = documentSnapshot.getString("Matched");
                                    if(!str.equals("null"))
                                    {
                                        DocumentReference mentored = fStore.collection("Users").document((String) documentSnapshot.get("Matched"));
                                        mentored.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {

                                                TextView mName1 = root.findViewById(R.id.mName1);
                                                mName1.setText(doc.get("First Name") + " " + doc.get("Last Name"));
                                                TextView mScore1 = root.findViewById(R.id.mScore1);
                                                mScore1.setText((String) doc.get("Email"));

                                                //TextView mMatch1 = root.findViewById(R.id.mMatch1);
                                                //mMatch1.setText((Integer)doc.get("Phone"));

                                            }
                                        });
                                    }
                                    else
                                    {
                                        TextView mName1 = root.findViewById(R.id.mName1);
                                        mName1.setText("Interests or Student has not been chosen");

                                    }
                                }
                            });

                        }
                    }


                });

                Log.d("SIZE OUTSIDE", String.valueOf(size[0]));

            }
        });



        return root;

    }



    }



